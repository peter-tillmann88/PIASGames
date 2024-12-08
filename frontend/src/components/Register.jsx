import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import Header from '../screen/homepage/Header'; // Assuming Header is located in the components folder
import Footer from '../components/Footer'; // Assuming Footer is located in the components folder

function RegisterPage() {
    const [name, setName] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [country, setCountry] = useState('');
    const [province, setProvince] = useState('');
    const [address, setAddress] = useState('');
    const [postalCode, setPostalCode] = useState('');
    const [billingAddress, setBillingAddress] = useState('');
    const [creditCard, setCreditCard] = useState('');
    const [expMonth, setExpMonth] = useState('');
    const [expYear, setExpYear] = useState('');
    const [cvv, setCvv] = useState('');
    const navigate = useNavigate();

    const handleRegister = async (e) => {
        e.preventDefault();
        if (
            name &&
            email &&
            password &&
            country &&
            province &&
            address &&
            postalCode &&
            billingAddress &&
            creditCard &&
            expMonth &&
            expYear &&
            cvv
        ) {
            // Simulate registration logic (you can connect to an API here)
            alert('Registration Successful');
            navigate('/login'); // Redirect to login after registration
        } else {
            alert('Please fill in all fields');
        }
    };

    return (
        <>
            {/* Using the existing Header */}
            <Header />

            {/* Registration Form */}
            <div className="min-h-screen flex flex-col justify-center items-center bg-gray-100 py-12">
                <h2 className="text-3xl font-bold mb-6">Register</h2>
                <form onSubmit={handleRegister} className="bg-white p-6 rounded shadow-lg w-full max-w-4xl">
                    {/* Name */}
                    <div className="mb-4">
                        <label htmlFor="name" className="block text-lg font-semibold mb-2">Name</label>
                        <input
                            type="text"
                            id="name"
                            className="w-full p-2 border border-gray-300 rounded"
                            value={name}
                            onChange={(e) => setName(e.target.value)}
                            placeholder="Enter your name"
                        />
                    </div>

                    {/* Email */}
                    <div className="mb-4">
                        <label htmlFor="email" className="block text-lg font-semibold mb-2">Email</label>
                        <input
                            type="email"
                            id="email"
                            className="w-full p-2 border border-gray-300 rounded"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            placeholder="Enter your email"
                        />
                    </div>

                    {/* Password */}
                    <div className="mb-4">
                        <label htmlFor="password" className="block text-lg font-semibold mb-2">Password</label>
                        <input
                            type="password"
                            id="password"
                            className="w-full p-2 border border-gray-300 rounded"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            placeholder="Enter your password"
                        />
                    </div>

                    {/* Shipping Address */}
                    <div className="mb-4">
                        <label htmlFor="country" className="block text-lg font-semibold mb-2">Country</label>
                        <select
                            id="country"
                            className="w-full p-2 border border-gray-300 rounded"
                            value={country}
                            onChange={(e) => setCountry(e.target.value)}
                        >
                            <option value="">Select Country</option>
                            <option value="US">United States</option>
                            <option value="CA">Canada</option>
                            {/* Add more countries as needed */}
                        </select>
                    </div>

                    <div className="mb-4">
                        <label htmlFor="province" className="block text-lg font-semibold mb-2">Province/State</label>
                        <input
                            type="text"
                            id="province"
                            className="w-full p-2 border border-gray-300 rounded"
                            value={province}
                            onChange={(e) => setProvince(e.target.value)}
                            placeholder="Enter your province or state"
                        />
                    </div>

                    <div className="mb-4">
                        <label htmlFor="address" className="block text-lg font-semibold mb-2">Address</label>
                        <input
                            type="text"
                            id="address"
                            className="w-full p-2 border border-gray-300 rounded"
                            value={address}
                            onChange={(e) => setAddress(e.target.value)}
                            placeholder="Enter your shipping address"
                        />
                    </div>

                    <div className="mb-4">
                        <label htmlFor="postalCode" className="block text-lg font-semibold mb-2">Postal Code</label>
                        <input
                            type="text"
                            id="postalCode"
                            className="w-full p-2 border border-gray-300 rounded"
                            value={postalCode}
                            onChange={(e) => setPostalCode(e.target.value)}
                            placeholder="Enter your postal code"
                        />
                    </div>

                    {/* Billing Address */}
                    <div className="mb-4">
                        <label htmlFor="billingAddress" className="block text-lg font-semibold mb-2">Billing Address</label>
                        <input
                            type="text"
                            id="billingAddress"
                            className="w-full p-2 border border-gray-300 rounded"
                            value={billingAddress}
                            onChange={(e) => setBillingAddress(e.target.value)}
                            placeholder="Enter your billing address"
                        />
                    </div>

                    {/* Credit Card Information */}
                    <div className="mb-4">
                        <label htmlFor="creditCard" className="block text-lg font-semibold mb-2">Credit Card Number</label>
                        <input
                            type="text"
                            id="creditCard"
                            className="w-full p-2 border border-gray-300 rounded"
                            value={creditCard}
                            onChange={(e) => setCreditCard(e.target.value)}
                            placeholder="Enter your credit card number"
                        />
                    </div>

                    <div className="mb-4 flex space-x-4">
                        <div className="w-1/2">
                            <label htmlFor="expMonth" className="block text-lg font-semibold mb-2">Exp Month</label>
                            <input
                                type="text"
                                id="expMonth"
                                className="w-full p-2 border border-gray-300 rounded"
                                value={expMonth}
                                onChange={(e) => setExpMonth(e.target.value)}
                                placeholder="MM"
                                maxLength="2"
                            />
                        </div>
                        <div className="w-1/2">
                            <label htmlFor="expYear" className="block text-lg font-semibold mb-2">Exp Year</label>
                            <input
                                type="text"
                                id="expYear"
                                className="w-full p-2 border border-gray-300 rounded"
                                value={expYear}
                                onChange={(e) => setExpYear(e.target.value)}
                                placeholder="YY"
                                maxLength="2"
                            />
                        </div>
                    </div>

                    <div className="mb-4">
                        <label htmlFor="cvv" className="block text-lg font-semibold mb-2">CVV</label>
                        <input
                            type="text"
                            id="cvv"
                            className="w-full p-2 border border-gray-300 rounded"
                            value={cvv}
                            onChange={(e) => setCvv(e.target.value)}
                            placeholder="Enter CVV"
                            maxLength="3"
                        />
                    </div>

                    {/* Submit Button */}
                    <button
                        type="submit"
                        className="w-full bg-blue-500 text-white py-2 rounded hover:bg-blue-700"
                    >
                        Register
                    </button>
                </form>
            </div>

            {/* Using the existing Footer */}
            <Footer />
        </>
    );
}

export default RegisterPage;
