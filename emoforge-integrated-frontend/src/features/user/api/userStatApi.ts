import { http } from "@/shared/api/httpClient";
import { API } from "@/shared/api/endpoints";

/**
 * MemberAttachmentStatsResponse - 회원 첨부파일 통계
 */
export interface MemberAttachmentStatsResponse {
    editorImageCount : number;
    attachmentCount : number;
};

export const fetchMemberAttachmentStats = async () => {
    const response = await http.get<MemberAttachmentStatsResponse>(
        `${API.ATTACH}/me/statistics`
    );
    
    return response.data;
}

/**
 * MemberPostStatsResponse - 회원 게시글 통계
 */
// PostStatsResponse (Spring Boot) 와 동일
export interface MemberPostStatsResponse {
    postCount : number;
    commentCount : number;
}

// 회원 게시글 통계 - 전체 
export const fetchMemberPostStats = async () => {
    const response = await http.get<MemberPostStatsResponse>(
        `${API.POST}/me/statistics`
    );
    return response.data;
}

// 회원 게시글 통계 - 오늘날짜만
export const fetchMemberPostStatsToday = async () => {
    const response = await http.get<MemberPostStatsResponse>(
        `${API.POST}/me/statistics/today`
    );
    return response.data;
}

/**
 * MemberDiaryStatsResponse - 회원 다이어리 통계
 */
export interface MemberDiaryStatsResponse {
    diaryEntryCount : number;
    gptSummaryCount : number;
    musicRecommendHistoryCount : number;
}

export const fetchMemberDiaryStats = async () => {
    const response = await http.get<MemberDiaryStatsResponse>(
        `${API.DIARY}/me/statistics`
    );
    return response.data;
}