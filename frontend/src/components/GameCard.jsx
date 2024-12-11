import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';

function GameCard({ game }) {
    // State to manage the current image source
    const [imageSrc, setImageSrc] = useState('/placeholder.jpg'); // Default to placeholder

    useEffect(() => {
        // Fetch signed URL dynamically if images exist
        async function fetchSignedUrl() {
            if (game.images && game.images.length > 0) {
                const fileName = game.images[0].imageUrl; // Assuming imageUrl holds the file name

                try {
                    // Call the API to get the signed URL
                    const response = await fetch(`/generate-signed-url?bucketName=product-images&fileName=${encodeURIComponent(fileName)}`);

                    if (response.ok) {
                        const data = await response.json();
                        setImageSrc(data.signedUrl); // Set the signed URL as the image source
                    } else {
                        console.error('Failed to fetch signed URL:', await response.text());
                    }
                } catch (error) {
                    console.error('Error fetching signed URL:', error);
                }
            }
        }

        fetchSignedUrl();
    }, [game.images]); // Dependency ensures it runs only when the images array changes

    // Generate categories text
    const categoriesText = (Array.isArray(game.categoryList) && game.categoryList.length > 0)
        ? game.categoryList.map(cat => cat.name).join(', ')
        : 'No category';

    // Generate platform text
    const platformText = game.platform ? game.platform : 'No platform';

    return (
        <Link to={`/gamedetailpage/${game.productId}`} className="block">
            <div className="bg-white p-2 rounded-md shadow-md hover:shadow-xl transition-shadow duration-300 transform hover:scale-105 cursor-pointer">
                {/* Image with fallback */}
                <img
                    src={imageSrc}
                    alt={game.name}
                    className="w-full h-40 object-cover rounded-md"
                    onError={() => setImageSrc('/placeholder.jpg')} // Fallback to placeholder
                />

                {/* Game Name */}
                <h2 className="text-lg font-semibold mt-2">{game.name}</h2>

                {/* Categories */}
                <p className="text-xs text-gray-600 mt-1">Categories: {categoriesText}</p>

                {/* Platform */}
                <p className="text-xs text-gray-600 mt-0.5">Platform: {platformText}</p>

                {/* Price */}
                <p className="text-md font-bold text-gray-800 mt-1">${game.price.toFixed(2)}</p>
            </div>
        </Link>
    );
}

export default GameCard;
