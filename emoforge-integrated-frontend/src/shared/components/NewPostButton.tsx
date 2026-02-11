// src/components/common/NewPostButton.tsx
import { Link } from 'react-router-dom';
import { PlusCircle } from 'lucide-react';

interface NewPostButtonProps {
  size?: 'sm' | 'md';
  position?: 'left' | 'right';
}

export default function NewPostButton({ size = 'sm', position = 'left' }: NewPostButtonProps) {
  const sizeClasses = size === 'sm' ? 'px-3 py-1 text-sm' : 'px-4 py-2 text-base';
  const positionClasses = position === 'right' ? 'ml-auto' : '';

  return (
    <Link
      to="new"
      className={`inline-flex items-center space-x-1 border border-indigo-500 
                 text-indigo-500 rounded-md hover:bg-indigo-500 hover:text-white 
                 transition ${sizeClasses} w-fit ${positionClasses}`}
    >
      <PlusCircle className="h-4 w-4" />
      <span>New Post</span>
    </Link>
  );
}
