import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';

function CheckoutPage() {
    const [formData, setFormData] = useState({
        name: '',
        email: '',
        creditCard: '',
    });
    const [orderData, setOrderData] = useState({
        items: [
            { name: 'Game 1', quantity: 2, price: 20 },
            { name: 'Game 2', quantity: 1, price: 30 },
        ],
        subtotal: 70,
        shipping: 9.99, // Default shipping (can be updated from CartPage)
        tax: 9.10, // 13% HST
        total: 89.09,
    });

    const navigate = useNavigate();

    // Generate Random Order Number
    const generateOrderNumber = () => {
        return 'ORD-' + Math.floor(Math.random() * 1000000);
    };

    // Handle form submission
    const handleSubmit = (e) => {
        e.preventDefault();

        const orderNumber = generateOrderNumber();

        const orderDetails = {
            ...orderData,
            orderNumber: orderNumber,
            orderDate: new Date().toISOString(),
        };

        // Redirect to order confirmation page with order details
        navigate('/order-confirmation', { state: orderDetails });
    };

    return (
        <div className="container mx-auto p-6">
            <h1 className="text-4xl font-bold mb-6">Checkout</h1>
            <form onSubmit={handleSubmit} className="space-y-4">
                <div>
                    <label className="block text-sm font-semibold">Name</label>
                    <input
                        type="text"
                        className="w-full p-2 border border-gray-300 rounded-lg"
                        value={formData.name}
                        onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                        required
                    />
                </div>

                <div>
                    <label className="block text-sm font-semibold">Email</label>
                    <input
                        type="email"
                        className="w-full p-2 border border-gray-300 rounded-lg"
                        value={formData.email}
                        onChange={(e) => setFormData({ ...formData, email: e.target.value })}
                        required
                    />
                </div>

                <div>
                    <label className="block text-sm font-semibold">Credit Card</label>
                    <input
                        type="text"
                        className="w-full p-2 border border-gray-300 rounded-lg"
                        value={formData.creditCard}
                        onChange={(e) => setFormData({ ...formData, creditCard: e.target.value })}
                        required
                    />
                </div>

                <button type="submit" className="bg-blue-500 text-white px-4 py-2 rounded-lg hover:bg-blue-600 mt-4">
                    Complete Purchase
                </button>
            </form>
        </div>
    );
}

export default CheckoutPage;
