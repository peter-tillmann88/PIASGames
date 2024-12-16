import React, { useState, useEffect } from 'react';
import Header from './AdminHeader';
import Footer from '../components/Footer';

function SalesHistoryPage() {
    const [salesData, setSalesData] = useState([]);
    const [filters, setFilters] = useState({ customer: '', product: '', startDate: '', endDate: '' });
    const [users, setUsers] = useState([]);
    const [products, setProducts] = useState([]);
    const [sortConfig, setSortConfig] = useState({ key: '', direction: '' });

    const [totalSales, setTotalSales] = useState(0);
    const [totalTax, setTotalTax] = useState(0);
    const [topProduct, setTopProduct] = useState('');
    const [topCustomer, setTopCustomer] = useState('');
    const [totalOrders, setTotalOrders] = useState(0);

    useEffect(() => {
        fetchUsers();
        fetchProducts();
        fetchSalesData();
        calculateSummary();
    }, [filters]);

    const fetchUsers = async () => {
        try {
            const response = await fetch('http://localhost:8080/api/users/all');
            if (response.ok) {
                const data = await response.json();
                setUsers(data.map(user => user.username));
            }
        } catch (error) {
            console.error('Error fetching users:', error);
        }
    };

    const fetchProducts = async () => {
        try {
            const response = await fetch('http://localhost:8080/api/products/all');
            if (response.ok) {
                const data = await response.json();
                setProducts(data.map(product => product.name));
            }
        } catch (error) {
            console.error('Error fetching products:', error);
        }
    };

    const fetchSalesData = async () => {
        const data = [
            { id: 1, user: 'John Doe', product: 'Game A', price: 49.99, quantity: 2, date: '2024-12-01' },
            { id: 2, user: 'Jane Smith', product: 'Game B', price: 39.99, quantity: 1, date: '2024-12-02' },
            { id: 3, user: 'John Doe', product: 'Game A', price: 49.99, quantity: 1, date: '2024-12-03' },
        ];

        const filteredData = data.filter((sale) => {
            const matchCustomer = filters.customer ? sale.user === filters.customer : true;
            const matchProduct = filters.product ? sale.product === filters.product : true;

            const matchStartDate = filters.startDate ? new Date(sale.date) >= new Date(filters.startDate) : true;
            const matchEndDate = filters.endDate ? new Date(sale.date) <= new Date(filters.endDate) : true;

            return matchCustomer && matchProduct && matchStartDate && matchEndDate;
        });

        setSalesData(filteredData);
    };

    const calculateSummary = () => {
        const totalSalesAmount = salesData.reduce((total, sale) => total + sale.price * sale.quantity, 0);
        const tax = totalSalesAmount * 0.1;

        const productCounts = {};
        const customerCounts = {};

        salesData.forEach((sale) => {
            productCounts[sale.product] = (productCounts[sale.product] || 0) + sale.quantity;
            customerCounts[sale.user] = (customerCounts[sale.user] || 0) + 1;
        });

        const topSellingProduct = Object.entries(productCounts).reduce(
            (top, current) => (current[1] > top[1] ? current : top),
            ['', 0]
        )[0];

        const topPurchasingCustomer = Object.entries(customerCounts).reduce(
            (top, current) => (current[1] > top[1] ? current : top),
            ['', 0]
        )[0];

        setTotalSales(totalSalesAmount.toFixed(2));
        setTotalTax(tax.toFixed(2));
        setTopProduct(topSellingProduct);
        setTopCustomer(topPurchasingCustomer);
        setTotalOrders(salesData.length);
    };

    const handleStartDateChange = (e) => {
        const startDate = e.target.value;
        setFilters((prev) => ({
            ...prev,
            startDate,
            endDate: prev.endDate || startDate,
        }));
    };

    const handleEndDateChange = (e) => {
        const endDate = e.target.value;
        setFilters((prev) => ({ ...prev, endDate }));
    };

    return (
        <div className="flex flex-col min-h-screen">
            <Header />
            <div className="flex-1 p-6">
                <h1 className="text-4xl font-bold mb-6">Sales History</h1>

                {/* Filters */}
                <div className="flex items-baseline gap-4 mb-6">
                    {/* Customer Dropdown */}
                    <div className="flex flex-col">
                        <label className="text-sm font-semibold mb-1">Customer:</label>
                        <select
                            value={filters.customer}
                            onChange={(e) => setFilters({ ...filters, customer: e.target.value })}
                            className="border border-gray-300 rounded p-2 h-10 w-48"
                        >
                            <option value="">All Customers</option>
                            {users.map((user, index) => (
                                <option key={index} value={user}>
                                    {user}
                                </option>
                            ))}
                        </select>
                    </div>

                    {/* Product Dropdown */}
                    <div className="flex flex-col">
                        <label className="text-sm font-semibold mb-1">Products:</label>
                        <select
                            value={filters.product}
                            onChange={(e) => setFilters({ ...filters, product: e.target.value })}
                            className="border border-gray-300 rounded p-2 h-10 w-48"
                        >
                            <option value="">All Products</option>
                            {products.map((product, index) => (
                                <option key={index} value={product}>
                                    {product}
                                </option>
                            ))}
                        </select>
                    </div>

                    {/* Start Date */}
                    <div className="flex flex-col">
                        <label className="text-sm font-semibold mb-1">From:</label>
                        <input
                            type="date"
                            value={filters.startDate}
                            onChange={handleStartDateChange}
                            className="border border-gray-300 rounded p-2 h-10 w-48"
                        />
                    </div>

                    {/* End Date */}
                    <div className="flex flex-col">
                        <label className="text-sm font-semibold mb-1">To:</label>
                        <input
                            type="date"
                            value={filters.endDate}
                            onChange={handleEndDateChange}
                            className="border border-gray-300 rounded p-2 h-10 w-48"
                        />
                    </div>
                </div>

                {/* Grid for Statistics */}
                <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6 mb-6">
                    <div className="bg-blue-500 text-white p-6 rounded shadow">
                        <h3 className="text-lg font-bold">Total Sales</h3>
                        <p className="text-2xl font-semibold">${totalSales}</p>
                        <p className="text-sm">Includes ${totalTax} in tax.</p>
                    </div>

                    <div className="bg-green-500 text-white p-6 rounded shadow">
                        <h3 className="text-lg font-bold">Top-Selling Product</h3>
                        <p className="text-2xl font-semibold">{topProduct || 'N/A'}</p>
                    </div>

                    <div className="bg-yellow-500 text-white p-6 rounded shadow">
                        <h3 className="text-lg font-bold">Top Customer</h3>
                        <p className="text-2xl font-semibold">{topCustomer || 'N/A'}</p>
                    </div>

                    <div className="bg-red-500 text-white p-6 rounded shadow">
                        <h3 className="text-lg font-bold">Total Orders</h3>
                        <p className="text-2xl font-semibold">{totalOrders}</p>
                    </div>
                </div>

                {/* Sales Table */}
                <table className="w-full border-collapse border border-gray-300">
                    <thead>
                        <tr className="bg-gray-200">
                            <th className="border border-gray-300 px-4 py-2">User</th>
                            <th className="border border-gray-300 px-4 py-2">Product</th>
                            <th className="border border-gray-300 px-4 py-2">Price</th>
                            <th className="border border-gray-300 px-4 py-2">Quantity</th>
                            <th className="border border-gray-300 px-4 py-2">Date</th>
                        </tr>
                    </thead>
                    <tbody>
                        {salesData.map((sale) => (
                            <tr key={sale.id} className="text-center">
                                <td className="border border-gray-300 px-4 py-2">{sale.user}</td>
                                <td className="border border-gray-300 px-4 py-2">{sale.product}</td>
                                <td className="border border-gray-300 px-4 py-2">${sale.price.toFixed(2)}</td>
                                <td className="border border-gray-300 px-4 py-2">{sale.quantity}</td>
                                <td className="border border-gray-300 px-4 py-2">{sale.date}</td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
            <Footer />
        </div>
    );
}

export default SalesHistoryPage;
