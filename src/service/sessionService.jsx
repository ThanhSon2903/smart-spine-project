import axiosClient from "../axiosClient";

export const startSession = async () => {
    return await axiosClient.post(
        "/session/create"
    );
};

export const endSession = async (
    sessionId
) => {
    return await axiosClient.put(
        `/session/end/${sessionId}`
    );
};