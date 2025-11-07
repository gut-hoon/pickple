import { axiosInstance } from "@/entities/axiosInstance";

export const createPick = async ({teamId=null, questionId=null, pickpleId=null}) => {
    const response = await axiosInstance.post(`/picks/next`, {
        teamId: teamId,
        questionId: questionId,
        pickpleId: pickpleId,
    });
    return response.data;
}