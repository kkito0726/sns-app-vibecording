<script setup lang="ts">
interface Props {
  message: string | null;
  centered?: boolean;
}

withDefaults(defineProps<Props>(), {
  centered: true,
});
</script>

<template>
  <div
    v-if="message"
    :class="['text-pink-500 text-sm', centered ? 'text-center' : '']"
  >
    {{ message }}
  </div>
</template>
