import { axiosInstance } from '@/entities/axiosInstance';

export const joinTeam = async (formData) => {
  const response = await axiosInstance.postForm('/participants', formData);
  return response.data;
};
