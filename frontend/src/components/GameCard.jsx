// GameCard.js
import React from 'react';
import { Link } from 'react-router-dom';

function GameCard({ game }) {
    const imageUrl = (game.images && game.images.length > 0)
        ? game.images[0].imageUrl
        : '/placeholder.jpg'; // Ensure this points to a valid placeholder image

    const categoriesText = (Array.isArray(game.categoryList) && game.categoryList.length > 0)
        ? game.categoryList.map(cat => cat.name).join(', ')
        : 'No category';

    const platformText = game.platform ? game.platform : 'No platform';

    return (
        <Link to={`/gamedetailpage/${game.productId}`} className="block">
            <div className="bg-white p-2 rounded-md shadow-md hover:shadow-xl transition-shadow duration-300 transform hover:scale-105 cursor-pointer">
                <img
                    src={imageUrl}
                    alt={game.name}
                    className="w-full h-40 object-cover rounded-md"
                    onError={(e) => { e.target.src = '/placeholder.jpg'; }}
                />
                <h2 className="text-lg font-semibold mt-2">{game.name}</h2>
                <p className="text-xs text-gray-600 mt-1">Categories: {categoriesText}</p>
                <p className="text-xs text-gray-600 mt-0.5">Platform: {platformText}</p>
                <p className="text-md font-bold text-gray-800 mt-1">${game.price.toFixed(2)}</p>
            </div>
        </Link>
    );
}

export default GameCard;
