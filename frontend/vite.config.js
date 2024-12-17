import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

export default defineConfig({
  plugins: [react()],
  base: '/', // Ensures the base path is correct
  build: {
    outDir: 'dist', // Default output folder
  },
  server: {
    historyApiFallback: true, // Enables fallback for SPA routing locally
  },
});
