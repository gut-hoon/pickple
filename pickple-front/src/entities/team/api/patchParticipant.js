import { axiosInstance } from '@/entities/axiosInstance';

export const patchParticipant = async (participantId, editedData) => {
    const response = await axiosInstance.patchForm(`/participants/${participantId}`, editedData);

    console.log("참가자 정보 수정 API 응답: ", response);

    return response.data;
};
