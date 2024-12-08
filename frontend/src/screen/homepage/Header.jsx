import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import logo from '../../assets/img/logo.svg'; // Import logo from assets
import cartIcon from '../../assets/img/cart-icon.svg';  // Cart icon (replace with your actual cart icon)
import SearchBar from '../../components/SearchBar';

function Header() {
    const [isAuthenticated, setIsAuthenticated] = useState(false); // Tracks login state
    const navigate = useNavigate();

    useEffect(() => {
        // Simulate fetching auth state (this could be from localStorage or API)
        const authStatus = localStorage.getItem('authStatus'); // Example check
        if (authStatus === 'loggedIn') {
            setIsAuthenticated(true);
        }
    }, []);

    const handleSignOut = () => {
        // Remove authentication status from localStorage or your authentication method
        localStorage.removeItem('authStatus');
        setIsAuthenticated(false);
        navigate('/'); // Redirect to home page after logout
    };

    return (
        <header className="bg-gray-800 text-white p-4">
            <div className="flex items-center justify-between max-w-7xl mx-auto">
                {/* Logo and Navigation */}
                <div className="flex items-center space-x-4">
                    <Link to="/home">
                        <img src={logo} alt="Logo" className="h-24 w-auto" />
                    </Link>
                    <nav className="space-x-6 hidden md:flex">
                        <Link to="/xbox" className="hover:text-gray-300">Xbox</Link>
                        <Link to="/playstation" className="hover:text-gray-300">PlayStation</Link>
                        <Link to="/pc" className="hover:text-gray-300">PC</Link>
                        <Link to="/switch" className="hover:text-gray-300">Switch</Link>
                    </nav>
                </div>

                {/* SearchBar in Header */}
                <div className="flex-1 flex justify-center md:justify-start mx-4">
                    <SearchBar />
                </div>

                {/* Right-aligned buttons and Cart Icon */}
                <div className="flex items-center space-x-4 ml-4">
                    {/* Login/SignUp buttons or Profile and SignOut buttons */}
                    {!isAuthenticated ? (
                        <>
                            <Link to="/login">
                                <button className="bg-blue-500 px-4 py-2 rounded hover:bg-blue-700">Sign In</button>
                            </Link>
                            <Link to="/register">
                                <button className="bg-green-500 px-4 py-2 rounded hover:bg-green-700">Sign Up</button>
                            </Link>
                        </>
                    ) : (
                        <>
                            <Link to="/profile">
                                <button className="bg-yellow-500 px-4 py-2 rounded hover:bg-yellow-700">Edit Profile</button>
                            </Link>
                            <button
                                className="bg-red-500 px-4 py-2 rounded hover:bg-red-700"
                                onClick={handleSignOut}
                            >
                                Sign Out
                            </button>
                        </>
                    )}

                    {/* Cart Icon */}
                    <Link to="/cart">
                        <img
                            src={cartIcon}
                            alt="Cart"
                            className="h-8 w-8 cursor-pointer hover:scale-110 transition-transform duration-200"
                        />
                    </Link>
                </div>
            </div>
        </header>
    );
}

export default Header;
