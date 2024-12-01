import React, { useState, useEffect } from 'react';

function Profile() {
    const [userInfo, setUserInfo] = useState({
        name: '',
        email: '',
        address: '',
        creditCard: ''
    });

    useEffect(() => {
        // Simulate fetching user info from an API
        const user = JSON.parse(localStorage.getItem('userInfo')) || {};
        setUserInfo(user);
    }, []);

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setUserInfo({ ...userInfo, [name]: value });
    };

    const handleSave = () => {
        // Save updated info to API or localStorage
        localStorage.setItem('userInfo', JSON.stringify(userInfo));
        alert('Profile updated!');
    };

    return (
        <div className="p-6">
            <h1>Account Settings</h1>
            <input
                type="text"
                name="name"
                placeholder="Name"
                value={userInfo.name}
                onChange={handleInputChange}
            />
            <input
                type="email"
                name="email"
                placeholder="Email"
                value={userInfo.email}
                onChange={handleInputChange}
            />
            <input
                type="text"
                name="address"
                placeholder="Address"
                value={userInfo.address}
                onChange={handleInputChange}
            />
            <input
                type="text"
                name="creditCard"
                placeholder="Credit Card Info"
                value={userInfo.creditCard}
                onChange={handleInputChange}
            />
            <button onClick={handleSave}>Save</button>
        </div>
    );
}

export default Profile;
