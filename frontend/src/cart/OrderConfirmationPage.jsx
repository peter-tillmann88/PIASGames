import React from 'react';
import { useLocation, Link } from 'react-router-dom';

function OrderConfirmationPage() {
    const location = useLocation();
    const orderData = location.state || {};

    return (
        <div className="container mx-auto p-6">
            <h1 className="text-4xl font-bold text-center mb-6">Order Confirmation</h1>
            <div className="bg-white p-6 rounded-lg shadow-lg">
                <h2 className="text-2xl font-semibold mb-4">Thank you for your purchase!</h2>

                <p className="mb-4">Your order has been successfully placed. Here are the details:</p>
                <h3 className="text-xl font-semibold">Order Number: {orderData.orderNumber}</h3>
                <p className="text-gray-500">Order Date: {new Date(orderData.orderDate).toLocaleDateString()}</p>

                <h3 className="text-xl font-semibold mb-4">Order Summary:</h3>
                <ul>
                    {orderData.items?.map((item, index) => (
                        <li key={index} className="flex justify-between">
                            <span>{item.name} (x{item.quantity})</span>
                            <span>${(item.price * item.quantity).toFixed(2)}</span>
                        </li>
                    ))}
                </ul>

                <div className="flex justify-between mt-6">
                    <p>Subtotal:</p>
                    <p>${orderData.subtotal.toFixed(2)}</p>
                </div>
                <div className="flex justify-between">
                    <p>Shipping:</p>
                    <p>${orderData.shipping.toFixed(2)}</p>
                </div>
                <div className="flex justify-between">
                    <p>Tax (13% HST):</p>
                    <p>${orderData.tax.toFixed(2)}</p>
                </div>
                <div className="flex justify-between mt-4 font-semibold">
                    <p>Total:</p>
                    <p>${orderData.total.toFixed(2)}</p>
                </div>

                <div className="mt-6 text-center">
                    <Link to="/">
                        <button className="bg-green-500 text-white px-4 py-2 rounded-lg hover:bg-green-600">
                            Continue Shopping
                        </button>
                    </Link>
                </div>
            </div>
        </div>
    );
}

export default OrderConfirmationPage;
