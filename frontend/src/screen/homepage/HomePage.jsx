import React, { useState, useEffect } from 'react';
import Header from '../../screen/homepage/Header';
import Sidebar from '../../components/Sidebar';
import GameCard from '../../components/GameCard';
import Footer from '../../components/Footer';

function HomePage() {
    const [games, setGames] = useState([]);
    const [loading, setLoading] = useState(true);
    const [filters, setFilters] = useState({
        category: '',
        platform: '',
        price: '',
        sort: '',
    });

    useEffect(() => {
        fetchGames();
    }, [filters]);

    const fetchGames = async () => {
        setLoading(true);
        try {
            let url = `${import.meta.env.VITE_API_URL}/products/all`;
            const response = await fetch(url, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                },
            });
            if (!response.ok) {
                throw new Error(`Error: ${response.status} ${response.statusText}`);
            }
            const data = await response.json();
            applyFilters(data);
        } catch (error) {
            console.error('Error fetching products:', error);
            setGames([]);
        } finally {
            setLoading(false);
        }
    };

    const applyFilters = (data) => {
        let filteredData = [...data];

        //apply cat filter
        if (filters.category) {
            filteredData = filteredData.filter(game =>
                Array.isArray(game.categoryList) &&
                game.categoryList.some(cat => cat.name === filters.category)
            );
        }

        //platform filter
        if (filters.platform) {
            filteredData = filteredData.filter(game =>
                game.platform === filters.platform
            );
        }

        //price filter 
        if (filters.price) {
            filteredData = filteredData.sort((a, b) => {
                if (filters.price === 'low-to-high') return a.price - b.price;
                if (filters.price === 'high-to-low') return b.price - a.price;
                return 0;
            });
        }

        //name filter
        if (filters.sort === 'alphabetical-asc') {
            filteredData = filteredData.sort((a, b) => a.name.localeCompare(b.name));
        }

        if (filters.sort === 'alphabetical-desc') {
            filteredData = filteredData.sort((a, b) => b.name.localeCompare(a.name));
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
        <div className="min-h-screen flex flex-col">
            <Header />
            <div className="flex flex-1">
                <Sidebar onFilterChange={handleFilterChange} />
                <div className="flex-1 p-6">
                    <h1 className="text-4xl font-bold mb-6">Browse Our Products</h1>
                    {loading ? (
                        <div>Loading...</div>
                    ) : (
                        games.length > 0 ? (
                            <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4">
                                {games.map((game) => (
                                    <GameCard key={game.productId} game={game} />
                                ))}
                            </div>
                        ) : (
                            <div>No products available</div>
                        )
                    )}
                </div>
            </div>
            <Footer />
        </div>
    );
}

export default HomePage;
