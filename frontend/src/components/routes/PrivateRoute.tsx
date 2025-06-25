import React, { useEffect, useState } from "react";
import { Navigate } from "react-router-dom";
import { useAuth } from "../../contexts/AuthContext";
import axios from "axios";
import Cookies from "js-cookie";
import { API_BASE_URL } from "../../utils/constants";

interface Props {
  children: React.ReactNode;
}

const PrivateRoute: React.FC<Props> = ({ children }) => {
  const { accessToken, setAccessToken } = useAuth();
  const [loading, setLoading] = useState(true);
  const [isValid, setIsValid] = useState<boolean>(false);

  useEffect(() => {
    const checkAuth = async () => {
      if (accessToken) {
        setIsValid(true);
        setLoading(false);
        return;
      }

      const refreshToken = Cookies.get("refreshToken");
      if (!refreshToken) {
        setIsValid(false);
        setLoading(false);
        return;
      }

      try {
        const response = await axios.post(`${API_BASE_URL}/auth/refresh`, {
          refreshToken,
        });

        setAccessToken(response.data.accessToken);
        setIsValid(true);
      } catch (error) {
        console.error("Token refresh failed:", error);
        setIsValid(false);
      } finally {
        setLoading(false);
      }
    };

    checkAuth();
  }, [accessToken, setAccessToken]);

  if (loading) return null;

  return isValid ? <>{children}</> : <Navigate to="/login" replace />;
};

export default PrivateRoute;