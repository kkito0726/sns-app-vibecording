<script setup lang="ts">
import { ref } from "vue";
import { useRouter } from "vue-router";
import { useAuthStore } from "@/stores/auth";
import AuthCard from "@/components/molecules/AuthCard.vue";
import FormInput from "@/components/atoms/FormInput.vue";
import ErrorAlert from "@/components/atoms/ErrorAlert.vue";

const email = ref("");
const password = ref("");
const error = ref<string | null>(null);
const router = useRouter();
const authStore = useAuthStore();

const handleLogin = async () => {
  error.value = null;
  try {
    await authStore.login({
      email: email.value,
      password: password.value,
    });
    router.push("/");
  } catch (err) {
    error.value = "Login failed. Please check your credentials.";
    console.error("Login failed:", err);
  }
};
</script>

<template>
  <AuthCard title="Login to Flick" accent-color="purple">
    <form @submit.prevent="handleLogin" class="space-y-6">
      <FormInput
        id="email"
        v-model="email"
        label="Email"
        type="email"
        placeholder="you@example.com"
        :required="true"
        accent-color="purple"
        @enter-pressed="handleLogin"
      />
      <FormInput
        id="password"
        v-model="password"
        label="Password"
        type="password"
        placeholder="********"
        :required="true"
        accent-color="purple"
        @enter-pressed="handleLogin"
      />
      <ErrorAlert :message="error" />
      <div>
        <button
          type="submit"
          class="w-full bg-purple-500 hover:bg-purple-600 text-white font-bold py-3 px-4 rounded-lg shadow-lg shadow-purple-500/50 transition-all duration-300 transform hover:scale-105"
        >
          Login
        </button>
      </div>
    </form>
    <div class="text-center mt-4">
      <RouterLink to="/register" class="text-gray-400 hover:text-purple-400">
        Don't have an account? Register here.
      </RouterLink>
    </div>
  </AuthCard>
</template>
