import React, {useState} from 'react';
import axios from 'axios';
import './Register.css';
import {API_BASE_URL} from '../../../utils/constants';
import {useNavigate} from 'react-router-dom';

const Register: React.FC = () => {
    const [phoneNumber, setPhoneNumber] = useState('');
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [message, setMessage] = useState('');
    const [error, setError] = useState('');
    const [passwordError, setPasswordError] = useState('');

    const navigate = useNavigate();

    const handleRegister = async () => {
        setPasswordError('');
        setError('');
        setMessage('');

        if (!username || username.length < 3 || username.length > 30) {
            setError('Username must be between 3 and 30 characters');
            return;
        }

        const phoneRegex = /^\+?[0-9]{10,15}$/;
        if (!phoneRegex.test(phoneNumber)) {
            setError('Phone number must be 10-15 digits and can start with +');
            return;
        }

        if (!password || password.length < 8 || password.length > 100) {
            setError('Password must be between 8 and 100 characters');
            return;
        }

        if (password !== confirmPassword) {
            setPasswordError('Passwords do not match');
            return;
        }

        try {
            await axios.post(`${API_BASE_URL}/auth/register`, {
                phoneNumber,
                password,
                username
            });

            setMessage('Registered successfully');
            setUsername('');
            setPhoneNumber('');
            setPassword('');
            setConfirmPassword('');
            setError('');
            navigate('/login');
        } catch (error) {
            setError('There was an error registering the user');
            console.error('Registration failed:', error);
        }
    };

    return (
        <div className="register-page-bg">
            <div className="register-container">
                <div className="register-form">
                    <h2>Register</h2>
                    <p className="welcome-message">Welcome! Create an account to start using the app.</p>

                    <div className="input-container">
                        <div className="input-wrapper">
                            <span className="input-icon">
                            <svg width="22" height="22" fill="none" stroke="#3e57d0" strokeWidth="2" strokeLinecap="round"
                                   strokeLinejoin="round" viewBox="0 0 24 24"><path d="M16 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/></svg>
                            </span>
                            <input
                                type="text"
                                placeholder="Username"
                                value={username}
                                onChange={(e) => setUsername(e.target.value)}
                                className="input-field"
                            />
                        </div>
                        <div className="input-wrapper">
                            <span className="input-icon">
                            <svg width="22" height="22" fill="none" stroke="#3e57d0" strokeWidth="2" strokeLinecap="round"
                                strokeLinejoin="round" viewBox="0 0 24 24"><path d="M16 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/></svg>
                            </span>
                            <input
                                type="text"
                                placeholder="Phone Number"
                                value={phoneNumber}
                                onChange={(e) => setPhoneNumber(e.target.value)}
                                className="input-field"
                            />
                        </div>
                        <div className="input-wrapper">
                              <span className="input-icon">
                                <svg width="22" height="22" fill="none" stroke="#3e57d0" strokeWidth="2" strokeLinecap="round"
                                     strokeLinejoin="round" viewBox="0 0 24 24"><rect x="3" y="11" width="18" height="11" rx="2"/><path d="M7 11V7a5 5 0 0 1 10 0v4"/></svg>
                              </span>
                            <input
                                type="password"
                                placeholder="Password"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                className="input-field"
                            />
                        </div>
                        <div className="input-wrapper">
                          <span className="input-icon">
                            <svg width="22" height="22" fill="none" stroke="#3e57d0" strokeWidth="2" strokeLinecap="round"
                                 strokeLinejoin="round" viewBox="0 0 24 24"><rect x="3" y="11" width="18" height="11" rx="2"/><path d="M7 11V7a5 5 0 0 1 10 0v4"/></svg>
                          </span>
                            <input
                                type="password"
                                placeholder="Confirm Password"
                                value={confirmPassword}
                                onChange={(e) => setConfirmPassword(e.target.value)}
                                className="input-field"
                            />
                        </div>
                        {passwordError && <div className="error-message">{passwordError}</div>}
                    </div>

                    <button onClick={handleRegister} className="register-button green-outline">Register</button>

                    {message && <p className="success-message">{message}</p>}
                    {error && <p className="error-message">{error}</p>}

                    <div className="footer-links">
                        <span>Already have an account? <a href="/login">Login</a></span>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Register;
