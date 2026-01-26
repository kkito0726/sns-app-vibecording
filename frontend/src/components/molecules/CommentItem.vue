<script setup lang="ts">
import { RouterLink } from "vue-router";
import Avatar from "@/components/atoms/Avatar.vue";
import type { CommentResponse } from "@/types/api";

interface Props {
  comment: CommentResponse;
}

defineProps<Props>();
</script>

<template>
  <div class="bg-gray-700/30 p-3 rounded-lg flex items-start space-x-3">
    <Avatar
      :src="comment.authorProfileImageUrl"
      alt="Author Profile"
      size="sm"
      border-color="purple"
      border-width="thin"
    />
    <div>
      <RouterLink
        :to="`/users/${comment.userId}`"
        class="font-bold text-purple-300 hover:text-purple-400 text-sm"
      >
        {{ comment.authorUsername }}
      </RouterLink>
      <p class="text-gray-200 text-sm" v-text="comment.text"></p>
      <span class="text-gray-500 text-xs">{{
        new Date(comment.createdAt).toLocaleString()
      }}</span>
    </div>
  </div>
</template>
