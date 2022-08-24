import React, { ChangeEvent, useEffect, useState } from 'react'

import './App.css'
import {
  getRedirectUrl,
  getSnsRedirect,
  setRedirectUrlToLocalStorage,
  setSnsRedirectToLocalStorage,
} from './background-script/background'
import { targetPorns, targetSnses } from './background-script/utils/constants'

function validateRedirectUrl(redirectUrl: string): boolean {
  const isSnsArray = targetSnses.filter((targetSns) => redirectUrl.includes(targetSns))
  const isPornArray = targetPorns.filter((targetPorn) => redirectUrl.includes(targetPorn))

  return isSnsArray.length === 0 && isPornArray.length === 0
}

/**
 * App.tsx
 * @constructor
 */
function App() {
  const [redirectUrl, setRedirectUrl] = useState<string>('')
  const [snsRedirect, setSnsRedirect] = useState<boolean>(false)
  const [errorMessage, setErrorMessage] = useState<string>('')
  const onSave = async () => {
    // リダイレクト先がSNSやポルノサイトの場合はエラーメッセージを表示する
    const validUrl = validateRedirectUrl(redirectUrl)
    if (!validUrl) {
      setErrorMessage('Pornographic sites and social networking sites cannot be redirected to.')
      return
    }
    setErrorMessage('')
    await setRedirectUrl(redirectUrl)
    await setRedirectUrlToLocalStorage(redirectUrl)
  }
  const onRedirectUrlChange = (event: ChangeEvent<HTMLInputElement>) => {
    setRedirectUrl(event.target.value)
  }

  const onSnsRedirectClick = async (event: ChangeEvent<HTMLInputElement>) => {
    setSnsRedirect(event.target.checked)
    await setSnsRedirectToLocalStorage(event.target.checked)
  }

  useEffect(() => {
    getSnsRedirect().then((snsRedirectSettings) => {
      setSnsRedirect(snsRedirectSettings)
    })
    getRedirectUrl().then((redirectUrlSetting) => {
      setRedirectUrl(redirectUrlSetting)
    })
  }, [])

  return (
    <div className="auto">
      <div className="flex items-center mb-5">
        <label
          htmlFor="url"
          className="inline-block w-20 mr-6 text-right
                                 font-bold text-gray-600"
        >
          Redirect URL
        </label>
        <input
          type="text"
          id="url"
          name="url"
          value={redirectUrl}
          onChange={onRedirectUrlChange}
          placeholder="Redirect URL"
          className="flex-1 py-2 border-b-2 border-gray-400 focus:border-blue-400
                      text-gray-600 placeholder-gray-400
                      outline-none w-96"
        />
        <button
          className="ml-5 bg-blue-500 hover:bg-blue-700 text-white peer-focus:outline-none peer-focus:ring-blue-300 font-bold py-2 px-4 rounded"
          onClick={onSave}
        >
          Save
        </button>
      </div>
      {errorMessage && <span className="text-red-600">{errorMessage}</span>}
      <label htmlFor="default-toggle" className="inline-flex relative items-center cursor-pointer">
        <input
          type="checkbox"
          id="default-toggle"
          className="sr-only peer"
          checked={snsRedirect}
          onChange={onSnsRedirectClick}
        />
        <div className="w-11 h-6 bg-gray-200 peer-focus:outline-none peer-focus:ring-4 peer-focus:ring-blue-300 dark:peer-focus:ring-blue-800 rounded-full peer dark:bg-gray-700 peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all dark:border-gray-600 peer-checked:bg-blue-600"></div>
        <span className="ml-3 text-sm font-medium text-gray-900 dark:text-gray-300">
          Redirect SNS as well
        </span>
      </label>
    </div>
  )
}

export default App
