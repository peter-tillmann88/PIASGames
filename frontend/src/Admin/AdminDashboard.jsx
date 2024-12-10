import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import Header from '../screen/homepage/Header';
import Footer from '../components/Footer';
import Sidebar from '../components/Sidebar';

function AdminDashboard() {
    const navigate = useNavigate();
    const [isAdmin, setIsAdmin] = useState(null); // Loading state for role verification

    useEffect(() => {
        const role = localStorage.getItem('role');
        if (role === 'ADMIN') {
            setIsAdmin(true); // Set as admin
            console.log("THIS IS ADMIN");
        } else {
            setIsAdmin(false); // Not an admin
            setTimeout(() => navigate('/'), 2000); // Redirect after 2 seconds
        }
    }, [navigate]);

    if (isAdmin === null) {
        // Show a loading indicator while verifying the role
        return (
            <div className="flex justify-center items-center min-h-screen">
                <p>Loading...</p>
            </div>
        );
    }

    if (isAdmin === false) {
        // Show an optional "Access Denied" message before redirecting
        return (
            <div className="flex justify-center items-center min-h-screen">
                <p>Access Denied. Redirecting to homepage...</p>
            </div>
        );
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
