import React, { useState } from 'react';
import Header from '../components/Header'; // Assuming Header is located in the components folder
import Footer from '../components/Footer'; // Assuming Footer is located in the components folder

function SignIn({ onSignIn }) {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();
        // Add API call for login
        onSignIn({ email });
    };

    return (
        <>
            {/* Using the existing Header */}
            <Header />

            {/* Sign In Form */}
            <div className="min-h-screen flex flex-col justify-center items-center bg-gray-100 py-12">
                <form onSubmit={handleSubmit} className="bg-white p-6 rounded shadow-lg w-full max-w-lg">
                    <h2 className="text-3xl font-bold mb-6 text-center">Sign In</h2>

                    <div className="mb-4">
                        <label htmlFor="email" className="block text-lg font-semibold mb-2">Email</label>
                        <input
                            type="email"
                            id="email"
                            placeholder="Enter your email"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
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
                        Sign In
                    </button>
                </form>
            </div>

            {/* Using the existing Footer */}
            <Footer />
        </>
    );
}

export default SignIn;
