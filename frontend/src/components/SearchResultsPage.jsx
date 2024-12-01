import React, { useEffect, useState } from 'react';
import { useLocation } from 'react-router-dom';
import Header from '../screen/homepage/Header'; // Make sure the correct path
import Footer from '../components/Footer'; // Make sure the correct path

function SearchResultsPage() {
    const [games, setGames] = useState([]);
    const location = useLocation();
    const queryParams = new URLSearchParams(location.search);
    const query = queryParams.get('query');

    useEffect(() => {
        const fetchGames = async () => {
            try {
                const response = await fetch(`/api/games/search?query=${query}`);
                const data = await response.json();
                setGames(data);
            } catch (error) {
                console.error('Error fetching search results:', error);
            }
        };

        if (query) {
            fetchGames();
        }
    }, [query]);

    return (
        <div className="min-h-screen flex flex-col">
            {/* Header */}
            <Header />

            {/* Main Content */}
            <div className="flex-1 p-6">
                <h1 className="text-3xl font-semibold mb-4">Search Results for "{query}"</h1>
                <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-6">
                    {games.length > 0 ? (
                        games.map((game) => (
                            <div key={game.id} className="bg-white p-4 rounded-md shadow-md">
                                <img src={game.imageUrl} alt={game.title} className="w-full h-48 object-cover rounded-md" />
                                <h2 className="text-xl font-semibold mt-4">{game.title}</h2>
                                <p className="text-sm text-gray-600 mt-2">{game.category}</p>
                                <p className="text-lg font-bold text-gray-800 mt-2">${game.price}</p>
                            </div>
                        ))
                    ) : (
                        <p>No games found for "{query}".</p>
                    )}
                </div>
            </div>

            {/* Footer */}
            <Footer />
        </div>
    );
}

export default SearchResultsPage;
