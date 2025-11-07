import { axiosInstance } from '@/entities/axiosInstance';

export const getInvitationCode = async (teamId) => {
  const response = await axiosInstance.get(`/teams/${teamId}/invite-code`);
  return response.data;
};
