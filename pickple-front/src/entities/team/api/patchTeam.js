import { axiosInstance } from '@/entities/axiosInstance';

export const patchTeam = async (teamId, formData) => {
  const response = await axiosInstance.patchForm(`/teams/${teamId}/profile`, formData);
  return response.data;
};
