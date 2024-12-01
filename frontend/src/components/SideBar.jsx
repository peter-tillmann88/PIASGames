import React, { useState } from 'react';

function Sidebar({ onFilterChange }) {
    const [selectedCategory, setSelectedCategory] = useState('');
    const [selectedGenre, setSelectedGenre] = useState('');
    const [selectedPrice, setSelectedPrice] = useState('');
    const [selectedSort, setSelectedSort] = useState('');

    const handleCategoryChange = (e) => {
        setSelectedCategory(e.target.value);
        onFilterChange('category', e.target.value);
    };

    const handleGenreChange = (e) => {
        setSelectedGenre(e.target.value);
        onFilterChange('genre', e.target.value);
    };

    const handlePriceChange = (e) => {
        setSelectedPrice(e.target.value);
        onFilterChange('price', e.target.value);
    };

    const handleSortChange = (e) => {
        setSelectedSort(e.target.value);
        onFilterChange('sort', e.target.value);
    };

    return (
        <div className="bg-white p-4 rounded-md shadow-md">
            <h3 className="text-lg font-semibold">Filter Products</h3>

            {/* Category filter */}
            <div className="mt-4">
                <label className="block text-sm font-medium text-gray-700">Category</label>
                <select
                    value={selectedCategory}
                    onChange={handleCategoryChange}
                    className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md"
                >
                    <option value="">All Categories</option>
                    <option value="xbox">Xbox</option>
                    <option value="playstation">PlayStation</option>
                    <option value="pc">PC</option>
                    <option value="switch">Switch</option>
                </select>
            </div>

            {/* Genre filter */}
            <div className="mt-4">
                <label className="block text-sm font-medium text-gray-700">Genre</label>
                <select
                    value={selectedGenre}
                    onChange={handleGenreChange}
                    className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md"
                >
                    <option value="">All Genres</option>
                    <option value="action">Action</option>
                    <option value="adventure">Adventure</option>
                    <option value="rpg">RPG</option>
                    <option value="sports">Sports</option>
                </select>
            </div>

            {/* Price filter */}
            <div className="mt-4">
                <label className="block text-sm font-medium text-gray-700">Price</label>
                <select
                    value={selectedPrice}
                    onChange={handlePriceChange}
                    className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md"
                >
                    <option value="">All Prices</option>
                    <option value="low-to-high">Low to High</option>
                    <option value="high-to-low">High to Low</option>
                </select>
            </div>

            {/* Sorting by Name */}
            <div className="mt-4">
                <label className="block text-sm font-medium text-gray-700">Sort By</label>
                <select
                    value={selectedSort}
                    onChange={handleSortChange}
                    className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md"
                >
                    <option value="">Default</option>
                    <option value="alphabetical-asc">Alphabetical (A-Z)</option>
                    <option value="alphabetical-desc">Alphabetical (Z-A)</option>
                </select>
            </div>
        </div>
    );
}

export default Sidebar;
