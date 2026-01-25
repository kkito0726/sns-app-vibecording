<script setup lang="ts">
import { RouterLink, RouterView } from "vue-router";
import { useAuthStore } from "@/stores/auth";
import { onMounted } from "vue";

const authStore = useAuthStore();

const handleLogout = () => {
  authStore.logout();
};

onMounted(() => {
  authStore.checkAuth();
});
</script>

<template>
  <div id="app" class="min-h-screen bg-gray-900 text-gray-100 font-sans">
    <header
      class="bg-gray-900/50 backdrop-blur-sm shadow-md shadow-purple-500/20 sticky top-0 z-50"
    >
      <div class="container mx-auto px-4 py-4 flex justify-between items-center">
        <RouterLink to="/" class="flex items-center">
          <img alt="Flick logo" class="h-10 w-10 mr-3" src="@/assets/logo.svg" />
          <span class="text-3xl font-bold text-purple-400 font-orbitron">Flick</span>
        </RouterLink>
        <nav class="flex items-center space-x-6 text-lg">
          <RouterLink
            v-if="authStore.isLoggedIn"
            to="/"
            class="text-gray-300 hover:text-purple-400 transition-colors duration-300"
            >Home</RouterLink
          >
          <RouterLink
            to="/about"
            class="text-gray-300 hover:text-purple-400 transition-colors duration-300"
            >About</RouterLink
          >
          <template v-if="!authStore.isLoggedIn">
            <RouterLink
              to="/login"
              class="text-gray-300 hover:text-purple-400 transition-colors duration-300"
              >Login</RouterLink
            >
            <RouterLink
              to="/register"
              class="bg-purple-500 hover:bg-purple-600 text-white font-bold py-2 px-4 rounded-lg shadow-lg shadow-purple-500/50 transition-all duration-300 transform hover:scale-105"
              >Register</RouterLink
            >
          </template>
          <template v-else>
            <RouterLink
              :to="`/users/${authStore.user?.id}`"
              class="text-gray-300 hover:text-purple-400 transition-colors duration-300"
              >Profile</RouterLink
            >
            <button
              @click="handleLogout"
              class="bg-pink-500 hover:bg-pink-600 text-white font-bold py-2 px-4 rounded-lg shadow-lg shadow-pink-500/50 transition-all duration-300 transform hover:scale-105"
            >
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
@import url("https://fonts.googleapis.com/css2?family=Orbitron:wght@400;700&display=swap");

.font-orbitron {
  font-family: "Orbitron", sans-serif;
}

body {
  background-color: #111827;
}
</style>
