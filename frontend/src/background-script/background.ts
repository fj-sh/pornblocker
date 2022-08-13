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

export async function setSnsRedirectToLocalStorage(snsRedirect: boolean) {
  await chrome.storage.local.set({ snsRedirect: snsRedirect })
}

export async function getSnsRedirect() {
  const localStorage = await chrome.storage.local.get('snsRedirect')
  if (localStorage.snsRedirect === undefined) {
    await chrome.storage.local.set({ snsRedirect: false })
    return false
  }
  return localStorage.snsRedirect
}

export async function getRedirectTimer() {
  const localStorage = await chrome.storage.local.get('redirectTimer')
  if (localStorage.redirectTimer === undefined) {
    await chrome.storage.local.set({ redirectTimer: DEFAULT_REDIRECT_TIME })
    return DEFAULT_REDIRECT_TIME
  }
  return localStorage.redirectTimer
}

async function setRedirectTimer(updatedRedirectTimer: number) {
  if (updatedRedirectTimer === 0) {
    await chrome.storage.local.set({ redirectTimer: DEFAULT_REDIRECT_TIME })
  } else {
    await chrome.storage.local.set({ redirectTimer: updatedRedirectTimer })
  }
}

async function minusRedirectTimer() {
  const redirectTimer = await getRedirectTimer()
  const updatedRedirectTimer = redirectTimer - 1
  await setRedirectTimer(updatedRedirectTimer)
  if (updatedRedirectTimer === 0) {
    const snsRedirect = await getSnsRedirect()
    if (snsRedirect) {
      await redirect(targetSnses)
    }
    await redirect(targetPorns)
  }
}

async function getCurrentUrl() {
  const tabs = await chrome.tabs.query({ active: true, lastFocusedWindow: true })
  if (tabs[0] === undefined) return undefined
  const currentUrl = tabs[0].url
  if (currentUrl === undefined) return undefined
  return currentUrl
}

async function getCurrentTabId() {
  const tabs = await chrome.tabs.query({ active: true, lastFocusedWindow: true })
  if (tabs[0] === undefined) return undefined
  const tabId = tabs[0].id
  if (tabId === undefined) return undefined
  return tabId
}

export async function setRedirectUrlToLocalStorage(redirectUrl: string) {
  await chrome.storage.local.set({ redirectUrl: redirectUrl })
}

export async function getRedirectUrl() {
  const localStorage = await chrome.storage.local.get('redirectUrl')
  if (localStorage.redirectUrl === undefined) {
    await chrome.storage.local.set({ redirectUrl: DEFAULT_REDIRECT_URL })
    return DEFAULT_REDIRECT_URL
  }
  return localStorage.redirectUrl
}
/**
 * リダイレクト
 * @param {string[]} targetUrls
 */
async function redirect(targetUrls: string[]) {
  const currentUrl = await getCurrentUrl()
  if (currentUrl === undefined || currentUrl.includes('chrome://extensions/')) return

  const tabId = await getCurrentTabId()
  const redirectUrl = await getRedirectUrl()
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
