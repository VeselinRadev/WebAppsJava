import React from "react";
import { useNavigate, useLocation } from "react-router-dom";
import { FiLogOut } from "react-icons/fi";
import "./Sidebar.css";

const Sidebar: React.FC = () => {
  const navigate = useNavigate();
  const location = useLocation();

  const isActive = (path: string) => location.pathname === path;

  const handleLogout = () => {
    localStorage.removeItem("token");
    navigate("/login");
  };

  return (
      <aside className="sidebar">
        <div className="sidebar-content">
          <h2 className="logo">Insurance</h2>
          <nav className="nav-buttons">
            <button
                className={isActive("/dashboard") ? "active" : ""}
                onClick={() => navigate("/dashboard")}
            >
              Insurances
            </button>
            <button
                className={isActive("/car") ? "active" : ""}
                onClick={() => navigate("/car")}
            >
              Cars
            </button>
            <button
                className={isActive("/client") ? "active" : ""}
                onClick={() => navigate("/client")}
            >
              Client
            </button>
          </nav>
          <button className="logout-btn" onClick={handleLogout}>
            <FiLogOut />
            Изход
          </button>
        </div>
      </aside>
  );
};

export default Sidebar;