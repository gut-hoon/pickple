import { axiosInstance } from '@/entities/axiosInstance';

export const getQuestionList = async (teamId, memberId=null) => {
    if (!teamId) {
        throw new Error('teamId is required');
    }
    
    const params = new URLSearchParams();
    params.append('team', teamId);
    if (memberId) params.append('member', memberId)

    const response = await axiosInstance.get(`/questions?${params.toString()}`);
    return response.data;
};