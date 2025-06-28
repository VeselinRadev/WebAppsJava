import React, { createContext, useContext, useEffect, useState } from "react";
import axios from "axios";
import Cookies from "js-cookie";
import { API_BASE_URL } from "../utils/constants";

interface AuthContextType {
    accessToken: string | null;
    setAccessToken: (token: string | null) => void;
    isLoading: boolean;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const useAuth = (): AuthContextType => {
    const context = useContext(AuthContext);
    if (!context) {
        throw new Error("useAuth must be used within an AuthProvider");
    }
    return context;
};

export const AuthProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
    const [accessToken, setAccessToken] = useState<string | null>(null);
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        const refresh = async () => {
            const refreshToken = Cookies.get("refreshToken");
            if (!refreshToken) {
                setIsLoading(false);
                return;
            }

            try {
                const res = await axios.post(`${API_BASE_URL}/auth/refresh`, { refreshToken });
                setAccessToken(res.data.accessToken);
                Cookies.set('refreshToken', res.data.refreshToken, {
                    expires: 7,
                    secure: true,
                    sameSite: 'Strict',
                });
            } catch (err) {
                console.warn("Auto-refresh failed");
            } finally {
                setIsLoading(false);
            }
        };

        refresh();
    }, []);

    return (
        <AuthContext.Provider value={{ accessToken, setAccessToken, isLoading }}>
            {children}
        </AuthContext.Provider>
    );
};