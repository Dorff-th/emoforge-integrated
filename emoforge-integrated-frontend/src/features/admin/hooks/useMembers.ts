import {
  keepPreviousData,
  useMutation,
  useQuery,
  useQueryClient,
} from '@tanstack/react-query';
import { http } from "@/shared/api/httpClient";
import { API } from "@/shared/api/endpoints";
import { useToast } from '@/shared/stores/useToast';
import { deleteMember } from "@/features/admin/api/adminMemberApi";
import type { PageResponse } from '@/features/post/types/Common';


//member response type
export interface MemberDTO {
  uuid: string;
  username: string;
  nickname: string;
  email: string;
  role: string;
  status: string;
  deleted: boolean;
  deletedAt : string;
  createdAt: string;
  lastLoginAt : string
}



export const memberKeys = {
  all: ['members'] as const,
  list: (
    page: number,
    size: number = 15,
    sort: string = 'createdAt',
    direction: 'ASC' | 'DESC' = 'DESC',
    nickname?: string,
    deleted?: boolean,
  ) =>
    [
      ...memberKeys.all,
      'list',
      { page, size, sort, direction, nickname: nickname?.trim() || '', deleted },
    ] as const,
};

// async function fetchMembers(): Promise<MemberDTO[]> {
//   const res = await http.get(`${API.ADMIN.AUTH}/members`, {
    
//   });
//   return res.data;
// }

export async function fetchMembers(
  page: number,
  size: number = 15,
  sort: string = 'createdAt',
  direction: 'ASC' | 'DESC' = 'DESC',
  nickname?: string,
  deleted?: boolean,
): Promise<PageResponse<MemberDTO>> {
  const res = await http.get(`${API.ADMIN.AUTH}/members`, {
    params: {
      page,
      size,
      sort,
      direction,
      nickname: nickname?.trim() ? nickname.trim() : undefined,
      deleted,
    },
  });
  return res.data;
}

async function updateMemberStatus(uuid: string, status: string): Promise<MemberDTO> {
  const res = await http.patch(`${API.ADMIN.AUTH}/members/${uuid}/status`, null, {
    params: { status },
  });
  return res.data;
}

async function updateMemberDeleted(uuid: string, deleted: boolean): Promise<MemberDTO> {
  const res = await http.patch(`${API.ADMIN.AUTH}/members/${uuid}/deleted`, null, {
    params: { deleted },
  });
  return res.data;
}

export interface UseMembersParams {
  page: number;
  size?: number;
  sort?: string;
  direction?: 'ASC' | 'DESC';
  nickname?: string;
  deleted?: boolean;
}

export function useMembers({
  page,
  size = 15,
  sort = 'createdAt',
  direction = 'DESC',
  nickname,
  deleted,
}: UseMembersParams) {
  return useQuery({
    queryKey: memberKeys.list(page, size, sort, direction, nickname, deleted),
    queryFn: () => fetchMembers(page, size, sort, direction, nickname, deleted),
    placeholderData: keepPreviousData,
  });
}

export function useToggleMemberStatus() {
  const queryClient = useQueryClient();
  const  toast  = useToast();

  return useMutation({
    mutationFn: ({ uuid, currentStatus }: { uuid: string; currentStatus: string }) => {
      const nextStatus = currentStatus === 'ACTIVE' ? 'INACTIVE' : 'ACTIVE';
      return updateMemberStatus(uuid, nextStatus);
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: memberKeys.all });
      toast.success('회원 상태가 변경되었습니다.');
    },
  });
}

export function useToggleMemberDeleted(options?: { onSuccess?: () => void }) {
  const queryClient = useQueryClient();
  const toast = useToast();

  return useMutation({
    mutationFn: ({ uuid, currentDeleted }: { uuid: string; currentDeleted: boolean }) => {
      return updateMemberDeleted(uuid, !currentDeleted);
    },
    onSuccess: async () => {
      await queryClient.invalidateQueries({ queryKey: memberKeys.all });
      toast.success('회원 탈퇴 상태가 변경되었습니다.');
      options?.onSuccess?.();
    },
  });
}

export function useDeleteMember() {
  const queryClient = useQueryClient();
  const toast = useToast();

  return useMutation({
    mutationFn: deleteMember,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: memberKeys.all});
      toast.success('사용자 정보가 삭제되었습니다.');
    },
  });
}
