import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom'; // Import useParams to get game ID from URL

function GameDetailPage() {
    const { gameId } = useParams();
    const [game, setGame] = useState(null);

    // Fetch game details by gameId
    useEffect(() => {
        const fetchGame = async () => {
            try {
                const response = await fetch(`/api/games/${gameId}`);
                const data = await response.json();
                setGame(data);
            } catch (error) {
                console.error('Error fetching game details:', error);
            }
        };
        fetchGame();
    }, [gameId]);

    if (!game) return <div>Loading...</div>;

    return (
        <div className="max-w-7xl mx-auto p-6">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                <div>
                    <img src={game.imageUrl} alt={game.title} className="w-full h-96 object-cover rounded-md" />
                </div>
                <div>
                    <h1 className="text-4xl font-bold">{game.title}</h1>
                    <p className="text-xl text-gray-600 mt-4">{game.description}</p>
                    <p className="text-lg font-bold text-gray-800 mt-4">${game.price}</p>
                    <p className="text-md text-gray-600 mt-2">Quantity Available: {game.quantity}</p>
                    <button className="bg-blue-500 px-4 py-2 mt-6 rounded text-white hover:bg-blue-700">
                        Add to Cart
                    </button>
                </div>
            </div>
        </div>
    );
}

export default GameDetailPage;
