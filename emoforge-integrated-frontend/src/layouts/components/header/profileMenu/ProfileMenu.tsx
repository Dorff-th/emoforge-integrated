import { useState, useRef } from "react";
import { ProfileTrigger } from "./ProfileTrigger";
import { ProfileDropdown } from "./ProfileDropdown";
import { useOnClickOutside } from "@/layouts/components/hooks/useOnClickOutside";

export function ProfileMenu() {
  const [open, setOpen] = useState(false);
  const ref = useRef<HTMLDivElement | null>(null);

  useOnClickOutside(ref as React.RefObject<HTMLElement>, () => setOpen(false));

  return (
    <div ref={ref} className="relative">
      <ProfileTrigger open={open} onToggle={() => setOpen((v) => !v)} />
      {open && <ProfileDropdown onClose={() => setOpen(false)} />}
    </div>
  );
}
