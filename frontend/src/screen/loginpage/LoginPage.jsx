import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import Header from '../../screen/homepage/Header';
import Footer from '../../components/Footer';

function LoginPage() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();

    useEffect(() => {
        const accessToken = localStorage.getItem('accessToken');
        if (accessToken) {
            const role = localStorage.getItem('role');
            if (role === 'ADMIN') {
                navigate('/admin-dashboard');
            } else {
                navigate('/');
            }
        }
    }, [navigate]);

    const handleLogin = async (e) => {
        e.preventDefault();
        setError('');

        if (!username || !password) {
            setError('Please fill in both username and password');
            return;
        }

        try {
            const response = await axios.post(`${import.meta.env.VITE_API_URL}/auth/login`, {
                username,
                password,
            });

            const { accessToken, refreshToken, role } = response.data;

            localStorage.setItem('accessToken', accessToken);
            localStorage.setItem('refreshToken', refreshToken);
            localStorage.setItem('role', role);

            const token = accessToken;
            const userIDResponse = await fetch(`${import.meta.env.VITE_API_URL}/users/profile`, {
                method: 'GET',
                headers: { Authorization: `Bearer ${token}` },
            });

            if (userIDResponse.ok) {
                const userProfile = await userIDResponse.json();
                const userID = userProfile.userID;

                const tempCart = JSON.parse(localStorage.getItem('tempCart')) || [];
                if (tempCart.length > 0) {
                    for (const item of tempCart) {
                        await axios.post(`${import.meta.env.VITE_API_URL}/cart-items/cart/${userID}/item/${item.id}/add`, null, {
                            headers: { Authorization: `Bearer ${token}` },
                            params: { quantity: item.quantity },
                        });
                    }
                    localStorage.removeItem('tempCart');
                }
            }

            if (role === 'ADMIN') {
                navigate('/admin-dashboard');
            } else {
                navigate('/');
            }
        } catch (err) {
            console.error('Login failed:', err.response || err.message);
            if (err.response && err.response.status === 401) {
                setError('Invalid username or password');
            } else {
                setError('An error occurred while logging in. Please try again.');
            }
        }
    };

    return (
        <>
            <Header />
            <div className="min-h-screen flex flex-col justify-center items-center bg-gray-100 py-12">
                <form onSubmit={handleLogin} className="bg-white p-6 rounded shadow-lg w-full max-w-lg">
                    <h2 className="text-3xl font-bold mb-6 text-center">Log In</h2>
                    {error && <p className="text-red-500 mb-4">{error}</p>}
                    <div className="mb-4">
                        <label htmlFor="username" className="block text-lg font-semibold mb-2">Username</label>
                        <input
                            type="text"
                            id="username"
                            placeholder="Enter your username"
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                            className="w-full p-2 border border-gray-300 rounded"
                        />
                    </div>
                    <div className="mb-4">
                        <label htmlFor="password" className="block text-lg font-semibold mb-2">Password</label>
                        <input
                            type="password"
                            id="password"
                            placeholder="Enter your password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            className="w-full p-2 border border-gray-300 rounded"
                        />
                    </div>
                    <button
                        type="submit"
                        className="w-full bg-blue-500 text-white py-2 rounded hover:bg-blue-700"
                    >
                        Log In
                    </button>
                    <div className="mt-4 text-center">
                        <p>Don't have an account?</p>
                        <button
                            type="button"
                            onClick={() => navigate('/register')}
                            className="w-full bg-green-500 text-white py-2 rounded hover:bg-green-700 mt-2"
                        >
                            Register
                        </button>
                    </div>
                </form>
            </div>
            <Footer />
        </>
    );
}

export default LoginPage;