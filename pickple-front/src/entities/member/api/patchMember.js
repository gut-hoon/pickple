import { axiosInstance } from '@/entities/axiosInstance';

export const patchMember = async (editedData) => {
    const response = await axiosInstance.patchForm('/members/me', editedData);

    console.log("사용자 정보 수정 API 응답: ", response);

    return response.data;
};
