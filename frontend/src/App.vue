<script setup lang="ts">
import { RouterLink, RouterView } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const authStore = useAuthStore()

const handleLogout = () => {
  authStore.clearToken()
  // 必要に応じてログインページなどにリダイレクト
}
</script>

<template>
  <div id="app" class="min-h-screen bg-gray-100 text-gray-800">
    <header class="bg-white shadow-md">
      <div class="container mx-auto px-4 py-4 flex justify-between items-center">
        <RouterLink to="/" class="flex items-center">
          <img alt="Vibecording logo" class="h-10 w-10 mr-3" src="@/assets/logo.svg" />
          <span class="text-2xl font-bold">Vibecording</span>
        </RouterLink>
        <nav>
          <RouterLink to="/" class="text-gray-600 hover:text-gray-900 px-3 py-2">Home</RouterLink>
          <RouterLink to="/about" class="text-gray-600 hover:text-gray-900 px-3 py-2">About</RouterLink>
          
          <template v-if="!authStore.isLoggedIn">
            <RouterLink to="/login" class="text-gray-600 hover:text-gray-900 px-3 py-2">Login</RouterLink>
            <RouterLink to="/register" class="bg-blue-500 hover:bg-blue-600 text-white font-bold py-2 px-4 rounded">Register</RouterLink>
          </template>
          <template v-else>
            <button @click="handleLogout" class="bg-red-500 hover:bg-red-600 text-white font-bold py-2 px-4 rounded">
              Logout
            </button>
          </template>
        </nav>
      </div>
    </header>

    <main class="container mx-auto px-4 py-8">
      <RouterView />
    </main>
  </div>
</template>

<style>
/* `main.css`でTailwindをインポートしているので、ここではグローバルスタイルは不要 */
</style>
