import { useEffect, useState } from "react";
import {
    Table,
    Button,
    Modal,
    Form,
    Input,
    Space,
    Popconfirm,
    message,
} from "antd";
import { EditOutlined, DeleteOutlined, PlusOutlined } from "@ant-design/icons";
import Sidebar from "./main-components/Sidebar";
import { useInsurance } from "../../../contexts/HttpRequestsContext";

interface Address {
    street?: string;
    city?: string;
    postalCode?: string;
}

interface Client {
    ucn: string;
    firstName: string;
    lastName: string;
    email?: string;
    phoneNumber: string;
    experienceYears: number;
    address?: Address;
}

export default function ClientDashboard() {
    const { getClients, createClient, updateClient, deleteClient } = useInsurance();
    const [clients, setClients] = useState<Client[]>([]);
    const [loading, setLoading] = useState(false);
    const [isModalVisible, setIsModalVisible] = useState(false);
    const [editingClient, setEditingClient] = useState<Client | null>(null);
    const [form] = Form.useForm();

    const fetchClients = async () => {
        setLoading(true);
        const data = await getClients();
        setClients(data || []);
        setLoading(false);
    };

    useEffect(() => {
        fetchClients();
    }, []);

    const handleSave = async (values: any) => {
        const { street, city, postalCode, ...rest } = values;
        const payload = {
            ...rest,
            address: {
                street,
                city,
                postalCode,
            },
        };

        try {
            if (editingClient) {
                await updateClient(editingClient.ucn, payload);
            } else {
                await createClient(payload);
            }
            setIsModalVisible(false);
            form.resetFields();
            fetchClients();
        } catch (err) {
            console.error("Error saving client:", err);
        }
    };

    const handleDelete = async (ucn: string) => {
        try {
            await deleteClient(ucn);
            fetchClients();
        } catch (err) {
            console.error("Delete failed:", err);
        }
    };

    const columns = [
        { title: "UCN", dataIndex: "ucn", key: "ucn" },
        { title: "First Name", dataIndex: "firstName", key: "firstName" },
        { title: "Last Name", dataIndex: "lastName", key: "lastName" },
        { title: "Email", dataIndex: "email", key: "email" },
        { title: "Phone", dataIndex: "phoneNumber", key: "phoneNumber" },
        { title: "Experience", dataIndex: "experienceYears", key: "experienceYears" },
        {
            title: "Address",
            key: "address",
            render: (_: any, record: Client) =>
                record.address
                    ? `${record.address.street ?? ""}, ${record.address.city ?? ""}`
                    : "-",
        },
        {
            title: "Actions",
            key: "actions",
            render: (_: any, record: Client) => (
                <Space>
                    <Button
                        icon={<EditOutlined />}
                        onClick={() => {
                            setEditingClient(record);
                            form.setFieldsValue({
                                ...record,
                                street: record.address?.street ?? "",
                                city: record.address?.city ?? "",
                                postalCode: record.address?.postalCode ?? "",
                            });
                            setIsModalVisible(true);
                        }}
                    />
                    <Popconfirm
                        title="Delete client?"
                        onConfirm={() => handleDelete(record.ucn)}
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
                <div style={{ padding: 24 }}>
                    <Button
                        type="primary"
                        icon={<PlusOutlined />}
                        onClick={() => {
                            setEditingClient(null);
                            form.resetFields();
                            setIsModalVisible(true);
                        }}
                    >
                        Add Client
                    </Button>

                    <Table
                        columns={columns}
                        dataSource={clients}
                        rowKey="ucn"
                        loading={loading}
                        style={{ marginTop: 16 }}
                    />

                    <Modal
                        title={editingClient ? "Edit Client" : "Add Client"}
                        open={isModalVisible}
                        onCancel={() => {
                            setIsModalVisible(false);
                            form.resetFields();
                        }}
                        footer={null}
                    >
                        <Form layout="vertical" form={form} onFinish={handleSave}>
                            {!editingClient && (
                                <Form.Item name="ucn" label="UCN" rules={[{ required: true }]}>
                                    <Input />
                                </Form.Item>
                            )}
                            <Form.Item name="firstName" label="First Name" rules={[{ required: true }]}>
                                <Input />
                            </Form.Item>
                            <Form.Item name="lastName" label="Last Name" rules={[{ required: true }]}>
                                <Input />
                            </Form.Item>
                            <Form.Item name="email" label="Email">
                                <Input type="email" />
                            </Form.Item>
                            <Form.Item name="phoneNumber" label="Phone Number" rules={[{ required: true }]}>
                                <Input />
                            </Form.Item>
                            <Form.Item name="experienceYears" label="Experience Years" rules={[{ required: true }]}>
                                <Input type="number" />
                            </Form.Item>
                            <Form.Item name="street" label="Street">
                                <Input />
                            </Form.Item>
                            <Form.Item name="city" label="City">
                                <Input />
                            </Form.Item>
                            <Form.Item name="postalCode" label="Postal Code">
                                <Input />
                            </Form.Item>
                            <Form.Item>
                                <Space>
                                    <Button type="primary" htmlType="submit">
                                        {editingClient ? "Update" : "Create"}
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