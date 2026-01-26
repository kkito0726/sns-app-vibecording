<script setup lang="ts">
interface Props {
  modelValue: string;
  label: string;
  type?: "text" | "email" | "password";
  id: string;
  placeholder?: string;
  required?: boolean;
  accentColor?: "purple" | "pink" | "cyan";
}

const props = withDefaults(defineProps<Props>(), {
  type: "text",
  placeholder: "",
  required: false,
  accentColor: "purple",
});

const emit = defineEmits<{
  "update:modelValue": [value: string];
  "enterPressed": [];
}>();

const ringColorClass = {
  purple: "focus:ring-purple-500",
  pink: "focus:ring-pink-500",
  cyan: "focus:ring-cyan-500",
};
</script>

<template>
  <div>
    <label :for="id" class="block text-lg font-medium text-gray-300 mb-2">
      {{ label }}
    </label>
    <input
      :id="id"
      :value="modelValue"
      @input="emit('update:modelValue', ($event.target as HTMLInputElement).value)"
      @keyup.enter="emit('enterPressed')"
      :type="type"
      :required="required"
      :placeholder="placeholder"
      :class="[
        'w-full px-4 py-3 bg-gray-900 border border-gray-700 rounded-lg text-gray-100 focus:outline-none focus:ring-2 transition-all duration-300',
        ringColorClass[accentColor],
      ]"
    />
  </div>
</template>
