import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import logo from '../../assets/img/logo.svg';
import cartIcon from '../../assets/img/cart-icon.svg';
import SearchBar from '../../components/SearchBar';

function Header() {
    const [isAuthenticated, setIsAuthenticated] = useState(false);
    const [cartCount, setCartCount] = useState(0);
    const navigate = useNavigate();

    useEffect(() => {
        const accessToken = localStorage.getItem('accessToken');
        setIsAuthenticated(!!accessToken);

        const updateCartCount = () => {
            const tempCart = JSON.parse(localStorage.getItem('tempCart')) || [];
            const cartTotal = tempCart.reduce((total, item) => total + item.quantity, 0);
            setCartCount(cartTotal);

            if (accessToken) {
                fetchCartCountFromServer();
            }
        };

        const fetchCartCountFromServer = async () => {
            const userID = localStorage.getItem('userID');
            if (userID && accessToken) {
                try {
                    const response = await fetch(`${import.meta.env.VITE_API_URL}/cart/${userID}/cart`, {
                        headers: {
                            Authorization: `Bearer ${accessToken}`,
                        },
                    });

                    if (response.ok) {
                        const data = await response.json();
                        const serverCartCount = data.reduce((total, item) => total + item.quantity, 0);
                        setCartCount(serverCartCount);
                    }
                } catch (err) {
                    console.error('Error fetching cart count:', err);
                }
            }
        };

        updateCartCount();
    }, []);

    const handleSignOut = () => {
        localStorage.removeItem('accessToken');
        localStorage.removeItem('refreshToken');
        localStorage.removeItem('userId');
        localStorage.removeItem('tempCart');
        setIsAuthenticated(false);
        navigate('/');
    };

    return (
        <header className="bg-gray-800 text-white p-4">
            <div className="flex items-center justify-between max-w-7xl mx-auto">
                {/* logo */}
                <div className="flex items-center flex-shrink-0">
                    <Link to="/">
                        <img src={logo} alt="Logo" className="h-16 w-auto" />
                    </Link>
                </div>

                {/* search bar */}
                <div className="flex-1 mx-6">
                    <SearchBar />
                </div>

                {/* buttons */}
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
                    <div className="relative">
                        <Link to="/cart">
                            <img
                                src={cartIcon}
                                alt="Cart"
                                className="h-10 w-10 cursor-pointer hover:scale-110 transition-transform duration-200"
                            />
                            {cartCount > 0 && (
                                <span className="absolute top-0 right-0 bg-red-600 text-white text-xs rounded-full h-5 w-5 flex items-center justify-center">
                                    {cartCount}
                                </span>
                            )}
                        </Link>
                    </div>
                </div>
            </div>
        </header>
    );
}

export default Header;