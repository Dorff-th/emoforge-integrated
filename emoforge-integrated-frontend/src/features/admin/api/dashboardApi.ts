import { http } from "@/shared/api/httpClient";

type AdminDashboardApiResponse = {
  memberCount?: number;
  postCount?: number;
  commentCount?: number;
  profileImageCount?: number;
  editorImageCount?: number;
  attachmentCount?: number;
  totalMembers?: number;
  totalPosts?: number;
  totalComments?: number;
  profileImageAttachments?: number;
  editorImageAttachments?: number;
  generalAttachments?: number;
};

export interface DashboardStats {
  memberCount: number;
  postCount: number;
  commentCount: number;
  profileImageCount: number;
  editorImageCount: number;
  attachmentCount: number;
}

export const dashboardKeys = {
  all: ["admin", "dashboard"] as const,
};

export async function fetchDashboardStats(): Promise<DashboardStats> {
  const res = await http.get<AdminDashboardApiResponse>("/api/admin/dashboard");
  const data = res.data;

  return {
    memberCount: data.memberCount ?? data.totalMembers ?? 0,
    postCount: data.postCount ?? data.totalPosts ?? 0,
    commentCount: data.commentCount ?? data.totalComments ?? 0,
    profileImageCount: data.profileImageCount ?? data.profileImageAttachments ?? 0,
    editorImageCount: data.editorImageCount ?? data.editorImageAttachments ?? 0,
    attachmentCount: data.attachmentCount ?? data.generalAttachments ?? 0,
  };
}
