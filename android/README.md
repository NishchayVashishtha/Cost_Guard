# CostGuard Android Mobile Application

The **CostGuard Android Mobile Application** provides real-time mobile observability, unit economics tracking, OpenTelemetry distributed trace inspection, and CI/CD policy regression alert monitoring on Android devices.

Developed for **AI DevOps (CSE 4011)** by **Nishchay Vashishtha**.

---

## Key Mobile Features

1. **Live Observability & Unit Economics Dashboard**:
   - Real-time KPI cards: **P95 Latency**, **Time to First Token (TTFT)**, **Token Throughput (tok/s)**, and **Regression Rate (%)**.
   - **Effective Cost per Successful Outcome (ECSO)** and total workload spend tracking.
   - Real-time backend connectivity status indicator.

2. **Distributed OpenTelemetry Traces Explorer**:
   - Scrollable list of OTel traces with status badges (`OK` / `ERROR`), duration, token count, and cost.
   - Trace detail screen with **hierarchical span waterfalls** and visual latency flame bars (`workflow`, `retrieval`, `llm`, `evaluation`).

3. **CI/CD Policy Regression Alerts**:
   - Real-time push / alert feed notifying engineers when candidate Pull Requests breach configured economic policies (e.g. context inflation, $>25\%$ cost explosion).

4. **Dynamic Endpoint Settings**:
   - Configure backend URL dynamically (e.g., `http://10.0.2.2:8001` for Android Emulator, `http://192.168.1.X:8001` for physical device over Wi-Fi, or production OpenShift Route URL `https://costguard.apps.cluster.openshift.com`).

---

## Technical Stack

- **Language**: Kotlin 1.9.23
- **UI Framework**: Android Jetpack Compose + Material 3 Design
- **Architecture**: Clean Architecture + MVVM (Model-View-ViewModel)
- **Async & State**: Kotlin Coroutines & `StateFlow`
- **Networking**: Retrofit 2.11.0 + OkHttp 4.12.0 + Gson
- **Navigation**: Jetpack Navigation Compose

---

## Building & Running the App

### Option A: Via Android Studio
1. Open Android Studio.
2. Select **File > Open** and choose the `android/` directory in this repository.
3. Allow Gradle to sync.
4. Select an Android Emulator (API 34 / Android 14) or connect a physical Android device with USB debugging.
5. Click **Run > Run 'app'** (`Shift + F10`).

### Option B: Via Command Line (Gradle)
```bash
cd android
./gradlew assembleDebug
```
The compiled APK will be generated at:
```
android/app/build/outputs/apk/debug/app-debug.apk
```
Install on connected device:
```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

---

## Connecting to CostGuard Backend

Ensure the CostGuard backend is running:
```bash
python -m uvicorn main:app --host 0.0.0.0 --port 8001
```

- **Android Emulator**: In the app settings screen, set URL to: `http://10.0.2.2:8001/`
- **Physical Device**: Set URL to your computer's local Wi-Fi IP address: `http://<YOUR_LAN_IP>:8001/`
- **OpenShift Cluster**: Set URL to your OpenShift route: `https://<YOUR_OPENSHIFT_ROUTE_URL>/`
