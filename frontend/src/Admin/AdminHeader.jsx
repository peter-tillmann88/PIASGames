import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import logo from '../assets/img/logo.svg'; 

function Header() {
    const [isAuthenticated, setIsAuthenticated] = useState(false);
    const navigate = useNavigate();

    useEffect(() => {
        const accessToken = localStorage.getItem('accessToken');
        setIsAuthenticated(!!accessToken);
    }, []);

    const handleSignOut = () => {
        localStorage.removeItem('accessToken');
        localStorage.removeItem('refreshToken');
        setIsAuthenticated(false);
        navigate('/'); 
    };

    return (
        <header className="bg-gray-800 text-white p-4">
            <div className="flex items-center justify-between max-w-7xl mx-auto">
                {/* logo*/}
                <div className="flex items-center flex-shrink-0">
                    <Link to="/admin-dashboard">
                        <img src={logo} alt="Logo" className="h-16 w-auto" /> 
                    </Link>
                </div>

                {/* links for nav */}
                <nav className="flex items-center space-x-6">
                    {isAuthenticated && (
                        <>
                            <Link 
                                to="/manage-inventory" 
                                className="text-gray-300 hover:text-white transition-colors"
                            >
                                Manage Inventory
                            </Link>
                            <Link 
                                to="/sales-history" 
                                className="text-gray-300 hover:text-white transition-colors"
                            >
                                Sales History
                            </Link>
                            <Link 
                                to="/manage-customers" 
                                className="text-gray-300 hover:text-white transition-colors"
                            >
                                Manage Customers
                            </Link>
                        </>
                    )}
                </nav>

                <div className="flex items-center space-x-4 flex-shrink-0">
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
                            <button
                                className="bg-red-500 px-4 py-2 rounded hover:bg-red-700"
                                onClick={handleSignOut}
                            >
                                Sign Out
                            </button>
                        </>
                    )}
                </div>
            </div>
        </header>
    );
}

export default Header;
