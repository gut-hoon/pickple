import { axiosInstance } from "@/entities/axiosInstance";

export const deleteQuestion = async (questionId) => {
    const response = await axiosInstance.delete(`/questions/${questionId}`);
    return response.data;
}