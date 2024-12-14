import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import Footer from '../../components/Footer';
import Header from '../../screen/homepage/Header';

function GameDetailPage() {
    const { id } = useParams();
    const [game, setGame] = useState(null);
    const [imageUrls, setImageUrls] = useState(['/placeholder.jpg']);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    // Helper function to fetch signed URLs for all images
    const fetchSignedUrls = async (images) => {
        const signedUrlPromises = images.map(async (image) => {
            let fileName = image.imageUrl;
            // If there's a path involved, extract the filename:
            if (fileName.includes('/')) {
                fileName = fileName.split('/').pop();
            }
            fileName = decodeURIComponent(fileName);

            const response = await fetch(`http://localhost:3000/generate-signed-url?bucketName=product-images&fileName=${encodeURIComponent(fileName)}`);
            if (!response.ok) {
                console.error('Failed to fetch signed URL:', await response.text());
                return '/placeholder.jpg';
            }

            const data = await response.json();
            return data.signedUrl;
        });

        return Promise.all(signedUrlPromises);
    };

    useEffect(() => {
        const fetchGame = async () => {
            try {
                const response = await fetch(`http://localhost:8080/api/products/get/${id}`);
                if (!response.ok) {
                    throw new Error(`Error: ${response.status} ${response.statusText}`);
                }
                const data = await response.json();

                // Set game details
                setGame(data);

                // If we have images, fetch signed URLs
                if (data.images && data.images.length > 0) {
                    const urls = await fetchSignedUrls(data.images);
                    setImageUrls(urls);
                } else {
                    setImageUrls(['/placeholder.jpg']);
                }
            } catch (err) {
                console.error('Error fetching game details:', err);
                setError(err.message);
            } finally {
                setLoading(false);
            }
        };
        fetchGame();
    }, [id]);

    // Conditional Rendering based on state
    if (loading) {
        return (
            <div className="flex flex-col min-h-screen">
                <Header />
                <div className="flex-grow flex items-center justify-center">
                    <div className="text-center text-xl">Loading...</div>
                </div>
                <Footer />
            </div>
        );
    }

    if (error) {
        return (
            <div className="flex flex-col min-h-screen">
                <Header />
                <div className="flex-grow flex items-center justify-center">
                    <div className="text-center text-red-500 text-xl">Error: {error}</div>
                </div>
                <Footer />
            </div>
        );
    }

    if (!game) {
        return (
            <div className="flex flex-col min-h-screen">
                <Header />
                <div className="flex-grow flex items-center justify-center">
                    <div className="text-center text-xl">Game not found.</div>
                </div>
                <Footer />
            </div>
        );
    }

    // Extract categories text
    const categoriesText = (Array.isArray(game.categoryList) && game.categoryList.length > 0)
        ? game.categoryList.map(cat => cat.name).join(', ')
        : 'No categories';

    return (
        <div className="flex flex-col min-h-screen">
            <Header />
            <main className="flex-grow flex items-center justify-center">
                <div className="max-w-4xl w-full p-4">
                    <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                        {/* Images Section */}
                        <div className="flex flex-col space-y-4">
                            {imageUrls.map((url, index) => (
                                <ImageWithFallback
                                    key={index}
                                    src={url}
                                    alt={`${game.name} Image ${index + 1}`}
                                    className="w-full h-40 object-cover rounded-md"
                                />
                            ))}
                        </div>
                        {/* Details Section */}
                        <div className="flex flex-col justify-center">
                            <h1 className="text-3xl font-bold">{game.name}</h1>
                            <p className="text-md text-gray-600 mt-2">Developer: {game.developer}</p>
                            <p className="text-lg font-bold text-gray-800 mt-2">${game.price.toFixed(2)}</p>
                            <p className="text-sm text-gray-600 mt-1">Quantity Available: {game.stock}</p>
                            <p className="text-sm text-gray-600 mt-1">Platform: {game.platform}</p>
                            <p className="text-sm text-gray-600 mt-1">Categories: {categoriesText}</p>
                            <p className="text-md text-gray-800 mt-4">{game.description}</p>
                            <button className="bg-blue-500 px-4 py-2 mt-6 rounded text-white hover:bg-blue-700">
                                Add to Cart
                            </button>
                        </div>
                    </div>
                </div>
            </main>
            <Footer />
        </div>
    );
}

// Custom Image component with fallback logic
function ImageWithFallback({ src, alt, className }) {
    const [imgSrc, setImgSrc] = useState(src);

    const handleError = () => {
        if (imgSrc !== '/placeholder.jpg') {
            setImgSrc('/placeholder.jpg');
        }
    };

    return (
        <img
            src={imgSrc}
            alt={alt}
            className={className}
            onError={handleError}
            loading="lazy"
        />
    );
}

export default GameDetailPage;
