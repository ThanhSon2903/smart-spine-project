import { useState } from "react";
import { Layout, Menu, Button } from "antd";
import {
  HomeOutlined,
  UnorderedListOutlined,
  MenuFoldOutlined,
  MenuUnfoldOutlined,
} from "@ant-design/icons";
import { useNavigate } from "react-router-dom";
import "./Sidebar.css";

function Sidebar() {
  const navigate = useNavigate();
  // State quản lý việc thụt ra thụt vào
  const [collapsed, setCollapsed] = useState(false);

  const menuItems = [
    {
      key: "dashboard",
      icon: <HomeOutlined className="menu-icon-glow" />,
      label: "Dashboard",
      onClick: () => navigate("/dashboard"),
    },
    {
      key: "sessions",
      icon: <UnorderedListOutlined className="menu-icon-glow" />,
      label: "Sessions",
      onClick: () => navigate("/sessions"),
    },
  ];

  return (
    <Layout.Sider 
      className="sidebar-custom" 
      width={240} // Tăng nhẹ width để chữ đứng thoải mái
      collapsedWidth={80} // Chiều rộng khi thụt vào
      collapsible 
      collapsed={collapsed}
      trigger={null}
    >
      <div className="sidebar-header-wrapper">
        <div className="sidebar-logo">
          {/* Ẩn chữ logo khi thu nhỏ để không bị vỡ giao diện */}
          {!collapsed && <span className="logo-text">PostureAI</span>}
        </div>
        
        {/* Nút trigger thụt ra thụt vào cực tinh tế */}
        <Button
          type="text"
          icon={collapsed ? <MenuUnfoldOutlined /> : <MenuFoldOutlined />}
          onClick={() => setCollapsed(!collapsed)}
          className="collapse-toggle-btn"
        />
      </div>

      <Menu
        theme="dark"
        mode="inline"
        defaultSelectedKeys={["dashboard"]}
        items={menuItems}
        className="sidebar-menu-custom"
      />
    </Layout.Sider>
  );
}

export default Sidebar;