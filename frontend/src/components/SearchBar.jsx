import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom'; // Use useNavigate for React Router v6+

function SearchBar() {
    const [query, setQuery] = useState('');
    const navigate = useNavigate(); // useNavigate for React Router v6+

    const handleSearch = (e) => {
        e.preventDefault();
        if (query.trim()) {
            navigate(`/search?query=${query}`); // Navigating to the search page with query params
        }
    };

    return (
        <form onSubmit={handleSearch} className="flex items-center space-x-2">
            <input
                type="text"
                value={query}
                onChange={(e) => setQuery(e.target.value)}
                placeholder="Search for games..."
                className="px-4 py-2 rounded bg-gray-700 text-white"
            />
            <button type="submit" className="bg-blue-500 px-4 py-2 rounded text-white hover:bg-blue-700">
                Search
            </button>
        </form>
    );
}

export default SearchBar;
