<template>
  <div class="flex items-start mt-8">
    <i class="flex items-center justify-center w-12 h-12 mr-4 text-gray-500">
      <slot name="icon"></slot>
    </i>
    <div class="flex-1">
      <h3 class="text-xl font-bold mb-2">
        <slot name="heading"></slot>
      </h3>
      <div class="text-gray-600">
        <slot></slot>
      </div>
    </div>
  </div>
</template>

<style scoped>
/* Scoped styles are not needed as we are using Tailwind CSS */
</style>