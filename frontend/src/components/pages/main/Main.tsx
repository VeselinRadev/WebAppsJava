import { Routes, Route } from "react-router-dom";
import Dashboard from "./Dashboard.tsx";
import CarDashboard from "./CarDashboard.tsx";
import ClientDashboard from "./ClientDashboard.tsx";
import InsuranceDetailsPage from "../InsuranceDetailsPage.tsx";

function Main() {
  return (
      <Routes>
          <Route path="/*" element={<Dashboard />} />
          <Route path="/car" element={<CarDashboard />} />
          <Route path="/client" element={<ClientDashboard />} />
          <Route path="/insurances/:insuranceId" element={<InsuranceDetailsPage />} />
      </Routes>
  );
}

export default Main;
