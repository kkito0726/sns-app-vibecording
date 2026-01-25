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

router.beforeEach((to, from, next) => {
  const authStore = useAuthStore();
  const requiresAuth = to.matched.some(record => record.meta.requiresAuth);
  const isLoginPage = to.name === 'login';
  const isRegisterPage = to.name === 'register';

  if (requiresAuth && !authStore.isLoggedIn) {
    // If route requires auth and user is not logged in, redirect to login
    next({ name: 'login' });
  } else if ((isLoginPage || isRegisterPage) && authStore.isLoggedIn) {
    // If user is logged in and tries to access login/register, redirect to home
    next({ name: 'home' });
  } else {
    // Otherwise, allow navigation
    next();
  }
});

export default router;
