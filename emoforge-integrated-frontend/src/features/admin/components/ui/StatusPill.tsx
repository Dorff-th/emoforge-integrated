import { cva, type VariantProps } from 'class-variance-authority';
import { cn } from '@/lib/utils';

const statusPillVariants = cva(
  `inline-flex items-center justify-center gap-1.5
   px-3 py-1 rounded-full text-xs font-medium
   border cursor-pointer select-none
   transition-all duration-200 ease-in-out
   focus:outline-none focus:ring-2 focus:ring-offset-1`,
  {
    variants: {
      state: {
        active:
          'bg-green-100 text-green-800 border-green-200 hover:bg-green-200 focus:ring-green-400',
        inactive:
          'bg-gray-100 text-gray-600 border-gray-200 hover:bg-gray-200 focus:ring-gray-400',
        deleted:
          'bg-red-100 text-red-700 border-red-200 hover:bg-red-200 focus:ring-red-400',
        safe:
          'bg-emerald-50 text-emerald-700 border-emerald-200 hover:bg-emerald-100 focus:ring-emerald-400',
      },
    },
    defaultVariants: {
      state: 'inactive',
    },
  }
);

interface StatusPillProps extends VariantProps<typeof statusPillVariants> {
  label: string;
  onClick: () => void;
  isLoading?: boolean;
  disabled?: boolean;
  ariaLabel?: string;
}

export function StatusPill({
  label,
  state,
  onClick,
  isLoading = false,
  disabled = false,
  ariaLabel,
}: StatusPillProps) {
  const isDisabled = disabled || isLoading;

  return (
    <button
      type="button"
      role="switch"
      aria-checked={state === 'active' || state === 'safe'}
      aria-label={ariaLabel || label}
      aria-busy={isLoading}
      disabled={isDisabled}
      onClick={onClick}
      className={cn(
        statusPillVariants({ state }),
        isDisabled && 'opacity-60 cursor-wait pointer-events-none'
      )}
    >
      {isLoading && (
        <svg
          className="animate-spin h-3 w-3"
          xmlns="http://www.w3.org/2000/svg"
          fill="none"
          viewBox="0 0 24 24"
        >
          <circle
            className="opacity-25"
            cx="12"
            cy="12"
            r="10"
            stroke="currentColor"
            strokeWidth="4"
          />
          <path
            className="opacity-75"
            fill="currentColor"
            d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4z"
          />
        </svg>
      )}
      {label}
    </button>
  );
}
