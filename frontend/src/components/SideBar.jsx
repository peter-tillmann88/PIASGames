// Sidebar.js
import React, { useState, useEffect } from 'react';

function Sidebar({ onFilterChange }) {
    // Hardcoded Platforms
    const platforms = [
        "PC",
        "Nintendo Switch",
        "PS4",
        "PS5",
        "Xbox Series X"
    ];

    // State for Categories
    const [categories, setCategories] = useState([]);
    const [categoriesLoading, setCategoriesLoading] = useState(true);
    const [categoriesError, setCategoriesError] = useState(null);

    const [selectedCategory, setSelectedCategory] = useState('');
    const [selectedPlatform, setSelectedPlatform] = useState('');
    const [selectedPrice, setSelectedPrice] = useState('');
    const [selectedSort, setSelectedSort] = useState('');

    useEffect(() => {
        const fetchCategories = async () => {
            try {
                const response = await fetch('http://localhost:8080/api/categories/all', {
                    method: 'GET',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                });
                if (!response.ok) {
                    throw new Error(`Error: ${response.status} ${response.statusText}`);
                }
                const data = await response.json();
                setCategories(data.object); // Updated here
            } catch (error) {
                console.error('Error fetching categories:', error);
                setCategoriesError('Failed to load categories.');
            } finally {
                setCategoriesLoading(false);
            }
        };

        fetchCategories();
    }, []);

    const handleCategoryChange = (e) => {
        setSelectedCategory(e.target.value);
        onFilterChange('category', e.target.value);
    };

    const handlePlatformChange = (e) => {
        setSelectedPlatform(e.target.value);
        onFilterChange('platform', e.target.value);
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

            {/* Category Filter */}
            <div className="mt-4">
                <label className="block text-sm font-medium text-gray-700">Category</label>
                {categoriesLoading ? (
                    <div className="mt-1 text-sm text-gray-500">Loading categories...</div>
                ) : categoriesError ? (
                    <div className="mt-1 text-sm text-red-500">{categoriesError}</div>
                ) : (
                    <select
                        value={selectedCategory}
                        onChange={handleCategoryChange}
                        className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md"
                    >
                        <option value="">All Categories</option>
                        {categories.map((category) => (
                            <option key={category.categoryId} value={category.name}>
                                {category.name}
                            </option>
                        ))}
                    </select>
                )}
            </div>

            {/* Platform Filter */}
            <div className="mt-4">
                <label className="block text-sm font-medium text-gray-700">Platform</label>
                <select
                    value={selectedPlatform}
                    onChange={handlePlatformChange}
                    className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md"
                >
                    <option value="">All Platforms</option>
                    {platforms.map((platform, index) => (
                        <option key={index} value={platform}>
                            {platform}
                        </option>
                    ))}
                </select>
            </div>

            {/* Price Filter */}
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

            {/* Sort By Filter */}
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
