import React, { useState, useEffect, useRef } from "react";
import { Card } from "antd";
import SockJS from "sockjs-client";
import {
  CheckCircleOutlined,
  ExclamationCircleOutlined,
  CloseCircleOutlined,
} from "@ant-design/icons";
import { Client } from "@stomp/stompjs";
import "./PostureStatus.css";

function PostureStatus({ sessionId, isTracking }) {
  const [metrics, setMetrics] = useState({
    status: "Offline",
    neckAngle: 0,
    torsoAngle: 0,
    shoulderRatio: 0,
  });

  const stompClientRef = useRef(null);

  useEffect(() => {
    // Chỉ kích hoạt kết nối khi được truyền tín hiệu tracking và có ID thực tế
    if (isTracking && sessionId) {
      console.log(`[WebSocket] Khởi chạy lắng nghe Session #${sessionId}...`);

      stompClientRef.current = new Client({
        webSocketFactory: () => new SockJS("https://deloyonrailway-production.up.railway.app/api/ws"),
        reconnectDelay: 5000,
        heartbeatIncoming: 10000,
        heartbeatOutgoing: 10000,
      });

      stompClientRef.current.onConnect = () => {
        console.log(`[WebSocket] Kết nối thành công tới cổng Session #${sessionId}`);
        
        // Mặc định khi vừa kết nối thành công thì đổi trạng thái từ Offline sang trạng thái hoạt động trực tuyến
        setMetrics(prev => ({ ...prev, status: "Good" }));

        stompClientRef.current.subscribe(
          `/topic/session/${sessionId}`,
          (message) => {
            const data = JSON.parse(message.body);
            console.log("[WebSocket] Dữ liệu Realtime nhận được từ Backend: ", data);

            // SỬA LỖI 1: Kiểm tra linh hoạt cả 2 trường hợp "postureStatus" hoặc "status" từ Backend Java gửi sang
            const backendStatus = data.postureStatus || data.status;
            let currentStatus = "Good";

            if (backendStatus === "WARNING_POSTURE" || backendStatus === "Warning") currentStatus = "Warning";
            if (backendStatus === "BAD_POSTURE" || backendStatus === "Bad") currentStatus = "Bad";

            setMetrics((prev) => ({
              status: currentStatus,
              neckAngle: data.neckAngle != null ? Number(data.neckAngle).toFixed(1) : prev.neckAngle,
              torsoAngle: data.torsoAngle != null ? Number(data.torsoAngle).toFixed(1) : prev.torsoAngle,
              // Backend viết map trúng biến request gửi đi (shoulderRatio hoặc shouterRatio)
              shoulderRatio: (data.shoulderRatio ?? data.shouterRatio ?? prev.shoulderRatio),
            }));
          }
        );
      };

      stompClientRef.current.onStompError = (frame) => {
        console.error("[WebSocket] Lỗi giao thức kết nối STOMP: ", frame);
      };

      stompClientRef.current.activate();
    } else {
      // Khi bấm End Session hoặc chưa hoạt động, đưa toàn bộ thông số về Offline an toàn
      setMetrics({
        status: "Offline",
        neckAngle: 0,
        torsoAngle: 0,
        shoulderRatio: 0,
      });
    }

    return () => {
      if (stompClientRef.current) {
        console.log("[WebSocket] Đóng cổng kết nối an toàn.");
        stompClientRef.current.deactivate();
      }
    };
  }, [sessionId, isTracking]);

  const renderIcon = () => {
    switch (metrics.status) {
      case "Good":
        return <CheckCircleOutlined className="good-icon" style={{ fontSize: 32, color: "#1ec76f" }} />;
      case "Warning":
        return <ExclamationCircleOutlined style={{ color: "#faad14", fontSize: 32 }} />;
      case "Bad":
        return <CloseCircleOutlined style={{ color: "#ff4d4f", fontSize: 32 }} />;
      default:
        return <CheckCircleOutlined style={{ color: "#666", fontSize: 32 }} />;
    }
  };

  // SỬA LỖI 2: Thiết lập lại cấu trúc JSX chuẩn khớp 100% với file PostureStatus.css của bạn
  return (
    <Card className="posture-status-card" bordered={false}>
      <div className="status-header">
        <span className="session-label">Session #{isTracking ? sessionId : "----"}</span>
      </div>

      <div className="status-content">
        <div className="status-indicator">
          {renderIcon()}
          <div className="status-text">
            <div className="status-title">Posture Status</div>
            <div className={`status-value ${metrics.status.toLowerCase()}`}>
              {metrics.status}
            </div>
          </div>
        </div>

        <div className="metrics-section">
          <div className="metric-item">
            <span className="metric-label">Neck Angle</span>
            <strong className="metric-value">{metrics.neckAngle}°</strong>
          </div>

          <div className="metric-item">
            <span className="metric-label">Torso Angle</span>
            <strong className="metric-value">{metrics.torsoAngle}°</strong>
          </div>

          <div className="metric-item">
            <span className="metric-label">Shoulder Ratio</span>
            <strong className="metric-value">
              {typeof metrics.shoulderRatio === 'number' ? metrics.shoulderRatio.toFixed(2) : metrics.shoulderRatio}
            </strong>
          </div>
        </div>
      </div>
    </Card>
  );
}

export default PostureStatus;