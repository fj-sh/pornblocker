export const DEFAULT_REDIRECT_URL = 'https://www.udemy.com/'

console.log('Runs ContentScript')

const timer = setInterval(async () => {
  console.log('redirectします。')
  await redirect()
}, 3000)

/**
 * 指定されたURLにリダイレクトする。
 */
async function redirect() {
  console.log('redirectが呼ばれました。')
  if (timer != null) {
    console.log('timerを削除')
    clearInterval(timer)
  }

  const result = await chrome.storage.local.get(['redirectUrl'])
  const redirectUrl = result.redirectUrl
  if (redirectUrl != null && typeof redirectUrl === 'string') {
    document.location.href = redirectUrl
  } else {
    document.location.href = DEFAULT_REDIRECT_URL
  }
}
