import { useState } from "react";
import axios from 'axios';
import { useNavigate, Link } from 'react-router-dom';

function Login() {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [isHovered, setIsHovered] = useState(false); // State xử lý hiệu ứng hover cho nút Login
    const navigate = useNavigate();

    const handleLogin = async (e) => {
        e.preventDefault();
        try {
            // Gọi API đăng nhập đến Spring Boot Backend
            const res = await axios.post(
                "https://deloyonrailway-production.up.railway.app/api/users/login",
                { email, password }
            );
            const token = res.data.data.accessToken;
            localStorage.setItem("token", token);
            console.log("Token hien tai " + token);

            alert("Đăng nhập thành công");
            navigate("/");
        } catch (error) {
            alert("Tên email hoặc mật khẩu không đúng");
        }
    };
    
    // Style đồng bộ cho các ô Input
    const inputStyle = {
        width: "100%",
        padding: "12px 16px",
        marginBottom: "20px",
        backgroundColor: "#1e1e1e",
        border: "1px solid #333",
        borderRadius: "8px",
        color: "#fff",
        fontSize: "14px",
        outline: "none",
        transition: "border-color 0.3s ease",
        boxSizing: "border-box"
    };

    return (
        <div
            style={{
                height: "100vh",
                display: "flex",
                justifyContent: "center",
                alignItems: "center",
                backgroundColor: "#0a0a0a", // Nền tối sâu sang trọng
                fontFamily: "'Segoe UI', Roboto, Helvetica, Arial, sans-serif"
            }}
        >
            <form
                onSubmit={handleLogin}
                style={{
                    width: "380px",
                    padding: "40px",
                    backgroundColor: "#121212", // Khối form sáng hơn nền để tạo chiều sâu
                    border: "1px solid #222",
                    borderRadius: "16px",
                    boxShadow: "0 8px 32px rgba(0, 0, 0, 0.5)", // Đổ bóng mờ hiện đại
                    boxSizing: "border-box"
                }}
            >
                <h2 style={{ 
                    color: "#fff", 
                    margin: "0 0 8px 0", 
                    fontSize: "26px", 
                    fontWeight: "600",
                    textAlign: "center" 
                }}>
                    AI Posture Tracking
                </h2>
                
                <p style={{ 
                    color: "#aaa", 
                    fontSize: "14px", 
                    textAlign: "center", 
                    marginBottom: "32px",
                    marginTop: "0"
                }}>
                    Chào mừng bạn quay trở lại!
                </p>

                <div>
                    <label style={{ color: "#ccc", fontSize: "12px", display: "block", marginBottom: "6px" }}>Email Address</label>
                    <input
                        type="email"
                        placeholder="name@example.com"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        style={inputStyle}
                        onFocus={(e) => e.target.style.borderColor = "#007bff"}
                        onBlur={(e) => e.target.style.borderColor = "#333"}
                    />
                </div>

                <div>
                    <label style={{ color: "#ccc", fontSize: "12px", display: "block", marginBottom: "6px" }}>Password</label>
                    <input
                        type="password"
                        placeholder="••••••••"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        style={inputStyle}
                        onFocus={(e) => e.target.style.borderColor = "#007bff"}
                        onBlur={(e) => e.target.style.borderColor = "#333"}
                    />
                </div>

                <button
                    type="submit"
                    onMouseEnter={() => setIsHovered(true)}
                    onMouseLeave={() => setIsHovered(false)}
                    style={{
                        width: "100%",
                        padding: "12px",
                        background: isHovered 
                            ? "linear-gradient(45deg, #0056b3, #00bfff)" 
                            : "linear-gradient(45deg, #007bff, #00d2ff)", // Nút bấm Gradient công nghệ
                        border: "none",
                        borderRadius: "8px",
                        color: "#fff",
                        fontSize: "16px",
                        fontWeight: "600",
                        cursor: "pointer",
                        transition: "all 0.3s ease",
                        boxShadow: isHovered ? "0 4px 12px rgba(0, 123, 255, 0.4)" : "none",
                        marginTop: "10px"
                    }}
                >
                    Login
                </button>

                <p style={{ 
                    color: "#aaa", 
                    fontSize: "14px", 
                    textAlign: "center", 
                    marginTop: "24px",
                    marginBottom: "0" 
                }}>
                    Chưa có tài khoản?{" "}
                    <Link to="/register" style={{ 
                        color: "#007bff", 
                        textDecoration: "none",
                        fontWeight: "500",
                        transition: "color 0.2s"
                    }}
                    onMouseEnter={(e) => e.target.style.color = "#00d2ff"}
                    onMouseLeave={(e) => e.target.style.color = "#007bff"}
                    >
                        Register
                    </Link>
                </p>

            </form>
        </div>
    );
}

export default Login;