import { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { http } from "@/shared/api/httpClient";
import { MessageCircle } from "lucide-react";
import type { PageResponse } from "../types/Common";
import Pagination from "@/features/post/components/Pagination";

interface PostSearchResult {
  id: number;
  title: string;
  authorNickname: string;
  createdAt: string;
  categoryName: string;
  commentCount: number;
}

export default function PostSearchPage() {
  const location = useLocation();
  const navigate = useNavigate();

  const [data, setData] = useState<PageResponse<PostSearchResult> | null>(null);
  const [loading, setLoading] = useState(false);

  const [pageInfo, setPageInfo] =
    useState<PageResponse<PostSearchResult> | null>(null);

  // -------------------------
  // 1️⃣ query string 파싱
  // -------------------------
  const params = new URLSearchParams(location.search);

  const page = Number(params.get("page") ?? 1);
  const size = Number(params.get("size") ?? 10);
  const keyword = params.get("keyword") ?? "";

  //페이지 처음 진입시 모든 체크 박스 체크

  const getBoolean = (key: string) => {
    const value = params.get(key);
    if (value === null) return true; // 기본 true
    return value === "true";
  };

  const titleChecked = getBoolean("titleChecked");
  const contentChecked = getBoolean("contentChecked");
  const commentChecked = getBoolean("commentChecked");
  const categoryChecked = getBoolean("categoryChecked");
  const authorChecked = getBoolean("authorChecked");

  //모든 체크박스가 체크가 안되어있는 경우
  const noFilterSelected =
    !titleChecked && !contentChecked && !commentChecked && !authorChecked;

  // -------------------------
  // 2️⃣ API 호출
  // -------------------------
  const fetchData = async () => {
    try {
      setLoading(true);

      const response = await http.get("/api/posts/search", {
        params: {
          page,
          size,
          keyword,
          titleChecked,
          contentChecked,
          commentChecked,
          categoryChecked,
          authorChecked,
        },
      });
      console.log(response.data);
      setData(response.data);
      setPageInfo(response.data);
    } catch (err) {
      console.error("검색 실패:", err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (!keyword) return;
    if (noFilterSelected) {
      setData(null); // 🔥 이전 결과 제거
      return;
    }

    fetchData();
  }, [
    page,
    size,
    keyword,
    titleChecked,
    contentChecked,
    commentChecked,
    categoryChecked,
    authorChecked,
    noFilterSelected,
  ]);

  // -------------------------
  // 3️⃣ 필터 변경 시 URL 업데이트
  // -------------------------

  const updateFilter = (key: string, value: boolean) => {
    const newParams = new URLSearchParams(location.search);
    newParams.set(key, String(value));
    newParams.set("page", "1"); // 필터 변경 시 첫 페이지

    navigate(`/posts/search?${newParams.toString()}`);
  };

  const handlePageChange = (newPage: number) => {
    const newParams = new URLSearchParams(location.search);
    newParams.set("page", String(newPage));
    navigate(`/posts/search?${newParams.toString()}`);
  };

  // -------------------------
  // 4️⃣ 렌더링
  // -------------------------
  return (
    <div className="p-6">
      <h2 className="text-xl font-semibold mb-4">검색 결과: "{keyword}"</h2>

      {/* 🔎 필터 영역 */}
      <div className="flex gap-4 mb-6">
        <label>
          <input
            type="checkbox"
            checked={titleChecked}
            onChange={(e) => updateFilter("titleChecked", e.target.checked)}
          />
          제목
        </label>

        <label>
          <input
            type="checkbox"
            checked={contentChecked}
            onChange={(e) => updateFilter("contentChecked", e.target.checked)}
          />
          내용
        </label>

        <label>
          <input
            type="checkbox"
            checked={commentChecked}
            onChange={(e) => updateFilter("commentChecked", e.target.checked)}
          />
          댓글
        </label>

        <label>
          <input
            type="checkbox"
            checked={authorChecked}
            onChange={(e) => updateFilter("authorChecked", e.target.checked)}
          />
          작성자
        </label>
      </div>

      {/* 📄 결과 영역 */}
      {noFilterSelected ? (
        <div className="text-center py-12 text-gray-400">
          검색 범위를 선택해주세요 🙃
        </div>
      ) : loading ? (
        <div>검색 중...</div>
      ) : data && data.dtoList.length === 0 ? (
        <div className="text-center py-12 text-gray-400">
          "{keyword}"에 대한 검색 결과가 없습니다.
        </div>
      ) : data ? (
        <>
          <div className="max-w-5xl mx-auto space-y-4 p-4">
            {data.dtoList.map((post) => (
              // <li
              //   key={post.id}
              //   className="border p-3 rounded-md hover:bg-gray-50"
              // >
              //   <div className="font-medium">{post.title}</div>
              //   <div className="text-sm text-gray-500">
              //     {post.author} · {post.createdAt}
              //   </div>
              // </li>

              <div
                key={post.id}
                data-id={post.id}
                className="post-card bg-white rounded-lg shadow-md p-4
                         hover:shadow-lg hover:-translate-y-1 hover:scale-[1.01]
                         transform transition duration-300 cursor-pointer"
                onClick={() => navigate(`/posts/${post.id}`)}
              >
                {/* 제목 */}
                <h3 className="text-base font-semibold text-gray-900 mb-2">
                  {post.title}
                </h3>

                {/* 댓글/첨부 */}
                <div className="flex items-center gap-4 text-sm text-gray-500 mb-2">
                  {post.commentCount > 0 && (
                    <div className="flex items-center gap-1">
                      <MessageCircle size={14} />
                      <span>{post.commentCount}</span>
                    </div>
                  )}
                </div>

                {/* 카테고리 · 날짜 · 닉네임 */}
                <p className="text-sm text-gray-500">
                  <span className="text-sm font-medium text-gray-700">
                    {post.categoryName}
                  </span>
                  <span className="mx-1">·</span>
                  <span>
                    {new Date(post.createdAt).toLocaleDateString("ko-KR", {
                      year: "numeric",
                      month: "2-digit",
                      day: "2-digit",
                    })}
                  </span>
                  <span className="mx-1">·</span>
                  <span>{post.authorNickname}</span>
                </p>
              </div>
            ))}
          </div>

          {data.totalElements > 0 && (
            <div className="mt-6 text-sm text-gray-500">
              총 {data.totalElements}건 · {data.page} /{data.totalPages} 페이지
            </div>
          )}
          {/* 📌 페이징 컴포넌트 */}
          {pageInfo && (
            <div className="mt-6">
              <Pagination
                page={page}
                startPage={pageInfo.startPage}
                endPage={pageInfo.endPage}
                prev={pageInfo.prev}
                next={pageInfo.next}
                onPageChange={handlePageChange}
              />
            </div>
          )}
        </>
      ) : null}
    </div>
  );
}
