import React, { useState, useEffect } from 'react';
import Header from '../../screen/homepage/Header'; // Ensure the correct path
import Sidebar from '../../components/Sidebar'; // Ensure the correct path
import GameCard from '../../components/GameCard'; // Ensure the correct path
import Footer from '../../components/Footer'; // Ensure the correct path

function HomePage() {
    const [games, setGames] = useState([]);
    const [featuredGames, setFeaturedGames] = useState([]); // For featured games
    const [filters, setFilters] = useState({
        category: '',
        genre: '',
        price: '',
        sort: '',
    });

    useEffect(() => {
        fetchGames();
    }, [filters]);

    const fetchGames = async () => {
        try {
            const response = await fetch('/api/games', {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                },
            });
            const data = await response.json();
            applyFilters(data);
        } catch (error) {
            console.error('Error fetching games:', error);
        }
    };

    const applyFilters = (data) => {
        let filteredData = [...data];

        // Apply category, genre, and price filters here
        if (filters.category) {
            filteredData = filteredData.filter(game => game.category === filters.category);
        }
        if (filters.genre) {
            filteredData = filteredData.filter(game => game.genre === filters.genre);
        }
        if (filters.price) {
            filteredData = filteredData.sort((a, b) => {
                if (filters.price === 'low-to-high') return a.price - b.price;
                if (filters.price === 'high-to-low') return b.price - a.price;
                return 0;
            });
        }
        if (filters.sort === 'alphabetical-asc') {
            filteredData = filteredData.sort((a, b) => a.title.localeCompare(b.title));
        }
        if (filters.sort === 'alphabetical-desc') {
            filteredData = filteredData.sort((a, b) => b.title.localeCompare(a.title));
        }

        setGames(filteredData);
        setFeaturedGames(filteredData.slice(0, 5)); // Get top 5 featured games
    };

    const handleFilterChange = (filterType, value) => {
        setFilters((prevFilters) => ({
            ...prevFilters,
            [filterType]: value,
        }));
    };

    return (
        <div className="min-h-screen flex flex-col">
            <Header />

            <div className="flex flex-1">
                <Sidebar onFilterChange={handleFilterChange} />

                <div className="flex-1 p-6">
                    <h1 className="text-4xl font-bold mb-6">Browse Our Games</h1>

                    {/* Featured Games Section with Horizontal Scroll */}
                    <div className="mb-8">
                        <h2 className="text-2xl font-semibold mb-4">Featured Games</h2>
                        <div className="flex overflow-x-auto space-x-4">
                            {featuredGames.map((game) => (
                                <div key={game.id} className="min-w-max">
                                    <GameCard game={game} />
                                </div>
                            ))}
                        </div>
                    </div>

                    {/* All Games Grid */}
                    <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-6">
                        {games.map((game) => (
                            <GameCard key={game.id} game={game} />
                        ))}
                    </div>
                </div>
            </div>

            <Footer />
        </div>
    );
}

export default HomePage;
