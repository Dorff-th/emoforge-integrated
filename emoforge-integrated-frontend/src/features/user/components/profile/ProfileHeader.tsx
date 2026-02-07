import { User } from "lucide-react";

export default function ProfileHeader() {
  return (
    <section>
      <div className="flex items-center gap-2">
        <User className="w-5 h-5 text-neutral-600" />
        <span className="text-lg font-semibold leading-none">Profile</span>
      </div>
      <p className="text-sm text-gray-500">계정 정보 및 프로필 설정</p>
    </section>
  );
}
