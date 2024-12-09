import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import Header from '../screen/homepage/Header';
import Footer from '../components/Footer';

function RegisterPage() {
    const [formData, setFormData] = useState({
        username: '',
        email: '',
        password: '',
        phone: '',
        creditCard: '',
        expiryDate: '',
        country: 'Canada',
        province: '',
        address: '',
        postalCode: ''
    });

    const navigate = useNavigate();

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    const handleRegister = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.post('http://localhost:8080/api/users/register', formData);
            localStorage.setItem('username', response.data.username);
            alert('Registration successful');
            navigate('/login');
        } catch (error) {
            alert('Error during registration');
        }
    };

    return (
        <>
            <Header />
            <div className="min-h-screen flex flex-col justify-center items-center bg-gray-100 py-12">
                <h2 className="text-3xl font-bold mb-6">Register</h2>
                <form onSubmit={handleRegister} className="bg-white p-6 rounded shadow-lg w-full max-w-4xl">
                    <div className="mb-4">
                        <label htmlFor="username" className="block text-lg font-semibold mb-2">Username</label>
                        <input
                            type="text"
                            id="username"
                            name="username"
                            className="w-full p-2 border border-gray-300 rounded"
                            value={formData.username}
                            onChange={handleInputChange}
                            placeholder="Enter your username"
                        />
                    </div>
                    <div className="mb-4">
                        <label htmlFor="email" className="block text-lg font-semibold mb-2">Email</label>
                        <input
                            type="email"
                            id="email"
                            name="email"
                            className="w-full p-2 border border-gray-300 rounded"
                            value={formData.email}
                            onChange={handleInputChange}
                            placeholder="Enter your email"
                        />
                    </div>
                    <div className="mb-4">
                        <label htmlFor="password" className="block text-lg font-semibold mb-2">Password</label>
                        <input
                            type="password"
                            id="password"
                            name="password"
                            className="w-full p-2 border border-gray-300 rounded"
                            value={formData.password}
                            onChange={handleInputChange}
                            placeholder="Enter your password"
                        />
                    </div>
                    <div className="mb-4">
                        <label htmlFor="phone" className="block text-lg font-semibold mb-2">Phone</label>
                        <input
                            type="text"
                            id="phone"
                            name="phone"
                            className="w-full p-2 border border-gray-300 rounded"
                            value={formData.phone}
                            onChange={handleInputChange}
                            placeholder="Enter your phone number"
                        />
                    </div>
                    <div className="mb-4">
                        <label htmlFor="creditCard" className="block text-lg font-semibold mb-2">Credit Card Number</label>
                        <input
                            type="text"
                            id="creditCard"
                            name="creditCard"
                            className="w-full p-2 border border-gray-300 rounded"
                            value={formData.creditCard}
                            onChange={handleInputChange}
                            placeholder="Enter your credit card number"
                            maxLength="16"
                        />
                    </div>
                    <div className="mb-4">
                        <label htmlFor="expiryDate" className="block text-lg font-semibold mb-2">Expiry Date</label>
                        <input
                            type="text"
                            id="expiryDate"
                            name="expiryDate"
                            className="w-full p-2 border border-gray-300 rounded"
                            value={formData.expiryDate}
                            onChange={handleInputChange}
                            placeholder="MM/YY"
                            maxLength="5"
                        />
                    </div>
                    <div className="mb-4">
                        <label htmlFor="country" className="block text-lg font-semibold mb-2">Country</label>
                        <select
                            id="country"
                            name="country"
                            className="w-full p-2 border border-gray-300 rounded"
                            value={formData.country}
                            onChange={handleInputChange}
                        >
                            <option value="Canada">Canada</option>
                            <option value="US">United States</option>
                        </select>
                    </div>
                    <div className="mb-4">
                        <label htmlFor="province" className="block text-lg font-semibold mb-2">Province/State</label>
                        <input
                            type="text"
                            id="province"
                            name="province"
                            className="w-full p-2 border border-gray-300 rounded"
                            value={formData.province}
                            onChange={handleInputChange}
                            placeholder="Enter your province or state"
                        />
                    </div>
                    <div className="mb-4">
                        <label htmlFor="address" className="block text-lg font-semibold mb-2">Address</label>
                        <input
                            type="text"
                            id="address"
                            name="address"
                            className="w-full p-2 border border-gray-300 rounded"
                            value={formData.address}
                            onChange={handleInputChange}
                            placeholder="Enter your address"
                        />
                    </div>
                    <div className="mb-4">
                        <label htmlFor="postalCode" className="block text-lg font-semibold mb-2">Postal Code</label>
                        <input
                            type="text"
                            id="postalCode"
                            name="postalCode"
                            className="w-full p-2 border border-gray-300 rounded"
                            value={formData.postalCode}
                            onChange={handleInputChange}
                            placeholder="Enter your postal code"
                        />
                    </div>
                    <button
                        type="submit"
                        className="w-full bg-blue-500 text-white py-2 rounded hover:bg-blue-700"
                    >
                        Register
                    </button>
                </form>
            </div>
            <Footer />
        </>
    );
}

export default RegisterPage;
