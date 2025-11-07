import { axiosInstance } from '@/entities/axiosInstance';

export const getMemberProfile = async (memberId) => {
  const response = await axiosInstance.get(`/members/${memberId}/profile`);

  console.log("API: ", response);

  return response.data;
};
