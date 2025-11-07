import { axiosInstance } from '@/entities/axiosInstance';

export const getCurrentSeason = async () => {
  const response = await axiosInstance.get('/seasons');
  return response.data;
};
