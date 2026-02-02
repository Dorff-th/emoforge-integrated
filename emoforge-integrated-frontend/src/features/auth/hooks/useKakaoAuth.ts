import { useEffect, useState } from 'react';
import { useQueryClient } from '@tanstack/react-query';
import type { KakaoLoginResponse } from '@/features/auth/api/auth.types';



export const useKakaoAuth = () => {
  const queryClient = useQueryClient();
  const [data, setData] = useState<KakaoLoginResponse | undefined>(() =>
    queryClient.getQueryData<KakaoLoginResponse>(['auth', 'kakao'])
  );

  useEffect(() => {
    // 🔥 query cache 변경 구독
    const unsubscribe = queryClient
      .getQueryCache()
      .subscribe(() => {
        const next = queryClient.getQueryData<KakaoLoginResponse>([
          'auth',
          'kakao',
        ]);
        setData(next);
      });

    return unsubscribe;
  }, [queryClient]);

  return data;
};
