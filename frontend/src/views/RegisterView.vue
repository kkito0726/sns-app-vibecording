<script setup lang="ts">
import { ref } from "vue";
import { useRouter } from "vue-router";
import { useAuthStore } from "@/stores/auth";
import AuthCard from "@/components/molecules/AuthCard.vue";
import FormInput from "@/components/atoms/FormInput.vue";
import ErrorAlert from "@/components/atoms/ErrorAlert.vue";

const username = ref("");
const email = ref("");
const password = ref("");
const error = ref<string | null>(null);
const router = useRouter();
const authStore = useAuthStore();

const handleRegister = async () => {
  error.value = null;
  try {
    await authStore.register({
      username: username.value,
      email: email.value,
      password: password.value,
    });
    // 登録成功後、自動でログインしてホーム画面へ遷移
    await authStore.login({
      email: email.value,
      password: password.value,
    });
    router.push("/");
  } catch (err) {
    error.value = "Registration failed. Please try again.";
    console.error("Registration failed:", err);
  }
};
</script>

<template>
  <AuthCard title="Create Flick Account" accent-color="pink">
    <form @submit.prevent="handleRegister" class="space-y-6">
      <FormInput
        id="username"
        v-model="username"
        label="Username"
        type="text"
        placeholder="yourusername"
        :required="true"
        accent-color="pink"
        @enter-pressed="handleRegister"
      />
      <FormInput
        id="email"
        v-model="email"
        label="Email"
        type="email"
        placeholder="you@example.com"
        :required="true"
        accent-color="pink"
        @enter-pressed="handleRegister"
      />
      <FormInput
        id="password"
        v-model="password"
        label="Password"
        type="password"
        placeholder="********"
        :required="true"
        accent-color="pink"
        @enter-pressed="handleRegister"
      />
      <ErrorAlert :message="error" />
      <div>
        <button
          type="submit"
          class="w-full bg-pink-500 hover:bg-pink-600 text-white font-bold py-3 px-4 rounded-lg shadow-lg shadow-pink-500/50 transition-all duration-300 transform hover:scale-105"
        >
          Register
        </button>
      </div>
    </form>
    <div class="text-center mt-4">
      <RouterLink to="/login" class="text-gray-400 hover:text-pink-400">
        Already have an account? Login here.
      </RouterLink>
    </div>
  </AuthCard>
</template>
