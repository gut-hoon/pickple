import { axiosInstance } from "@/entities/axiosInstance";

export const createQuestion = async (teamId, content) => {
    const response = await axiosInstance.post(`/questions`, {
        teamId: teamId,
        content: content
    });
    return response.data;
};