<script setup lang="ts">
import { ref } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'
import FormField from '@/components/molecules/FormField.vue'
import BaseButton from '@/components/atoms/BaseButton.vue'
import { useAuthStore } from '@/stores/auth'

const email = ref('')
const password = ref('')
const router = useRouter()
const authStore = useAuthStore()

const handleLogin = async () => {
  try {
    const response = await axios.post('/api/auth/login', {
      email: email.value,
      password: password.value
    })
    
    // トークンをPiniaストアに保存
    authStore.setToken(response.data.token)
    console.log('Login successful')

    // ログイン成功後、ホームページにリダイレクト
    router.push('/')
  } catch (error) {
    console.error('Login failed:', error)
    // ここでユーザーにエラーメッセージを表示する処理を追加できます
  }
}
</script>

<template>
  <div class="max-w-md mx-auto mt-10 bg-white p-8 rounded-lg shadow-md">
    <h1 class="text-2xl font-bold text-center mb-6">Login</h1>
    <form @submit.prevent="handleLogin" class="space-y-6">
      <FormField id="email" label="Email" v-model="email" />
      <FormField id="password" label="Password" v-model="password" type="password" />

      <div>
        <BaseButton type="submit">Login</BaseButton>
      </div>
    </form>
  </div>
</template>