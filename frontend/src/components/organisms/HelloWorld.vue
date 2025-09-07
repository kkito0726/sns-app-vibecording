<script setup lang="ts">
defineProps<{
  msg: string
}>()
</script>

<template>
  <div>
    <h1 class="text-3xl font-bold text-green-500">{{ msg }}</h1>
    <h3 class="text-lg mt-2">
      You’ve successfully created a project with
      <a href="https://vite.dev/" target="_blank" rel="noopener" class="text-blue-500 hover:underline">Vite</a> +
      <a href="https://vuejs.org/" target="_blank" rel="noopener" class="text-blue-500 hover:underline">Vue 3</a>.
    </h3>
  </div>
</template>

<style scoped>
/* Scoped styles are not needed as we are using Tailwind CSS */
</style>