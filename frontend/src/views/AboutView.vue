<script setup lang="ts">
import { RouterLink } from 'vue-router'
</script>

<template>
  <div class="about-view py-12 px-4 text-center">
    <div class="max-w-4xl mx-auto">
      <!-- Hero Section -->
      <h1 class="text-6xl font-bold text-purple-400 font-orbitron mb-6 leading-tight neon-text-shadow">
        Flickへようこそ
      </h1>
      <p class="text-2xl text-gray-300 mb-12 max-w-2xl mx-auto leading-relaxed">
        人生の儚い瞬間と生の感情を捉え、あなただけの「Flick」を世界と共有しましょう。
      </p>

      <!-- Introduction -->
      <section class="mb-16 bg-gray-800/40 backdrop-blur-sm p-8 rounded-xl shadow-lg shadow-pink-500/10 border border-pink-500/20">
        <h2 class="text-4xl font-bold text-pink-400 mb-6 font-orbitron">Flickとは？</h2>
        <p class="text-xl text-gray-200 leading-loose">
          Flickは、日々のきらめきや感情のうねりをシームレスに共有するために設計された、活気あるソーシャルネットワーキングサービスです。ちょっとした思いつき、目を引く写真、あるいは瞬間の本質を捉えた短い動画など、Flickはあなたが自由に自分を表現し、真に繋がることを可能にします。
        </p>
      </section>

      <!-- Key Concepts Section -->
      <section class="grid md:grid-cols-3 gap-8 mb-16">
        <div class="concept-card">
          <h3 class="text-3xl font-bold text-cyan-400 mb-4 font-orbitron">多様なFlick共有</h3>
          <p class="text-lg text-gray-300">
            テキストのみのつぶやき、魅力的な画像、ダイナミックな短い動画など、制限なく自分を表現しましょう。あらゆる瞬間が共有されるのを待つFlickです。
          </p>
        </div>
        <div class="concept-card">
          <h3 class="text-3xl font-bold text-purple-400 mb-4 font-orbitron">感覚的なコミュニケーション</h3>
          <p class="text-lg text-gray-300">
            「いいね」やコメントを超えて、多様なFlickの生々しく、フィルターのかかっていない本質を通して他者と繋がり、より深く感覚的な繋がりを育みます。
          </p>
        </div>
        <div class="concept-card">
          <h3 class="text-3xl font-bold text-pink-400 mb-4 font-orbitron">シンプルなUI/UX</h3>
          <p class="text-lg text-gray-300">
            直感的なデザインを体験してください。洗練されたインターフェースにより、投稿と閲覧が簡単になり、複雑さではなくコンテンツに集中できます。
          </p>
        </div>
      </section>

      <!-- Call to Action -->
      <div class="mt-16">
        <RouterLink to="/register" class="inline-block bg-gradient-to-r from-purple-600 to-pink-500 hover:from-purple-700 hover:to-pink-600 text-white font-bold py-4 px-10 rounded-full text-xl shadow-lg shadow-purple-500/50 transition-all duration-300 transform hover:scale-105 neon-button">
          今すぐFlickに参加！
        </RouterLink>
      </div>
    </div>
  </div>
</template>

<style scoped>
.neon-text-shadow {
  text-shadow:
    0 0 7px rgba(255, 0, 255, 0.7),
    0 0 10px rgba(255, 0, 255, 0.7),
    0 0 21px rgba(255, 0, 255, 0.7),
    0 0 42px rgba(0, 255, 255, 0.5),
    0 0 82px rgba(0, 255, 255, 0.5),
    0 0 92px rgba(0, 255, 255, 0.5),
    0 0 102px rgba(0, 255, 255, 0.5),
    0 0 151px rgba(0, 255, 255, 0.5);
}

.concept-card {
  @apply bg-gray-800/40 backdrop-blur-sm p-6 rounded-xl shadow-lg border transition-all duration-300 transform hover:scale-105;
  border-color: rgba(79, 70, 229, 0.3); /* blue-500/30 */
}

.concept-card:nth-child(1) {
  border-color: rgba(6, 182, 212, 0.3); /* cyan-500/30 */
  box-shadow: 0 0 15px rgba(6, 182, 212, 0.4);
}
.concept-card:nth-child(2) {
  border-color: rgba(168, 85, 247, 0.3); /* purple-500/30 */
  box-shadow: 0 0 15px rgba(168, 85, 247, 0.4);
}
.concept-card:nth-child(3) {
  border-color: rgba(236, 72, 153, 0.3); /* pink-500/30 */
  box-shadow: 0 0 15px rgba(236, 72, 153, 0.4);
}

.concept-card:hover {
  box-shadow: 0 0 25px rgba(var(--neon-glow-rgb), 0.7);
}

.concept-card:nth-child(1):hover { --neon-glow-rgb: 6, 182, 212; } /* cyan */
.concept-card:nth-child(2):hover { --neon-glow-rgb: 168, 85, 247; } /* purple */
.concept-card:nth-child(3):hover { --neon-glow-rgb: 236, 72, 153; } /* pink */

.neon-button {
  box-shadow: 0 0 15px rgba(168, 85, 247, 0.7);
}

.neon-button:hover {
  box-shadow: 0 0 25px rgba(168, 85, 247, 1);
}
</style>