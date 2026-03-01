// features/user/hooks/useProfileImage.ts
import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import {
  fetchProfileImage,
  uploadProfileImage,
} from "../api/profileImageApi";
import axios from "axios";
import { useToast } from "@/shared/stores/useToast";

export const profileImageQueryKey = (uuid: string) =>
  ["profileImage", uuid];

export const useProfileImage = (uuid?: string) => {
  const queryClient = useQueryClient();
  const toast = useToast();


  const query = useQuery({
    queryKey: uuid ? profileImageQueryKey(uuid) : [],
    queryFn: () => fetchProfileImage(uuid!),
    enabled: !!uuid,
    staleTime: 1000 * 60 * 10, // 10분
    gcTime: 1000 * 60 * 30,
  });

  const mutation = useMutation({
    mutationFn: ({ file }: { file: File }) =>
      uploadProfileImage(file, "PROFILE_IMAGE", "CONFIRMED", uuid!),

    onSuccess: (data) => {
      // 🔥 즉시 UI 반영
      queryClient.setQueryData(
        profileImageQueryKey(uuid!),
        data
      );
      
    },
     onError: (error) => {
    if (axios.isAxiosError(error)) {
      const message = error.response?.data?.message;

      switch (message) {
        case "INVALID_FILE_EXTENSION":
          toast.error("이미지 파일만 업로드 가능합니다.");
          return;
        case "FILE_SIZE_EXCEEDED":
          toast.error("프로필 이미지는 2MB 이하만 가능합니다.");
          return;
      }
    }

    toast.error("프로필 이미지 업로드에 실패했습니다.");
  },
  });

  return {
    publicUrl: query.data?.publicUrl ?? null,
    isLoading: query.isLoading,
    upload: mutation.mutate,
    isUploading: mutation.isPending,
  };
};
