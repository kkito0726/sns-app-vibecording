<script setup lang="ts">
import { ref } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'
import FormField from '@/components/molecules/FormField.vue'
import BaseButton from '@/components/atoms/BaseButton.vue'

const username = ref('')
const email = ref('')
const password = ref('')
const router = useRouter()

const handleRegister = async () => {
  try {
    await axios.post('/api/users/register', {
      username: username.value,
      email: email.value,
      password: password.value
    })
    // 登録成功後、ログインページにリダイレクト
    router.push('/login')
  } catch (error) {
    console.error('Registration failed:', error)
    // ここでユーザーにエラーメッセージを表示する処理を追加できます
  }
}
</script>

<template>
  <div class="max-w-md mx-auto mt-10 bg-white p-8 rounded-lg shadow-md">
    <h1 class="text-2xl font-bold text-center mb-6">Create an Account</h1>
    <form @submit.prevent="handleRegister" class="space-y-6">
      <FormField id="username" label="Username" v-model="username" />
      <FormField id="email" label="Email" v-model="email" />
      <FormField id="password" label="Password" v-model="password" type="password" />

      <div>
        <BaseButton type="submit">Register</BaseButton>
      </div>
    </form>
  </div>
</template>