import {createRoot} from "react-dom/client";
import {BrowserRouter} from "react-router-dom";
import App from "./App.tsx";
import "./index.css";
import {InsuranceProvider} from "./contexts/HttpRequestsContext.tsx";
import {AuthProvider, useAuth} from "./contexts/AuthContext.tsx";
import React from "react";
import { attachAuthToken } from "./utils/axiosInstance";

const AuthInterceptorSetup: React.FC<{ children: React.ReactNode }> = ({ children }) => {
    const { accessToken } = useAuth();

    React.useEffect(() => {
        attachAuthToken(() => accessToken);
    }, [accessToken]);

    return <>{children}</>;
};

createRoot(document.getElementById("root")!).render(
    <React.StrictMode>
        <BrowserRouter>
            <AuthProvider>
                <AuthInterceptorSetup>
                    <InsuranceProvider>
                        <App/>
                     </InsuranceProvider>
                </AuthInterceptorSetup>
            </AuthProvider>
        </BrowserRouter>
    </React.StrictMode>
);