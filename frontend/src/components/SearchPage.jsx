import React, { useState, useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import Header from '../screen/homepage/Header';
import Sidebar from '../components/Sidebar';
import GameCard from '../components/GameCard';
import Footer from '../components/Footer';

function useQuery() {
    return new URLSearchParams(useLocation().search);
}

function SearchPage() {
    const query = useQuery();
    const searchQuery = query.get('query') || '';
    const [games, setGames] = useState([]);
    const [loading, setLoading] = useState(true);
    const [filters, setFilters] = useState({
        category: '',
        price: '',
        sort: '',
    });

    useEffect(() => {
        fetchSearchResults();
    }, [searchQuery, filters]);

    const fetchSearchResults = async () => {
        setLoading(true);
        try {
            let url = `${import.meta.env.VITE_API_URL}/products/search?query=${encodeURIComponent(searchQuery)}`;

            if (filters.category) {
                url += `&category=${encodeURIComponent(filters.category)}`;
            }
            if (filters.price) {
                url += `&price=${encodeURIComponent(filters.price)}`;
            }
            if (filters.sort) {
                url += `&sort=${encodeURIComponent(filters.sort)}`;
            }

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
            setGames(data);
        } catch (error) {
            console.error('Error fetching search results:', error);
            setGames([]);
        } finally {
            setLoading(false);
        }
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
                    <h1 className="text-4xl font-bold mb-6">Search Results for "{searchQuery}"</h1>
                    {loading ? (
                        <div>Loading...</div>
                    ) : (
                        games.length > 0 ? (
                            <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-6">
                                {games.map((game) => (
                                    <GameCard key={game.productId} game={game} />
                                ))}
                            </div>
                        ) : (
                            <div>No products found</div>
                        )
                    )}
                </div>
            </div>
            <Footer />
        </div>
    );
}

export default SearchPage;
