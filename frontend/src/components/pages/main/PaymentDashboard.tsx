// PaymentDashboard.tsx
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
    DatePicker,
    message,
    Switch,
} from "antd";
import { EditOutlined, DeleteOutlined, PlusOutlined } from "@ant-design/icons";
import dayjs from "dayjs";
import Sidebar from "./main-components/Sidebar";
import { useInsurance } from "../../../contexts/HttpRequestsContext";

interface Payment {
    id: number;
    paymentDate: string;
    dueDate: string;
    amount: number;
    paymentMethod: "CASH" | "CARD" | "TRANSFER";
    isPaid: boolean;
    insurance: { policyNumber: string };
}

export default function PaymentDashboard() {
    const { getPayments, createPayment, updatePayment, deletePayment } = useInsurance();
    const [payments, setPayments] = useState<Payment[]>([]);
    const [loading, setLoading] = useState(false);
    const [isModalVisible, setIsModalVisible] = useState(false);
    const [editingPayment, setEditingPayment] = useState<Payment | null>(null);
    const [form] = Form.useForm();

    const fetchPayments = async () => {
        setLoading(true);
        const data = await getPayments();
        setPayments(data);
        setLoading(false);
    };

    useEffect(() => {
        fetchPayments();
    }, []);

    const handleSave = async (values: any) => {
        const payload = {
            ...values,
            paymentDate: values.paymentDate.format("YYYY-MM-DD"),
            dueDate: values.dueDate.format("YYYY-MM-DD"),
            insurance: { policyNumber: values.policyNumber },
        };

        try {
            if (editingPayment) {
                await updatePayment(editingPayment.id, payload);
                message.success("Payment updated");
            } else {
                await createPayment(payload);
                message.success("Payment created");
            }
            setIsModalVisible(false);
            form.resetFields();
            fetchPayments();
        } catch (err) {
            console.error("Error saving payment:", err);
        }
    };

    const handleDelete = async (id: number) => {
        try {
            await deletePayment(id);
            message.success("Payment deleted");
            fetchPayments();
        } catch (err) {
            console.error("Delete failed:", err);
        }
    };

    const columns = [
        { title: "ID", dataIndex: "id", key: "id" },
        { title: "Payment Date", dataIndex: "paymentDate", key: "paymentDate" },
        { title: "Due Date", dataIndex: "dueDate", key: "dueDate" },
        { title: "Amount", dataIndex: "amount", key: "amount" },
        { title: "Method", dataIndex: "paymentMethod", key: "paymentMethod" },
        {
            title: "Paid",
            dataIndex: "isPaid",
            key: "isPaid",
            render: (isPaid: boolean) => (isPaid ? "Yes" : "No"),
        },
        {
            title: "Policy #",
            dataIndex: ["insurance", "policyNumber"],
            key: "insurance",
        },
        {
            title: "Actions",
            key: "actions",
            render: (_: any, record: Payment) => (
                <Space>
                    <Button
                        icon={<EditOutlined />}
                        onClick={() => {
                            setEditingPayment(record);
                            form.setFieldsValue({
                                ...record,
                                paymentDate: dayjs(record.paymentDate),
                                dueDate: dayjs(record.dueDate),
                                policyNumber: record.insurance.policyNumber,
                            });
                            setIsModalVisible(true);
                        }}
                    />
                    <Popconfirm
                        title="Delete payment?"
                        onConfirm={() => handleDelete(record.id)}
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
                            setEditingPayment(null);
                            form.resetFields();
                            setIsModalVisible(true);
                        }}
                    >
                        Add Payment
                    </Button>

                    <Table
                        columns={columns}
                        dataSource={payments}
                        rowKey="id"
                        loading={loading}
                        style={{ marginTop: 16 }}
                    />

                    <Modal
                        title={editingPayment ? "Edit Payment" : "Add Payment"}
                        open={isModalVisible}
                        onCancel={() => {
                            setIsModalVisible(false);
                            form.resetFields();
                        }}
                        footer={null}
                    >
                        <Form layout="vertical" form={form} onFinish={handleSave}>
                            <Form.Item name="paymentDate" label="Payment Date" rules={[{ required: true }]}> <DatePicker style={{ width: "100%" }} /> </Form.Item>
                            <Form.Item name="dueDate" label="Due Date" rules={[{ required: true }]}> <DatePicker style={{ width: "100%" }} /> </Form.Item>
                            <Form.Item name="amount" label="Amount" rules={[{ required: true }]}> <Input type="number" /> </Form.Item>
                            <Form.Item name="paymentMethod" label="Payment Method" rules={[{ required: true }]}> <Select> <Select.Option value="CASH">Cash</Select.Option> <Select.Option value="CARD">Card</Select.Option> <Select.Option value="TRANSFER">Transfer</Select.Option> </Select> </Form.Item>
                            <Form.Item name="isPaid" label="Is Paid" valuePropName="checked"> <Switch /> </Form.Item>
                            <Form.Item name="policyNumber" label="Policy Number" rules={[{ required: true }]}> <Input /> </Form.Item>
                            <Form.Item>
                                <Space>
                                    <Button type="primary" htmlType="submit">
                                        {editingPayment ? "Update" : "Create"}
                                    </Button>
                                    <Button onClick={() => {
                                        setIsModalVisible(false);
                                        form.resetFields();
                                    }}>Cancel</Button>
                                </Space>
                            </Form.Item>
                        </Form>
                    </Modal>
                </div>
            </main>
        </div>
    );
}