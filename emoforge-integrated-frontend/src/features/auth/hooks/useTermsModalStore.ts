// useTermsModalStore.ts
import { create } from 'zustand';

interface TermsModalState {
  open: boolean;
  openModal: () => void;
  closeModal: () => void;
}

export const useTermsModalStore = create<TermsModalState>((set) => ({
  open: false,
  openModal: () => set({ open: true }),
  closeModal: () => set({ open: false }),
}));
