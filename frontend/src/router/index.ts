import { createRouter, createWebHistory } from "vue-router";
import HomeView from "../views/HomeView.vue";
import { useAuthStore } from "@/stores/auth";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      name: "home",
      component: HomeView,
      meta: { requiresAuth: true },
    },
    {
      path: "/about",
      name: "about",
      component: () => import("../views/AboutView.vue"),
    },
    {
      path: "/login",
      name: "login",
      component: () => import("../views/LoginView.vue"),
      meta: { requiresAuth: false },
    },
    {
      path: "/register",
      name: "register",
      component: () => import("../views/RegisterView.vue"),
      meta: { requiresAuth: false },
    },
    {
      path: "/users/:userId",
      name: "userProfile",
      component: () => import("../views/UserProfileView.vue"),
      props: true,
      meta: { requiresAuth: true },
    },
    // Add a catch-all route for 404s
    {
      path: '/:pathMatch(.*)*',
      name: 'NotFound',
      redirect: '/', // Redirect to home for now, can create a 404 page later
    },
  ],
});

router.beforeEach(async (to, from, next) => { // async を追加
  const authStore = useAuthStore();
  await authStore.checkAuth(); // await を追加

  const requiresAuth = to.matched.some(record => record.meta.requiresAuth);
  const isLoginPage = to.name === 'login';
  const isRegisterPage = to.name === 'register';

  if (requiresAuth && !authStore.isLoggedIn) {
    // 認証が必要なルートでログインしていない場合、ログインページへリダイレクト
    next({ name: 'login' });
  } else if ((isLoginPage || isRegisterPage) && authStore.isLoggedIn) {
    // ログイン済みでログイン/登録ページにアクセスしようとした場合、ホームへリダイレクト
    next({ name: 'home' });
  } else {
    // それ以外は通常通りナビゲーションを許可
    next();
  }
});

export default router;
