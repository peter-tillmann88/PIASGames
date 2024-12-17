import React, { useState, useEffect } from 'react';
import Header from './AdminHeader';
import Footer from '../components/Footer';
import Modal from 'react-modal';

Modal.setAppElement('#root');

function ManageCustomersPage() {
    const [customers, setCustomers] = useState([]);
    const [loading, setLoading] = useState(true);
    const [isEditModalOpen, setIsEditModalOpen] = useState(false);
    const [isHistoryModalOpen, setIsHistoryModalOpen] = useState(false);
    const [editCustomer, setEditCustomer] = useState(null);
    const [selectedCustomerHistory, setSelectedCustomerHistory] = useState([]);
    const [errorMessage, setErrorMessage] = useState('');
    const [successMessage, setSuccessMessage] = useState('');

    useEffect(() => {
        fetchCustomers();
    }, []);

    const fetchCustomers = async () => {
        setLoading(true);
        try {
            const response = await fetch('http://localhost:8080/api/users/all');
            if (!response.ok) throw new Error('Failed to fetch customers');
            const data = await response.json();
            setCustomers(data);
            setErrorMessage('');
        } catch (error) {
            setErrorMessage('Error fetching customers: ' + error.message);
        } finally {
            setLoading(false);
        }
    };

    const fetchPurchaseHistory = async (customer) => {
        try {
            const token = localStorage.getItem('accessToken');
            const response = await fetch('http://localhost:8080/api/order/all', {
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json',
                },
            });
            if (!response.ok) throw new Error('Failed to fetch purchase history');
            const allOrders = await response.json();
            const userOrders = allOrders.filter(order => order.username === customer.username);
            const history = userOrders.map(order => ({
                orderId: order.orderID, 
                date: new Date(order.orderDate).toLocaleDateString(),
                items: order.orderItems.map(item => ({
                    product: item.productName,
                    quantity: item.quantity,
                    price: item.priceAtPurchase,
                })),
                total: order.totalAmount,
            }));
            setSelectedCustomerHistory(history);
            setErrorMessage('');
        } catch (error) {
            setSelectedCustomerHistory([]);
            setErrorMessage('Error fetching purchase history: ' + error.message);
        }
    };

    const openEditModal = (customer) => {
        setEditCustomer({ ...customer });
        setIsEditModalOpen(true);
        setErrorMessage('');
        setSuccessMessage('');
    };

    const closeEditModal = () => {
        setIsEditModalOpen(false);
        setEditCustomer(null);
    };

    const openHistoryModal = (customer) => {
        fetchPurchaseHistory(customer);
        setIsHistoryModalOpen(true);
    };

    const closeHistoryModal = () => {
        setIsHistoryModalOpen(false);
        setSelectedCustomerHistory([]);
    };

    const handleEditInputChange = (field, value) => {
        setEditCustomer((prev) => ({ ...prev, [field]: value }));
    };

    const handleUpdateCustomer = async (e) => {
        e.preventDefault();
        setErrorMessage('');
        setSuccessMessage('');
        try {
            const response = await fetch(`http://localhost:8080/api/users/profile?username=${editCustomer.username}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    email: editCustomer.email,
                    phone: editCustomer.phone,
                    creditCard: editCustomer.creditCard,
                    expiryDate: editCustomer.expiryDate,
                    address: editCustomer.address,
                    postalCode: editCustomer.postalCode,
                    country: editCustomer.country,
                    province: editCustomer.province,
                }),
            });
            if (response.ok) {
                setSuccessMessage('Customer updated successfully!');
                await fetchCustomers();
                closeEditModal();
            } else {
                const errorText = await response.text();
                setErrorMessage('Failed to update customer: ' + errorText);
            }
        } catch (error) {
            setErrorMessage('Error updating customer: ' + error.message);
        }
    };

    return (
        <div className="flex flex-col min-h-screen">
            <Header />
            <main className="flex-grow p-6">
                <div className="container mx-auto">
                    <h1 className="text-4xl font-bold mb-6 text-center">Manage Customers</h1>

                    {loading ? (
                        <div>Loading customers...</div>
                    ) : (
                        <table className="w-full border-collapse border border-gray-300">
                            <thead>
                                <tr className="bg-gray-200">
                                    <th className="border border-gray-300 px-4 py-2">Name</th>
                                    <th className="border border-gray-300 px-4 py-2">Email</th>
                                    <th className="border border-gray-300 px-4 py-2">Phone</th>
                                    <th className="border border-gray-300 px-4 py-2">Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                {customers.map((customer) => (
                                    <tr key={customer.userId} className="text-center">
                                        <td className="border border-gray-300 px-4 py-2">{customer.username}</td>
                                        <td className="border border-gray-300 px-4 py-2">{customer.email}</td>
                                        <td className="border border-gray-300 px-4 py-2">{customer.phone || 'N/A'}</td>
                                        <td className="border border-gray-300 px-4 py-2 space-x-2">
                                            <button
                                                className="bg-yellow-500 text-white px-2 py-1 rounded hover:bg-yellow-700"
                                                onClick={() => openEditModal(customer)}
                                            >
                                                Edit
                                            </button>
                                            <button
                                                className="bg-blue-500 text-white px-2 py-1 rounded hover:bg-blue-700"
                                                onClick={() => openHistoryModal(customer)}
                                            >
                                                View Purchase History
                                            </button>
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    )}

                    {errorMessage && <p className="text-red-500 mt-4">{errorMessage}</p>}
                    {successMessage && <p className="text-green-500 mt-4">{successMessage}</p>}

                    {/*update customer*/}
                    <Modal
                        isOpen={isEditModalOpen && editCustomer !== null}
                        onRequestClose={closeEditModal}
                        contentLabel="Edit Customer"
                        className="max-w-3xl mx-auto mt-20 bg-white p-6 rounded shadow-lg overflow-auto max-h-screen"
                        overlayClassName="fixed inset-0 bg-gray-600 bg-opacity-50 flex justify-center items-start z-50"
                    >
                        {editCustomer && (
                            <div>
                                <h2 className="text-2xl font-bold mb-4">Edit Customer</h2>
                                <form onSubmit={handleUpdateCustomer}>
                                    {[
                                        { label: 'Email', field: 'email' },
                                        { label: 'Credit Card #', field: 'creditCard' },
                                        { label: 'Expiry Date (MM/YY)', field: 'expiryDate' },
                                        { label: 'Address', field: 'address' },
                                        { label: 'Postal Code', field: 'postalCode' },
                                        { label: 'Country', field: 'country' },
                                        { label: 'Province/State', field: 'province' },
                                    ].map(({ label, field }) => (
                                        <div className="mb-4" key={field}>
                                            <label className="block text-sm font-medium text-gray-700">{label}:</label>
                                            <input
                                                type="text"
                                                value={editCustomer[field] || ''}
                                                onChange={(e) => handleEditInputChange(field, e.target.value)}
                                                className="w-full border border-gray-300 rounded px-3 py-2"
                                                required
                                            />
                                        </div>
                                    ))}
                                    <button
                                        type="submit"
                                        className="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-700"
                                    >
                                        Update Customer
                                    </button>
                                    <button
                                        type="button"
                                        onClick={closeEditModal}
                                        className="bg-gray-500 text-white px-4 py-2 rounded hover:bg-gray-700 ml-2"
                                    >
                                        Cancel
                                    </button>
                                </form>
                            </div>
                        )}
                    </Modal>

                    {/*modal for purchase history*/}
                    <Modal
                        isOpen={isHistoryModalOpen}
                        onRequestClose={closeHistoryModal}
                        contentLabel="Purchase History"
                        className="max-w-3xl mx-auto mt-20 bg-white p-6 rounded shadow-lg overflow-auto max-h-screen"
                        overlayClassName="fixed inset-0 bg-gray-600 bg-opacity-50 flex justify-center items-start z-50"
                    >
                        <h2 className="text-2xl font-bold mb-4">Purchase History</h2>
                        <div className="max-h-80 overflow-y-auto">
                            {selectedCustomerHistory.length > 0 ? (
                                selectedCustomerHistory.map((history, index) => (
                                    <div key={index} className="p-4 border-b border-gray-300">
                                        <p><strong>Order ID:</strong> {history.orderId}</p> 
                                        <p><strong>Date:</strong> {history.date}</p>
                                        <p>
                                            <strong>Items:</strong>{' '}
                                            {history.items.map((item, i) => (
                                                <span key={i}>
                                                    {item.product} (x{item.quantity}) - ${item.price.toFixed(2)}
                                                    {i < history.items.length - 1 && ', '}
                                                </span>
                                            ))}
                                        </p>
                                        <p><strong>Total:</strong> ${history.total.toFixed(2)}</p>
                                    </div>
                                ))
                            ) : (
                                <p>No purchase history available.</p>
                            )}
                        </div>
                        <button
                            onClick={closeHistoryModal}
                            className="bg-gray-500 text-white px-4 py-2 rounded hover:bg-gray-700 mt-4"
                        >
                            Close
                        </button>
                    </Modal>
                </div>
            </main>
            <Footer />
        </div>
    );
}

export default ManageCustomersPage;
