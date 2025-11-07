import { axiosInstance } from '@/entities/axiosInstance';

export const createTeam = async (formData) => {
  const response = await axiosInstance.postForm('/teams', formData);
  return response.data;
};
