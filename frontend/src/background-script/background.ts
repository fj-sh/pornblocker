import { DEFAULT_REDIRECT_URL } from '../content-script/redirect'

const BACKGROUND_ALARM_NAME = 'redirectAlarm'

chrome.alarms.create(BACKGROUND_ALARM_NAME, {
  periodInMinutes: 3 / 60,
})

chrome.alarms.onAlarm.addListener(async (alarm) => {
  if (alarm.name === BACKGROUND_ALARM_NAME) {
    await redirectSns()
  }
})

const targetSnses = ['twitter.com', 'www.tiktok.com']

/**
 * SNSをリダイレクトする。
 */
export async function redirectSns() {
  console.log('redirectSns in background')
  const result = await chrome.tabs.query({ active: true, lastFocusedWindow: true })
  console.log('background result', result)
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
