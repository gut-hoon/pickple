import { axiosInstance } from '@/entities/axiosInstance';

export const getHotboard = async () => {
  const response = await axiosInstance.get('/hotboard');
  return response.data;
};
