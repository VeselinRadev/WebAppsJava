import React, { useState } from "react";
import axios from "axios";
import Cookies from 'js-cookie';
import { useAuth } from "../../../contexts/AuthContext.tsx";
import { Link, useNavigate } from "react-router-dom";
import "./Login.css";
import { API_BASE_URL } from "../../../utils/constants";

const Login: React.FC = () => {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const navigate = useNavigate();
  const { setAccessToken } = useAuth();

  const handleLogin = async () => {
    if (!username || username.length < 3 || username.length > 30) {
      setError('Username must be between 3 and 30 characters');
      return;
    }

    if (!password || password.length < 8 || password.length > 100) {
      setError('Password must be between 8 and 100 characters');
      return;
    }
    try {
      const response = await axios.post(`${API_BASE_URL}/auth/login`, {
        username,
        password,
      });
      setAccessToken(response.data.accessToken);
      Cookies.set('refreshToken', response.data.refreshToken, {
        expires: 7,
        secure: true,
        sameSite: 'Strict',
      });

      navigate("/");
    } catch (error: any) {
      const message = error?.response?.data?.message || "Something went wrong. Please try again.";
      setError(message);
      console.error("Login failed:", error);
    }
  };

  return (
    <div className="login-container">
      <div className="login-form">
        <h2>Login</h2>
        <p className="welcome-message">Welcome!</p>

        <div className="input-container">
          <div className="input-wrapper">
            <span className="input-icon">
              <svg
                width="22"
                height="22"
                fill="none"
                stroke="#3e57d0"
                strokeWidth="2"
                strokeLinecap="round"
                strokeLinejoin="round"
                viewBox="0 0 24 24"
              >
                <path d="M16 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2" />
                <circle cx="12" cy="7" r="4" />
              </svg>
            </span>
            <input
              type="email"
              placeholder="Username or Email"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              className="input-field"
            />
          </div>
          <div className="input-wrapper">
            <span className="input-icon">
              <svg
                width="22"
                height="22"
                fill="none"
                stroke="#3e57d0"
                strokeWidth="2"
                strokeLinecap="round"
                strokeLinejoin="round"
                viewBox="0 0 24 24"
              >
                <rect x="3" y="11" width="18" height="11" rx="2" />
                <path d="M7 11V7a5 5 0 0 1 10 0v4" />
              </svg>
            </span>
            <input
              type="password"
              placeholder="Password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              className="input-field"
            />
          </div>
        </div>

        <button onClick={handleLogin} className="login-button green-outline">
          Login
        </button>

        {error && <p className="error-message">{error}</p>}

        <div className="footer-links">
          <span>
            New User? <Link to="/register">Register</Link>
          </span>
        </div>
      </div>
    </div>
  );
};

export default Login;
