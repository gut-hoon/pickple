import { axiosInstance } from '@/entities/axiosInstance';

export const getParticipantProfile = async (participantId) => {
    
  const response = await axiosInstance.get(`/participants/${participantId}/profile`);

  console.log("참가자 프로필 조회 API: ", response);

  return response.data;
};
