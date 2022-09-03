import { targetPorns, targetSnses } from './utils/constants'
export const DEFAULT_REDIRECT_URL = 'https://www.udemy.com/'


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

async function redirect() {
  const tabId = await getCurrentTabId()
  const redirectUrl = await getRedirectUrl()
  await chrome.tabs.create({
    url: redirectUrl,
  })
  if (tabId) {
    await chrome.tabs.remove(tabId)
  }
}

async function isCurrentUrlSns() {
  const currentUrl = await getCurrentUrl()
  const isSns = targetSnses.filter((snsUrl) => currentUrl?.includes(snsUrl))
  return isSns.length !== 0
}

async function isCurrentUrlPorns() {
  const currentUrl = await getCurrentUrl()
  const isPorn = targetPorns.filter((snsUrl) => currentUrl?.includes(snsUrl))
  return isPorn.length !== 0
}

async function main () {
  const currentUrl = await getCurrentUrl()
  if (currentUrl === undefined || currentUrl.includes('chrome://extensions/')) return

  const snsRedirect = await getSnsRedirect()
  const isSns = await isCurrentUrlSns()
  if (snsRedirect && isSns) {
    await redirect()
    return
  }

  const isPorn = await isCurrentUrlPorns()
  if (isPorn) {
    await redirect()
  }

}

chrome.webNavigation.onCompleted.addListener(async () => {
  await main()
})

const ALARM_NAME = 'PornBlocker'

chrome.alarms.create(ALARM_NAME, {
  periodInMinutes: 1
})

chrome.alarms.onAlarm.addListener(async (alarm) => {
  if (alarm.name == ALARM_NAME) {
    await main()
  }
})

