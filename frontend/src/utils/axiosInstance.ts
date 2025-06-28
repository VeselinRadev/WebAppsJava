import axios from "axios";

import { API_BASE_URL } from "./constants.ts";

const axiosInstance = axios.create({
    baseURL: API_BASE_URL,
});

export const attachAuthToken = (getAccessToken: () => string | null) => {
    axiosInstance.interceptors.request.use((config) => {
        const token = getAccessToken();
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    });
};

export default axiosInstance;