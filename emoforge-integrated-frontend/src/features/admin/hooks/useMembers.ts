import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { http } from "@/shared/api/httpClient";
import { API } from "@/shared/api/endpoints";
import { useToast } from '@/shared/stores/useToast';
import { deleteMember } from "@/features/admin/api/adminMemberApi";

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
};

async function fetchMembers(): Promise<MemberDTO[]> {
  const res = await http.get(`${API.ADMIN.AUTH}/members`);
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

export function useMembers() {
  return useQuery({
    queryKey: memberKeys.all,
    queryFn: fetchMembers,
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

export function useToggleMemberDeleted() {
  const queryClient = useQueryClient();
  const toast = useToast();

  return useMutation({
    mutationFn: ({ uuid, currentDeleted }: { uuid: string; currentDeleted: boolean }) => {
      return updateMemberDeleted(uuid, !currentDeleted);
    },
    onSuccess: (updatedMember) => {
      queryClient.setQueryData<MemberDTO[]>(memberKeys.all, (old) =>
        old?.map((m) => (m.uuid === updatedMember.uuid ? updatedMember : m))
      );
      toast.success('회원 탈퇴 상태가 변경되었습니다.');
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
