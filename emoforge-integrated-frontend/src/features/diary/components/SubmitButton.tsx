import React from 'react';

interface SubmitButtonProps {
  isLoading: boolean;
  onClick: () => void;
}

const SubmitButton: React.FC<SubmitButtonProps> = ({ isLoading, onClick }) => {
  return (
    <div className="mt-6 text-center">
      <button
        onClick={onClick}
        disabled={isLoading}
        className="bg-blue-600 text-white px-6 py-2 rounded hover:bg-blue-700 disabled:opacity-50 disabled:cursor-not-allowed transition-all"
      >
        {isLoading ? '저장 중...' : '저장하기'}
      </button>
    </div>
  );
};

export default SubmitButton;
