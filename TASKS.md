# 開発タスクリスト

## フェーズ1: 環境構築と基本設定

### インフラ (Infrastructure)
- [x] `docker-compose.yml`を作成し、MySQLとMinIOのサービスを定義する。

### バックエンド (Backend)
- [x] `build.gradle.kts`にMinIOクライアントライブラリの依存関係を追加する。
- [x] `application.properties`に、Dockerで起動するMySQLとMinIOへの接続設定を記述する。
- [x] jOOQのコード生成タスクを設定する。

### フロントエンド (Frontend)
- [x] `frontend`ディレクトリで `npm install` を実行して、依存関係をインストールする。

## フェーズ2: 認証機能の実装

### バックエンド (Backend)
- [x] `users`テーブルのマイグレーションファイルを作成する。（V1__Initial_schema.sqlとして全テーブル定義済み）
- [ ] jOOQコードを生成する。
- [ ] ユーザー登録API (`POST /api/users/register`) を実装する。
- [ ] Spring SecurityとJWTを設定し、ログインAPI (`POST /api/auth/login`) を実装する。

### フロントエンド (Frontend)
- [ ] 登録ページ、ログインページを作成する。
- [ ] APIを呼び出して、ユーザー登録・ログイン機能を使えるようにする。
- [ ] ログイン後の認証状態をPiniaで管理する。

## フェーズ3: Flick投稿・表示機能

### バックエンド (Backend)
- [ ] `flicks`テーブルなどのマイグレーションファイルを作成する。
- [ ] jOOQコードを再生成する。
- [ ] Flick投稿API (`POST /api/flicks`) を実装する（ファイルアップロード含む）。
- [ ] Flickフィード取得API (`GET /api/feed`) を実装する。

### フロントエンド (Frontend)
- [ ] Flick投稿フォームを作成する（テキスト、画像、動画対応）。
- [ ] フィードページを作成し、取得したFlickを表示する。