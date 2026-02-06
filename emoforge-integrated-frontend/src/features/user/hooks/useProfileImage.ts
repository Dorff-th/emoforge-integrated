// features/user/hooks/useProfileImage.ts
import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import {
  fetchProfileImage,
  uploadProfileImage,
} from "../api/profileImageApi";

export const profileImageQueryKey = (uuid: string) =>
  ["profileImage", uuid];

export const useProfileImage = (uuid?: string) => {
  const queryClient = useQueryClient();

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

      // 필요하면 백엔드 재동기화
      // queryClient.invalidateQueries({
      //   queryKey: profileImageQueryKey(uuid!),
      // });
    },
  });

  return {
    publicUrl: query.data?.publicUrl ?? null,
    isLoading: query.isLoading,
    upload: mutation.mutate,
    isUploading: mutation.isPending,
  };
};
