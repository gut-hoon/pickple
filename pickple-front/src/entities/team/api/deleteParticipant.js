import { axiosInstance } from "@/entities/axiosInstance";

export const deleteParticipant = async (participantId) => {
    const response = await axiosInstance.delete(`/participants/${participantId}`)
    return response.data;
}