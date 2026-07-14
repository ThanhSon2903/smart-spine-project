import { useState } from "react";
import axios from 'axios';
import { useNavigate, Link } from 'react-router-dom';

function Register() {
    const [username, setUserName] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [isHovered, setIsHovered] = useState(false); // Thêm state để làm hiệu ứng hover cho nút
    const navigate = useNavigate();

    const handleRegister = async (e) => {
        e.preventDefault();
        try {
            const res = await axios.post(
                "https://deloyonrailway-production.up.railway.app/api/users/register",
                { username, email, password }
            );
            localStorage.setItem("verifyEmail", email);
            alert(res.data.data)
            navigate("/verify-otp");
        } catch (error) {
            alert("Đăng ký thất bại");
            console.log(error);
        }
    };

    // Style chung cho các ô Input để tránh lặp lại code
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
                onSubmit={handleRegister}
                style={{
                    width: "380px",
                    padding: "40px",
                    backgroundColor: "#121212", // Màu hộp form nhẹ hơn nền một chút
                    border: "1px solid #222",
                    borderRadius: "16px",
                    boxShadow: "0 8px 32px rgba(0, 0, 0, 0.5)", // Đổ bóng tạo chiều sâu
                    boxSizing: "border-box"
                }}
            >
                <h2 style={{ 
                    color: "#fff", 
                    margin: "0 0 8px 0", 
                    fontSize: "28px", 
                    fontWeight: "600",
                    textAlign: "center" 
                }}>
                    Create Account
                </h2>
                
                <p style={{ 
                    color: "#aaa", 
                    fontSize: "14px", 
                    textAlign: "center", 
                    marginBottom: "32px",
                    marginTop: "0"
                }}>
                    Đăng ký để trải nghiệm hệ thống
                </p>

                <div>
                    <label style={{ color: "#ccc", fontSize: "12px", display: "block", marginBottom: "6px" }}>Full Name</label>
                    <input
                        type="text"
                        placeholder="Nhập họ và tên"
                        value={username}
                        autoComplete="off"
                        onChange={(e) => setUserName(e.target.value)}
                        style={inputStyle}
                        onFocus={(e) => e.target.style.borderColor = "#007bff"}
                        onBlur={(e) => e.target.style.borderColor = "#333"}
                    />
                </div>

                <div>
                    <label style={{ color: "#ccc", fontSize: "12px", display: "block", marginBottom: "6px" }}>Email Address</label>
                    <input
                        type="email"
                        placeholder="name@example.com"
                        value={email}
                        autoComplete="new-email"
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
                        autoComplete="new-password"
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
                            : "linear-gradient(45deg, #007bff, #00d2ff)", // Nút bấm hiệu ứng Gradient
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
                    Register
                </button>

                <p style={{ 
                    color: "#aaa", 
                    fontSize: "14px", 
                    textAlign: "center", 
                    marginTop: "24px",
                    marginBottom: "0" 
                }}>
                    Đã có tài khoản?{" "}
                    <Link to="/login" style={{ 
                        color: "#007bff", 
                        textDecoration: "none",
                        fontWeight: "500",
                        transition: "color 0.2s"
                    }}
                    onMouseEnter={(e) => e.target.style.color = "#00d2ff"}
                    onMouseLeave={(e) => e.target.style.color = "#007bff"}
                    >
                        Login
                    </Link>
                </p>
            </form>
        </div>
    );
}

export default Register;