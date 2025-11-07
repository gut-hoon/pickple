import { axiosInstance } from '@/entities/axiosInstance';

export const refreshAllDoneQuestion = async (teamId) => {
    const response = await axiosInstance.patch(`/questions/refresh`, {
        teamId: teamId
    });
    return response.data;
};