import { axiosInstance } from '@/entities/axiosInstance';

export const getTeamProfile = async (teamId) => {
    const response = await axiosInstance.get(`/teams/${teamId}/profile`);
    return response.data;
};
