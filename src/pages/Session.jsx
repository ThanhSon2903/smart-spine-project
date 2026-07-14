import { useEffect, useState } from "react";
import axiosClient from "../api/axiosClient";

function Session() {
    const [sessions, setSessions] = useState([]);

    const [searchTerm, setSearchTerm] = useState("");
    const [filterDate, setFilterDate] = useState("");
    const [sortConfig, setSortConfig] = useState({ key: "startTime", direction: "desc" });

    // State lưu dữ liệu chi tiết của một Session lấy từ API View Detail
    const [selectedSession, setSelectedSession] = useState(null);
    // State quản lý trạng thái đang tải dữ liệu chi tiết
    const [loadingDetail, setLoadingDetail] = useState(false);

    // State phục vụ phân trang cho bảng chi tiết Postures
    const [currentPage, setCurrentPage] = useState(1);
    const itemsPerPage = 5; // Số dòng hiển thị trên mỗi trang chi tiết

    useEffect(() => {
        fetchFunction();
    }, []);

    const formatDate = (date) => {
        if (!date) return "-";
        return new Date(date).toLocaleString("vi-VN", {
            day: "2-digit",
            month: "2-digit",
            year: "numeric",
            hour: "2-digit",
            minute: "2-digit",
            second: "2-digit" 
        });
    }

    const formatDuration = (duration) => {
        if (!duration) return "0 giây";
        return duration.replace(/([a-zA-ZÀ-ỹ]+)(\d+)/g, "$1 $2");
    }

    // Làm tròn số thập phân cho các thông số góc nhìn gọn gàng
    const formatAngle = (angle) => {
        if (angle === undefined || angle === null) return "-";
        return Number(angle).toFixed(2) + "°";
    }

    // API lấy danh sách tổng quan ban đầu
    const fetchFunction = async () => {
        try {
            const token = localStorage.getItem("token");
            const res = await axiosClient.get(
                "/sessions/list-sessions",
                {
                    headers: {
                        Authorization: `Bearer ${token}`
                    }
                }
            );
            setSessions(res.data.data);
        } catch (error) {
            console.log("Error fetching list sessions:", error);
        }
    }

    // Hàm gọi API Xem chi tiết theo sessionId
    const handleViewDetail = async (sessionId) => {
        setLoadingDetail(true);
        setSelectedSession(null); 
        setCurrentPage(1); // Reset về trang 1 khi xem session mới
        try {
            const token = localStorage.getItem("token");
            const res = await axiosClient.get(
                `/sessions/view-detail/${sessionId}`,
                {
                    headers: {
                        Authorization: `Bearer ${token}`
                    }
                }
            );
            setSelectedSession(res.data.data); 
        } catch (error) {
            console.log("Error fetching session detail:", error);
            alert("Không thể tải dữ liệu chi tiết của session này.");
        } finally {
            setLoadingDetail(false);
        }
    }

    const handleSort = (key) => {
        let direction = "asc";
        if (sortConfig.key === key && sortConfig.direction === "asc") {
            direction = "desc";
        }
        setSortConfig({ key, direction });
    }

    const processedSessions = [...sessions]
        .filter((item) =>
            item.sessionId.toString().toLowerCase().includes(searchTerm.toLowerCase())
        )
        .filter((item) => {
            if (!filterDate) return true;
            const sessionDate = item.startTime.split("T")[0];
            return sessionDate === filterDate;
        })
        .sort((a, b) => {
            if (!a[sortConfig.key] || !b[sortConfig.key]) return 0;
            const dateA = new Date(a[sortConfig.key]).getTime();
            const dateB = new Date(b[sortConfig.key]).getTime();
            return sortConfig.direction === "asc" ? dateA - dateB : dateB - dateA;
        });

    const getSortIcon = (key) => {
        if (sortConfig.key !== key) return "↕️";
        return sortConfig.direction === "asc" ? "🔼" : "🔽";
    };

    // Logic xử lý Phân trang dữ liệu cho danh sách tư thế chi tiết
    const postureList = selectedSession?.postureResponses || [];
    const totalPages = Math.ceil(postureList.length / itemsPerPage);
    const indexOfLastItem = currentPage * itemsPerPage;
    const indexOfFirstItem = indexOfLastItem - itemsPerPage;
    const currentPostures = postureList.slice(indexOfFirstItem, indexOfLastItem);

    return (
        <div className="min-h-screen bg-slate-950 p-4 md:p-8 font-sans antialiased text-slate-200">
            <div className="max-w-7xl mx-auto">

                {/* Header Section */}
                <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4 mb-8">
                    <div>
                        <h3 className="text-3xl font-bold tracking-tight text-white flex items-center gap-2">
                            <span>📋</span> Tracking session history
                        </h3>
                        <p className="text-sm text-slate-400 mt-1">
                            Review and manage your posture monitoring sessions
                        </p>
                    </div>

                    <div className="bg-slate-900 border border-slate-800 rounded-2xl px-6 py-4 shadow-xl flex items-center gap-4">
                        <div className="p-3 bg-blue-950/60 text-blue-400 border border-blue-900/50 rounded-xl font-semibold">
                            📊 Total
                        </div>
                        <div>
                            <p className="text-xs text-slate-500 font-medium uppercase tracking-wider">Total Sessions</p>
                            <h2 className="text-2xl font-bold text-white">{sessions.length}</h2>
                        </div>
                    </div>
                </div>

                {/* Thanh Công Cụ: Tìm kiếm & Trạng thái lọc */}
                <div className="mb-4 flex flex-col md:flex-row gap-4 items-center justify-between">
                    <div className="flex flex-col sm:flex-row gap-3 w-full md:w-auto items-stretch sm:items-center">
                        <div className="relative w-full sm:w-64">
                            <input
                                type="text"
                                placeholder="🔍 Tìm kiếm theo số ID..."
                                value={searchTerm}
                                onChange={(e) => setSearchTerm(e.target.value)}
                                className="w-full bg-slate-900 border border-slate-800 rounded-xl px-4 py-2 text-sm text-slate-200 placeholder-slate-500 focus:outline-none focus:border-blue-500 transition-colors"
                            />
                        </div>

                        <div className="relative w-full sm:w-48">
                            <input
                                type="date"
                                value={filterDate}
                                onChange={(e) => setFilterDate(e.target.value)}
                                className="w-full bg-slate-900 border border-slate-800 rounded-xl px-4 py-2 text-sm text-slate-200 focus:outline-none focus:border-blue-500 transition-colors"
                            />
                            {filterDate && (
                                <button 
                                    onClick={() => setFilterDate("")}
                                    className="absolute right-8 top-1/2 -translate-y-1/2 text-xs text-slate-500 hover:text-slate-300"
                                >
                                    ✕
                                </button>
                            )}
                        </div>
                    </div>
                    
                    {(searchTerm || filterDate) && (
                        <div className="text-xs text-slate-400 self-start md:self-center">
                            Tìm thấy <span className="text-blue-400 font-bold">{processedSessions.length}</span> kết quả phù hợp.
                        </div>
                    )}
                </div>

                {/* Main Table Card */}
                <div className="bg-slate-900 rounded-2xl border border-slate-800 shadow-2xl overflow-hidden mb-8">
                    <div className="overflow-x-auto">
                        <table className="w-full border-collapse text-left text-sm text-slate-300">
                            <thead className="bg-slate-950/60 border-b border-slate-800 text-xs font-semibold uppercase tracking-wider text-slate-400">
                                <tr>
                                    <th className="px-6 py-4 w-20">ID</th>
                                    <th className="px-6 py-4 cursor-pointer hover:text-white select-none transition-colors" onClick={() => handleSort("startTime")}>
                                        Start Time <span className="ml-1 text-[10px]">{getSortIcon("startTime")}</span>
                                    </th>
                                    <th className="px-6 py-4 cursor-pointer hover:text-white select-none transition-colors" onClick={() => handleSort("endTime")}>
                                        End Time <span className="ml-1 text-[10px]">{getSortIcon("endTime")}</span>
                                    </th>
                                    <th className="px-6 py-4 text-center">Duration</th>
                                    <th className="px-6 py-4 text-center">Bad Posture</th>
                                    <th className="px-6 py-4 text-center w-32">View</th>
                                </tr>
                            </thead>

                            <tbody className="divide-y divide-slate-800">
                                {processedSessions.length === 0 ? (
                                    <tr>
                                        <td colSpan={6} className="py-12 text-center text-slate-500">
                                            <div className="flex flex-col items-center justify-center gap-2">
                                                <span className="text-3xl">📂</span>
                                                <p>No session history found.</p>
                                            </div>
                                        </td>
                                    </tr>
                                ) : (
                                    processedSessions.map((item) => (
                                        <tr key={item.sessionId} className={`hover:bg-slate-800/40 transition-colors ${selectedSession?.sessionId === item.sessionId ? 'bg-blue-950/20 border-l-2 border-l-blue-500' : ''}`}>
                                            <td className="px-6 py-4 font-semibold text-blue-400">#{item.sessionId}</td>
                                            <td className="px-6 py-4 whitespace-nowrap">{formatDate(item.startTime)}</td>
                                            <td className="px-6 py-4 whitespace-nowrap">{formatDate(item.endTime)}</td>
                                            <td className="px-6 py-4 text-center whitespace-nowrap">
                                                <span className="inline-flex items-center px-2.5 py-1 rounded-md text-xs font-medium bg-emerald-950/60 text-emerald-400 border border-emerald-900/40">
                                                    {formatDuration(item.duration)}
                                                </span>
                                            </td>
                                            <td className="px-6 py-4 text-center whitespace-nowrap">
                                                <span className={`inline-flex items-center px-2.5 py-1 rounded-md text-xs font-medium border ${item.badPostureDuration > 0 ? 'bg-red-950/60 text-red-400 border-red-900/40' : 'bg-slate-800/60 text-slate-400 border-slate-700/50'}`}>
                                                    {item.badPostureDuration}
                                                </span>
                                            </td>
                                            <td className="px-6 py-4 text-center">
                                                <button 
                                                    onClick={() => handleViewDetail(item.sessionId)}
                                                    className="inline-flex items-center justify-center font-medium text-xs bg-blue-600 hover:bg-blue-500 text-white px-3 py-2 rounded-lg transition-colors shadow-lg shadow-blue-900/20 whitespace-nowrap"
                                                >
                                                    👁 View Details
                                                </button>
                                            </td>
                                        </tr>
                                    ))
                                )}
                            </tbody>
                        </table>
                    </div>
                </div>

                {/* LOADING */}
                {loadingDetail && (
                    <div className="flex items-center justify-center p-8 bg-slate-900 border border-slate-800 rounded-2xl">
                        <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-500 mr-3"></div>
                        <p className="text-sm text-slate-400">Đang tải thông tin chi tiết và danh sách tư thế...</p>
                    </div>
                )}

                {/* KHU VỰC CHI TIẾT PHIÊN */}
                {selectedSession && !loadingDetail && (
                    <div className="bg-slate-900 rounded-2xl border border-slate-800 shadow-2xl p-6 mt-8 animate-fadeIn transition-all">
                        
                        <div className="flex items-center justify-between border-b border-slate-800 pb-4 mb-6">
                            <div>
                                <h4 className="text-xl font-bold text-white flex items-center gap-2">
                                    <span>🤖</span> Session Tracking Details
                                </h4>
                                <p className="text-xs text-slate-400 mt-1">
                                    Dữ liệu tổng hợp và lịch sử các tư thế đã phân tích trong phiên làm việc
                                </p>
                            </div>
                            <button 
                                onClick={() => setSelectedSession(null)}
                                className="bg-slate-800 hover:bg-slate-700 text-slate-300 px-3 py-1.5 text-xs rounded-xl transition-colors border border-slate-700"
                            >
                                ✕ Đóng chi tiết
                            </button>
                        </div>

                        {/* THÔNG TIN TỔNG QUAN PHIÊN */}
                        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-5 gap-4 mb-8">
                            <div className="bg-slate-950/60 p-4 rounded-xl border border-slate-800">
                                <p className="text-xs font-semibold text-slate-500 uppercase tracking-wider">Session ID</p>
                                <p className="text-lg font-bold text-blue-400 mt-1">#{selectedSession.sessionId}</p>
                            </div>
                            <div className="bg-slate-950/60 p-4 rounded-xl border border-slate-800">
                                <p className="text-xs font-semibold text-slate-500 uppercase tracking-wider">Start Time</p>
                                <p className="text-sm font-medium text-slate-300 mt-1.5">{formatDate(selectedSession.startTime)}</p>
                            </div>
                            <div className="bg-slate-950/60 p-4 rounded-xl border border-slate-800">
                                <p className="text-xs font-semibold text-slate-500 uppercase tracking-wider">End Time</p>
                                <p className="text-sm font-medium text-slate-300 mt-1.5">{formatDate(selectedSession.endTime)}</p>
                            </div>
                            <div className="bg-slate-950/60 p-4 rounded-xl border border-slate-800">
                                <p className="text-xs font-semibold text-slate-500 uppercase tracking-wider">Duration</p>
                                <p className="text-sm font-bold text-slate-200 mt-1.5">{formatDuration(selectedSession.duration)}</p>
                            </div>
                            <div className="bg-slate-950/60 p-4 rounded-xl border border-slate-800">
                                <p className="text-xs font-semibold text-slate-500 uppercase tracking-wider">Bad Posture Count</p>
                                <p className="text-sm font-bold text-slate-200 mt-1.5">
                                    {selectedSession.badPostureDuration} lần
                                </p>
                            </div>
                        </div>

                        {/* TIÊU ĐỀ BẢNG TƯ THẾ */}
                        <div className="mb-3 flex items-center justify-between">
                            <h5 className="text-sm font-semibold text-slate-400 uppercase tracking-wide flex items-center gap-2">
                                <span>📋</span> Danh sách các tư thế đã detect được
                            </h5>
                            <span className="text-xs text-slate-500">
                                Hiển thị dòng {indexOfFirstItem + 1} - {Math.min(indexOfLastItem, postureList.length)} trong tổng số {postureList.length} kết quả
                            </span>
                        </div>

                        {/* BẢNG TƯ THẾ ĐG DETECT MÀU DARK TỐI MƯỢT MÀ */}
                        <div className="overflow-x-auto border border-slate-800 rounded-xl mb-4 bg-slate-950/40">
                            <table className="w-full border-collapse text-left text-sm text-slate-300">
                                <thead className="bg-slate-950/80 text-xs font-semibold uppercase text-slate-400 border-b border-slate-800">
                                    <tr>
                                        <th className="px-4 py-3 w-16 text-center">ID</th>
                                        <th className="px-4 py-3">Thời Gian Ghi Nhận</th>
                                        <th className="px-4 py-3">Trạng Thái Tư Thế</th>
                                        <th className="px-4 py-3 text-center">Góc Cổ</th>
                                        <th className="px-4 py-3 text-center">Góc Lưng</th>
                                        <th className="px-4 py-3 text-center">Góc Vai</th>
                                    </tr>
                                </thead>
                                <tbody className="divide-y divide-slate-800/50">
                                    {currentPostures.length > 0 ? (
                                        currentPostures.map((posture) => (
                                            <tr key={posture.postureSnapshotId} className="hover:bg-slate-900/60 transition-colors">
                                                <td className="px-4 py-3 text-center font-medium text-slate-500">
                                                    {posture.postureSnapshotId}
                                                </td>
                                                <td className="px-4 py-3 whitespace-nowrap text-xs text-slate-400">
                                                    {formatDate(posture.createdAt)}
                                                </td>
                                                {/* Loại bỏ Badge sặc sỡ, dùng chữ đơn giản tiệp màu nền tối */}
                                                <td className="px-4 py-3 font-medium text-slate-300">
                                                    {posture.status === "GOOD_POSTURE" && "GOOD_POSTURE"}
                                                    {posture.status === "WARNING_POSTURE" && "WARNING_POSTURE"}
                                                    {posture.status === "BAD_POSTURE" && "BAD_POSTURE"}
                                                </td>
                                                <td className="px-4 py-3 text-center font-mono text-slate-300 text-xs">
                                                    {formatAngle(posture.neckAngle)}
                                                </td>
                                                <td className="px-4 py-3 text-center font-mono text-slate-300 text-xs">
                                                    {formatAngle(posture.torsoAngle)}
                                                </td>
                                                <td className="px-4 py-3 text-center font-mono text-slate-300 text-xs">
                                                    {formatAngle(posture.shouterRatio)}
                                                </td>
                                               
                                            </tr>
                                        ))
                                    ) : (
                                        <tr>
                                            <td colSpan={6} className="py-8 text-center text-slate-500 text-xs">
                                                Không có dữ liệu tư thế chi tiết nào được ghi nhận trong phiên này.
                                            </td>
                                        </tr>
                                    )}
                                </tbody>
                            </table>
                        </div>

                        {/* ĐIỀU HƯỚNG PHÂN TRANG (PAGINATION TỐI) */}
                        {totalPages > 1 && (
                            <div className="flex items-center justify-end gap-2 pt-2 border-t border-slate-800/60">
                                <button
                                    onClick={() => setCurrentPage(prev => Math.max(prev - 1, 1))}
                                    disabled={currentPage === 1}
                                    className="px-3 py-1.5 text-xs font-medium rounded-lg border border-slate-800 bg-slate-950/40 text-slate-400 hover:bg-slate-800 hover:text-white disabled:opacity-40 disabled:hover:bg-transparent transition-colors"
                                >
                                    ◀ Trước
                                </button>
                                
                                <div className="flex items-center gap-1">
                                    {[...Array(totalPages)].map((_, i) => (
                                        <button
                                            key={i + 1}
                                            onClick={() => setCurrentPage(i + 1)}
                                            className={`w-7 h-7 flex items-center justify-center text-xs font-semibold rounded-md transition-colors ${
                                                currentPage === i + 1
                                                    ? 'bg-slate-800 text-white border border-slate-700'
                                                    : 'border border-slate-800 hover:bg-slate-800 text-slate-400'
                                            }`}
                                        >
                                            {i + 1}
                                        </button>
                                    ))}
                                </div>

                                <button
                                    onClick={() => setCurrentPage(prev => Math.min(prev + 1, totalPages))}
                                    disabled={currentPage === totalPages}
                                    className="px-3 py-1.5 text-xs font-medium rounded-lg border border-slate-800 bg-slate-950/40 text-slate-400 hover:bg-slate-800 hover:text-white disabled:opacity-40 disabled:hover:bg-transparent transition-colors"
                                >
                                    Sau ▶
                                </button>
                            </div>
                        )}

                    </div>
                )}

            </div>
        </div>
    );
}

export default Session;