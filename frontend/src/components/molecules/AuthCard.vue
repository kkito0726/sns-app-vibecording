<script setup lang="ts">
interface Props {
  title: string;
  accentColor?: "purple" | "pink";
}

const props = withDefaults(defineProps<Props>(), {
  accentColor: "purple",
});

const colorClasses = {
  purple: {
    shadow: "shadow-purple-500/20",
    border: "border-purple-500/30",
    title: "text-purple-400",
  },
  pink: {
    shadow: "shadow-pink-500/20",
    border: "border-pink-500/30",
    title: "text-pink-400",
  },
};
</script>

<template>
  <div
    :class="[
      'max-w-md mx-auto mt-10 bg-gray-800/50 backdrop-blur-sm p-8 rounded-xl shadow-lg border',
      colorClasses[accentColor].shadow,
      colorClasses[accentColor].border,
    ]"
  >
    <h1
      :class="[
        'text-4xl font-bold text-center mb-8 font-orbitron',
        colorClasses[accentColor].title,
      ]"
    >
      {{ title }}
    </h1>
    <slot />
  </div>
</template>
