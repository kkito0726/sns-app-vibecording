import { ref } from "vue";
import { defineStore } from "pinia";
import axios from "axios";
import { useAuthStore } from "./auth";

export interface Flick {
  id: number;
  userId: number;
  textContent: string | null;
  imageUrl: string | null;
  videoUrl: string | null;
  postType: string;
  createdAt: string;
  // フロントエンドで管理する状態
  likes: number;
  isLiked: boolean;
}

export const useFlickStore = defineStore("flick", () => {
  const flicks = ref<Flick[]>([]);

  const authStore = useAuthStore();

  async function fetchUserFlicks(username: string) {
    try {
      // TODO: APIが実装されたら差し替える
      // const response = await axios.get(`/api/users/${username}/flicks`, {
      //   headers: {
      //     Authorization: `Bearer ${authStore.token}`
      //   }
      // });
      // flicks.value = response.data;

      // ダミーデータ
      const dummyFlicks: Flick[] = [
        {
          id: 1,
          userId: 1,
          textContent: "First flick!",
          imageUrl: null,
          videoUrl: null,
          postType: "TEXT",
          createdAt: "2025-09-15T12:00:00Z",
          likes: 10,
          isLiked: false,
        },
        {
          id: 2,
          userId: 1,
          textContent: "With image",
          imageUrl: "https://via.placeholder.com/300",
          videoUrl: null,
          postType: "IMAGE",
          createdAt: "2025-09-15T13:00:00Z",
          likes: 5,
          isLiked: true,
        },
      ];
      flicks.value = dummyFlicks;
    } catch (error) {
      console.error(`Error fetching flicks for user ${username}:`, error);
    }
  }

  async function like(flickId: number) {
    try {
      await axios.post(
        `/api/flicks/${flickId}/like`,
        {},
        {
          headers: {
            Authorization: `Bearer ${authStore.token}`,
          },
        }
      );
      const flick = flicks.value.find((f) => f.id === flickId);
      if (flick) {
        flick.isLiked = true;
        flick.likes++;
      }
    } catch (error) {
      console.error("Error liking flick:", error);
    }
  }

  async function unlike(flickId: number) {
    try {
      await axios.delete(`/api/flicks/${flickId}/like`, {
        headers: {
          Authorization: `Bearer ${authStore.token}`,
        },
      });
      const flick = flicks.value.find((f) => f.id === flickId);
      if (flick) {
        flick.isLiked = false;
        flick.likes--;
      }
    } catch (error) {
      console.error("Error unliking flick:", error);
    }
  }

  return { flicks, fetchUserFlicks, like, unlike };
});
