import React, { useState, useEffect } from 'react';
import Header from './AdminHeader';
import Footer from '../components/Footer';

function SalesHistoryPage() {
    const [salesData, setSalesData] = useState([]);
    const [filters, setFilters] = useState({ customer: '', product: '', startDate: '', endDate: '' });
    const [users, setUsers] = useState([]);
    const [products, setProducts] = useState([]);
    const [totalSales, setTotalSales] = useState(0);
    const [totalTax, setTotalTax] = useState(0);
    const [topProduct, setTopProduct] = useState('');
    const [topCustomer, setTopCustomer] = useState('');
    const [totalOrders, setTotalOrders] = useState(0);

    useEffect(() => {
        fetchUsers();
        fetchProducts();
        fetchSalesData();
    }, [filters]);

    useEffect(() => {
        calculateSummary();
    }, [salesData]);

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
        try {
            const token = localStorage.getItem('accessToken');
            const response = await fetch('http://localhost:8080/api/order/all', {
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            });

            if (response.ok) {
                const orders = await response.json();
                const groupedOrders = orders.map(order => ({
                    orderID: order.orderID,
                    user: order.username,
                    date: order.orderDate,
                    total: order.totalAmount,
                    items: order.orderItems.map(item => ({
                        product: item.productName,
                        quantity: item.quantity,
                        price: item.priceAtPurchase
                    }))
                }));

                const filteredData = groupedOrders.filter(order => {
                    const matchCustomer = filters.customer ? order.user === filters.customer : true;
                    const matchProduct = filters.product
                        ? order.items.some(item => item.product === filters.product)
                        : true;
                    const matchStartDate = filters.startDate ? new Date(order.date) >= new Date(filters.startDate) : true;
                    const matchEndDate = filters.endDate ? new Date(order.date) <= new Date(filters.endDate) : true;
                    return matchCustomer && matchProduct && matchStartDate && matchEndDate;
                });

                setSalesData(filteredData);
            } else {
                console.error('Failed to fetch orders');
            }
        } catch (error) {
            console.error('Error fetching orders:', error);
        }
    };

    const calculateSummary = () => {
        const totalSalesAmount = salesData.reduce((total, order) => total + order.total, 0);
        const tax = totalSalesAmount * 0.1;

        const productCounts = {};
        const customerCounts = {};

        salesData.forEach(order => {
            customerCounts[order.user] = (customerCounts[order.user] || 0) + 1;
            order.items.forEach(item => {
                productCounts[item.product] = (productCounts[item.product] || 0) + item.quantity;
            });
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

    return (
        <div className="flex flex-col min-h-screen">
            <Header />
            <div className="flex-1 p-6">
                <h1 className="text-4xl font-bold mb-6">Sales History</h1>

                {/* Filters */}
                <div className="flex items-baseline gap-4 mb-6">
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

                    <div className="flex flex-col">
                        <label className="text-sm font-semibold mb-1">Product:</label>
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

                    <div className="flex flex-col">
                        <label className="text-sm font-semibold mb-1">From:</label>
                        <input
                            type="date"
                            value={filters.startDate}
                            onChange={(e) => setFilters({ ...filters, startDate: e.target.value })}
                            className="border border-gray-300 rounded p-2 h-10 w-48"
                        />
                    </div>

                    <div className="flex flex-col">
                        <label className="text-sm font-semibold mb-1">To:</label>
                        <input
                            type="date"
                            value={filters.endDate}
                            onChange={(e) => setFilters({ ...filters, endDate: e.target.value })}
                            className="border border-gray-300 rounded p-2 h-10 w-48"
                        />
                    </div>
                </div>

                {/* Statistics */}
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
                            <th className="border border-gray-300 px-4 py-2">Order ID</th>
                            <th className="border border-gray-300 px-4 py-2">Customer</th>
                            <th className="border border-gray-300 px-4 py-2">Date</th>
                            <th className="border border-gray-300 px-4 py-2">Items</th>
                            <th className="border border-gray-300 px-4 py-2">Total</th>
                        </tr>
                    </thead>
                    <tbody>
                        {salesData.map((order) => (
                            <tr key={order.orderID} className="text-center">
                                <td className="border border-gray-300 px-4 py-2">{order.orderID}</td>
                                <td className="border border-gray-300 px-4 py-2">{order.user}</td>
                                <td className="border border-gray-300 px-4 py-2">{new Date(order.date).toLocaleDateString()}</td>
                                <td className="border border-gray-300 px-4 py-2">
                                    {order.items.map((item, index) => (
                                        <div key={index}>
                                            {item.product} (x{item.quantity})
                                        </div>
                                    ))}
                                </td>
                                <td className="border border-gray-300 px-4 py-2">${order.total.toFixed(2)}</td>
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
