# SmartDoc-JP
SmartDoc-JP: AIを活用した次世代ドキュメント管理システム

概要 (Overview)
Spring Boot 3とGoogle Gemini APIを活用し、社内ドキュメントの自動要約・分類を実現するシステムです。 「2025年の崖」と呼ばれるDX（デジタルトランスフォーメーション）の課題解決を意識して開発しました。
该系统利用 Spring Boot 3 和 Google Gemini API 自动汇总和分类内部文档。其开发目的是为了解决被称为“2025 年悬崖”的数字化转型 (DX) 挑战。

技術スタック (Tech Stack)
言語: Java 21

フレームワーク: Spring Boot 4.0.1

データベース: MySQL 8.0

AI: Google Gemini 1.5 Flash (Spring AI)

管理ツール: Maven, Git

環境構築 (Setup)
リポジトリをクローン: git clone...

データベースを起動: docker-compose up -d

アプリケーションを実行: ./mvnw spring-boot:run
