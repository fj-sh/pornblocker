import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import { crx, defineManifest } from '@crxjs/vite-plugin'

const manifest = defineManifest({
  manifest_version: 3,
  name: 'Porn Blocker',
  description:
    'PornBlocker is a plugin that prevents you from viewing pornographic sites. When you open a porn site, it redirects you to a useful site.',
  version: '1.0.0',
  icons: {
    '16': 'blocker.png',
    '48': 'blocker.png',
    '128': 'blocker.png',
  },
  action: {
    default_popup: 'index.html',
  },
})

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react(), crx({ manifest })],
})