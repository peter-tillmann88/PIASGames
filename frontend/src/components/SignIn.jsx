import React, { useState } from 'react';
import Header from '../components/Header'; 
import Footer from '../components/Footer'; 
function SignIn({ onSignIn }) {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();
        onSignIn({ email });
    };

    return (
        <>
            <Header />

            {/* sign in form*/}
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
            <Footer />
        </>
    );
}

export default SignIn;
