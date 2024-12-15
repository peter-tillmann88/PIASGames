import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

function CheckoutPage() {
    const [formData, setFormData] = useState({
        name: '',
        email: '',
        creditCard: '',
        expiryDate: '',
    });
    const [user, setUser] = useState(null);
    const [orderData, setOrderData] = useState({
        items: [],
        subtotal: 0,
        shipping: 0,
        tax: 0,
        total: 0,
    });
    const navigate = useNavigate();

    useEffect(() => {
        const token = localStorage.getItem('accessToken');
        let userId = localStorage.getItem('userId');

        const fetchUserProfile = async () => {
            if (token && !userId) {
                try {
                    const response = await fetch('http://localhost:8080/api/users/profile', {
                        method: 'GET',
                        headers: {
                            Authorization: `Bearer ${token}`,
                        },
                    });

                    if (!response.ok) {
                        throw new Error('Failed to fetch user profile');
                    }

                    const data = await response.json();
                    userId = data.userID;
                    localStorage.setItem('userId', userId);
                } catch (err) {
                    console.error('Error fetching user profile:', err);
                }
            }

            if (token && userId) {
                setUser({ userId, token });
                fetchCartData(userId, token);
            } else {
                navigate('/login');
            }
        };

        fetchUserProfile();
    }, []);

    const fetchCartData = async (userId, token) => {
        try {
            const response = await fetch(`http://localhost:8080/api/cart/${userId}/cart`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    Authorization: `Bearer ${token}`,
                },
            });

            if (!response.ok) {
                throw new Error('Failed to fetch cart data');
            }

            const data = await response.json();

            const subtotal = data.reduce((acc, item) => acc + item.unit_price * item.quantity, 0);
            const shipping = 9.99;
            const tax = subtotal * 0.13;
            const total = subtotal + shipping + tax;

            setOrderData({
                items: data,
                subtotal,
                shipping,
                tax,
                total,
            });
        } catch (error) {
            console.error('Error fetching cart data:', error);
        }
    };

    const generateOrderNumber = () => {
        return 'ORD-' + Math.floor(Math.random() * 1000000);
    };

    const isValidCreditCard = (creditCard) => {
        const cleanedCard = creditCard.replace(/\D/g, '');
        return /^\d{16}$/.test(cleanedCard);
    };

    const isValidExpiryDate = (expiryDate) => {
        const [month, year] = expiryDate.split('/').map(Number);
        if (!month || !year || month < 1 || month > 12) return false;
        const now = new Date();
        const currentYear = now.getFullYear() % 100;
        const currentMonth = now.getMonth() + 1;
        return year > currentYear || (year === currentYear && month >= currentMonth);
    };

    const handleSubmit = (e) => {
        e.preventDefault();

        if (!isValidCreditCard(formData.creditCard)) {
            alert('Invalid credit card number. Please enter a valid 16-digit credit card number.');
            return;
        }

        if (!isValidExpiryDate(formData.expiryDate)) {
            alert('Invalid expiry date. Please ensure the expiry date is in the future.');
            return;
        }

        const orderNumber = generateOrderNumber();

        const orderDetails = {
            ...orderData,
            orderNumber,
            orderDate: new Date().toISOString(),
        };

        navigate('/order-confirmation', { state: orderDetails });
    };

    return (
        <div className="container mx-auto p-6">
            <h1 className="text-4xl font-bold mb-6">Checkout</h1>

            <div className="mb-6">
                <h2 className="text-2xl font-semibold mb-4">Order Summary</h2>
                {orderData.items.length === 0 ? (
                    <p>Your cart is empty.</p>
                ) : (
                    <div>
                        {orderData.items.map((item, index) => (
                            <div key={index} className="flex justify-between mb-2">
                                <span>{item.product_name} (x{item.quantity})</span>
                                <span>${(item.unit_price * item.quantity).toFixed(2)}</span>
                            </div>
                        ))}
                        <div className="border-t mt-4 pt-4">
                            <div className="flex justify-between">
                                <span>Subtotal:</span>
                                <span>${orderData.subtotal.toFixed(2)}</span>
                            </div>
                            <div className="flex justify-between">
                                <span>Tax (HST 13%):</span>
                                <span>${orderData.tax.toFixed(2)}</span>
                            </div>
                            <div className="flex justify-between">
                                <span>Shipping:</span>
                                <span>${orderData.shipping.toFixed(2)}</span>
                            </div>
                            <div className="flex justify-between font-bold text-lg mt-4">
                                <span>Total:</span>
                                <span>${orderData.total.toFixed(2)}</span>
                            </div>
                        </div>
                    </div>
                )}
            </div>

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
                        onChange={(e) => {
                            const value = e.target.value.replace(/\D/g, '');
                            if (value.length <= 16) {
                                setFormData({ ...formData, creditCard: value });
                            }
                        }}
                        maxLength={16}
                        required
                    />
                </div>

                <div>
                    <label className="block text-sm font-semibold">Expiry Date (MM/YY)</label>
                    <input
                        type="text"
                        className="w-full p-2 border border-gray-300 rounded-lg"
                        value={formData.expiryDate}
                        onChange={(e) => setFormData({ ...formData, expiryDate: e.target.value })}
                        required
                    />
                </div>

                <button
                    type="submit"
                    className="bg-blue-500 text-white px-4 py-2 rounded-lg hover:bg-blue-600 mt-4"
                >
                    Complete Purchase
                </button>
            </form>
        </div>
    );
}

export default CheckoutPage;