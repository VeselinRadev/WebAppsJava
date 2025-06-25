import React from "react";
import { useNavigate } from "react-router-dom";

import { FiLogOut } from "react-icons/fi";
import { message } from "antd";
import "./Sidebar.css";

interface SidebarProps {

}

const Sidebar: React.FC<SidebarProps> = ({ mode, setMode }) => {
  const navigate = useNavigate();

  const handleLogout = () => {
    localStorage.removeItem("token");
    navigate("/login");
  };

  return (
    <aside className="sidebar">
      <div className="sidebar-content">
        <h2 className="logo">Застраховки</h2>
        <button className="logout-btn" onClick={handleLogout}>
          <FiLogOut />
          Изход
        </button>
      </div>
    </aside>
  );
};

export default Sidebar;
