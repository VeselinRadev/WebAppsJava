import { Routes, Route } from "react-router-dom";
import Login from "/Dashboard.tsx";

function Main() {
  return (
      <Routes><Route path="/*" element={<Dashboard />} />
      </Routes>
  );
}

export default Main;
