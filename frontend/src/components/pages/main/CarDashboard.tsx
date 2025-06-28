import { useEffect, useState } from "react";
import {
    Table,
    Button,
    Modal,
    Form,
    Input,
    Space,
    Popconfirm,
    Select,
    message,
} from "antd";
import { EditOutlined, DeleteOutlined, PlusOutlined } from "@ant-design/icons";
import Sidebar from "./main-components/Sidebar";
import { useInsurance } from "../../../contexts/HttpRequestsContext";

interface Car {
    plate: string;
    vin: string;
    make: string;
    model: string;
    year: number;
    volume: number;
    power: number;
    seats: number;
    registrationYear: number;
    fuelType: string;
    client: { id: number; name: string };
}

export default function CarDashboard() {
    const { getCars, createCar, updateCar, deleteCar, getDependencies } = useInsurance();

    const [cars, setCars] = useState<Car[]>([]);
    const [clients, setClients] = useState<any[]>([]);
    const [loading, setLoading] = useState(false);
    const [isModalVisible, setIsModalVisible] = useState(false);
    const [editingCar, setEditingCar] = useState<Car | null>(null);
    const [form] = Form.useForm();

    const fetchCars = async () => {
        setLoading(true);
        try {
            const data = await getCars();
            setCars(data || []);
        } catch (err) {
            console.error("Error fetching cars:", err);
            setCars([]);
        } finally {
            setLoading(false);
        }
    };

    const fetchClients = async () => {
        try {
            //const { clients } = await getDependencies();
            setClients(clients);
        } catch (err) {
            console.error("Failed to fetch clients", err);
        }
    };

    useEffect(() => {
        fetchCars();
        fetchClients();
    }, []);

    const handleSave = async (values: any) => {
        try {
            if (editingCar) {
                await updateCar(editingCar.plate, values);
                message.success("Car updated");
            } else {
                await createCar(values);
                message.success("Car created");
            }
            setIsModalVisible(false);
            form.resetFields();
            fetchCars();
        } catch (err) {
            console.error("Save failed:", err);
        }
    };

    const handleDelete = async (plate: string) => {
        try {
            await deleteCar(plate);
            message.success("Car deleted");
            fetchCars();
        } catch (err) {
            console.error("Delete failed:", err);
        }
    };

    const columns = [
        { title: "Plate", dataIndex: "plate", key: "plate" },
        { title: "VIN", dataIndex: "vin", key: "vin" },
        { title: "Make", dataIndex: "make", key: "make" },
        { title: "Model", dataIndex: "model", key: "model" },
        { title: "Year", dataIndex: "year", key: "year" },
        { title: "Fuel", dataIndex: "fuelType", key: "fuelType" },
        {
            title: "Client",
            dataIndex: ["client", "name"],
            key: "client",
        },
        {
            title: "Actions",
            key: "actions",
            render: (_: any, record: Car) => (
                <Space>
                    <Button
                        icon={<EditOutlined />}
                        onClick={() => {
                            setEditingCar(record);
                            form.setFieldsValue({
                                ...record,
                                clientId: record.client.id,
                            });
                            setIsModalVisible(true);
                        }}
                    />
                    <Popconfirm
                        title="Delete car?"
                        onConfirm={() => handleDelete(record.plate)}
                        okText="Yes"
                        cancelText="No"
                    >
                        <Button icon={<DeleteOutlined />} danger />
                    </Popconfirm>
                </Space>
            ),
        },
    ];

    return (
        <div style={{ display: "flex", width: "100%" }}>
            <Sidebar />
            <main style={{ flex: 1 }}>
                <div style={{ padding: "24px" }}>
                    <Button
                        type="primary"
                        icon={<PlusOutlined />}
                        onClick={() => {
                            setEditingCar(null);
                            form.resetFields();
                            setIsModalVisible(true);
                        }}
                    >
                        Add Car
                    </Button>

                    <Table
                        columns={columns}
                        dataSource={cars}
                        rowKey={(record) => record.plate || `car-${Math.random()}`}
                        loading={loading}
                        style={{ marginTop: 16 }}
                    />

                    <Modal
                        title={editingCar ? "Edit Car" : "Add Car"}
                        open={isModalVisible}
                        onCancel={() => {
                            setIsModalVisible(false);
                            form.resetFields();
                        }}
                        footer={null}
                    >
                        <Form layout="vertical" form={form} onFinish={handleSave}>
                            {!editingCar && (
                                <Form.Item name="plate" label="Plate" rules={[{ required: true }]}>
                                    <Input />
                                </Form.Item>
                            )}
                            <Form.Item name="vin" label="VIN" rules={[{ required: true }]}>
                                <Input />
                            </Form.Item>
                            <Form.Item name="make" label="Make" rules={[{ required: true }]}>
                                <Input />
                            </Form.Item>
                            <Form.Item name="model" label="Model" rules={[{ required: true }]}>
                                <Input />
                            </Form.Item>
                            <Form.Item name="year" label="Year" rules={[{ required: true }]}>
                                <Input type="number" />
                            </Form.Item>
                            <Form.Item name="volume" label="Volume" rules={[{ required: true }]}>
                                <Input type="number" />
                            </Form.Item>
                            <Form.Item name="power" label="Power" rules={[{ required: true }]}>
                                <Input type="number" />
                            </Form.Item>
                            <Form.Item name="seats" label="Seats" rules={[{ required: true }]}>
                                <Input type="number" />
                            </Form.Item>
                            <Form.Item
                                name="registrationYear"
                                label="Registration Year"
                                rules={[{ required: true }]}
                            >
                                <Input type="number" />
                            </Form.Item>
                            <Form.Item name="fuelType" label="Fuel Type" rules={[{ required: true }]}>
                                <Select>
                                    <Select.Option value="PETROL">Petrol</Select.Option>
                                    <Select.Option value="DIESEL">Diesel</Select.Option>
                                    <Select.Option value="ELECTRIC">Electric</Select.Option>
                                </Select>
                            </Form.Item>
                            <Form.Item name="clientId" label="Client" rules={[{ required: true }]}>
                                <Select>
                                    {clients.map((client) => (
                                        <Select.Option key={client.id} value={client.id}>
                                            {client.name}
                                        </Select.Option>
                                    ))}
                                </Select>
                            </Form.Item>
                            <Form.Item>
                                <Space>
                                    <Button type="primary" htmlType="submit">
                                        {editingCar ? "Update" : "Create"}
                                    </Button>
                                    <Button
                                        onClick={() => {
                                            setIsModalVisible(false);
                                            form.resetFields();
                                        }}
                                    >
                                        Cancel
                                    </Button>
                                </Space>
                            </Form.Item>
                        </Form>
                    </Modal>
                </div>
            </main>
        </div>
    );
}