import React from 'react';
import { Link } from 'react-router-dom';

function GameCard({ game }) {
    return (
        <div className="bg-white p-4 rounded-md shadow-md">
            <img src={game.imageUrl} alt={game.title} className="w-full h-48 object-cover rounded-md" />
            <h2 className="text-xl font-semibold mt-4">{game.title}</h2>
            <p className="text-sm text-gray-600 mt-2">{game.category}</p>
            <p className="text-lg font-bold text-gray-800 mt-2">${game.price}</p>

            <Link to={`/gamedetailpage/${game.id}`} className="text-blue-500 mt-4 block hover:underline">
                View Details
            </Link>
        </div>
    );
}

export default GameCard;
