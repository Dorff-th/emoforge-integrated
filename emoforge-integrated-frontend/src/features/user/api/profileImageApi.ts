// features/user/api/profileImageApi.ts
import { http } from "@/shared/api/httpClient";
import { API } from "@/shared/api/endpoints";

/**
 * BE Controller
 * - AttachmentController
 * - Base: /api/attach
 *
 * Endpoints
 * - POST
 * - DELETE /{id}
 * - GET  /profile/{memberUuid}
 * 
 */

export interface ProfileImageResponse {
  publicUrl: string | null;
}

export const fetchProfileImage = async (uuid: string) => {
  const { data } = await http.get<ProfileImageResponse>(
    `${API.ATTACH}/profile/${uuid}`
  );
  
  return data;
};

export const uploadProfileImage = async (
  file: File,
  uploadType: string,
  attachmentStatus: string,
  memberUuid?: string,
  postId?: number
) => {

  const formData = new FormData();
  formData.append("file", file);
  formData.append("uploadType", uploadType);
  formData.append("attachmentStatus", attachmentStatus);
  if (memberUuid) formData.append("memberUuid", memberUuid);
  if (postId) formData.append("postId", postId.toString());

  const { data } = await http.post<ProfileImageResponse>(
    `${API.ATTACH}`,
    formData,
    {
      headers: { "Content-Type": "multipart/form-data" },
    }
  );

  return data;
};
