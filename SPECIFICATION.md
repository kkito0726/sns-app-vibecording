# SNS アプリケーション「Flick」基本仕様書

## 1. 概要

「Flick」は、ユーザーが日常のきらめきや感情の動きを「Flick」として素早く捉え、共有するためのソーシャルネットワーキングサービスである。テキスト、画像、短い動画など、様々なフォーマットで気軽に投稿・共有する体験を提供する。

## 2. コンセプト

- **多様な Flick の共有:** テキストのみのつぶやき、お気に入りの写真、その場の空気感を伝える短い動画など、形式にとらわれず気軽に投稿・共有する。
- **感覚的なコミュニケーション:** 「いいね」やコメントに加え、多様な Flick を通じた感覚的なつながりを重視する。
- **シンプルな UI/UX:** ユーザーが直感的に操作でき、どんな形式のコンテンツでも気軽に投稿・閲覧できるシンプルなインターフェースを目指す。

## 3. 機能要件

### 3.1. ユーザー管理機能

- **サインアップ（ユーザー登録）:** メールアドレス、ユーザー名、パスワードで登録できる。
- **サインイン（ログイン）:** 登録したメールアドレスとパスワードでログインできる。
- **サインアウト（ログアウト）:** アプリケーションからログアウトできる。
- **プロフィール編集:** プロフィール画像、自己紹介文などを編集できる。

### 3.2. 投稿 (Flick) 機能

- **Flick 作成・投稿:**
  - 以下の形式のコンテンツを投稿できる。
    - テキストのみ
    - 画像（＋任意でテキスト）
    - 短い動画（＋任意でテキスト）
- **Flick 閲覧:** 投稿された Flick を閲覧できる。
- **Flick 削除:** 自分が投稿した Flick を削除できる。

### 3.3. ソーシャル機能

- **フォロー:** 他のユーザーをフォローできる。
- **いいね:** 他のユーザーの Flick に「いいね」を付けられる。
- **コメント:** 他のユーザーの Flick にコメントを投稿できる。

### 3.4. フィード機能

- **フォローフィード:** フォローしているユーザーの Flick が時系列で表示される。

### 3.5. プロフィール機能

- ユーザーのプロフィールページ。
- プロフィール情報（アイコン、ユーザー名、自己紹介）を表示する。
- そのユーザーが投稿した Flick の一覧をグリッド形式で表示する。
- フォロー数・フォロワー数を表示する。

### 3.6. 検索機能

- ユーザー名で他のユーザーを検索できる。

## 4. 非機能要件

- **パフォーマンス:** API レスポンスは平均 500ms 以内を目指す。メディアのアップロード・ストリーミングは遅延なくスムーズに行われること。
- **セキュリティ:**
  - パスワードはハッシュ化して保存する。
  - API 通信はすべて HTTPS で行う。
  - SQL インジェクション、XSS などの基本的な脆弱性対策を講じる。
- **スケーラビリティ:** 将来的なユーザー増に対応できるよう、ステートレスなバックエンド設計とし、必要に応じてスケールアウト可能な構成を考慮する。
- **使用技術:**
  - **バックエンド:** Kotlin, Spring Boot
  - **フロントエンド:** Vue.js, TypeScript, Vite
  - **データベース:** MySQL
  - **インフラ:** Docker コンテナでの開発・デプロイを想定

## 5. システム構成案

- **フロントエンド (Vite + Vue + TypeScript):**
  - `Vue Router`による画面遷移管理
  - `Pinia`による状態管理
  - `Axios`等を用いたバックエンド API との非同期通信
- **バックエンド (Kotlin + Spring Boot):**
  - RESTful API の提供
  - `jOOQ` を用いた DB アクセス
  - `Spring Security` を用いた認証・認可
  - メディアファイルの保存先は `MinIO` を使用

## 6. API 設計 (概要)

| Method | Endpoint                   | 説明                                                       |
| ------ | -------------------------- | ---------------------------------------------------------- |
| POST   | /api/users/register        | ユーザー登録                                               |
| POST   | /api/auth/login            | ログイン                                                   |
| GET    | /api/users/me              | 自分のプロフィール取得                                     |
| PUT    | /api/users/me              | 自分のプロフィール更新                                     |
| GET    | /api/users/{userid}        | 特定ユーザーのプロフィール取得                             |
| POST   | /api/users/{userId}/follow | ユーザーをフォロー                                         |
| DELETE | /api/users/{userId}/follow | ユーザーのフォロー解除                                     |
| POST   | /api/flicks                | Flick を投稿。投稿タイプに応じて異なるボディを受け付ける。 |
| GET    | /api/flicks/{id}           | 特定の Flick を取得                                        |
| DELETE | /api/flicks/{id}           | Flick を削除                                               |
| GET    | /api/feed                  | フォロー中のユーザーの Flick フィードを取得                |
| POST   | /api/flicks/{id}/like      | Flick に「いいね」する                                     |
| DELETE | /api/flicks/{id}/like      | Flick の「いいね」を取り消す                               |
| POST   | /api/flicks/{id}/comments  | Flick にコメントする                                       |
| GET    | /api/flicks/{id}/comments  | Flick のコメント一覧を取得                                 |

## 7. データベース設計 (概要)

- **users**
  - `id` (PK)
  - `username` (UNIQUE)
  - `email` (UNIQUE)
  - `password_hash`
  - `profile_image_url`
  - `bio`
  - `created_at`
- **flicks**
  - `id` (PK)
  - `user_id` (FK to users.id)
  - `post_type` (TEXT, IMAGE, VIDEO など)
  - `text_content` (テキスト投稿の本文、または画像・動画のキャプション)
  - `image_url` (NULLABLE)
  - `video_url` (NULLABLE)
  - `created_at`
- **follows**
  - `follower_id` (FK to users.id)
  - `following_id` (FK to users.id)
- **likes**
  - `user_id` (FK to users.id)
  - `flick_id` (FK to flicks.id)
- **comments**
  - `id` (PK)
  - `user_id` (FK to users.id)
  - `flick_id` (FK to flicks.id)
  - `text`
  - `created_at`
