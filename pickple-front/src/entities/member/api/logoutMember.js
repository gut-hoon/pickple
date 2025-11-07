import { axiosInstance } from "@/entities/axiosInstance";

export const logoutMember = async () => {
    const response = await axiosInstance.post('/auth/logout');
    return response.data;
};
