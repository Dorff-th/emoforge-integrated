import { useMemo } from "react";
import { v4 as uuidv4 } from "uuid";
import PostForm from "@/features/post/components/PostForm";
import { PenSquare } from "lucide-react";

export default function PostWritePage() {
  const groupTempKey = useMemo(() => uuidv4(), []);

  return (
    <div className="max-w-4xl mx-auto py-8 px-4">
      <h1 className="flex items-center gap-2 text-xl font-semibold mb-6">
        <PenSquare size={20} />
        Post Write
      </h1>
      <PostForm mode="write" groupTempKey={groupTempKey} />
    </div>
  );
}
