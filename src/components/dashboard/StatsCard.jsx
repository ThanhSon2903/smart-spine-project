import { Card, Statistic } from "antd";
import { ArrowUpOutlined, ArrowDownOutlined, WarningOutlined } from "@ant-design/icons";
import "./StatsCard.css";

function StatsCard({ title, value, change, changeType, subtitle, icon }) {
  const isPositive = changeType === "positive";

  return (
    <Card className="stats-card">
      <div className="stats-card-header">
        <div className="stats-title">{title}</div>
        {icon === "warning" && <WarningOutlined className="warning-icon" />}
      </div>

      <div className="stats-value">{value}</div>

      {subtitle && <div className="stats-subtitle">{subtitle}</div>}

      {change && (
        <div className={`stats-change ${isPositive ? "positive" : "negative"}`}>
          {isPositive ? (
            <ArrowUpOutlined />
          ) : (
            <ArrowDownOutlined />
          )}
          <span>{change}</span>
        </div>
      )}
    </Card>
  );
}

export default StatsCard;
