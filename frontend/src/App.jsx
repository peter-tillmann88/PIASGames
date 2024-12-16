import React from 'react';
import { Route, Routes } from 'react-router-dom';
import Homepage from './screen/homepage/HomePage';
import LoginPage from './screen/loginpage/LoginPage';
import CartPage from './cart/CartPage';
import CheckoutPage from './cart/CheckoutPage';
import GameDetailPage from './screen/gamedetailpage/GameDetailPage';
import OrderConfirmationPage from './cart/OrderConfirmationPage';
import Footer from "./components/Footer.jsx";
import SearchPage from "./components/SearchPage.jsx";
import RegisterPage from './components/Register';
import ProfilePage from './components/Profile';
import AdminDashboard from './Admin/AdminDashboard';
import ManageCustomers from './Admin/ManageCustomersPage.jsx';
import ManageInventory from './Admin/ManageInventoryPage.jsx';
import Saleshistory from './Admin/SalesHistoryPage.jsx';


function App() {
    return (
        <Routes>
            <Route path="/" element={<Homepage />} />
            <Route path="/login" element={<LoginPage />} />
            <Route path="/cart" element={<CartPage />} />
            <Route path="/checkout" element={<CheckoutPage />} />
            <Route path="/game/:id" element={<GameDetailPage />} />
            <Route path="/order-confirmation" element={<OrderConfirmationPage />} />
            <Route path="/gamedetailpage/:id" element={<GameDetailPage />} />
            <Route path="/footer" element={<Footer />} />
            <Route path="/search" element={<SearchPage />} />
            <Route path="/register" element={<RegisterPage />} />
            <Route path="/profile" element={<ProfilePage />} />
            <Route path="/admin-dashboard" element={<AdminDashboard />} />
            <Route path="/manage-customers" element={<ManageCustomers />} /> 
            <Route path="/sales-history" element={<Saleshistory />} /> 
            <Route path="/manage-inventory" element={<ManageInventory />} />  


        </Routes>
    );
}

export default App;
