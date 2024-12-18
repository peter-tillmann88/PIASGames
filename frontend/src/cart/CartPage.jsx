import React, { useState, useEffect } from 'react';
import CartItem from '../cart/CartItem';
import { useNavigate } from 'react-router-dom';
import Header from '../screen/homepage/Header';
import Footer from '../components/Footer';
import axios from 'axios';

function CartPage() {
    const [cartItems, setCartItems] = useState([]);
    const [user, setUser] = useState(null);
    const [shippingOption, setShippingOption] = useState('regular');
    const navigate = useNavigate();

    useEffect(() => {
        const token = localStorage.getItem('accessToken');
        if (token) {
            fetch(`${import.meta.env.VITE_API_URL}/users/profile`, {
                method: 'GET',
                headers: { Authorization: `Bearer ${token}` },
            })
                .then(res => {
                    if (!res.ok) throw new Error('Failed to fetch user profile');
                    return res.json();
                })
                .then(data => {
                    setUser({ userID: data.userID, token });
                })
                .catch(err => console.error('Error fetching user profile:', err));
        }
    }, []);

    const fetchProductStock = async (productId) => {
        if (!productId) return 0;
        try {
            const response = await axios.get(`${import.meta.env.VITE_API_URL}/products/get/${productId}`);
            if (response.status === 200) {
                return response.data.stock || 0;
            }
            return 0;
        } catch (error) {
            console.error(`Error fetching stock for product ${productId}:`, error);
            return 0;
        }
    };

    const fetchSignedUrls = async (items) => {
        const signedUrlPromises = items.map(async (item) => {
            if (!item.image_name) {
                return { ...item, imageUrl: '/placeholder.jpg' };
            }

            const fileName = encodeURIComponent(item.image_name);
            try {
                const response = await fetch(`${import.meta.env.VITE_IMAGE_SERVER_URL}/generate-signed-url?bucketName=product-images&fileName=${fileName}`);
                if (!response.ok) {
                    console.error('Failed to fetch signed URL:', await response.text());
                    return { ...item, imageUrl: '/placeholder.jpg' };
                }

                const data = await response.json();
                return { ...item, imageUrl: data.signedUrl || '/placeholder.jpg' };
            } catch (err) {
                console.error('Error fetching signed URL:', err);
                return { ...item, imageUrl: '/placeholder.jpg' };
            }
        });

        return Promise.all(signedUrlPromises);
    };

    useEffect(() => {
        const fetchCartItems = async () => {
            let fetchedCartItems = [];

            if (user) {
                const tempCart = JSON.parse(localStorage.getItem('tempCart')) || [];
                if (tempCart.length > 0) {
                    for (const item of tempCart) {
                        try {
                            await axios.post(
                                `${import.meta.env.VITE_API_URL}/cart-items/cart/${user.userID}/item/${item.id}/add`,
                                null,
                                {
                                    headers: { Authorization: `Bearer ${user.token}` },
                                    params: { quantity: item.quantity },
                                }
                            );
                        } catch (error) {
                            console.error(`Error adding temp cart item (Product ID: ${item.id}) to backend:`, error);
                        }
                    }
                    localStorage.removeItem('tempCart');
                }

                try {
                    const response = await axios.get(`${import.meta.env.VITE_API_URL}/cart/${user.userID}/cart`, {
                        headers: { Authorization: `Bearer ${user.token}` },
                    });

                    fetchedCartItems = response.data.map(item => ({
                        ...item,
                        cartItemId: item.cart_itemid,
                        product_name: item.product_name || item.name || 'Unknown Product',
                        unit_price: Number(item.unit_price) || 0,
                        quantity: item.quantity,
                        productId: item.productid,
                        image_name: item.image_name || null
                    }));
                } catch (err) {
                    console.error('Error fetching cart items for logged in user:', err);
                }

            } else {
                const tempCart = JSON.parse(localStorage.getItem('tempCart')) || [];
                fetchedCartItems = tempCart.map(item => ({
                    ...item,
                    cartItemId: item.cartItemId || `temp-${item.id}-${Date.now()}`,
                    unit_price: Number(item.price) || 0,
                    product_name: item.name || 'Unknown Product',
                    productId: item.id,
                    image_name: null,
                    imageUrl: item.imageUrl || '/placeholder.jpg'
                }));
            }

            let cartItemsWithImages = fetchedCartItems;
            if (user) {
                cartItemsWithImages = await fetchSignedUrls(fetchedCartItems);
            }

            const cartItemsWithStock = await Promise.all(
                cartItemsWithImages.map(async (item) => {
                    const stock = await fetchProductStock(item.productId);
                    return { ...item, maxQuantity: stock };
                })
            );

            setCartItems(cartItemsWithStock);
        };

        fetchCartItems();
    }, [user]);

    const handleQuantityUpdate = async (cartItemId, newQuantity) => {
        const updatedCart = cartItems.map((item) => {
            if (item.cartItemId === cartItemId) {
                return { ...item, quantity: newQuantity };
            }
            return item;
        });

        setCartItems(updatedCart);

        if (!user) {
            localStorage.setItem('tempCart', JSON.stringify(updatedCart));
        } else {
            const cartItem = updatedCart.find(item => item.cartItemId === cartItemId);
            if (cartItem) {
                try {
                    await axios.patch(
                        `${import.meta.env.VITE_API_URL}/cart-items/cart/${user.userID}/item/${cartItem.productId}/update-quantity`,
                        null,
                        {
                            headers: { Authorization: `Bearer ${user.token}` },
                            params: { newQuantity },
                        }
                    );
                    console.log('Quantity updated successfully for Product ID:', cartItem.productId);
                } catch (error) {
                    console.error('Error updating quantity:', error);
                }
            }
        }
    };

    const handleRemoveItem = (cartItemId) => {
        if (user) {
            const cartItem = cartItems.find(item => item.cartItemId === cartItemId);
            if (cartItem) {
                axios.delete(`${import.meta.env.VITE_API_URL}/cart-items/cart/${user.userID}/item/${cartItem.productId}/del`, {
                    headers: { Authorization: `Bearer ${user.token}` },
                })
                    .then(() => {
                        setCartItems(cartItems.filter(item => item.cartItemId !== cartItemId));
                        console.log(`Removed Product ID: ${cartItem.productId} from cart.`);
                    })
                    .catch(err => console.error('Error removing item:', err));
            } else {
                console.error('Cart item not found:', cartItemId);
            }
        } else {
            const updatedCart = cartItems.filter(item => item.cartItemId !== cartItemId);
            localStorage.setItem('tempCart', JSON.stringify(updatedCart));
            setCartItems(updatedCart);
            console.log(`Removed Temp Cart Item ID: ${cartItemId} from localStorage.`);
        }
    };

    const calculateSubtotal = () => cartItems.reduce((sum, item) => sum + item.unit_price * item.quantity, 0);
    const calculateTax = (subtotal) => subtotal * 0.13;
    const calculateShipping = () => (shippingOption === 'express' ? 19.99 : 9.99);
    const calculateTotal = () => {
        const subtotal = calculateSubtotal();
        return subtotal + calculateTax(subtotal) + calculateShipping();
    };

    const handleProceedToCheckout = async () => {
        if (user) {
            try {
                await Promise.all(cartItems.map(async (item) => {
                    await axios.patch(
                        `${import.meta.env.VITE_API_URL}/cart-items/cart/${user.userID}/item/${item.productId}/update-quantity`,
                        null,
                        {
                            headers: { Authorization: `Bearer ${user.token}` },
                            params: { newQuantity: item.quantity },
                        }
                    );
                }));
                navigate('/checkout');
            } catch (error) {
                console.error('Error updating quantities before checkout:', error);
                alert('There was an error processing your cart. Please try again.');
            }
        } else {
            navigate('/login');
        }
    };

    return (
        <div className="flex flex-col min-h-screen">
            <Header />
            <div className="p-6 flex-grow">
                <h1 className="text-4xl font-bold mb-6">Your Cart</h1>
                {cartItems.length === 0 ? (
                    <p>Your cart is empty.</p>
                ) : (
                    cartItems.map(item => (
                        <CartItem
                            key={item.cartItemId}
                            item={item}
                            onRemove={handleRemoveItem}
                            onQuantityUpdate={handleQuantityUpdate}
                        />
                    ))
                )}
                <div className="mt-6">
                    <h2 className="text-lg font-semibold">Shipping Options</h2>
                    <div className="flex items-center mt-2">
                        <input
                            type="radio"
                            id="regular"
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
                            value="express"
                            checked={shippingOption === 'express'}
                            onChange={() => setShippingOption('express')}
                        />
                        <label htmlFor="express" className="ml-2">Express Shipping ($19.99)</label>
                    </div>
                </div>
                <div className="mt-6 text-lg">
                    <p>Subtotal: ${calculateSubtotal().toFixed(2)}</p>
                    <p>Tax (13%): ${calculateTax(calculateSubtotal()).toFixed(2)}</p>
                    <p>Shipping: ${calculateShipping().toFixed(2)}</p>
                </div>
                <div className="text-2xl font-bold mt-4">
                    Total: ${calculateTotal().toFixed(2)}
                </div>
                <button
                    onClick={handleProceedToCheckout}
                    className="mt-6 bg-blue-600 text-white px-4 py-2 rounded mr-4"
                >
                    Proceed to Checkout
                </button>

                <button
                    onClick={() => navigate('/')}
                    className="mt-6 bg-green-600 text-white px-4 py-2 rounded"
                >
                    Continue Shopping
                </button>

            </div>
            <Footer />
        </div>
    );
}

export default CartPage;