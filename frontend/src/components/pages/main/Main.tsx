import { Routes, Route } from "react-router-dom";
import Login from "../login/Login.tsx";

function Main() {
  return (
      <Routes>
        <Route path="/*" element={<Login />} />
      </Routes>
  );
}

export default Main;
