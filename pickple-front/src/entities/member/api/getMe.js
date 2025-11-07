import { axiosInstance } from '@/entities/axiosInstance';

export const getMe = async () => {
  const response = await axiosInstance.get(`/members/me`);

  console.log("본인 정보 조회 API 응답: ", response);

  return response.data;
};
