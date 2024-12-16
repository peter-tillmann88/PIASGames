import React, { useState, useEffect } from 'react';
import axios from 'axios';
import Header from '../screen/homepage/Header';
import Footer from '../components/Footer';

function Profile() {
    const [userInfo, setUserInfo] = useState({
        email: '',
        phone: '',
        creditCard: '',
        expiryDate: '',
        address: '',
        postalCode: '',
        country: '',
        province: '',
        role: '',
        createdAt: '',
        userID: '',
        username: '',
    });

    const [username, setUsername] = useState('');
    const [isEditing, setIsEditing] = useState(false);
    const [purchaseHistory, setPurchaseHistory] = useState([]);
    const [wishlist, setWishlist] = useState([]);

    useEffect(() => {
        const savedUsername = localStorage.getItem('username');
        if (savedUsername) {
            setUsername(savedUsername);
            fetchUserProfile(savedUsername);
            fetchOrderHistory(); 
            fetchWishlist(savedUsername);
        }
    }, []);

    const fetchUserProfile = async () => {
        try {
            const token = localStorage.getItem('accessToken');

            const response = await axios.get('http://localhost:8080/api/users/profile', {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });
            setUserInfo(response.data);
        } catch (error) {
            alert('Error fetching user profile');
            console.error(error);
        }
    };


    const fetchOrderHistory = async () => {
        try {
            const token = localStorage.getItem('accessToken');
            const response = await axios.get('http://localhost:8080/api/order/history', {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });
            setPurchaseHistory(response.data);
        } catch (error) {
            console.error('Error fetching order history:', error);
        }
    };

    const fetchWishlist = async (username) => {
        try {
            const response = await axios.get(`http://localhost:8080/api/users/wishlist`, {
                params: { username }
            });
            setWishlist(response.data);
        } catch (error) {
            console.error('Error fetching wishlist:', error);
        }
    };

    const handleEditToggle = () => {
        setIsEditing(!isEditing);
    };

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setUserInfo({ ...userInfo, [name]: value });
    };

    const handleSaveChanges = async () => {
        try {
            await axios.put(`http://localhost:8080/api/users/profile`, {
                userID: userInfo.userID,
                username: userInfo.username,
                email: userInfo.email,
                phone: userInfo.phone,
                creditCard: userInfo.creditCard,
                expiryDate: userInfo.expiryDate,
                address: userInfo.address,
                postalCode: userInfo.postalCode,
                country: userInfo.country,
                province: userInfo.province
            }, {
                params: { username }
            });
            alert('Profile updated successfully');
            setIsEditing(false);
            fetchUserProfile(username);
        } catch (error) {
            if (error.response && error.response.status === 409) {
                alert('Email already exists.');
            } else if (error.response && error.response.status === 400) {
                alert('Invalid input data.');
            } else {
                alert('Error updating profile');
            }
            console.error(error);
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
                email: '',
                phone: '',
                creditCard: '',
                expiryDate: '',
                address: '',
                postalCode: '',
                country: '',
                province: '',
                role: '',
                createdAt: '',
                userID: '',
                username: '',
            });
        } catch (error) {
            alert('Error deleting account');
            console.error(error);
        }
    };

    return (
        <>
            <Header />
            <div className="min-h-screen flex flex-col justify-center items-center bg-gray-100 py-12">
                <h2 className="text-3xl font-bold mb-6">Profile</h2>
                <div className="bg-white p-6 rounded shadow-lg w-full max-w-4xl">
                    {isEditing ? (
                        <>
                            <div className="mb-4">
                                <label className="block text-lg font-semibold mb-2">Email</label>
                                <input
                                    type="text"
                                    name="email"
                                    value={userInfo.email}
                                    onChange={handleInputChange}
                                    className="w-full p-2 border border-gray-300 rounded"
                                />
                            </div>
                            <div className="mb-4">
                                <label className="block text-lg font-semibold mb-2">Credit Card #</label>
                                <input
                                    type="text"
                                    name="creditCard"
                                    value={userInfo.creditCard}
                                    onChange={handleInputChange}
                                    className="w-full p-2 border border-gray-300 rounded"
                                    maxLength="16"
                                />
                            </div>
                            <div className="mb-4">
                                <label className="block text-lg font-semibold mb-2">Expiry Date</label>
                                <input
                                    type="text"
                                    name="expiryDate"
                                    value={userInfo.expiryDate}
                                    onChange={handleInputChange}
                                    className="w-full p-2 border border-gray-300 rounded"
                                    placeholder="MM/YY"
                                    maxLength="5"
                                />
                            </div>
                            <div className="mb-4">
                                <label className="block text-lg font-semibold mb-2">Address</label>
                                <input
                                    type="text"
                                    name="address"
                                    value={userInfo.address}
                                    onChange={handleInputChange}
                                    className="w-full p-2 border border-gray-300 rounded"
                                />
                            </div>
                            <div className="mb-4">
                                <label className="block text-lg font-semibold mb-2">Postal Code</label>
                                <input
                                    type="text"
                                    name="postalCode"
                                    value={userInfo.postalCode}
                                    onChange={handleInputChange}
                                    className="w-full p-2 border border-gray-300 rounded"
                                />
                            </div>
                            <div className="mb-4">
                                <label className="block text-lg font-semibold mb-2">Country</label>
                                <input
                                    type="text"
                                    name="country"
                                    value={userInfo.country}
                                    onChange={handleInputChange}
                                    className="w-full p-2 border border-gray-300 rounded"
                                />
                            </div>
                            <div className="mb-4">
                                <label className="block text-lg font-semibold mb-2">Province/State</label>
                                <input
                                    type="text"
                                    name="province"
                                    value={userInfo.province}
                                    onChange={handleInputChange}
                                    className="w-full p-2 border border-gray-300 rounded"
                                />
                            </div>
                            <div className="flex justify-between">
                                <button
                                    onClick={handleSaveChanges}
                                    className="bg-blue-500 text-white py-2 px-4 rounded hover:bg-blue-700"
                                >
                                    Save Changes
                                </button>
                                <button
                                    onClick={handleEditToggle}
                                    className="bg-gray-500 text-white py-2 px-4 rounded hover:bg-gray-700"
                                >
                                    Cancel
                                </button>
                            </div>
                        </>
                    ) : (
                        <>
                            <p className="mb-4 text-lg">
                                <strong>User ID:</strong> {userInfo.userID || 'N/A'}
                            </p>
                            <p className="mb-4 text-lg">
                                <strong>Email:</strong> {userInfo.email || 'N/A'}
                            </p>
                            <p className="mb-4 text-lg">
                                <strong>Phone:</strong> {userInfo.phone || 'N/A'}
                            </p>
                            <p className="mb-4 text-lg">
                                <strong>Credit Card #:</strong> {userInfo.creditCard || 'N/A'}
                            </p>
                            <p className="mb-4 text-lg">
                                <strong>Address:</strong> {userInfo.address || 'N/A'}
                            </p>
                            <p className="mb-4 text-lg">
                                <strong>Postal Code:</strong> {userInfo.postalCode || 'N/A'}
                            </p>
                            <p className="mb-4 text-lg">
                                <strong>Country:</strong> {userInfo.country || 'N/A'}
                            </p>
                            <p className="mb-4 text-lg">
                                <strong>Province/State:</strong> {userInfo.province || 'N/A'}
                            </p>
                            <button
                                onClick={handleEditToggle}
                                className="w-full bg-yellow-500 text-white py-2 rounded hover:bg-yellow-700 mb-4"
                            >
                                Edit Profile
                            </button>
                            <button
                                onClick={handleDeleteAccount}
                                className="w-full bg-red-500 text-white py-2 rounded hover:bg-red-700"
                            >
                                Delete Account
                            </button>
                        </>
                    )}
                </div>

                {/* order history*/}
                <div className="bg-white p-6 mt-6 rounded shadow-lg w-full max-w-4xl">
                    <h3 className="text-2xl font-bold mb-4">Order History</h3>
                    {purchaseHistory.length > 0 ? (
                        purchaseHistory.map((order) => (
                            <div key={order.orderID} className="mb-6 border-b pb-4">
                                <p><strong>Order ID:</strong> {order.orderID}</p>
                                <p><strong>Date:</strong> {new Date(order.orderDate).toLocaleString()}</p>
                                <p><strong>Status:</strong> {order.status}</p>
                                <p><strong>Total Amount:</strong> ${order.totalAmount.toFixed(2)}</p>

                                <h4 className="text-xl font-semibold mt-4 mb-2">Order Items</h4>
                                <ul className="ml-4 list-disc">
                                    {order.orderItems.map((item) => (
                                        <li key={item.orderItemID} className="mb-2">
                                            <p><strong>Product Name:</strong> {item.productName}</p>
                                            <p><strong>Quantity:</strong> {item.quantity}</p>
                                            <p><strong>Price at Purchase:</strong> ${item.priceAtPurchase}</p>
                                        </li>
                                    ))}
                                </ul>
                            </div>
                        ))
                    ) : (
                        <p>No orders found.</p>
                    )}
                </div>

                {/* Wishlist Section
                <div className="bg-white p-6 mt-6 rounded shadow-lg w-full max-w-4xl">
                    <h3 className="text-2xl font-bold mb-4">Wishlist</h3>
                    {wishlist.length > 0 ? (
                        <ul>
                            {wishlist.map((item, index) => (
                                <li key={index} className="mb-2">
                                    <p>
                                        <strong>Product:</strong> {item.productName}
                                    </p>
                                    <p>
                                        <strong>Price:</strong> ${item.price}
                                    </p>
                                </li>
                            ))}
                        </ul>
                    ) : (
                        <p>No wishlist items available.</p>
                    )}
                </div> */}
            </div>
            <Footer />
        </>
    );
}

export default Profile;
