import React, { useEffect, useState } from 'react';
import { Redirect } from 'react-router-dom';
import Header from '../screen/homepage/Header';
import Footer from '../components/Footer';
import Sidebar from '../components/Sidebar';

function AdminDashboard() {
    const [isAdmin, setIsAdmin] = useState(false);

    useEffect(() => {
        const token = localStorage.getItem('token');
        if (token) {
            const decodedToken = JSON.parse(atob(token.split('.')[1]));  // Decode JWT
            if (decodedToken.admin) {
                setIsAdmin(true);
            }
        }
    }, []);

    if (!isAdmin) {
        return <Redirect to="/" />; // Redirect if not admin
    }

    return (
        <div className="admin-dashboard">
            <Header />
            <div className="flex">
                <Sidebar />
                <div className="admin-content flex-1 p-6">
                    <h1 className="text-4xl font-bold mb-6">Admin Dashboard</h1>
                    <div className="admin-links">
                        <button className="btn">View Sales History</button>
                        <button className="btn">Manage Customers</button>
                        <button className="btn">Manage Inventory</button>
                    </div>
                </div>
            </div>
            <Footer />
        </div>
    );
}

export default AdminDashboard;
