# AI Smart Document Management System (AI-SDMS)

## 📖 概要 (Project Overview)
**「ドキュメント管理を、もっとスマートに。」**
Spring Boot 3とGoogle Gemini APIを活用し、 単なるファイルの保存・削除にとどまらず、アップロードされた技術文書の内容を AI が自動的に解析し、要約を生成することで、情報の検索性と管理効率を劇的に向上させます。 「2025年の崖」と呼ばれるDX（デジタルトランスフォーメーション）の課題解決を意識して開発しました。


日本のIT現場における「ドキュメント整理の繁雑さ」という課題を解決するために開発しました。

> **開発期間**: 約20日間
> **担当**: バックエンド設計・開発、API連携、テスト実装（個人開発）

---

## 🛠 技術スタック (Tech Stack)
最新のLTSバージョンを採用し、保守性とパフォーマンスを意識した選定を行いました。

- **Language**: Java 21
- **Framework**: Spring Boot 3.4.1
- **AI Integration**: Google Gemini 2.5-flash (Spring AI)
- **Build Tool**: Maven
- **Database**: MySQL 8.0
- **Version Control**: Git / GitHub

---

## 🏗 アーキテクチャ (Architecture)
保守性を高めるため、責任の分離（Separation of Concerns）を意識したレイヤードアーキテクチャを採用しています。

```mermaid
graph TD
    Client[Client / API Tester] -->|HTTP Request| Controller
    subgraph "Application Layer"
        Controller -->|DTO| Service
        Service -->|Validation/Logic| Repository
        Service -->|Prompt Engineering| GeminiService[Gemini AI Service]
    end
    subgraph "Infrastructure"
        Repository -->|JPA| Database[(Database)]
        GeminiService -->|REST| GoogleAI[Google Gemini API]
    end
    %% AOP Logic Visualization
    AOP[AOP Logging & Exception Handler] -.->|Cross-Cutting| Controller
    AOP -.->|Cross-Cutting| Service
## 環境構築 (Setup)
リポジトリをクローン: git clone...

データベースを起動: docker-compose up -d

アプリケーションを実行: ./mvnw spring-boot:run

sequenceDiagram
actor User
participant View as Thymeleaf Interface
participant Ctrl as ChatController
participant Svc as GeminiService
participant DB as MySQL Database
participant AI as Google Gemini API

    User->>View: 输入提示词 (Prompt)
    View->>Ctrl: POST /api/chat
    Ctrl->>Svc: processPrompt(text)
    
    rect rgb(240, 248, 255)
        note right of Svc: 外部 AI 调用
        Svc->>AI: 发送 HTTP 请求
        activate AI
        AI-->>Svc: 返回生成内容 (JSON)
        deactivate AI
    end

    Svc->>DB: save(ChatRecord)
    DB-->>Svc: Acknowledge
    Svc-->>Ctrl: 返回完整响应对象
    Ctrl-->>View: 更新 Model 并返回视图
    View-->>User: 渲染 AI 回复
