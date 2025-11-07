import { axiosInstance } from '@/entities/axiosInstance';

export const registerMember = async (fullName, birthDate) => {
    const response = await axiosInstance.patch('/members/register', {
      fullName,
      birthDate,
    });
    return response.data;
};
