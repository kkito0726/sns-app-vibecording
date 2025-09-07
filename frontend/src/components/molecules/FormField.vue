<script setup lang="ts">
import BaseLabel from '../atoms/BaseLabel.vue'
import BaseInput from '../atoms/BaseInput.vue'

withDefaults(defineProps<{
  label: string
  id: string
  type?: string
}>(), {
  type: 'text'
})

const model = defineModel()
</script>

<template>
  <div>
    <BaseLabel :for="id">{{ label }}</BaseLabel>
    <BaseInput :id="id" :type="type" v-model="model" />
  </div>
</template>