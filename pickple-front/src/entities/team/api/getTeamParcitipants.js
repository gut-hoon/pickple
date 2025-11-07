import { axiosInstance } from '@/entities/axiosInstance';

export const getTeamParticipants = async (teamId) => {
    const response = await axiosInstance.get(`/participants?team=${teamId}`);
    return response.data;
};