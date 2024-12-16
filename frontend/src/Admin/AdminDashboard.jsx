import React, { useEffect, useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import Header from './AdminHeader';
import Footer from '../components/Footer';
import inventory from '../assets/img/manage-inventory.svg';
import sales from '../assets/img/sales-history.svg';
import customers from '../assets/img/manage-customers.svg'; 

function AdminDashboard() {
    const navigate = useNavigate();
    const [isAdmin, setIsAdmin] = useState(null);

    useEffect(() => {
        const role = localStorage.getItem('role');
        if (role === 'ADMIN') {
            setIsAdmin(true); 
            console.log("THIS IS ADMIN");
        } else {
            setIsAdmin(false); 
            setTimeout(() => navigate('/'), 2000); 
        }
    }, [navigate]);

    if (isAdmin === null) {
        return (
            <div className="flex justify-center items-center min-h-screen">
                <p>Loading...</p>
            </div>
        );
    }

    if (isAdmin === false) {
        return (
            <div className="flex justify-center items-center min-h-screen">
                <p>Access Denied. Redirecting to homepage...</p>
            </div>
        );
    }

    return (
        <div className="flex flex-col min-h-screen">
            <Header />

            
            <div className="flex flex-1 flex-col items-center p-6">
                <h1 className="text-4xl font-bold mb-6">Admin Dashboard</h1>
                <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-8 w-full max-w-6xl">
                    {/* sales */}
                    <Link to="/sales-history" className="group">
                        <div className="relative bg-white rounded-lg shadow-md overflow-hidden transform hover:scale-105 transition-transform duration-300">
                            <div className="flex items-center justify-center w-full h-64 bg-gray-100">
                                <img
                                    src={sales}
                                    alt="Sales History"
                                    className="w-4/5 h-full object-contain"
                                />
                            </div>
                            <div className="p-4">
                                <h2 className="text-xl font-bold group-hover:text-blue-500 transition-colors duration-300">
                                    Sales History
                                </h2>
                                <p className="text-sm text-gray-600">
                                    View and analyze the sales data.
                                </p>
                            </div>
                        </div>
                    </Link>

                    {/* customers */}
                    <Link to="/manage-customers" className="group">
                        <div className="relative bg-white rounded-lg shadow-md overflow-hidden transform hover:scale-105 transition-transform duration-300">
                            <div className="flex items-center justify-center w-full h-64 bg-gray-100">
                                <img
                                    src={customers}
                                    alt="Manage Customers"
                                    className="w-4/5 h-full object-contain"
                                />
                            </div>
                            <div className="p-4">
                                <h2 className="text-xl font-bold group-hover:text-blue-500 transition-colors duration-300">
                                    Manage Customers
                                </h2>
                                <p className="text-sm text-gray-600">
                                    Manage customer accounts and details.
                                </p>
                            </div>
                        </div>
                    </Link>

                    {/* inventory */}
                    <Link to="/manage-inventory" className="group">
                        <div className="relative bg-white rounded-lg shadow-md overflow-hidden transform hover:scale-105 transition-transform duration-300">
                            <div className="flex items-center justify-center w-full h-64 bg-gray-100">
                                <img
                                    src={inventory}
                                    alt="Manage Inventory"
                                    className="w-4/5 h-full object-contain"
                                />
                            </div>
                            <div className="p-4">
                                <h2 className="text-xl font-bold group-hover:text-blue-500 transition-colors duration-300">
                                    Manage Inventory
                                </h2>
                                <p className="text-sm text-gray-600">
                                    Update and track inventory.
                                </p>
                            </div>
                        </div>
                    </Link>
                </div>
            </div>

            <Footer />
        </div>
    );
}

export default AdminDashboard;
