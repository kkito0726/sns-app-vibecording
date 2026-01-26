<script setup lang="ts">
import { ref } from "vue";

interface Props {
  loading?: boolean;
  placeholder?: string;
}

const props = withDefaults(defineProps<Props>(), {
  loading: false,
  placeholder: "コメントを追加...",
});

const emit = defineEmits<{
  submit: [text: string];
}>();

const text = ref("");

const handleSubmit = () => {
  if (!text.value.trim()) return;
  emit("submit", text.value);
  text.value = "";
};
</script>

<template>
  <form @submit.prevent="handleSubmit" class="flex items-center space-x-3">
    <textarea
      v-model="text"
      rows="1"
      class="flex-grow px-3 py-2 bg-gray-900 border border-gray-700 rounded-lg text-gray-100 placeholder-gray-500 focus:outline-none focus:ring-1 focus:ring-cyan-500 transition-all duration-300"
      :placeholder="placeholder"
    ></textarea>
    <button
      type="submit"
      :disabled="loading || !text.trim()"
      class="bg-cyan-500 hover:bg-cyan-600 text-white font-bold py-2 px-4 rounded-lg shadow-lg shadow-cyan-500/50 transition-all duration-300 transform hover:scale-105 disabled:opacity-50 disabled:cursor-not-allowed"
    >
      投稿
    </button>
  </form>
</template>
