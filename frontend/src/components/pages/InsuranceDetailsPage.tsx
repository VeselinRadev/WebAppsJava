// InsuranceDetailsPage.tsx
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
} from "antd";
import dayjs from "dayjs";
import Sidebar from "./main/main-components/Sidebar";
import { useInsurance } from "../../contexts/HttpRequestsContext";

export default function InsuranceDetailsPage() {
    const { policyNumber } = useParams<{ policyNumber: string }>();
    const {
        getInsurances,
        getPayments,
        createPayment,
        updateInsurance,
    } = useInsurance();

    const [insurance, setInsurance] = useState<any>(null);
    const [payments, setPayments] = useState<any[]>([]);
    const [isModalVisible, setIsModalVisible] = useState(false);
    const [isEditVisible, setIsEditVisible] = useState(false);
    const [form] = Form.useForm();
    const [editForm] = Form.useForm();

    const fetchInsurance = async () => {
        const all = await getInsurances();
        const match = all.find((i: any) => i.policyNumber === policyNumber);
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
        const all = await getPayments();
        const relevant = all.filter((p) => p.insurance.policyNumber === policyNumber);
        setPayments(relevant);
    };

    useEffect(() => {
        fetchInsurance();
        fetchPayments();
    }, [policyNumber]);

    const handleSavePayment = async (values: any) => {
        const payload = {
            ...values,
            paymentDate: values.paymentDate.format("YYYY-MM-DD"),
            dueDate: values.dueDate.format("YYYY-MM-DD"),
            isPaid: values.isPaid || false,
            insurance: { policyNumber },
        };
        try {
            await createPayment(payload);
            message.success("Payment added");
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
            await updateInsurance(policyNumber!, payload);
            message.success("Insurance updated");
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
        { title: "Method", dataIndex: "paymentMethod", key: "paymentMethod" },
        { title: "Paid", dataIndex: "isPaid", key: "isPaid", render: (val: boolean) => (val ? "Yes" : "No") },
    ];

    return (
        <div style={{ display: "flex", width: "100vw", height: "100vh" }}>
            <Sidebar />
            <main style={{ flex: 1, padding: 32, overflowY: "auto" }}>
                <div style={{ display: "flex", justifyContent: "space-between", marginBottom: 24 }}>
                    <h2>Insurance Policy: {policyNumber}</h2>
                    <Button onClick={() => setIsEditVisible(true)}>Edit Insurance</Button>
                </div>

                {insurance ? (
                    <>
                        <Descriptions bordered column={2} size="middle" style={{ marginBottom: 32, width: "100%" }}>
                            <Descriptions.Item label="Start Date">{insurance.startDate}</Descriptions.Item>
                            <Descriptions.Item label="End Date">{insurance.endDate}</Descriptions.Item>
                            <Descriptions.Item label="Sticker">{insurance.sticker}</Descriptions.Item>
                            <Descriptions.Item label="Green Card">{insurance.greenCard}</Descriptions.Item>
                            <Descriptions.Item label="Status">{insurance.status}</Descriptions.Item>
                            <Descriptions.Item label="Client">{insurance.client?.name}</Descriptions.Item>
                            <Descriptions.Item label="Car">{insurance.car?.registrationNumber}</Descriptions.Item>
                            <Descriptions.Item label="Insurer">{insurance.insurer?.username}</Descriptions.Item>
                            <Descriptions.Item label="Details" span={2}>{insurance.details}</Descriptions.Item>
                        </Descriptions>

                        <Divider orientation="left">Client Full Details</Divider>
                        <Card size="small" style={{ marginBottom: 24, width: "100%" }}>
                            <Row gutter={16}>
                                <Col span={12}><p><b>Name:</b> {insurance.client?.name}</p></Col>
                                <Col span={12}><p><b>Email:</b> {insurance.client?.email || "-"}</p></Col>
                                <Col span={12}><p><b>Phone:</b> {insurance.client?.phoneNumber}</p></Col>
                                <Col span={12}><p><b>Experience Years:</b> {insurance.client?.experienceYears}</p></Col>
                                <Col span={24}><p><b>Address:</b> {insurance.client?.address?.street || "-"}</p></Col>
                            </Row>
                        </Card>

                        <Divider orientation="left">Car Full Details</Divider>
                        <Card size="small" style={{ marginBottom: 32, width: "100%" }}>
                            <Row gutter={16}>
                                <Col span={12}><p><b>Registration Number:</b> {insurance.car?.registrationNumber}</p></Col>
                            </Row>
                        </Card>
                    </>
                ) : (
                    <p>Loading insurance details...</p>
                )}

                <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
                    <h3>Payments</h3>
                    <Button type="primary" onClick={() => setIsModalVisible(true)}>Add Partial Payment</Button>
                </div>

                <Table
                    columns={paymentColumns}
                    dataSource={payments}
                    rowKey="id"
                    pagination={false}
                    style={{ marginBottom: 32 }}
                />

                <Modal
                    title="New Payment"
                    open={isModalVisible}
                    onCancel={() => {
                        setIsModalVisible(false);
                        form.resetFields();
                    }}
                    footer={null}
                >
                    <Form layout="vertical" form={form} onFinish={handleSavePayment}>
                        <Form.Item name="paymentDate" label="Payment Date" rules={[{ required: true }]}> <DatePicker style={{ width: "100%" }} /> </Form.Item>
                        <Form.Item name="dueDate" label="Due Date" rules={[{ required: true }]}> <DatePicker style={{ width: "100%" }} /> </Form.Item>
                        <Form.Item name="amount" label="Amount" rules={[{ required: true }]}> <Input type="number" /> </Form.Item>
                        <Form.Item name="paymentMethod" label="Payment Method" rules={[{ required: true }]}> <Select> <Select.Option value="CASH">Cash</Select.Option> <Select.Option value="CARD">Card</Select.Option> <Select.Option value="TRANSFER">Transfer</Select.Option> </Select> </Form.Item>
                        <Form.Item name="isPaid" label="Is Paid" valuePropName="checked"> <Switch /> </Form.Item>
                        <Form.Item>
                            <Space>
                                <Button type="primary" htmlType="submit">Save</Button>
                                <Button onClick={() => setIsModalVisible(false)}>Cancel</Button>
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