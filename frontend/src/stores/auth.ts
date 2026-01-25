import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import api from '@/api'
import type { LoginUserRequest, RegisterUserRequest, UserResponse } from '@/types/api'

export const useAuthStore = defineStore('auth', () => {
  const token = ref<string | null>(localStorage.getItem('token'))
  const user = ref<UserResponse | null>(null)

  const isLoggedIn = computed(() => !!token.value && !!user.value)

  function setToken(newToken: string) {
    token.value = newToken
    localStorage.setItem('token', newToken)
  }

  function clearToken() {
    token.value = null
    user.value = null
    localStorage.removeItem('token')
  }

  async function login(credentials: LoginUserRequest) {
    const { data } = await api.post('/auth/login', credentials)
    setToken(data.token)
    await fetchUser()
  }

  async function register(userInfo: RegisterUserRequest) {
    await api.post('/users/register', userInfo)
  }

  async function logout() {
    clearToken()
  }

  async function fetchUser() {
    if (token.value) {
      try {
        const { data } = await api.get<UserResponse>('/users/me')
        user.value = data
      } catch (error) {
        console.error('Failed to fetch user', error)
        clearToken()
      }
    }
  }

  async function checkAuth() {
    if (token.value) {
      await fetchUser()
    }
  }

  return { token, user, isLoggedIn, login, register, logout, checkAuth }
})
