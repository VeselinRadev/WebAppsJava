import { useEffect, useState } from "react";
import {
  Table,
  Button,
  Modal,
  Form,
  Input,
  Space,
  message,
  Select,
  DatePicker,
} from "antd";
import { useNavigate } from "react-router-dom";
import { PlusOutlined } from "@ant-design/icons";
import Sidebar from "./main-components/Sidebar";
import { useInsurance } from "../../../contexts/HttpRequestsContext";

// Interfaces
interface Insurance {
  id: number;
  policyNumber: string;
  startDate: string;
  endDate: string;
  sticker: string;
  greenCard: string;
  details?: string;
  status: string;
}

export default function Dashboard() {
  const {
    getInsurances,
    createInsurance,
    updateInsurance,
  } = useInsurance();

  const [insurances, setInsurances] = useState<Insurance[]>([]);
  const [loading, setLoading] = useState(false);
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [editingInsurance, setEditingInsurance] = useState<Insurance | null>(null);
  const [form] = Form.useForm();
  const navigate = useNavigate();

  // Fetch insurances
  const fetchInsurances = async () => {
    setLoading(true);
    try {
      const data = await getInsurances();

      const filtered = data.filter((item: any) => !!item.policyNumber);
      setInsurances(filtered);
    } catch (err) {
      console.error("Error fetching insurances:", err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchInsurances();
  }, []);

  // Save handler
  const handleSave = async (values: any) => {
    const payload = {
      startDate: values.startDate.format("YYYY-MM-DD"),
      sticker: values.sticker,
      greenCard: values.greenCard,
      details: values.details,
      plate: values.plate,
      ucn: values.ucn,
      numberOfPayments: values.numberOfPayments,
    };

    try {
      if (editingInsurance) {
        await updateInsurance(editingInsurance.policyNumber, payload);
      } else {
        await createInsurance(payload);
      }
      setIsModalVisible(false);
      form.resetFields();
      fetchInsurances();
    } catch (err) {
      console.error("Error saving insurance:", err);
    }
  };

  // Columns
  const columns = [
    { title: "Policy #", dataIndex: "policyNumber", key: "policyNumber" },
    { title: "Start Date", dataIndex: "startDate", key: "startDate" },
    { title: "End Date", dataIndex: "endDate", key: "endDate" },
    { title: "Sticker", dataIndex: "sticker", key: "sticker" },
    { title: "Green Card", dataIndex: "greenCard", key: "greenCard" },
    {
      title: "Status",
      dataIndex: "status",
      key: "status",
      render: (text: string) => text || "-",
    },
    {
      title: "Details",
      dataIndex: "details",
      key: "details",
      render: (text: string) => text || "-",
    },
  ];

  return (
      <div style={{ display: "flex", width: "100%" }}>
        <Sidebar />
        <main style={{ flex: 1 }}>
          <div style={{ padding: "24px" }}>
            <div style={{ marginBottom: "16px" }}>
              <Button
                  type="primary"
                  icon={<PlusOutlined />}
                  onClick={() => {
                    setEditingInsurance(null);
                    form.resetFields();
                    setIsModalVisible(true);
                  }}
              >
                Add Insurance
              </Button>
            </div>

            <Table
                columns={columns}
                dataSource={insurances}
                rowKey={(record) => record.policyNumber}
                loading={loading}
                onRow={(record) => ({
                  onClick: () => {
                    if (record.policyNumber) {
                      navigate(`/insurances/${record.id}`);
                    } else {
                      message.error("Missing policy number");
                    }
                  },
                })}
            />

            <Modal
                title={editingInsurance ? "Edit Insurance" : "Add Insurance"}
                open={isModalVisible}
                onCancel={() => {
                  setIsModalVisible(false);
                  form.resetFields();
                }}
                footer={null}
            >
              <Form layout="vertical" form={form} onFinish={handleSave}>
                <Form.Item name="startDate" label="Start Date" rules={[{ required: true }]}>
                  <DatePicker style={{ width: "100%" }} />
                </Form.Item>
                <Form.Item name="sticker" label="Sticker" rules={[{ required: true, len: 9 }]}>
                  <Input />
                </Form.Item>
                <Form.Item name="greenCard" label="Green Card" rules={[{ required: true }]}>
                  <Input />
                </Form.Item>
                <Form.Item name="details" label="Details">
                  <Input.TextArea rows={3} />
                </Form.Item>
                <Form.Item name="plate" label="Car Plate" rules={[{ required: true }]}>
                  <Input />
                </Form.Item>
                <Form.Item name="ucn" label="Client UCN" rules={[{ required: true, len: 10 }]}>
                  <Input />
                </Form.Item>
                <Form.Item name="numberOfPayments" label="Payments" rules={[{ required: true }]}>
                  <Select>
                    <Select.Option value={1}>1</Select.Option>
                    <Select.Option value={2}>2</Select.Option>
                    <Select.Option value={4}>4</Select.Option>
                  </Select>
                </Form.Item>
                <Form.Item>
                  <Space>
                    <Button type="primary" htmlType="submit">
                      {editingInsurance ? "Update" : "Create"}
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