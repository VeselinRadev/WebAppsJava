import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import {
    Descriptions,
    Button,
    Modal,
    Form,
    Input,
    DatePicker,
    Select,
    Table,
    Space,
    Switch,
    message,
    Divider,
    Card,
    Row,
    Col,
    Popconfirm,
} from "antd";
import dayjs from "dayjs";
import Sidebar from "./main/main-components/Sidebar";
import { useInsurance } from "../../contexts/HttpRequestsContext";

export default function InsuranceDetailsPage() {
    const { insuranceId } = useParams<{ insuranceId: string }>();
    const id = parseInt(insuranceId || "", 10);

    const {
        getInsurances,
        getPaymentsByInsuranceId,
        createPayment,
        updateInsurance,
        getClientByInsuranceId,
        getCarByInsuranceId,
        anulateInsurance,
        updatePayment,
    } = useInsurance();

    const [insurance, setInsurance] = useState<any>(null);
    const [client, setClient] = useState<any>(null);
    const [payments, setPayments] = useState<any[]>([]);
    const [car, setCar] = useState<any>(null);
    const [isModalVisible, setIsModalVisible] = useState(false);
    const [isEditVisible, setIsEditVisible] = useState(false);
    const [form] = Form.useForm();
    const [editForm] = Form.useForm();
    const [isPayModalVisible, setIsPayModalVisible] = useState(false);
    const [selectedPaymentId, setSelectedPaymentId] = useState<number | null>(null);
    const [payForm] = Form.useForm();

    const fetchInsurance = async () => {
        const all = await getInsurances();
        const match = all.find((i: any) => i.id === id);
        setInsurance(match || null);
        if (match) {
            editForm.setFieldsValue({
                ...match,
                startDate: dayjs(match.startDate),
                endDate: dayjs(match.endDate),
            });
        }
    };

    const fetchPayments = async () => {
        try {
            const payments = await getPaymentsByInsuranceId(id);
            setPayments(payments);
        } catch (err) {
            console.error("Error fetching payments:", err);
        }
    };

    const fetchClient = async () => {
        try {
            const data = await getClientByInsuranceId(id);
            setClient(data);
        } catch (err) {
            console.error("Error fetching client:", err);
        }
    };

    const fetchCar = async () => {
        try {
            const result = await getCarByInsuranceId(id);
            setCar(result);
        } catch (err) {
            console.error("Error fetching car:", err);
        }
    };

    useEffect(() => {
        if (insuranceId) {
            fetchInsurance();
            fetchPayments();
            fetchClient();
            fetchCar();
        }
    }, [insuranceId]);

    const handleSavePayment = async (values: any) => {
        const payload = {
            ...values,
            paymentDate: values.paymentDate.format("YYYY-MM-DD"),
            dueDate: values.dueDate.format("YYYY-MM-DD"),
            isPaid: values.isPaid || false,
            insurance: { id },
        };
        try {
            await createPayment(payload);
            setIsModalVisible(false);
            form.resetFields();
            fetchPayments();
        } catch (err) {
            console.error("Create payment failed", err);
        }
    };

    const handleEditInsurance = async (values: any) => {
        const payload = {
            ...values,
            startDate: values.startDate.format("YYYY-MM-DD"),
            endDate: values.endDate.format("YYYY-MM-DD"),
        };
        try {
            await updateInsurance(id, payload);
            setIsEditVisible(false);
            fetchInsurance();
        } catch (err) {
            console.error("Update insurance failed", err);
        }
    };

    const paymentColumns = [
        { title: "Date", dataIndex: "paymentDate", key: "paymentDate" },
        { title: "Due", dataIndex: "dueDate", key: "dueDate" },
        { title: "Amount", dataIndex: "amount", key: "amount" },
        { title: "Method", dataIndex: "method", key: "method" },
        {
            title: "Actions",
            key: "actions",
            render: (_: any, record: any) =>
                !record.isPaid && (
                    <Button type="link" onClick={() => {
                        setSelectedPaymentId(record.id);
                        setIsPayModalVisible(true);
                    }}>
                        Pay
                    </Button>
                ),
        }
    ];

    return (
        <div style={{ display: "flex", width: "100vw", height: "100vh" }}>
            <Sidebar />
            <main style={{ flex: 1, padding: 32, overflowY: "auto" }}>
                <div style={{ display: "flex", justifyContent: "space-between", marginBottom: 24 }}>
                    <h2>Insurance ID: {insuranceId}</h2>
                    <Popconfirm
                        title="Are you sure you want to anulate this insurance?"
                        onConfirm={async () => {
                            try {
                                await anulateInsurance(id);
                                await fetchInsurance();
                            } catch (e) {
                                console.error("Annulation failed:", e);
                            }
                        }}
                        okText="Yes"
                        cancelText="No"
                        disabled={insurance?.status === "ANNULLED"}
                    >
                        <Button danger disabled={insurance?.status === "ANNULLED"}>
                            {insurance?.status === "ANNULLED" ? "Anulated" : "Anulate"}
                        </Button>
                    </Popconfirm>
                </div>

                {insurance ? (
                    <>
                        <Descriptions bordered column={2} size="middle" style={{ marginBottom: 32, width: "100%" }}>
                            <Descriptions.Item label="Start Date">{insurance.startDate}</Descriptions.Item>
                            <Descriptions.Item label="End Date">{insurance.endDate}</Descriptions.Item>
                            <Descriptions.Item label="Sticker">{insurance.sticker}</Descriptions.Item>
                            <Descriptions.Item label="Green Card">{insurance.greenCard}</Descriptions.Item>
                            <Descriptions.Item label="Status">{insurance.status}</Descriptions.Item>
                            <Descriptions.Item label="Details" span={2}>{insurance.details}</Descriptions.Item>
                        </Descriptions>

                        <Divider orientation="left">Client Full Details</Divider>
                        <Card size="small" style={{ marginBottom: 24, width: "100%" }}>
                            {client ? (
                                <Row gutter={16}>
                                    <Col span={12}><p><b>Name:</b> {client.firstName} {client.lastName}</p></Col>
                                    <Col span={12}><p><b>Email:</b> {client.email || "-"}</p></Col>
                                    <Col span={12}><p><b>Phone:</b> {client.phoneNumber}</p></Col>
                                    <Col span={12}><p><b>Experience Years:</b> {client.experienceYears}</p></Col>
                                    <Col span={24}><p><b>Address:</b> {client.address?.street || "-"}</p></Col>
                                </Row>
                            ) : (
                                <p>Loading client data...</p>
                            )}
                        </Card>

                        <Divider orientation="left">Car Full Details</Divider>
                        <Card size="small" style={{ marginBottom: 32, width: "100%" }}>
                            <Row gutter={16}>
                                <Col span={12}><p><b>Plate:</b> {car?.plate}</p></Col>
                                <Col span={12}><p><b>VIN:</b> {car?.vin}</p></Col>
                                <Col span={12}><p><b>Make:</b> {car?.make}</p></Col>
                                <Col span={12}><p><b>Model:</b> {car?.model}</p></Col>
                                <Col span={12}><p><b>Year:</b> {car?.year}</p></Col>
                                <Col span={12}><p><b>Volume:</b> {car?.volume}</p></Col>
                                <Col span={12}><p><b>Power:</b> {car?.power}</p></Col>
                                <Col span={12}><p><b>Seats:</b> {car?.seats}</p></Col>
                                <Col span={12}><p><b>Fuel Type:</b> {car?.fuelType}</p></Col>
                            </Row>
                        </Card>
                    </>
                ) : (
                    <p>Loading insurance details...</p>
                )}

                <Table
                    columns={paymentColumns}
                    dataSource={payments}
                    rowKey="id"
                    pagination={false}
                    style={{ marginBottom: 32 }}
                />

                <Modal
                    title="Confirm Payment"
                    open={isPayModalVisible}
                    onCancel={() => {
                        setIsPayModalVisible(false);
                        setSelectedPaymentId(null);
                        payForm.resetFields();
                    }}
                    footer={null}
                >
                    <Form
                        layout="vertical"
                        form={payForm}
                        onFinish={async (values) => {
                            if (!selectedPaymentId) return;
                            try {
                                await updatePayment(selectedPaymentId, {
                                    paid: true,
                                    paymentMethod: values.method,
                                });
                                setIsPayModalVisible(false);
                                setSelectedPaymentId(null);
                                payForm.resetFields();
                                fetchPayments();
                            } catch (err) {
                                console.error("Payment update failed", err);
                            }
                        }}
                    >
                        <Form.Item
                            name="method"
                            label="Payment Method"
                            rules={[{ required: true, message: "Please select payment method" }]}
                        >
                            <Select placeholder="Select method">
                                <Select.Option value="CREDIT_CARD">Credit Card</Select.Option>
                                <Select.Option value="BANK_TRANSFER">Bank Transfer</Select.Option>
                                <Select.Option value="CASH">Cash</Select.Option>
                                <Select.Option value="OTHER">Other</Select.Option>
                            </Select>
                        </Form.Item>
                        <Form.Item>
                            <Space>
                                <Button type="primary" htmlType="submit">Confirm</Button>
                                <Button onClick={() => setIsPayModalVisible(false)}>Cancel</Button>
                            </Space>
                        </Form.Item>
                    </Form>
                </Modal>

                <Modal
                    title="Edit Insurance"
                    open={isEditVisible}
                    onCancel={() => setIsEditVisible(false)}
                    footer={null}
                >
                    <Form layout="vertical" form={editForm} onFinish={handleEditInsurance}>
                        <Form.Item name="startDate" label="Start Date" rules={[{ required: true }]}> <DatePicker style={{ width: "100%" }} /> </Form.Item>
                        <Form.Item name="endDate" label="End Date" rules={[{ required: true }]}> <DatePicker style={{ width: "100%" }} /> </Form.Item>
                        <Form.Item name="sticker" label="Sticker" rules={[{ required: true }]}> <Input /> </Form.Item>
                        <Form.Item name="greenCard" label="Green Card" rules={[{ required: true }]}> <Input /> </Form.Item>
                        <Form.Item name="details" label="Details"> <Input.TextArea rows={3} /> </Form.Item>
                        <Form.Item name="status" label="Status" rules={[{ required: true }]}> <Select> <Select.Option value="PENDING">Pending</Select.Option> <Select.Option value="ACTIVE">Active</Select.Option> <Select.Option value="EXPIRED">Expired</Select.Option> </Select> </Form.Item>
                        <Form.Item>
                            <Space>
                                <Button type="primary" htmlType="submit">Update</Button>
                                <Button onClick={() => setIsEditVisible(false)}>Cancel</Button>
                            </Space>
                        </Form.Item>
                    </Form>
                </Modal>
            </main>
        </div>
    );
}