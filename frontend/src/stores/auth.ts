import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useAuthStore = defineStore('auth', () => {
  const token = ref<string | null>(null)

  const isLoggedIn = computed(() => !!token.value)

  function setToken(newToken: string) {
    token.value = newToken
    // オプション：localStorageにトークンを保存して永続化する
    // localStorage.setItem('authToken', newToken)
  }

  function clearToken() {
    token.value = null
    // localStorage.removeItem('authToken')
  }

  // アプリケーション初期化時にlocalStorageからトークンを読み込む
  // function loadToken() {
  //   const storedToken = localStorage.getItem('authToken')
  //   if (storedToken) {
  //     token.value = storedToken
  //   }
  // }

  return { token, isLoggedIn, setToken, clearToken }
})
