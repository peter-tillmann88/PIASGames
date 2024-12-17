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
    const [billingInfo, setBillingInfo] = useState({
        creditCard: '',
        address: '',
        postalCode: '',
        province: '',
        country: '',
    });
    const [tempBilling, setTempBilling] = useState(false);
    const [attemptCount, setAttemptCount] = useState(0);

    const navigate = useNavigate();

    useEffect(() => {
        const token = localStorage.getItem('accessToken');
        let userID = null;

        const fetchUserProfile = async () => {
            if (token && !userID) {
                try {
                    const response = await fetch(`${import.meta.env.VITE_API_URL}/users/profile`, {
                        method: 'GET',
                        headers: { Authorization: `Bearer ${token}` },
                    });

                    if (!response.ok) {
                        throw new Error('Failed to fetch user profile');
                    }

                    const data = await response.json();
                    userID = data.userID;
                    localStorage.setItem('userID', userID);

                    setBillingInfo({
                        creditCard: data.creditCard || '',
                        address: data.address || '',
                        postalCode: data.postalCode || '',
                        province: data.province || '',
                        country: data.country || '',
                    });

                    setUser({ userID, token });
                    fetchCartData(userID, token);
                } catch (err) {
                    console.error('Error fetching user profile:', err);
                }
            } else {
                navigate('/login');
            }
        };

        fetchUserProfile();
    }, [navigate]);

    const fetchCartData = async (userID, token) => {
        try {
            const response = await fetch(`${import.meta.env.VITE_API_URL}/cart/${userID}/cart`, {
                method: 'GET',
                headers: { Authorization: `Bearer ${token}` },
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

    const handleBillingChange = (e) => {
        const { name, value } = e.target;
        setBillingInfo({ ...billingInfo, [name]: value });
    };

    const handleCompletePurchase = async () => {
        const currentAttempt = attemptCount + 1;
        setAttemptCount(currentAttempt);

        if (currentAttempt % 3 === 0) {
            alert('Credit Card Authorization Failed.');
            return;
        }

        try {
            const response = await fetch(`${import.meta.env.VITE_API_URL}/order/checkout`, {
                method: 'POST',
                headers: { Authorization: `Bearer ${user.token}` },
            });

            if (!response.ok) {
                throw new Error('Failed to complete purchase');
            }

            const order = await response.json();
            const orderDetails = {
                items: orderData.items.map((item) => ({
                    name: item.product_name,
                    price: item.unit_price,
                    quantity: item.quantity,
                })),
                subtotal: orderData.subtotal,
                shipping: orderData.shipping,
                tax: orderData.tax,
                total: orderData.total,
                orderNumber: `ORD-${order.orderID}`,
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
                                <span>
                                    {item.product_name} (x{item.quantity})
                                </span>
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

            <h2 className="text-2xl font-semibold mb-4">Billing Information</h2>
            <form>
                {['creditCard', 'address', 'postalCode', 'province', 'country'].map((field) => (
                    <label key={field} className="block mb-2">
                        {field.charAt(0).toUpperCase() + field.slice(1)}:
                        <input
                            type="text"
                            name={field}
                            value={billingInfo[field]}
                            onChange={handleBillingChange}
                            className="block w-full p-2 border rounded"
                            disabled={!tempBilling}
                        />
                    </label>
                ))}
                <label className="flex items-center mb-4">
                    <input
                        type="checkbox"
                        checked={tempBilling}
                        onChange={() => setTempBilling(!tempBilling)}
                        className="mr-2"
                    />
                    Use Temporary Billing Information
                </label>
            </form>

            <div className="flex gap-4">
                <button
                    onClick={handleCompletePurchase}
                    className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
                >
                    Complete Purchase
                </button>
                <button
                    onClick={() => navigate('/')}
                    className="bg-gray-500 text-white px-4 py-2 rounded hover:bg-gray-600"
                >
                    Continue Shopping
                </button>
            </div>
        </div>
    );
}

export default CheckoutPage;
