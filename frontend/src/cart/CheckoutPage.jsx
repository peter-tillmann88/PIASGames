import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

function CheckoutPage() {
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
        let userID = localStorage.getItem('userID');

        const fetchUserProfile = async () => {
            if (token && !userID) {
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
                    userID = data.userID;
                    localStorage.setItem('userID', userID);
                } catch (err) {
                    console.error('Error fetching user profile:', err);
                }
            }

            if (token && userID) {
                setUser({ userID, token });
                fetchCartData(userID, token);
            } else {
                navigate('/login');
            }
        };

        fetchUserProfile();
    }, [navigate]);

    const fetchCartData = async (userID, token) => {
        try {
            const response = await fetch(`http://localhost:8080/api/cart/${userID}/cart`, {
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

    const handleCompletePurchase = async () => {
        if (!user) return;

        try {
            const response = await fetch('http://localhost:8080/api/order/checkout', {
                method: 'POST',
                headers: {
                    Authorization: `Bearer ${user.token}`,
                },
            });

            if (!response.ok) {
                // Attempt to parse the error message from the response
                let errorMessage = 'Failed to complete purchase.';
                try {
                    const errorData = await response.json();
                    // Adjust the key based on your backend's error response structure
                    errorMessage = errorData.message || errorMessage;
                } catch (parseError) {
                    console.error('Error parsing error response:', parseError);
                }
                throw new Error(`Failed to complete purchase. ${errorMessage}`);
            }

            const order = await response.json();
            // Assuming the order JSON contains orderID, orderDate, and other info.
            // If it doesn't, you'll need to adjust how you generate the order number/date.
            const orderNumber = "ORD-" + order.orderID;
            const orderDetails = {
                items: orderData.items.map(item => ({
                    name: item.product_name,
                    price: item.unit_price,
                    quantity: item.quantity
                })),
                subtotal: orderData.subtotal,
                shipping: orderData.shipping,
                tax: orderData.tax,
                total: orderData.total,
                orderNumber,
                orderDate: order.orderDate || new Date().toISOString(),
            };

            navigate('/order-confirmation', { state: orderDetails });
        } catch (error) {
            console.error('Error completing purchase:', error);
            alert('Failed to complete purchase. Please try again.');
        }
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

            {/* Removed the CVV input field as it references undefined `formData` */}
            {/* <div>
                <label className="block text-sm font-semibold">CVV for credit card registered on Account</label>
                <input
                    type="text"
                    className="w-full p-2 border border-gray-300 rounded-lg"
                    value={formData.expiryDate}
                    onChange={(e) => setFormData({ ...formData, expiryDate: e.target.value })}
                    required
                />
            </div> */}

            <button
                onClick={handleCompletePurchase}
                className="bg-blue-500 text-white px-4 py-2 rounded-lg hover:bg-blue-600 mt-4"
            >
                Complete Purchase
            </button>
        </div>
    );
}

export default CheckoutPage;
