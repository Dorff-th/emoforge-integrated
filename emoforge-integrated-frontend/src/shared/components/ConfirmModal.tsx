// components/ui/ConfirmModal.tsx
import {
  AlertDialog,
  AlertDialogAction,
  AlertDialogCancel,
  AlertDialogContent,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogTitle,
  AlertDialogPortal,
  AlertDialogOverlay,
} from '@/components/ui/alert-dialog';

interface ConfirmModalProps {
  open: boolean;
  title?: string;
  description?: string;
  onConfirm: () => void;
  onCancel: () => void;
}

export default function ConfirmModal({
  open,
  title = '확인',
  description = '이 작업을 진행하시겠습니까?',
  onConfirm,
  onCancel,
}: ConfirmModalProps) {
  return (
    <AlertDialog open={open} onOpenChange={onCancel}>
      <AlertDialogPortal>
        <AlertDialogOverlay className="bg-black/10 backdrop-blur-[1px]" />
        <AlertDialogContent>
        <AlertDialogHeader>
          <AlertDialogTitle>{title}</AlertDialogTitle>
          <AlertDialogDescription  className="whitespace-pre-line">{description}</AlertDialogDescription>
        </AlertDialogHeader>
        <AlertDialogFooter>
          <AlertDialogCancel onClick={onCancel}>취소</AlertDialogCancel>
          <AlertDialogAction onClick={onConfirm}>확인</AlertDialogAction>
        </AlertDialogFooter>
        </AlertDialogContent>
      </AlertDialogPortal>
    </AlertDialog>
  );
}

