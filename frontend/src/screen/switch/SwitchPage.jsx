import React, { useState, useEffect } from 'react';
import Header from '../../screen/homepage/Header';  // Ensure correct import
import Sidebar from '../../components/Sidebar';  // Ensure correct import
import GameCard from '../../components/GameCard';  // Ensure correct import
import Footer from '../../components/Footer';  // Ensure correct import

function SwitchPage() {
    const [games, setGames] = useState([]);
    const [filters, setFilters] = useState({
        genre: '',
        price: '',
        sort: '',
    });

    useEffect(() => {
        fetchGames();
    }, [filters]);

    const fetchGames = async () => {
        try {
            const response = await fetch(`/api/games?platform=switch&genre=${filters.genre}`);
            const data = await response.json();
            applyFilters(data);
        } catch (error) {
            console.error('Error fetching games:', error);
        }
    };

    const applyFilters = (data) => {
        let filteredData = [...data];

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
    };

    const handleFilterChange = (filterType, value) => {
        setFilters((prevFilters) => ({
            ...prevFilters,
            [filterType]: value,
        }));
    };

    return (
        <div className="flex flex-col min-h-screen">
            <Header />
            <div className="flex flex-1">
                <Sidebar onFilterChange={handleFilterChange} />
                <div className="flex-1 p-6">
                    <h1 className="text-4xl font-bold mb-6">Nintendo Switch Games</h1>
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

export default SwitchPage;
