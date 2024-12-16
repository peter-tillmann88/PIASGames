import React from 'react';
import { useNavigate } from 'react-router-dom';

function SignOut() {
    const navigate = useNavigate();

    const handleSignOut = () => {
        navigate('/login'); 
    };

    return (
        <button onClick={handleSignOut}>Sign Out</button>
    );
}

export default SignOut;
