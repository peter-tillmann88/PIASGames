import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css'; // Import Tailwind CSS
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import App from './App';
import LoginPage from './screen/loginpage/LoginPage.jsx'; // Updated import path
import HomePage from './screen/homepage/HomePage.jsx'; // Updated import path
import CheckoutPage from './cart/CheckoutPage';
import CartPage from "./cart/CartPage.jsx";
import OrderConfirmationPage from "./cart/OrderConfirmationPage.jsx";
import XboxPage from "./screen/xbox/XboxPage.jsx";
import PlaystationPage from "./screen/playstation/PlaystationPage.jsx";
import PcPage from "./screen/pc/PcPage.jsx";
import SwitchPage from "./screen/switch/SwitchPage.jsx";
import Footer from "./components/Footer.jsx";

import SearchResultsPage from "./components/SearchResultsPage.jsx";
import RegisterPage from "./components/Register.jsx";
import ProfilePage from "./components/Profile.jsx";
ReactDOM.createRoot(document.getElementById('root')).render(
    <React.StrictMode>
        <Router>
            <Routes>
                <Route path="/xbox" element={<XboxPage />} />
                <Route path="/playstation" element={<PlaystationPage />} />
                <Route path="/pc" element={<PcPage />} />
                <Route path="/switch" element={<SwitchPage />} />

                <Route path="/" element={<App />} />
                <Route path="/login" element={<LoginPage />} />
                <Route path="/home" element={<HomePage />} />
                <Route path="/cart" element={<CartPage />} />
                <Route path="/checkout" element={<CheckoutPage />} />
                <Route path="/order-confirmation" element={<OrderConfirmationPage />} />
                <Route path="/footer" element={<Footer />} />
                <Route path="/search" element={<SearchResultsPage />} />
                <Route path="/register" element={<RegisterPage />} />
                <Route path="/profile" element={<ProfilePage />} />
            </Routes>
        </Router>
    </React.StrictMode>
);
