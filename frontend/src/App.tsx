import React, { ChangeEvent, useRef } from 'react'

import './App.css'
import { DEFAULT_REDIRECT_URL } from './content-script/redirect'

/**
 * App.tsx
 * @constructor
 */
function App() {
  const redirectUrl = useRef<HTMLInputElement>(null)
  const alsoSnsRedirect = useRef<boolean>(false)
  const onSave = () => {
    if (redirectUrl.current && redirectUrl.current.value) {
      console.log(redirectUrl.current.value)
    }
  }

  const onSnsRedirectClick = (event: ChangeEvent<HTMLInputElement>) => {
    console.log('onSnsRedirectClick', event.target.checked)
    alsoSnsRedirect.current = event.target.checked
  }

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
          ref={redirectUrl}
          defaultValue={DEFAULT_REDIRECT_URL}
          placeholder="Redirect URL"
          className="flex-1 py-2 border-b-2 border-gray-400 focus:border-blue-400
                      text-gray-600 placeholder-gray-400
                      outline-none w-96"
        />
        <button
          className="ml-5 bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded"
          onClick={onSave}
        >
          Save
        </button>
      </div>
      <label htmlFor="default-toggle" className="inline-flex relative items-center cursor-pointer">
        <input
          type="checkbox"
          id="default-toggle"
          className="sr-only peer"
          defaultChecked={alsoSnsRedirect.current}
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
