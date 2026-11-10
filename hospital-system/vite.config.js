import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import path from 'path'

export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': path.resolve(__dirname, 'src')
    }
  },
  server: {
    port: 5175, // 👈 确保这里的端口和你当前运行的 5175 一致！
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        //rewrite: path => path.replace(/^\/api/, '')
      }
    }
  }
})