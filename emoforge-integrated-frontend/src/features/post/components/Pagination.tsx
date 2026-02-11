import React from 'react';

interface PaginationProps {
  page: number;
  startPage: number;
  endPage: number;
  prev: boolean;
  next: boolean;
  onPageChange: (page: number) => void;
}

const Pagination: React.FC<PaginationProps> = ({
  page,
  startPage,
  endPage,
  prev,
  next,
  onPageChange,
}) => {
  return (
    <div className="flex items-center justify-center mt-10 gap-2">
      {/* 이전 버튼 */}
      {prev && (
        <button
          onClick={() => onPageChange(startPage - 1)}
          className="px-3 py-2 rounded border bg-white text-gray-600
                     hover:shadow-lg hover:-translate-y-1 hover:scale-[1.01]
                     transform transition duration-300 cursor-pointer"
        >
          «
        </button>
      )}

      {/* 페이지 번호 버튼 */}
      {Array.from({ length: endPage - startPage + 1 }, (_, i) => startPage + i).map((pageNum) => (
        <button
          key={pageNum}
          onClick={() => onPageChange(pageNum)}
          className={`px-3 py-2 rounded border transform transition duration-300 cursor-pointer
                      hover:shadow-lg hover:-translate-y-1 hover:scale-[1.01] 
                      ${
                        page === pageNum
                          ? 'bg-gray-800 text-white font-bold shadow ring-2 ring-offset-2 ring-gray-500'
                          : 'bg-white text-gray-800 hover:bg-gray-100'
                      }`}
        >
          {pageNum}
        </button>
      ))}

      {/* 다음 버튼 */}
      {next && (
        <button
          onClick={() => onPageChange(endPage + 1)}
          className="px-3 py-2 rounded border bg-white text-gray-600
                     hover:shadow-lg hover:-translate-y-1 hover:scale-[1.01]
                     transform transition duration-300 cursor-pointer"
        >
          »
        </button>
      )}
    </div>
  );
};

export default Pagination;
