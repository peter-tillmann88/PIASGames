import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';

function GameCard({ game }) {
    const [imageSrc, setImageSrc] = useState('/placeholder.jpg');

    useEffect(() => {
        async function fetchSignedUrl() {
            if (game.images && game.images.length > 0) {
                let fileName = game.images[0].imageUrl;

                if (fileName.includes('/')) {
                    fileName = fileName.split('/').pop();
                }

                fileName = decodeURIComponent(fileName);

                try {
                    const response = await fetch(
                        `http://localhost:3000/generate-signed-url?bucketName=product-images&fileName=${encodeURIComponent(fileName)}`
                    );
                    if (response.ok) {
                        const data = await response.json();
                        setImageSrc(data.signedUrl);
                    } else {
                        console.error('Failed to fetch signed URL:', await response.text());
                    }
                } catch (error) {
                    console.error('Error fetching signed URL:', error);
                }
            }
        }

        fetchSignedUrl();
    }, [game.images]);

    const categoriesText = game.categoryList && game.categoryList.length > 0
        ? game.categoryList.map(cat => cat.name).join(', ')
        : 'No category';

    const platformText = game.platform || 'No platform';

    return (
        <Link to={`/gamedetailpage/${game.productId}`} className="block text-center">
            <div className="relative w-full h-[300px]">
                <img
                    src={imageSrc}
                    alt={game.name}
                    className="w-full h-full object-contain"
                    onError={() => setImageSrc('/placeholder.jpg')}
                />
            </div>
            <h2 className="text-base font-medium mt-2 truncate">{game.name}</h2>
            <p className="text-xs text-gray-500 mt-1">Categories: {categoriesText}</p>
            <p className="text-xs text-gray-500 mt-1">Platform: {platformText}</p>
            <p className="text-lg font-bold text-gray-700 mt-2">${game.price.toFixed(2)}</p>
        </Link>
    );
}

export default GameCard;
