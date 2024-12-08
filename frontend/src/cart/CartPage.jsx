import React, { useState, useEffect } from 'react';
import CartItem from '../cart/CartItem'; // Ensure correct path
import { Link, useNavigate } from 'react-router-dom';
import Header from '../screen/homepage/Header'; // Assuming Header component is in ../components
import Footer from '../components/Footer'; // Assuming Footer component is in ../components

function CartPage() {
    const [cartItems, setCartItems] = useState([]);
    const [user, setUser] = useState(null); // Track user (if signed in)
    const [shippingOption, setShippingOption] = useState('regular'); // Regular shipping by default
    const navigate = useNavigate();

    useEffect(() => {
        // Fetch cart items from database (replace with actual API endpoint)
        if (user) {
            fetchCartItems();
        }
    }, [user]);

    const fetchCartItems = async () => {
        try {
            const response = await fetch('/api/cart', {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${user.token}`, // Assuming JWT token
                },
            });
            const data = await response.json();
            setCartItems(data);
        } catch (error) {
            console.error('Error fetching cart items:', error);
        }
    };

    const handleRemoveItem = async (itemId) => {
        try {
            await fetch(`/api/cart/${itemId}`, {
                method: 'DELETE',
                headers: {
                    'Authorization': `Bearer ${user.token}`,
                },
            });
            setCartItems(cartItems.filter(item => item.id !== itemId)); // Remove item from state
        } catch (error) {
            console.error('Error removing item:', error);
        }
    };

    const calculateSubtotal = () => {
        return cartItems.reduce((acc, item) => acc + item.price * item.quantity, 0);
    };

    const calculateTax = (subtotal) => {
        return subtotal * 0.13; // HST 13%
    };

    const calculateShipping = () => {
        return shippingOption === 'express' ? 19.99 : 9.99; // Express costs $19.99, regular is $9.99
    };

    const calculateTotal = (subtotal, tax, shipping) => {
        return subtotal + tax + shipping;
    };

    const handleCheckout = () => {
        // Proceed with checkout, redirect to the checkout page or call an API
        if (!user) {
            alert('You must be signed in to proceed to checkout.');
            navigate('/login'); // Redirect to login page if not signed in
        } else {
            // Redirect or call checkout API
            navigate('/checkout');
        }
    };

    return (
        <div className="flex flex-col min-h-screen"> {/* Use flexbox to ensure footer is at bottom */}
            <Header /> {/* Include the header here */}

            <div className="p-6 flex-grow"> {/* Allow content to grow to fill space */}
                <h1 className="text-4xl font-bold mb-6">Your Cart</h1>
                {cartItems.length === 0 ? (
                    <p>Your cart is empty.</p>
                ) : (
                    <div>
                        {cartItems.map((item) => (
                            <CartItem
                                key={item.id}
                                item={item}
                                onRemove={handleRemoveItem}
                            />
                        ))}
                    </div>
                )}

                {/* Shipping options */}
                <div className="mt-6">
                    <h2 className="text-lg font-semibold">Shipping Options</h2>
                    <div className="flex items-center mt-2">
                        <input
                            type="radio"
                            id="regular"
                            name="shippingOption"
                            value="regular"
                            checked={shippingOption === 'regular'}
                            onChange={() => setShippingOption('regular')}
                        />
                        <label htmlFor="regular" className="ml-2">Regular Shipping ($9.99)</label>
                    </div>
                    <div className="flex items-center mt-2">
                        <input
                            type="radio"
                            id="express"
                            name="shippingOption"
                            value="express"
                            checked={shippingOption === 'express'}
                            onChange={() => setShippingOption('express')}
                        />
                        <label htmlFor="express" className="ml-2">Express Shipping ($19.99)</label>
                    </div>
                </div>

                {/* Cart Price Breakdown */}
                <div className="mt-6 flex justify-between text-lg font-semibold">
                    <p>Subtotal: ${calculateSubtotal().toFixed(2)}</p>
                    <p>Tax (HST 13%): ${calculateTax(calculateSubtotal()).toFixed(2)}</p>
                    <p>Shipping: ${calculateShipping().toFixed(2)}</p>
                </div>
                <div className="mt-4 text-2xl font-bold flex justify-between">
                    <p>Total: ${calculateTotal(calculateSubtotal(), calculateTax(calculateSubtotal()), calculateShipping()).toFixed(2)}</p>
                </div>

                <div className="mt-6">
                    <button
                        className="bg-blue-600 text-white py-2 px-4 rounded"
                        onClick={handleCheckout}
                    >
                        Proceed to Checkout
                    </button>
                </div>
            </div>

            <Footer /> {/* Include the footer here */}
        </div>
    );
}

export default CartPage;
