<script setup lang="ts">
const model = defineModel()
withDefaults(defineProps<{
  type?: string
}>(), {
  type: 'text',
})
</script>

<template>
  <input :type="type" v-model="model" class="mt-1 block w-full px-3 py-2 bg-white border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-blue-500 focus:border-blue-500 sm:text-sm" />
</template>