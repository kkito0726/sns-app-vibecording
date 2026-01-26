<script setup lang="ts">
interface Props {
  loading: boolean;
  error?: string | null;
  isEmpty?: boolean;
  loadingText?: string;
  emptyText?: string;
}

const props = withDefaults(defineProps<Props>(), {
  error: null,
  isEmpty: false,
  loadingText: "読み込み中...",
  emptyText: "データがありません。",
});
</script>

<template>
  <div v-if="loading" class="text-center text-purple-300 text-xl">
    {{ loadingText }}
  </div>
  <div v-else-if="error" class="text-center text-pink-500 text-xl">
    {{ error }}
  </div>
  <div v-else-if="isEmpty" class="text-center text-gray-400 text-xl">
    {{ emptyText }}
  </div>
  <slot v-else />
</template>
