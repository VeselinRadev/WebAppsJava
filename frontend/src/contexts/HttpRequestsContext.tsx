// HttpRequestsContext.tsx
import React, { createContext, useContext } from "react";
import axios from "axios";
import { message } from "antd";

interface HttpRequestsContextType {
    getInsurances: () => Promise<any[]>;
    createInsurance: (data: any) => Promise<void>;
    updateInsurance: (policyNumber: string, data: any) => Promise<void>;
    deleteInsurance: (policyNumber: string) => Promise<void>;
    getCars: () => Promise<any[]>;
    createCar: (data: any) => Promise<void>;
    updateCar: (plate: string, data: any) => Promise<void>;
    deleteCar: (plate: string) => Promise<void>;

    getClients: () => Promise<any[]>;
    createClient: (data: any) => Promise<void>;
    updateClient: (ucn: string, data: any) => Promise<void>;
    deleteClient: (ucn: string) => Promise<void>;

    getPayments: () => Promise<any[]>;
    createPayment: (data: any) => Promise<void>;
    updatePayment: (id: number, data: any) => Promise<void>;
    deletePayment: (id: number) => Promise<void>;

    getDependencies: () => Promise<{
        clients: any[];
        cars: any[];
        insurers: any[];
    }>;
}

const mockClients = [
    {
        ucn: "1234567890",
        firstName: "John",
        lastName: "Doe",
        email: "john@example.com",
        phoneNumber: "0888123456",
        experienceYears: 4,
        address: {
            street: "Main Street 1",
            city: "Sofia",
            zipCode: "1000",
            country: "Bulgaria",
        },
    },
];
const HttpRequestsContext = createContext<HttpRequestsContextType | null>(null);

export const useInsurance = () => {
    const ctx = useContext(HttpRequestsContext);
    if (!ctx) throw new Error("useInsurance must be used within InsuranceProvider");
    return ctx;
};

const mockInsurances = [
    {
        policyNumber: "MOCK-001",
        startDate: "2025-01-01",
        endDate: "2026-01-01",
        sticker: "STICK123",
        greenCard: "GC123",
        details: "Mock policy details",
        status: "PENDING",
        client: { id: 1, name: "Mock Client" },
        car: { id: 1, registrationNumber: "MOCK-1234" },
        insurer: { id: 1, username: "mockinsurer" },
    },
];

const mockCars = [
    {
        plate: "MOCK-123",
        vin: "VIN123456789",
        make: "Toyota",
        model: "Corolla",
        year: 2021,
        volume: 1800,
        power: 140,
        seats: 5,
        registrationYear: 2021,
        fuelType: "PETROL",
        client: { id: 1, name: "Mock Client" },
    },
];

const mockDependencies = {
    clients: [{ id: 1, name: "Mock Client" }],
    cars: mockCars,
    insurers: [{ id: 1, username: "mockinsurer" }],
};

const mockPayments = [
    {
        id: 1,
        paymentDate: "2025-06-01",
        dueDate: "2025-06-30",
        amount: 200,
        paymentMethod: "CARD",
        isPaid: true,
        insurance: { policyNumber: "MOCK-001" },
    },
];

export const InsuranceProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
    const getInsurances = async () => {
        try {
            const res = await axios.get("/api/insurances");
            if (!Array.isArray(res.data)) throw new Error("Invalid insurance response");
            return res.data;
        } catch (err) {
            console.warn("Insurances API failed. Using mock.");
            message.warning("Using mock insurances");
            return mockInsurances;
        }
    };

    const createInsurance = async (data: any) => {
        try {
            await axios.post("/api/insurances", data);
        } catch {
            message.error("Failed to create insurance");
        }
    };

    const updateInsurance = async (policyNumber: string, data: any) => {
        try {
            await axios.put(`/api/insurances/${policyNumber}`, data);
        } catch {
            message.error("Failed to update insurance");
        }
    };

    const deleteInsurance = async (policyNumber: string) => {
        try {
            await axios.delete(`/api/insurances/${policyNumber}`);
        } catch {
            message.error("Failed to delete insurance");
        }
    };

    const getCars = async () => {
        try {
            const res = await axios.get("/api/cars");
            if (!Array.isArray(res.data)) throw new Error("Invalid car response");
            return res.data;
        } catch (err) {
            console.warn("Cars API failed. Using mock.");
            message.warning("Using mock cars");
            return mockCars;
        }
    };

    const createCar = async (data: any) => {
        try {
            await axios.post("/api/cars", data);
        } catch {
            message.error("Failed to create car");
        }
    };

    const updateCar = async (plate: string, data: any) => {
        try {
            await axios.put(`/api/cars/${plate}`, data);
        } catch {
            message.error("Failed to update car");
        }
    };

    const deleteCar = async (plate: string) => {
        try {
            await axios.delete(`/api/cars/${plate}`);
        } catch {
            message.error("Failed to delete car");
        }
    };

    const getDependencies = async () => {
        try {
            const [clients, cars, insurers] = await Promise.all([
                axios.get("/api/clients"),
                axios.get("/api/cars"),
                axios.get("/api/insurers"),
            ]);
            return {
                clients: Array.isArray(clients.data) ? clients.data : [],
                cars: Array.isArray(cars.data) ? cars.data : [],
                insurers: Array.isArray(insurers.data) ? insurers.data : [],
            };
        } catch (err) {
            console.warn("Dependencies API failed. Using mock.");
            message.warning("Using mock dependencies");
            return mockDependencies;
        }
    };

    const getClients = async () => {
        try {
            const res = await axios.get("/api/clients");
            return Array.isArray(res.data) ? res.data : mockClients;
        } catch (err) {
            console.warn("Using mock clients");
            return mockClients;
        }
    };

    const createClient = async (data: any) => {
        try {
            await axios.post("/api/clients", data);
        } catch {
            message.error("Failed to create client");
        }
    };

    const updateClient = async (ucn: string, data: any) => {
        try {
            await axios.put(`/api/clients/${ucn}`, data);
        } catch {
            message.error("Failed to update client");
        }
    };

    const deleteClient = async (ucn: string) => {
        try {
            await axios.delete(`/api/clients/${ucn}`);
        } catch {
            message.error("Failed to delete client");
        }
    };


    const getPayments = async () => {
        try {
            const res = await axios.get("/api/payments");
            return Array.isArray(res.data) ? res.data : mockPayments;
        } catch {
            message.warning("Using mock payments");
            return mockPayments;
        }
    };

    const createPayment = async (data: any) => {
        try {
            await axios.post("/api/payments", data);
        } catch {
            message.error("Failed to create payment");
        }
    };

    const updatePayment = async (id: number, data: any) => {
        try {
            await axios.put(`/api/payments/${id}`, data);
        } catch {
            message.error("Failed to update payment");
        }
    };

    const deletePayment = async (id: number) => {
        try {
            await axios.delete(`/api/payments/${id}`);
        } catch {
            message.error("Failed to delete payment");
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
                getDependencies,
            }}
        >
            {children}
        </HttpRequestsContext.Provider>
    );
};