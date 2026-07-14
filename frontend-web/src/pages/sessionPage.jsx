import React, { useState, useEffect } from 'react';
import { Calendar, Clock, AlertTriangle, ChevronLeft, ChevronRight, Eye, RefreshCw } from 'lucide-react';

const SessionsPage = () => {
  const [sessions, setSessions] = useState([]);
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);
  const [isLoading, setIsLoading] = useState(false);
  const pageSize = 8; // Số lượng phiên trên mỗi trang

  // Hàm gọi API lấy danh sách phiên làm việc
  const fetchSessions = async (page) => {
    setIsLoading(true);
    try {
      // Thay URL này bằng API thật của bạn, nhớ kèm Token trong Header nếu có Spring Security
      const response = await fetch(`/sessions/periods?page=${page}&size=${pageSize}`, {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}` // Nếu bạn dùng JWT
        }
      });
      const apiResponse = await response.json(); // parse dữ thành object từ json
        if (pageData) {
            setSessions(pageData.content || []);
            setTotalPages(pageData.totalPages || 0);
            setTotalElements(pageData.totalElements || 0);
        }
    } catch (error) {
      console.error("Lỗi khi lấy danh sách sessions:", error);
    } finally {
      setIsLoading(false);
    }
  };

  // Gọi API mỗi khi thay đổi trang
  useEffect(() => {
    fetchSessions(currentPage);
  }, [currentPage]);

  // Hàm format hiển thị thời gian từ ISO String (Backend) sang dạng dễ đọc
  const formatDateTime = (isoString) => {
    if (!isoString) return "---";
    const date = new Date(isoString);
    return date.toLocaleString('vi-VN', {
      hour: '2-digit',
      minute: '2-digit',
      day: '2-digit',
      month: '2-digit',
      year: 'numeric'
    });
  };

  return (
    <div className="min-h-screen bg-[#121212] text-gray-100 p-6">
      {/* Header */}
      <div className="flex justify-between items-center mb-8">
        <div>
          <h1 className="text-2xl font-bold text-white tracking-wide">Work Sessions History</h1>
          <p className="text-sm text-gray-400 mt-1">Quản lý và xem lại lịch sử tư thế ngồi của bạn (Tổng số: {totalElements} phiên)</p>
        </div>
        <button 
          onClick={() => fetchSessions(currentPage)}
          className="flex items-center gap-2 bg-[#1e1e1e] hover:bg-[#2a2a2a] border border-gray-800 text-sm px-4 py-2 rounded-lg transition"
        >
          <RefreshCw size={16} className={isLoading ? "animate-spin" : ""} />
          Làm mới
        </button>
      </div>

      {/* Bảng Danh Sách Sessions */}
      <div className="bg-[#1e1e1e] border border-gray-800 rounded-xl overflow-hidden shadow-xl">
        <div className="overflow-x-auto">
          <table className="w-full text-left border-collapse">
            <thead>
              <tr className="bg-[#151515] border-b border-gray-850 text-gray-400 text-xs uppercase tracking-wider font-semibold">
                <th className="py-4 px-6">Mã Phiên</th>
                <th className="py-4 px-6">Thời Gian Bắt Đầu</th>
                <th className="py-4 px-6">Tổng Thời Lượng</th>
                <th className="py-4 px-6">Thời Gian Ngồi Sai</th>
                <th className="py-4 px-6">Cảnh Báo (Alerts)</th>
                <th className="py-4 px-6 text-center">Hành Động</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-800 text-sm">
              {isLoading ? (
                // Trạng thái Loading giả lập dòng trống
                <tr className="text-center text-gray-500">
                  <td colSpan="6" className="py-12">Đang tải dữ liệu lịch sử...</td>
                </tr>
              ) : sessions.length === 0 ? (
                <tr className="text-center text-gray-500">
                  <td colSpan="6" className="py-12">Bạn chưa có phiên làm việc nào được ghi nhận.</td>
                </tr>
              ) : (
                sessions.map((session, index) => {
                  // Tính toán nhanh xem phiên này tốt hay tệ để đổi màu sắc cảnh báo
                  const isBadSession = session.totalAlerts > 5 || parseInt(session.badPostureDuration) > 15;

                  return (
                    <tr key={session.id} className="hover:bg-[#252525] transition duration-150">
                      {/* Mã Phiên */}
                      <td className="py-4 px-6 font-medium text-blue-400">
                        #SESS-{session.id}
                      </td>
                      
                      {/* Thời gian */}
                      <td className="py-4 px-6 text-gray-300">
                        <div className="flex items-center gap-2">
                          <Calendar size={14} className="text-gray-500" />
                          {formatDateTime(session.startTime)}
                        </div>
                      </td>

                      {/* Tổng thời lượng */}
                      <td className="py-4 px-6 text-gray-300">
                        <div className="flex items-center gap-2">
                          <Clock size={14} className="text-gray-500" />
                          {session.duration || "0 mins"}
                        </div>
                      </td>

                      {/* Thời gian ngồi sai */}
                      <td className="py-4 px-6">
                        <span className={`inline-flex items-center gap-1.5 px-2.5 py-1 rounded-md text-xs font-medium ${
                          parseInt(session.badPostureDuration) > 0 
                            ? 'bg-orange-500/10 text-orange-400 border border-orange-500/20' 
                            : 'bg-green-500/10 text-green-400 border border-green-500/20'
                        }`}>
                          {session.badPostureDuration || "0 mins"}
                        </span>
                      </td>

                      {/* Số lần cảnh báo */}
                      <td className="py-4 px-6">
                        <div className="flex items-center gap-1.5">
                          {session.totalAlerts > 0 && (
                            <AlertTriangle size={14} className={isBadSession ? "text-red-400" : "text-amber-400"} />
                          )}
                          <span className={isBadSession ? "text-red-400 font-semibold" : "text-gray-300"}>
                            {session.totalAlerts} lần
                          </span>
                        </div>
                      </td>

                      {/* Nút Hành động */}
                      <td className="py-4 px-6 text-center">
                        <button 
                          onClick={() => alert(`Xem chi tiết biểu đồ phân tích của phiên #${session.id}`)}
                          className="inline-flex items-center gap-1.5 bg-[#2a2a2a] hover:bg-blue-600 hover:text-white border border-gray-700 hover:border-blue-500 text-xs px-3 py-1.5 rounded-md transition duration-200"
                        >
                          <Eye size={12} />
                          Chi tiết
                        </button>
                      </td>
                    </tr>
                  );
                })
              )}
            </tbody>
          </table>
        </div>

        {/* Thanh Phân Trang (Pagination Controls) */}
        {totalPages > 1 && (
          <div className="bg-[#151515] px-6 py-4 flex items-center justify-between border-t border-gray-800">
            <div className="text-xs text-gray-400">
              Hiển thị trang <span className="text-white font-medium">{currentPage + 1}</span> / {totalPages}
            </div>
            <div className="flex items-center gap-2">
              <button
                onClick={() => setCurrentPage(prev => Math.max(prev - 1, 0))}
                disabled={currentPage === 0 || isLoading}
                className="p-1.5 rounded-md bg-[#252525] border border-gray-700 hover:bg-[#303030] disabled:opacity-40 disabled:hover:bg-[#252525] transition"
              >
                <ChevronLeft size={16} />
              </button>
              
              {/* Vòng lặp hiển thị số trang */}
              {[...Array(totalPages)].map((_, idx) => (
                <button
                  key={idx}
                  onClick={() => setCurrentPage(idx)}
                  className={`text-xs px-3 py-1.5 rounded-md border transition ${
                    currentPage === idx
                      ? 'bg-blue-600 text-white border-blue-500 font-semibold'
                      : 'bg-[#252525] border-gray-700 text-gray-300 hover:bg-[#303030]'
                  }`}
                >
                  {idx + 1}
                </button>
              ))}

              <button
                onClick={() => setCurrentPage(prev => Math.min(prev + 1, totalPages - 1))}
                disabled={currentPage === totalPages - 1 || isLoading}
                className="p-1.5 rounded-md bg-[#252525] border border-gray-700 hover:bg-[#303030] disabled:opacity-40 disabled:hover:bg-[#252525] transition"
              >
                <ChevronRight size={16} />
              </button>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default SessionsPage;