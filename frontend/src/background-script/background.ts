import { targetPorns, targetSnses } from './utils/constants'

const BACKGROUND_ALARM_NAME = 'redirectAlarm'
export const DEFAULT_REDIRECT_URL = 'https://www.udemy.com/'
const DEFAULT_REDIRECT_TIME = 10

chrome.alarms.clearAll().then((result) => {
  chrome.alarms.create(BACKGROUND_ALARM_NAME, {
    periodInMinutes: 1 / 60,
  })
})

chrome.alarms.onAlarm.addListener(async (alarm) => {
  if (alarm.name === BACKGROUND_ALARM_NAME) {
    await minusRedirectTimer()
  }
})

export async function getRedirectTimer() {
  const localStorage = await chrome.storage.local.get('redirectTimer')
  const redirectTimer = localStorage.redirectTimer
  return redirectTimer === undefined ? 60 : redirectTimer
}

async function minusRedirectTimer() {
  const localStorage = await chrome.storage.local.get('redirectTimer')
  const redirectTimer = localStorage.redirectTimer
  console.log('minusRedirectTimer', redirectTimer)
  if (redirectTimer === undefined) {
    await chrome.storage.local.set({ redirectTimer: DEFAULT_REDIRECT_TIME })
  } else {
    const updatedRedirectTimer = redirectTimer - 1
    console.log('updatedRedirectTimer', updatedRedirectTimer)
    if (updatedRedirectTimer === 0) {
      await chrome.storage.local.set({ redirectTimer: DEFAULT_REDIRECT_TIME })
      await redirect(targetSnses)
      await redirect(targetPorns)
    } else {
      await chrome.storage.local.set({ redirectTimer: redirectTimer - 1 })
    }
  }
}

/**
 * リダイレクト
 * @param targetUrls
 */
async function redirect(targetUrls: string[]) {
  console.log('Called Redirect')
  const tabs = await chrome.tabs.query({ active: true, lastFocusedWindow: true })
  if (tabs[0] === undefined) return
  const currentUrl = tabs[0].url
  console.log('currentUrl', currentUrl)
  if (currentUrl === undefined) return
  const tabId = tabs[0].id
  const localStorage = await chrome.storage.local.get(['redirectUrl'])
  const redirectUrlFromLocalStorage = localStorage.redirectUrl
  const redirectUrl =
    redirectUrlFromLocalStorage != null && typeof redirectUrlFromLocalStorage === 'string'
      ? redirectUrlFromLocalStorage
      : DEFAULT_REDIRECT_URL
  if (currentUrl.includes('chrome://extensions/')) return
  const isTarget = targetUrls.filter((pornUrl) => currentUrl.includes(pornUrl))
  if (isTarget.length === 0) return
  console.log(`Redirect to ${redirectUrl} from ${currentUrl}`)
  await chrome.tabs.create({
    url: redirectUrl,
  })
  if (tabId) {
    await chrome.tabs.remove(tabId)
  }
}

/**
 * SNSをリダイレクトする。
 */
export async function redirectSns() {
  const result = await chrome.tabs.query({ active: true, lastFocusedWindow: true })
  if (result[0].url === undefined) return
  const url = result[0].url
  const tabId = result[0].id
  console.log('redirectSns in background', url)
  if (url == null || url.includes('chrome://extensions/')) return
  const isTargets = targetSnses.filter((sns) => url.includes(sns))
  if (isTargets.length !== 0) {
    chrome.tabs.create({
      url: DEFAULT_REDIRECT_URL,
    })
    if (tabId) {
      chrome.tabs.remove(tabId)
    }
  }
}

export {}
