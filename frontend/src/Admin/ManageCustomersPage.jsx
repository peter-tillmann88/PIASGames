import React, { useState, useEffect } from 'react';
import Header from '../screen/homepage/Header';
import Footer from '../components/Footer';
import Sidebar from '../components/Sidebar';

function ManageCustomersPage() {
    const [customers, setCustomers] = useState([]);

    useEffect(() => {
        fetchCustomers();
    }, []);

    const fetchCustomers = async () => {
        // Example fetch for customers
        const data = [
            { id: 1, name: 'John Doe', email: 'johndoe@example.com', purchaseHistory: 'Game A, Game B' },
            { id: 2, name: 'Jane Smith', email: 'janesmith@example.com', purchaseHistory: 'Game C' },
        ];
        setCustomers(data);
    };

    return (
        <div className="manage-customers">
            <Header />
            <div className="flex">
                <Sidebar />
                <div className="flex-1 p-6">
                    <h1 className="text-4xl font-bold mb-6">Manage Customers</h1>
                    <table className="customer-table">
                        <thead>
                        <tr>
                            <th>Name</th>
                            <th>Email</th>
                            <th>Purchase History</th>
                            <th>Action</th>
                        </tr>
                        </thead>
                        <tbody>
                        {customers.map((customer) => (
                            <tr key={customer.id}>
                                <td>{customer.name}</td>
                                <td>{customer.email}</td>
                                <td>{customer.purchaseHistory}</td>
                                <td><button className="btn">Edit</button></td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>
            </div>
            <Footer />
        </div>
    );
}

export default ManageCustomersPage;
