import React from 'react';
import { Route, Routes } from 'react-router-dom';
import Homepage from './screen/homepage/HomePage';  // Correct path
import LoginPage from './screen/loginpage/LoginPage';
import CartPage from './cart/CartPage';
import CheckoutPage from './cart/CheckoutPage';
import GameDetailPage from './screen/gamedetailpage/GaneDetailPage';
import OrderConfirmationPage from './cart/OrderConfirmationPage';
import XboxPage from './screen/xbox/XboxPage';  // Ensure correct import paths
import PlaystationPage from './screen/playstation/PlaystationPage';
import PcPage from './screen/pc/PcPage';
import SwitchPage from './screen/switch/SwitchPage';
import Footer from "./components/Footer.jsx";
import SearchResultsPage from "./components/SearchResultsPage.jsx";
import RegisterPage from './components/Register';
import ProfilePage from './components/Profile';
function App() {
    return (
        <Routes>
            <Route path="/xbox" element={<XboxPage />} />
            <Route path="/playstation" element={<PlaystationPage />} />
            <Route path="/pc" element={<PcPage />} />
            <Route path="/switch" element={<SwitchPage />} />

            <Route path="/" element={<Homepage />} />
            <Route path="/login" element={<LoginPage />} />
            <Route path="/cart" element={<CartPage />} />
            <Route path="/checkout" element={<CheckoutPage />} />
            <Route path="/game/:id" element={<GameDetailPage />} />
            <Route path="/order-confirmation" element={<OrderConfirmationPage />} />
            <Route path="/footer" element={<Footer />} />
            <Route path="/search" element={<SearchResultsPage />} />
            <Route path="/register" element={<RegisterPage />} />
            <Route path="/profile" element={<ProfilePage />} />
        </Routes>
    );
}

export default App;
