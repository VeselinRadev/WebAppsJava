import React, { createContext, useContext } from "react";
import { message } from "antd";
import axiosInstance from "../utils/axiosInstance";
import { useAuth } from "./AuthContext";

interface HttpRequestsContextType {
    getInsurances: () => Promise<any[]>;
    createInsurance: (data: any) => Promise<void>;
    updateInsurance: (policyNumber: string, data: any) => Promise<void>;
    deleteInsurance: (policyNumber: string) => Promise<void>;
    getCars: () => Promise<any[] | undefined>;
    createCar: (data: any) => Promise<void>;
    updateCar: (plate: string, data: any) => Promise<void>;
    deleteCar: (plate: string) => Promise<void>;

    getClients: () => Promise<any[] | undefined>;
    createClient: (data: any) => Promise<void>;
    updateClient: (ucn: string, data: any) => Promise<void>;
    deleteClient: (ucn: string) => Promise<void>;

    getPayments: () => Promise<any[] | undefined>;
    createPayment: (data: any) => Promise<void>;
    updatePayment: (id: number, data: any) => Promise<void>;
    deletePayment: (id: number) => Promise<void>;

    getPaymentsByInsuranceId: (insuranceId: number) => Promise<any[]>;
    getClientByInsuranceId: (insuranceId: number) => Promise<any>;
    getCarByInsuranceId: (insuranceId: number) => Promise<any>;
    anulateInsurance: (id: number) => Promise<void>;
}

const HttpRequestsContext = createContext<HttpRequestsContextType | null>(null);

export const useInsurance = () => {
    const ctx = useContext(HttpRequestsContext);
    if (!ctx) throw new Error("useInsurance must be used within InsuranceProvider");
    return ctx;
};


export const InsuranceProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {

    const getInsurances = async () => {
        try {
            const res = await axiosInstance.get("/insurances");
            return res.data;
        } catch (err) {
            message.error("Failed to load insurance");
            return [];
        }
    };

    const createInsurance = async (data: any) => {
        try {
            await axiosInstance.post("/insurances", data);
        } catch (err: any) {
            const res =
                err?.response?.data?.message ||
                err?.message ||
                "Something went wrong. Please try again.";
            message.error(res);
        }
    };

    const updateInsurance = async (policyNumber: string, data: any) => {
        try {
            await axiosInstance.put(`/insurances/${policyNumber}`, data);
        } catch (err) {
            message.error("Failed to update insurance");
            throw err;
        }
    };

    const deleteInsurance = async (policyNumber: string) => {
        try {
            await axiosInstance.delete(`/insurances/${policyNumber}`);
        } catch {
            message.error("Failed to delete insurance");
        }
    };

    const getCars = async () => {
        try {
            const res = await axiosInstance.get("/cars");
            return Array.isArray(res.data) ? res.data : [];
        } catch (err) {
        }
    };

    const createCar = async (data: any) => {
        try {
            await axiosInstance.post("/cars", data);
        } catch {
            message.error("Failed to create car");
        }
    };

    const updateCar = async (plate: string, data: any) => {
        try {
            await axiosInstance.patch(`/cars/${plate}`, data);
        } catch {
            message.error("Failed to update car");
        }
    };

    const deleteCar = async (plate: string) => {
        try {
            await axiosInstance.delete(`/cars/${plate}`);
        } catch {
            message.error("Failed to delete car");
        }
    };

    const getClients = async () => {
        try {
            const res = await axiosInstance.get("/clients");
            return Array.isArray(res.data) ? res.data : [];
        } catch (err) {
        }
    };

    const createClient = async (data: any) => {
        try {
            await axiosInstance.post("/clients", data);
        } catch {
            message.error("Failed to create client");
        }
    };

    const updateClient = async (ucn: string, data: any) => {
        try {
            await axiosInstance.patch(`/clients/${ucn}`, data);
        } catch(err: any) {
            const res =
                err?.response?.data?.message ||
                err?.message ||
                "Something went wrong. Please try again.";
            message.error(res);
        }
    };

    const deleteClient = async (ucn: string) => {
        try {
            await axiosInstance.delete(`/clients/${ucn}`);
        } catch(err: any) {
            const res =
                err?.response?.data?.message ||
                err?.message ||
                "Something went wrong. Please try again.";
            message.error(res);
        }
    };

    const getPayments = async () => {
        try {
            const res = await axiosInstance.get("/payments");
            return Array.isArray(res.data) ? res.data : [];
        } catch(err: any) {
            const res =
                err?.response?.data?.message ||
                err?.message ||
                "Something went wrong. Please try again.";
            message.error(res);
        }
    };

    const createPayment = async (data: any) => {
        try {
            await axiosInstance.post("/payments", data);
        } catch {
            message.error("Failed to create payment");
        }
    };


    const updatePayment = async (id: number, data: { paid: boolean, paymentMethod: string }) => {
        try {
            await axiosInstance.patch(`/payments/${id}`, data);
        } catch (err: any) {
            const msg = err?.response?.data?.message || err?.message || "Failed to update payment.";
            message.error(msg);
        }
    };

    const deletePayment = async (id: number) => {
        try {
            await axiosInstance.delete(`/payments/${id}`);
        } catch {
            message.error("Failed to delete payment");
        }
    };

    const getPaymentsByInsuranceId = async  (insuranceId: number) => {
        try {
            const response = await axiosInstance.get("/payments", {
                params: {
                    insuranceId: insuranceId,
                },
            });
            return response.data;
        } catch (err: any) {
            const res =
                err?.response?.data?.message ||
                err?.message ||
                "Something went wrong. Please try again.";
            message.error(res);
        }
    };

    const getClientByInsuranceId = async (insuranceId: number): Promise<any> => {
        try {
            const response = await axiosInstance.get(`/clients/insurance/${insuranceId}`);
            return response.data;
        } catch (err: any) {
            const res =
                err?.response?.data?.message ||
                err?.message ||
                "Something went wrong. Please try again.";
            message.error(res);
        }
    };

    const getCarByInsuranceId = async (insuranceId: number) => {
        try {
            const response = await axiosInstance.get(`/cars/insurance/${insuranceId}`);
            return response.data;
        } catch (err: any) {
            const res =
                err?.response?.data?.message ||
                err?.message ||
                "Something went wrong. Please try again.";
            message.error(res);
        }
    };

    const anulateInsurance = async (id: number) => {
        try {
            await axiosInstance.patch(`/insurances/${id}`, {
                status: "ANNULLED",
            });
        } catch (err: any) {
            const msg =
                err?.response?.data?.message || err?.message || "Failed to annulate insurance.";
            message.error(msg);
        }
    };
    return (
        <HttpRequestsContext.Provider
            value={{
                getInsurances,
                createInsurance,
                updateInsurance,
                deleteInsurance,
                getCars,
                createCar,
                updateCar,
                deleteCar,
                getClients,
                createClient,
                updateClient,
                deleteClient,
                getPayments,
                createPayment,
                updatePayment,
                deletePayment,
                getPaymentsByInsuranceId,
                getClientByInsuranceId,
                getCarByInsuranceId,
                anulateInsurance,
            }}
        >
            {children}
        </HttpRequestsContext.Provider>
    );
};