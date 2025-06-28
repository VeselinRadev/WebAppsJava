import React from "react";
import { Navigate } from "react-router-dom";
import { useAuth } from "../../contexts/AuthContext";

interface Props {
  children: React.ReactNode;
}

const PrivateRoute: React.FC<Props> = ({ children }) => {
  const { accessToken, isLoading } = useAuth();

  if (isLoading) return null;

  return accessToken ? <>{children}</> : <Navigate to="/login" replace />;
};

export default PrivateRoute;