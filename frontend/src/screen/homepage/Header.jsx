import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import logo from '../../assets/img/logo.svg'; 
import cartIcon from '../../assets/img/cart-icon.svg'; 
import SearchBar from '../../components/SearchBar';

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
                <div className="flex items-center">
                    <Link to="/">
                        <img src={logo} alt="Logo" className="h-24 w-auto" />
                    </Link>
                </div>


                <div className="flex-1 mx-4">
                    <SearchBar />
                </div>


                <div className="flex items-center space-x-4">
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
                                <button className="bg-yellow-500 px-4 py-2 rounded hover:bg-yellow-700">View Profile</button>
                            </Link>
                            <button
                                className="bg-red-500 px-4 py-2 rounded hover:bg-red-700"
                                onClick={handleSignOut}
                            >
                                Sign Out
                            </button>
                        </>
                    )}

                    <Link to="/cart">
                        <img
                            src={cartIcon}
                            alt="Cart"
                            className="h-10 w-10 cursor-pointer hover:scale-110 transition-transform duration-200"
                        />
                    </Link>
                </div>
            </div>
        </header>
    );
}

export default Header;
