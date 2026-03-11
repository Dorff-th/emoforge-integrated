import { http } from "@/shared/api/httpClient";
import { API } from "@/shared/api/endpoints";

export async function deleteMember(uuid: string): Promise<void> {
  await http.delete(`${API.ADMIN.AUTH}/members/${uuid}`);
}