import React, { useState, useEffect } from 'react';
import { jsPDF } from 'jspdf';
import 'jspdf-autotable';
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
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    //fetch users and product
    useEffect(() => {
        fetchUsers();
        fetchProducts();
    }, []);

    //fetch the sales
    useEffect(() => {
        fetchSalesData();
    }, [filters]);

    //caulculate summary
    useEffect(() => {
        calculateSummary();
    }, [salesData]);

    const fetchUsers = async () => {
        try {
            const response = await fetch(`${import.meta.env.VITE_API_URL}/users/all`);
            if (response.ok) {
                const data = await response.json();
                setUsers(data.map(user => user.username));
            } else {
                throw new Error('Failed to fetch users');
            }
        } catch (error) {
            console.error('Error fetching users: ', error);
            setError('Error fetching users');
        }
    };

    const fetchProducts = async () => {
        try {
            const response = await fetch(`${import.meta.env.VITE_API_URL}/products/all`);
            if (response.ok) {
                const data = await response.json();
                setProducts(data.map(product => product.name));
            } else {
                throw new Error('Failed to fetch products');
            }
        } catch (error) {
            console.error('Error fetching products: ', error);
            setError('Error fetching products');
        }
    };

    const fetchSalesData = async () => {
        setLoading(true);
        try {
            const token = localStorage.getItem('accessToken');
            if (!token) {
                throw new Error('Unauthorized: No access token found');
            }

            const response = await fetch(`${import.meta.env.VITE_API_URL}/order/all`, {
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
                setError(null);
            } else if (response.status === 401) {
                throw new Error('Unauthorized: Invalid access token');
            } else {
                throw new Error('Failed to fetch orders');
            }
        } catch (error) {
            console.error('Error fetching orders:', error);
            setError(error.message);
            setSalesData([]);
        } finally {
            setLoading(false);
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
            (top, current) => (current[1] > (top[1] || 0) ? current : top),
            ['', 0]
        )[0];

        const topPurchasingCustomer = Object.entries(customerCounts).reduce(
            (top, current) => (current[1] > (top[1] || 0) ? current : top),
            ['', 0]
        )[0];

        setTotalSales(totalSalesAmount.toFixed(2));
        setTotalTax(tax.toFixed(2));
        setTopProduct(topSellingProduct);
        setTopCustomer(topPurchasingCustomer);
        setTotalOrders(salesData.length);
    };

    const handleDownloadPDF = () => {
        const doc = new jsPDF();

        //title for pdf
        doc.setFontSize(18);
        doc.text('PIASGames Sales History Report', 14, 20);

        //filters applied
        doc.setFontSize(12);
        let filterTextY = 30;
        doc.text('Filters Applied:', 14, filterTextY);
        filterTextY += 6;

        if (filters.customer) {
            doc.text(`Customer: ${filters.customer}`, 14, filterTextY);
            filterTextY += 6;
        }
        if (filters.product) {
            doc.text(`Product: ${filters.product}`, 14, filterTextY);
            filterTextY += 6;
        }
        if (filters.startDate) {
            doc.text(`From: ${new Date(filters.startDate).toLocaleDateString()}`, 14, filterTextY);
            filterTextY += 6;
        }
        if (filters.endDate) {
            doc.text(`To: ${new Date(filters.endDate).toLocaleDateString()}`, 14, filterTextY);
            filterTextY += 6;
        }

        //summary
        doc.setFontSize(14);
        doc.text('Summary:', 14, filterTextY + 10);
        doc.setFontSize(12);
        doc.text(`Total Sales: $${totalSales}`, 14, filterTextY + 16);
        doc.text(`Total Tax: $${totalTax}`, 14, filterTextY + 22);
        doc.text(`Top-Selling Product: ${topProduct || 'N/A'}`, 14, filterTextY + 28);
        doc.text(`Top Customer: ${topCustomer || 'N/A'}`, 14, filterTextY + 34);
        doc.text(`Total Orders: ${totalOrders}`, 14, filterTextY + 40);

        //sales reflecting filters
        doc.autoTable({
            startY: filterTextY + 50,
            head: [['Order ID', 'Customer', 'Date', 'Items', 'Total']],
            body: salesData.map(order => [
                order.orderID,
                order.user,
                new Date(order.date).toLocaleDateString(),
                order.items.map(item => `${item.product} (x${item.quantity})`).join(', '),
                `$${order.total.toFixed(2)}`
            ]),
            styles: { fontSize: 8 },
            headStyles: { fillColor: [100, 100, 100] },
            columnStyles: {
                0: { cellWidth: 20 }, 
                1: { cellWidth: 30 }, 
                2: { cellWidth: 25 }, 
                3: { cellWidth: 80 }, 
                4: { cellWidth: 20 }  
            }
        });

        //download the pdf
        doc.save('sales_history_report.pdf');
    };

    const handleFilterChange = (e) => {
        const { name, value } = e.target;
        setFilters(prevFilters => ({
            ...prevFilters,
            [name]: value
        }));
    };

    return (
        <div className="flex flex-col min-h-screen">
            <Header />
            <div className="flex-1 p-6">
                <h1 className="text-4xl font-bold mb-6">Sales History</h1>

                <div className="flex items-center justify-between gap-4 mb-6 flex-wrap">
                    <div className="flex items-baseline gap-4 flex-wrap">
                        <div className="flex flex-col">
                            <label className="text-sm font-semibold mb-1">Customer:</label>
                            <select
                                name="customer"
                                value={filters.customer}
                                onChange={handleFilterChange}
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
                                name="product"
                                value={filters.product}
                                onChange={handleFilterChange}
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
                                name="startDate"
                                value={filters.startDate}
                                onChange={handleFilterChange}
                                className="border border-gray-300 rounded p-2 h-10 w-48"
                            />
                        </div>

                        <div className="flex flex-col">
                            <label className="text-sm font-semibold mb-1">To:</label>
                            <input
                                type="date"
                                name="endDate"
                                value={filters.endDate}
                                onChange={handleFilterChange}
                                className="border border-gray-300 rounded p-2 h-10 w-48"
                            />
                        </div>
                    </div>
                    <button
                        className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-700"
                        onClick={handleDownloadPDF}
                    >
                        Download PDF
                    </button>
                </div>
                {loading && <p className="text-center">Loading sales data...</p>}
                {error && <p className="text-center text-red-500">{error}</p>}
                {!loading && !error && (
                    <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6 mb-6">
                        <div className="bg-blue-500 text-white p-6 rounded shadow">
                            <h3 className="text-lg font-bold">Total Sales</h3>
                            <p className="text-2xl font-semibold">${totalSales}</p>
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
                )}

                {!loading && !error && salesData.length > 0 && (
                    <div className="overflow-x-auto">
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
                )}
                {!loading && !error && salesData.length === 0 && (
                    <p className="text-center">No sales data available for the selected filters.</p>
                )}
            </div>
            <Footer />
        </div>
    );
}

export default SalesHistoryPage;
