import React, { useState, useEffect } from 'react';
import Header from '../screen/homepage/Header';
import Footer from '../components/Footer';
import Sidebar from '../components/Sidebar';

function SalesHistoryPage() {
    const [salesData, setSalesData] = useState([]);
    const [filters, setFilters] = useState({ customer: '', date: '' });

    useEffect(() => {
        fetchSalesData();
    }, [filters]);

    const fetchSalesData = async () => {
        // Example fetch for sales data
        // This can be connected to an API when you're ready
        const data = [
            { id: 1, user: 'John Doe', product: 'Game A', price: 49.99, quantity: 2, date: '2024-12-01' },
            { id: 2, user: 'Jane Smith', product: 'Game B', price: 39.99, quantity: 1, date: '2024-12-02' },
        ];
        setSalesData(data);
    };

    const handleFilterChange = (e) => {
        const { name, value } = e.target;
        setFilters((prevFilters) => ({ ...prevFilters, [name]: value }));
    };

    return (
        <div className="sales-history">
            <Header />
            <div className="flex">
                <Sidebar />
                <div className="flex-1 p-6">
                    <h1 className="text-4xl font-bold mb-6">Sales History</h1>
                    <div className="filter-form">
                        <input
                            type="text"
                            name="customer"
                            value={filters.customer}
                            onChange={handleFilterChange}
                            placeholder="Search by customer"
                            className="input"
                        />
                        <input
                            type="date"
                            name="date"
                            value={filters.date}
                            onChange={handleFilterChange}
                            className="input"
                        />
                    </div>
                    <table className="sales-table">
                        <thead>
                        <tr>
                            <th>User</th>
                            <th>Product</th>
                            <th>Price</th>
                            <th>Quantity</th>
                            <th>Date</th>
                        </tr>
                        </thead>
                        <tbody>
                        {salesData.map((sale) => (
                            <tr key={sale.id}>
                                <td>{sale.user}</td>
                                <td>{sale.product}</td>
                                <td>{sale.price}</td>
                                <td>{sale.quantity}</td>
                                <td>{sale.date}</td>
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

export default SalesHistoryPage;
