import { axiosInstance } from '@/entities/axiosInstance';

export const getMyTeams = async () => {
  const response = await axiosInstance.get('/teams/me');
  return response.data;
};
