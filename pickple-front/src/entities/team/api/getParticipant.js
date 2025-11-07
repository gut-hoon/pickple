import { axiosInstance } from '@/entities/axiosInstance';

export const getParticipant = async (participantId) => {
    
  const response = await axiosInstance.get(`/participants/${participantId}`);

  console.log("참가자 조회 API: ", response);

  return response.data;
};
