
以下のコマンドを実行します。

```shell
npm init vite@latest
npm i @crxjs/vite-plugin -D
```

`vite.confit.ts`を編集します。

```ts
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

```

`public`以下に`blocker.png`というアイコンファイルを置きます。

アイコンは以下のサイトで探します。

[flaticon](https://www.flaticon.com/free-icons/red-cross)

## ESLint と Prettier の設定

```shell
npm i --save-dev eslint @typescript-eslint/parser @typescript-eslint/eslint-plugin
npx eslint --init
npm install -D prettier eslint-config-prettier
touch .prettierrc.cjs
```

`.eslintrc.cjs`を修正します。

```js
  'extends': [
    'plugin:react/recommended',
    'google',
    'prettier',
  ],
```

`.prettierrc.js`を以下のように編集します。

```js
module.exports = {
  singleQuote: true,
  trailingComma: 'es5',
  printWidth: 100,
  semi: false,
}
```


## Tailwind CSS の導入

```
npm i -D tailwindcss postcss autoprefixer
npx tailwindcss init -p
```

tailwind.config.cjs

```js
content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
```


index.css

```css
@tailwind base;
@tailwind components;
@tailwind utilities;
```

## テストを導入する

```shell
npm install jest @types/jest ts-jest --save-dev
touch jest.config.json
```

jest.config.json

```json
{
  "roots": [
    "<rootDir>/src"
  ],
  "testMatch": [
    "**/__tests__/**/*.+(ts|tsx|js)",
    "**/?(*.)+(spec|test).+(ts|tsx|js)"
  ],
  "transform": {
    "^.+\\.(ts|tsx)$": "ts-jest"
  }
}

```

https://medium.com/information-and-technology/integration-testing-browser-extensions-with-jest-676b4e9940ca
