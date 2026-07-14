import axiosClient from "../api/axiosClient";

export const getDashboardSummary  = async () => {
    return await axiosClient.get("/dashboard/summary");
};

export const getCurrentPosture = async (sessionId) => {
    return await axiosClient.get(`/posture/current/${sessionId}`);
}