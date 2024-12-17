import React, { useState, useEffect } from 'react';

function CartItem({ item, onRemove, onQuantityUpdate }) {
    const [quantity, setQuantity] = useState(item.quantity);

    useEffect(() => {
        setQuantity(item.quantity);
    }, [item.quantity]);

    const handleQuantityChange = (e) => {
        let newQuantity = parseInt(e.target.value, 10);

        if (isNaN(newQuantity)) {
            newQuantity = 1;
        }

        if (newQuantity > item.maxQuantity) {
            newQuantity = item.maxQuantity;
        } else if (newQuantity < 1) {
            newQuantity = 1;
        }

        setQuantity(newQuantity);
        onQuantityUpdate(item.cartItemId, newQuantity);
    };

    const incrementQuantity = () => {
        if (quantity < item.maxQuantity) {
            const newQuantity = quantity + 1;
            setQuantity(newQuantity);
            onQuantityUpdate(item.cartItemId, newQuantity);
        }
    };

    const decrementQuantity = () => {
        if (quantity > 1) {
            const newQuantity = quantity - 1;
            setQuantity(newQuantity);
            onQuantityUpdate(item.cartItemId, newQuantity);
        }
    };

    return (
        <div className="flex justify-between items-center border-b py-4">
            <div className="flex items-center">
                <img
                    src={item.imageUrl || '/placeholder.jpg'}
                    alt={item.product_name}
                    className="w-16 h-16 mr-4 object-cover"
                />
                <div>
                    <p className="font-semibold">{item.product_name}</p>
                    <p className="text-gray-500">Price: ${item.unit_price.toFixed(2)}</p>
                </div>
            </div>
            <div className="flex items-center space-x-4">
                <div className="flex items-center space-x-2">
                    <button
                        onClick={decrementQuantity}
                        className="px-2 py-1 border rounded-md bg-gray-200 hover:bg-gray-300"
                        disabled={quantity <= 1}
                    >
                        -
                    </button>
                    <input
                        type="number"
                        value={quantity}
                        onChange={handleQuantityChange}
                        className="border p-2 w-16 text-center"
                        min="1"
                        max={item.maxQuantity}
                    />
                    <button
                        onClick={incrementQuantity}
                        className="px-2 py-1 border rounded-md bg-gray-200 hover:bg-gray-300"
                        disabled={quantity >= item.maxQuantity}
                    >
                        +
                    </button>
                </div>
                <p className="font-semibold">
                    ${(item.unit_price * quantity).toFixed(2)}
                </p>
                <button
                    onClick={() => onRemove(item.cartItemId)}
                    className="text-red-600 hover:underline"
                >
                    Remove
                </button>
            </div>
            {item.maxQuantity < item.quantity && (
                <p className="text-red-500 text-sm mt-2">
                    Only {item.maxQuantity} items available.
                </p>
            )}
        </div>
    );
}

export default CartItem;
