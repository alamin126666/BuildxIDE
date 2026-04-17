# 🔨 Buildx IDE

A mobile Android IDE that compiles APKs using GitHub Actions. Develop Android apps directly from your phone!

## 📱 Features

- **Code Editor** - Syntax highlighting for Kotlin, Java, XML, JSON, and more
- **File Explorer** - Browse and manage your GitHub repository files
- **One-Click Build** - Trigger GitHub Actions builds and download APKs
- **GitHub Integration** - OAuth authentication and seamless sync
- **Multiple Themes** - Dark, Light, and AMOLED themes
- **Offline Support** - Cache files locally for offline editing

## 🏗️ Architecture

```
┌─────────────────────────────────────────────────────────┐
│                    BUILDX IDE APP                        │
│                                                          │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌────────┐ │
│  │  Code    │  │  File    │  │  Build   │  │ GitHub │ │
│  │  Editor  │  │  Manager │  │  Panel   │  │  Auth  │ │
│  └────┬─────┘  └────┬─────┘  └────┬─────┘  └───┬────┘ │
│       │              │              │              │      │
│  ┌────▼──────────────▼──────────────▼──────────────▼──┐ │
│  │              ViewModel Layer (MVVM)                  │ │
│  └────────────────────────┬───────────────────────────┘ │
│                           │                              │
│  ┌────────────────────────▼───────────────────────────┐ │
│  │              Repository Layer                        │ │
│  └────────────────────────┬───────────────────────────┘ │
└───────────────────────────┼─────────────────────────────┘
                            │
               ┌────────────▼────────────┐
               │    GitHub REST API      │
               │    GitHub Actions       │
               └─────────────────────────┘
```

## 🛠️ Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Kotlin |
| UI | Jetpack Compose |
| Architecture | MVVM + Clean Architecture |
| DI | Hilt |
| Network | Retrofit2 + OkHttp3 |
| Local DB | Room Database |
| Auth | GitHub OAuth 2.0 |
| Code Editor | Sora Editor |
| CI/CD | GitHub Actions |

## 🚀 Getting Started

### Prerequisites

- Android Studio Hedgehog (2023.1.1) or later
- JDK 17
- Android SDK 35

### Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/buildx-ide.git
   cd buildx-ide
   ```

2. **Create GitHub OAuth App**
   - Go to Settings → Developer settings → OAuth Apps → New OAuth App
   - Application name: `Buildx IDE`
   - Homepage URL: `https://github.com/yourusername/buildx-ide`
   - Authorization callback URL: `buildxide://oauth/callback`
   - Save the Client ID and Client Secret

3. **Configure credentials**
   ```bash
   # Create local.properties (DO NOT COMMIT THIS FILE)
   echo "GITHUB_CLIENT_ID=your_client_id" > local.properties
   echo "GITHUB_CLIENT_SECRET=your_client_secret" >> local.properties
   ```

4. **Build the project**
   ```bash
   ./gradlew assembleDebug
   ```

## 📋 Project Structure

```
BuildxIDE/
├── app/
│   ├── src/main/java/com/buildxide/
│   │   ├── BuildxApplication.kt
│   │   ├── di/                    # Hilt modules
│   │   ├── data/
│   │   │   ├── local/             # Room DB
│   │   │   ├── remote/            # API interfaces
│   │   │   └── repository/        # Repositories
│   │   ├── domain/
│   │   │   ├── model/             # Domain models
│   │   │   └── usecase/           # Use cases
│   │   ├── ui/
│   │   │   ├── navigation/        # Navigation graph
│   │   │   ├── screen/            # Screens
│   │   │   └── theme/             # Colors, Typography
│   │   └── util/                  # Utilities
│   └── src/main/res/              # Resources
├── .github/workflows/             # CI/CD workflows
└── build.gradle.kts
```

## 🔐 GitHub Actions Setup

For your projects to build APKs via GitHub Actions, add these secrets to your repository:

```
KEYSTORE_BASE64    → base64 encoded keystore file
KEY_ALIAS          → key alias name
KEY_PASSWORD       → key password
STORE_PASSWORD     → keystore password
```

Generate base64 keystore:
```bash
base64 -i my-keystore.jks | tr -d '\n'
```

## 🎨 Screenshots

| Splash | Login | Home |
|--------|-------|------|
| ![Splash](screenshots/splash.png) | ![Login](screenshots/login.png) | ![Home](screenshots/home.png) |

| IDE | Build | Settings |
|-----|-------|----------|
| ![IDE](screenshots/ide.png) | ![Build](screenshots/build.png) | ![Settings](screenshots/settings.png) |

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

```
MIT License

Copyright (c) 2024 Buildx IDE

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

## 🙏 Acknowledgments

- [Sora Editor](https://github.com/Rosemoe/sora-editor) - Code editor component
- [GitHub API](https://docs.github.com/en/rest) - GitHub integration
- [Jetpack Compose](https://developer.android.com/jetpack/compose) - UI framework

---

<p align="center">
  Made with ❤️ for mobile developers
</p>
