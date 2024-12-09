import React, { useState, useEffect } from 'react';
import axios from 'axios';
import Header from '../screen/homepage/Header';
import Footer from '../components/Footer';

function Profile() {
    const [userInfo, setUserInfo] = useState({
        username: '',
        email: '',
        phone: '',
        role: '',
        createdAt: ''
    });

    const [username, setUsername] = useState('');

    useEffect(() => {
        const savedUsername = localStorage.getItem('username');
        if (savedUsername) {
            setUsername(savedUsername);
            fetchUserProfile(savedUsername);
        }
    }, []);

    const fetchUserProfile = async (username) => {
        try {
            const response = await axios.get(`http://localhost:8080/api/users/profile`, {
                params: { username }
            });
            setUserInfo(response.data);
        } catch (error) {
            alert('Error fetching user profile');
        }
    };

    const handleDeleteAccount = async () => {
        try {
            await axios.delete(`http://localhost:8080/api/users/profile`, {
                params: { username }
            });
            alert('Account deleted successfully');
            localStorage.removeItem('username');
            setUserInfo({
                username: '',
                email: '',
                phone: '',
                role: '',
                createdAt: ''
            });
        } catch (error) {
            alert('Error deleting account');
        }
    };

    return (
        <>
            <Header />
            <div className="min-h-screen flex flex-col justify-center items-center bg-gray-100 py-12">
                <h2 className="text-3xl font-bold mb-6">Profile</h2>
                <div className="bg-white p-6 rounded shadow-lg w-full max-w-4xl">
                    <p className="mb-4 text-lg">
                        <strong>Username:</strong> {userInfo.username || 'N/A'}
                    </p>
                    <p className="mb-4 text-lg">
                        <strong>Email:</strong> {userInfo.email || 'N/A'}
                    </p>
                    <p className="mb-4 text-lg">
                        <strong>Phone:</strong> {userInfo.phone || 'N/A'}
                    </p>
                    <p className="mb-4 text-lg">
                        <strong>Role:</strong> {userInfo.role || 'N/A'}
                    </p>
                    <p className="mb-4 text-lg">
                        <strong>Created At:</strong> {userInfo.createdAt || 'N/A'}
                    </p>
                    <button
                        onClick={handleDeleteAccount}
                        className="w-full bg-red-500 text-white py-2 rounded hover:bg-red-700"
                    >
                        Delete Account
                    </button>
                </div>
            </div>
            <Footer />
        </>
    );
}

export default Profile;
