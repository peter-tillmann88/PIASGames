import React from "react";
import { useNavigate } from 'react-router-dom';
import logo from "../assets/img/logo.svg"; // Update with your logo path
import navIcon1 from "../assets/img/nav-icon1.svg";
import navIcon2 from "../assets/img/nav-icon2.svg";
import navIcon3 from "../assets/img/nav-icon3.svg";

function Footer({ isLoggedIn }) {
    const navigate = useNavigate();

    const handleAccountSettings = () => {
        navigate('/account-settings');
    };

    return (
        <footer className="footer bg-gray-800 text-white py-6">
            <div className="container mx-auto flex flex-col md:flex-row justify-between items-center">
                {/* Left side with logo and social icons */}
                <div className="flex items-center space-x-4">
                    <img src={logo} alt="Logo" className="h-16" /> {/* Adjust size with h-16 */}
                    <div className="flex space-x-4">
                        <a href="#"><img src={navIcon1} alt="Icon1" className="h-8 hover:scale-110 transition-transform" /></a>
                        <a href="#"><img src={navIcon2} alt="Icon2" className="h-8 hover:scale-110 transition-transform" /></a>
                        <a href="#"><img src={navIcon3} alt="Icon3" className="h-8 hover:scale-110 transition-transform" /></a>
                    </div>
                </div>

                {/* Right side with copyright */}
                <div className="mt-4 md:mt-0 text-center md:text-right">
                    <p>© PIASGames 2024. All Rights Reserved</p>
                    {isLoggedIn && (
                        <button
                            onClick={handleAccountSettings}
                            className="mt-2 bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600 transition"
                        >
                            Account Settings
                        </button>
                    )}
                </div>
            </div>
        </footer>
    );
}

export default Footer;
