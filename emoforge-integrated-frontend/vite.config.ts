import { defineConfig, loadEnv } from 'vite'
import react from '@vitejs/plugin-react'
import path from 'path'

export default defineConfig(({ mode }) => {
  const env = loadEnv(mode, process.cwd(), '')
  const allowedHosts = env.VITE_DEV_ALLOWED_HOST
    ? env.VITE_DEV_ALLOWED_HOST.split(',')
      .map(host => host.trim())
      .filter(Boolean)
    : undefined

  return {
    base: "/",
    plugins: [react()],
    server: {
      host: true,       
      port: 5173,
      ...(allowedHosts ? { allowedHosts } : {}),
      proxy: {
        '/api': {
          //target: 'http://auth.127.0.0.1.nip.io:8081',
          target: env.VITE_API_AUTH_BASE_URL,
          changeOrigin: true,
          rewrite: path => path.replace(/^\/api/, ''),
        },
      },
       // Disable caching for development
      headers: {
        "Cache-Control": "no-store",
        "Pragma": "no-cache",
        "Expires": "0",
      },
    },
    resolve: {
      alias: {
        '@': path.resolve(__dirname, './src'),
        '@store': path.resolve(__dirname, 'src/store'),
      },
    },
  }
})
