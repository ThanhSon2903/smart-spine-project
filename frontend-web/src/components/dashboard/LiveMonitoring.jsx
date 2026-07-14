import React, { useState } from "react";
import { Button, message } from "antd";
import { PlayCircleOutlined, StopOutlined } from "@ant-design/icons";
import axiosClient from "../../api/axiosClient";
import "./LiveMonitoring.css";

function LiveMonitoring({ isTracking, sessionId, onStart, onEnd }) {
  const [loading, setLoading] = useState(false);

  const handleStartSession = async () => {
    setLoading(true);
    try {
      const response = await axiosClient.post("/sessions/start",
        {},
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`,
            RefreshToken: localStorage.getItem("RefreshToken")
          }
        }
      );

      const realSessionId = response.data.data.sessionId;
      
      // Kích hoạt hàm onStart truyền ngược lên cha (Dashboard)
      onStart(realSessionId);
      message.success(`Đã bắt đầu Session #${realSessionId}`);
    } catch (error) {
      console.error(error);
      message.error("Không thể bắt đầu phiên giám sát!");
    } finally {
      setLoading(false);
    }
  };

  const handleEndSession = async () => {
    setLoading(true);
    try {
      await axiosClient.post(`/sessions/${sessionId}/end`);
      
      // Kích hoạt hàm onEnd truyền ngược lên cha (Dashboard)
      onEnd();
      message.success(`Đã kết thúc Session #${sessionId}`);
    } catch (error) {
      console.error(error);
      message.error("Không thể kết thúc phiên!");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="live-monitoring" style={{ height: "100%", display: "flex", flexDirection: "column" }}>
      <div
        style={{
          background: "#1b1b1b",
          border: "1px solid #2d2d2d",
          borderRadius: "10px",
          flex: 1, // Ép phần khung đen tự động co giãn kéo dài bằng thẻ cha
          display: "flex",
          flexDirection: "column",
          justifyContent: "center",
          alignItems: "center",
          padding: "40px",
        }}
      >
        {!isTracking ? (
          <>
            <h2 style={{ color: "#fff", marginBottom: 8 }}>
              No active session
            </h2>
            <p style={{ color: "#888", marginBottom: 30 }}>
              Start a session to begin monitoring
            </p>
          </>
        ) : (
          <>
            <h2 style={{ color: "#52c41a", marginBottom: 8 }}>
              Session #{sessionId}
            </h2>
            <p style={{ color: "#888", marginBottom: 30 }}>
              AI posture monitoring is running...
            </p>
          </>
        )}

        <div style={{ display: "flex", gap: "12px" }}>
          <Button
            type="primary"
            icon={<PlayCircleOutlined />}
            loading={loading}
            disabled={isTracking}
            onClick={handleStartSession}
            style={{
              backgroundColor: "#1677ff",
              borderColor: "#1677ff",
            }}
          >
            Start Session
          </Button>

          <Button
            type="primary"
            icon={<StopOutlined />}
            loading={loading}
            disabled={!isTracking}
            onClick={handleEndSession}
            style={{
              backgroundColor: "#1677ff",
              borderColor: "#1677ff",
              opacity: isTracking ? 1 : 0.55,
              cursor: isTracking ? "pointer" : "not-allowed",
            }}
          >
            End Session
          </Button>
        </div>
      </div>
    </div>
  );
}

export default LiveMonitoring;