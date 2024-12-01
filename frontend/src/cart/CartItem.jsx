import React, { useState } from 'react';

function CartItem({ item, onRemove }) {
    const [quantity, setQuantity] = useState(item.quantity);

    const handleQuantityChange = (e) => {
        const newQuantity = parseInt(e.target.value);
        setQuantity(newQuantity);
        // Optionally update the cart in the database here
    };

    const handleRemoveItem = () => {
        onRemove(item.id);
    };

    return (
        <div className="flex justify-between items-center border-b py-4">
            <div className="flex items-center">
                <img src={item.imageUrl} alt={item.name} className="w-16 h-16 mr-4" />
                <div>
                    <p className="font-semibold">{item.name}</p>
                    <p className="text-gray-500">{item.platform}</p>
                </div>
            </div>

            <div className="flex items-center space-x-4">
                <input
                    type="number"
                    value={quantity}
                    onChange={handleQuantityChange}
                    className="border p-2 w-16 text-center"
                    min="1"
                />
                <p className="font-semibold">${(item.price * quantity).toFixed(2)}</p>
                <button
                    onClick={handleRemoveItem}
                    className="text-red-600 hover:underline"
                >
                    Remove
                </button>
            </div>
        </div>
    );
}

export default CartItem;
