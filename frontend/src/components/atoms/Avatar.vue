<script setup lang="ts">
interface Props {
  src?: string | null;
  alt?: string;
  size?: "sm" | "md" | "lg" | "xl";
  borderColor?: "purple" | "pink" | "cyan";
  borderWidth?: "thin" | "normal" | "thick";
}

const props = withDefaults(defineProps<Props>(), {
  alt: "Profile",
  size: "md",
  borderColor: "purple",
  borderWidth: "normal",
});

const defaultImage = "/src/assets/default_profile_icon.svg";

const sizeClasses = {
  sm: "w-8 h-8",
  md: "w-10 h-10",
  lg: "w-16 h-16",
  xl: "w-32 h-32",
};

const borderColorClasses = {
  purple: "border-purple-400",
  pink: "border-pink-400",
  cyan: "border-cyan-400",
};

const borderWidthClasses = {
  thin: "border",
  normal: "border-2",
  thick: "border-4",
};
</script>

<template>
  <img
    :src="src || defaultImage"
    :alt="alt"
    :class="[
      'rounded-full object-cover',
      sizeClasses[size],
      borderColorClasses[borderColor],
      borderWidthClasses[borderWidth],
    ]"
  />
</template>
