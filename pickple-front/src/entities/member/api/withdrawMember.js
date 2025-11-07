import { axiosInstance } from "@/entities/axiosInstance";

export const withdrawMember = async () => {
    const response = await axiosInstance.delete('/members/me');
    return response.data;
};
