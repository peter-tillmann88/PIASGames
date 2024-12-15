import React, { useState, useEffect } from 'react';
import CartItem from '../cart/CartItem';
import { useNavigate } from 'react-router-dom';
import Header from '../screen/homepage/Header';
import Footer from '../components/Footer';

function CartPage() {
    const [cartItems, setCartItems] = useState([]);
    const [originalCartItems, setOriginalCartItems] = useState([]); // To track original quantities
    const [user, setUser] = useState(null);
    const [shippingOption, setShippingOption] = useState('regular');
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
                            'Authorization': `Bearer ${token}`,
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
            } else {
                setUser(null);
            }
        };

        fetchUserProfile();
    }, []);

    useEffect(() => {
        if (user !== null) {
            fetchCartItems();
        } else {
            const tempCart = JSON.parse(localStorage.getItem('tempCart')) || [];
            const updatedTempCart = tempCart.map(item => ({ ...item, imageUrl: '/placeholder.jpg' }));
            setCartItems(updatedTempCart);
        }
    }, [user]);

    const fetchSignedUrls = async (items) => {
        const signedUrlPromises = items.map(async (item) => {
            const fileName = item.image_name;
            if (!fileName) {
                return { ...item, imageUrl: '/placeholder.jpg' };
            }

            const response = await fetch(`http://localhost:3000/generate-signed-url?bucketName=product-images&fileName=${encodeURIComponent(fileName)}`);
            if (!response.ok) {
                console.error('Failed to fetch signed URL:', await response.text());
                return { ...item, imageUrl: '/placeholder.jpg' };
            }

            const data = await response.json();
            return { ...item, imageUrl: data.signedUrl || '/placeholder.jpg' };
        });

        return Promise.all(signedUrlPromises);
    };

    const fetchCartItems = async () => {
        if (!user) {
            return;
        }

        try {
            const response = await fetch(`http://localhost:8080/api/cart/${user.userId}/cart`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    Authorization: `Bearer ${user.token}`,
                },
            });

            if (!response.ok) {
                throw new Error('Failed to fetch cart items');
            }

            const data = await response.json();

            const cartItemsFromServer = data.map(item => ({
                ...item,
                cartItemId: item.cart_itemid,
                price: item.unit_price,
            }));

            const cartItemsWithImages = await fetchSignedUrls(cartItemsFromServer);
            setCartItems(cartItemsWithImages);
            setOriginalCartItems(cartItemsWithImages.map(item => ({ ...item }))); // Save original state
        } catch (error) {
            console.error('Error fetching cart items:', error);
        }
    };

    const handleRemoveItem = async (cartItemId) => {
        if (!user) {
            const tempCart = JSON.parse(localStorage.getItem('tempCart')) || [];
            const updatedCart = tempCart.filter(item => item.cartItemId !== cartItemId);
            localStorage.setItem('tempCart', JSON.stringify(updatedCart));
            setCartItems(updatedCart);
            return;
        }

        try {
            await fetch(`http://localhost:8080/api/cart/item/${cartItemId}`, {
                method: 'DELETE',
                headers: {
                    Authorization: `Bearer ${user.token}`,
                },
            });
            setCartItems(cartItems.filter(item => item.cartItemId !== cartItemId));
        } catch (error) {
            console.error('Error removing item:', error);
        }
    };

    const handleUpdateQuantities = async () => {
        const updates = cartItems.filter((item, index) => {
            return item.quantity !== originalCartItems[index].quantity;
        });

        const updatePromises = updates.map(async (item) => {
            await fetch(`http://localhost:8080/api/cart/item/${item.cartItemId}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    Authorization: `Bearer ${user.token}`,
                },
                body: JSON.stringify({ quantity: item.quantity }),
            });
        });

        try {
            await Promise.all(updatePromises);
        } catch (error) {
            console.error('Error updating quantities:', error);
        }
    };

    const calculateSubtotal = () => {
        return cartItems.reduce((acc, item) => acc + item.unit_price * item.quantity, 0);
    };

    const calculateTax = (subtotal) => {
        return subtotal * 0.13;
    };

    const calculateShipping = () => {
        return shippingOption === 'express' ? 19.99 : 9.99;
    };

    const calculateTotal = (subtotal, tax, shipping) => {
        return subtotal + tax + shipping;
    };

    const handleCheckout = async () => {
        if (!user) {
            alert('You must be logged in to proceed to checkout.');
            navigate('/login');
            return;
        }

        await handleUpdateQuantities(); // Update quantities before checkout
        navigate('/checkout');
    };

    return (
        <div className="flex flex-col min-h-screen">
            <Header />
            <div className="p-6 flex-grow">
                <h1 className="text-4xl font-bold mb-6">Your Cart</h1>
                {cartItems.length === 0 ? (
                    <p>Your cart is empty.</p>
                ) : (
                    <div>
                        {cartItems.map((item, index) => (
                            <CartItem
                                key={item.cartItemId}
                                item={item}
                                onRemove={handleRemoveItem}
                                onQuantityChange={(newQuantity) => {
                                    const updatedCart = [...cartItems];
                                    updatedCart[index].quantity = newQuantity;
                                    setCartItems(updatedCart);
                                }}
                            />
                        ))}
                    </div>
                )}

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
            <Footer />
        </div>
    );
}

export default CartPage;shab