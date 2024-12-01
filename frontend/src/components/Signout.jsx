import React from 'react';
import { useNavigate } from 'react-router-dom';

function SignOut() {
    const navigate = useNavigate();

    const handleSignOut = () => {
        // Clear user authentication (e.g., tokens, session data)
        navigate('/login'); // Redirect to login after signing out
    };

    return (
        <button onClick={handleSignOut}>Sign Out</button>
    );
}

export default SignOut;
