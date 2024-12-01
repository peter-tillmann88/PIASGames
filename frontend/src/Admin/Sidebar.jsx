import React from 'react';
import { Link } from 'react-router-dom';

function Sidebar() {
    return (
        <div className="sidebar w-64 bg-gray-800 text-white p-4">
            <h2 className="text-2xl font-bold mb-4">Admin Panel</h2>
            <ul>
                <li>
                    <Link to="/admin/sales-history" className="text-lg">Sales History</Link>
                </li>
                <li>
                    <Link to="/admin/manage-customers" className="text-lg">Manage Customers</Link>
                </li>
                <li>
                    <Link to="/admin/manage-inventory" className="text-lg">Manage Inventory</Link>
                </li>
            </ul>
        </div>
    );
}

export default Sidebar;
