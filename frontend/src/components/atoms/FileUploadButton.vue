<script setup lang="ts">
import { ref } from "vue";

interface Props {
  accept: string;
  color?: "purple" | "pink" | "cyan";
  title?: string;
}

const props = withDefaults(defineProps<Props>(), {
  color: "purple",
  title: "ファイルをアップロード",
});

const emit = defineEmits<{
  change: [file: File | null];
}>();

const inputRef = ref<HTMLInputElement | null>(null);

const colorClasses = {
  purple: "bg-purple-500 hover:bg-purple-600 shadow-purple-500/50",
  pink: "bg-pink-500 hover:bg-pink-600 shadow-pink-500/50",
  cyan: "bg-cyan-500 hover:bg-cyan-600 shadow-cyan-500/50",
};

const handleChange = (event: Event) => {
  const target = event.target as HTMLInputElement;
  if (target.files && target.files.length > 0) {
    emit("change", target.files[0]);
  } else {
    emit("change", null);
  }
};

const click = () => {
  inputRef.value?.click();
};

const reset = () => {
  if (inputRef.value) {
    inputRef.value.value = "";
  }
};

defineExpose({ click, reset });
</script>

<template>
  <div>
    <input
      type="file"
      :accept="accept"
      @change="handleChange"
      class="hidden"
      ref="inputRef"
    />
    <button
      type="button"
      @click="click"
      :class="[
        'flex items-center justify-center p-2 rounded-full text-white shadow-lg transition-all duration-300 transform hover:scale-110',
        colorClasses[color],
      ]"
      :title="title"
    >
      <slot />
    </button>
  </div>
</template>
