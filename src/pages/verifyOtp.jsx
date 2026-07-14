import { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";

function VerifyOtp() {
    const [email, setEmail] = useState(
        localStorage.getItem("verifyEmail") || ""
    );
    const [otp, setOtp] = useState("");
    const [isHoveredVerify, setIsHoveredVerify] = useState(false); // State hover nút chính
    const [isHoveredResend, setIsHoveredResend] = useState(false); // State hover nút phụ
    const navigate = useNavigate();

    const handleVerify = async (e) => {
        e.preventDefault();
        try {
            
            await axios.post(
                "https://deloyonrailway-production.up.railway.app/api/users/verify-otp",
                { email, otp }
            );
            alert("Xác thực thành công");
            localStorage.removeItem("verifyEmail");
            navigate("/login");
        } catch (error) {
            alert("OTP không hợp lệ");
            console.log(error);
        }
    };

    const handleResendOtp = async () => {
        try {
            await axios.post(
                `https://deloyonrailway-production.up.railway.app/api/users/resent-otp/${email}`
            );
            alert("Đã gửi lại OTP");
        } catch (error) {
            alert("Gửi OTP thất bại");
        }
    };

    return (
        <div
            style={{
                height: "100vh",
                display: "flex",
                justifyContent: "center",
                alignItems: "center",
                backgroundColor: "#0a0a0a", // Nền tối sâu đồng bộ
                fontFamily: "'Segoe UI', Roboto, Helvetica, Arial, sans-serif"
            }}
        >
            <form
                onSubmit={handleVerify}
                style={{
                    width: "380px",
                    padding: "40px",
                    backgroundColor: "#121212",
                    border: "1px solid #222",
                    borderRadius: "16px",
                    boxShadow: "0 8px 32px rgba(0, 0, 0, 0.5)",
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
                    Email Verification
                </h2>
                
                <p style={{ 
                    color: "#aaa", 
                    fontSize: "14px", 
                    textAlign: "center", 
                    marginBottom: "32px",
                    marginTop: "0",
                    lineHeight: "1.4"
                }}>
                    Vui lòng nhập mã OTP đã được gửi tới tài khoản của bạn.
                </p>

                <div>
                    <label style={{ color: "#888", fontSize: "12px", display: "block", marginBottom: "6px" }}>Đăng ký cho Email</label>
                    <input
                        type="email"
                        value={email}
                        disabled
                        style={{
                            width: "100%",
                            padding: "12px 16px",
                            marginBottom: "20px",
                            backgroundColor: "#161616",
                            border: "1px solid #222",
                            borderRadius: "8px",
                            color: "#666", // Làm mờ chữ của ô disabled để tạo phân cấp trực quan
                            fontSize: "14px",
                            cursor: "not-allowed",
                            boxSizing: "border-box"
                        }}
                    />
                </div>

                <div>
                    <label style={{ color: "#ccc", fontSize: "12px", display: "block", marginBottom: "6px" }}>Enter OTP</label>
                    <input
                        type="text"
                        placeholder="Nhập mã OTP"
                        value={otp}
                        onChange={(e) => setOtp(e.target.value)}
                        style={{
                            width: "100%",
                            padding: "12px 16px",
                            marginBottom: "24px",
                            backgroundColor: "#1e1e1e",
                            border: "1px solid #333",
                            borderRadius: "8px",
                            color: "#fff",
                            fontSize: "14px",
                            letterSpacing: otp ? "4px" : "normal", // Tạo khoảng cách chữ khi gõ số OTP cho đẹp
                            textAlign: otp ? "center" : "left", // Căn giữa mã OTP khi bắt đầu điền số
                            outline: "none",
                            transition: "border-color 0.3s ease",
                            boxSizing: "border-box"
                        }}
                        onFocus={(e) => e.target.style.borderColor = "#007bff"}
                        onBlur={(e) => e.target.style.borderColor = "#333"}
                    />
                </div>

                {/* Nút chính - Verify OTP */}
                <button
                    type="submit"
                    onMouseEnter={() => setIsHoveredVerify(true)}
                    onMouseLeave={() => setIsHoveredVerify(false)}
                    style={{
                        width: "100%",
                        padding: "12px",
                        background: isHoveredVerify 
                            ? "linear-gradient(45deg, #0056b3, #00bfff)" 
                            : "linear-gradient(45deg, #007bff, #00d2ff)",
                        border: "none",
                        borderRadius: "8px",
                        color: "#fff",
                        fontSize: "16px",
                        fontWeight: "600",
                        cursor: "pointer",
                        transition: "all 0.3s ease",
                        boxShadow: isHoveredVerify ? "0 4px 12px rgba(0, 123, 255, 0.4)" : "none",
                        marginBottom: "12px"
                    }}
                >
                    Verify OTP
                </button>

                {/* Nút phụ - Resend OTP */}
                <button
                    type="button"
                    onClick={handleResendOtp}
                    onMouseEnter={() => setIsHoveredResend(true)}
                    onMouseLeave={() => setIsHoveredResend(false)}
                    style={{
                        width: "100%",
                        padding: "12px",
                        backgroundColor: isHoveredResend ? "#252525" : "transparent",
                        border: "1px solid #333",
                        borderRadius: "8px",
                        color: isHoveredResend ? "#fff" : "#aaa",
                        fontSize: "14px",
                        fontWeight: "500",
                        cursor: "pointer",
                        transition: "all 0.2s ease"
                    }}
                >
                    Resend OTP
                </button>
            </form>
        </div>
    );
}

export default VerifyOtp;