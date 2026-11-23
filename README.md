# CostGuard

**CostGuard** is an **economic intelligence, profiling, distributed observability, and pre-deployment regression-testing layer for AI applications and agentic workloads**.

CostGuard helps engineering teams understand the financial, behavioral, and quality consequences of an AI/LLM change *before it reaches production*: what became more expensive, why, whether latency or task success rates regressed, and whether proposed modifications satisfy explicit economic policies.

---

## Key Pillars

### 1. Unified LLM Execution Engine
- **Multi-Provider Support**: Direct integration with OpenAI (`gpt-4o`, `gpt-4o-mini`), Google Gemini (`gemini-1.5-flash`), Anthropic (`claude-3-5-sonnet`), Groq (`llama-3.3-70b`), local Ollama (`llama3`), and OpenAI-compatible gateways (e.g., Bifrost).
- **Intelligent Contextual Fallback (`IntelligentOfflineSimulator`)**: Built-in offline simulator that enables complete local development, demonstration, and automated test execution without external paid API keys.
- **RAG Codebase & Policy Q&A**: Semantic document indexing and grounded LLM synthesis via `CostGuardRAGBot`.

### 2. Distributed Observability & Telemetry
- **OpenTelemetry Standard Tracing**: Full parent-child span hierarchy tracking complete task lifecycles: `workflow` $\rightarrow$ `retrieval` $\rightarrow$ `llm` $\rightarrow$ `evaluation`.
- **Granular Latency & Token Metrics**:
  - **Streaming TTFT (Time To First Token)** tracking.
  - **P50 / P90 / P95 Latency** profiling.
  - **Token Throughput (tokens/second)** and Prompt vs. Completion token distribution.
  - **Error and Regression Rates** across workflow executions.
- **Observability Dashboard (`/observability`)**:
  - Interactive telemetry KPI cards.
  - Hierarchical visual waterfall / Gantt latency flame bars for each trace.
  - Live interactive LLM sandbox.
  - One-click OpenTelemetry standard JSON export.

### 3. Economics of Successful Outcomes (Unit Economics)
Traditional AI monitoring measures raw cost per token or cost per API call, which fails to capture real workflow value. CostGuard pioneers **Outcome Economics**:
- **Cost per Task ($/Task)**: Total workload spend divided by total attempted requests.
- **Effective Cost per Successful Outcome (ECSO - $/Success)**:
  $$\text{ECSO} = \frac{\text{Total Workload Cost}}{\text{Successful Tasks}} = \frac{\text{Cost per Attempt}}{\text{Task Success Rate}}$$
  *Catches hidden economic regressions where a cheaper model fails more frequently, doubling the effective cost per completed outcome.*
- **Wasted Spend ($ & %)**: Quantifies capital lost to failed tasks, validation rejections, and unnecessary retries.
- **Economic Efficiency Score**: Successful business outcomes delivered per $100 budget.
- **CI/CD Regression Gates**: Automated policies enforcing caps on `max_candidate_cost_per_successful_task_usd`, `min_candidate_task_success_rate_percent`, and `max_candidate_wasted_spend_percent`.

---

## Quickstart

### 1. Installation
Clone the repository and install dependencies:

```bash
git clone https://github.com/NishchayVashishtha/CostGuard.git
cd CostGuard
pip install -r requirements.txt
```

### 2. Start the Local Web Server
Launch the FastAPI application with live hot-reloading:

```bash
python -m uvicorn main:app --host 127.0.0.1 --port 8000
```

Open in your browser:
- **Observability Dashboard**: [http://localhost:8000/observability](http://localhost:8000/observability)
- **Overview & Reports**: [http://localhost:8000/](http://localhost:8000/)
- **Interactive OpenAPI Docs**: [http://localhost:8000/docs](http://localhost:8000/docs)

---

## Live Demonstrations

CostGuard includes three ready-to-run demonstration scripts:

### A. Live RAG + LLM + Observability Demonstration
Demonstrates grounded semantic retrieval, live LLM generation, distributed tracing with TTFT, and context inflation profiling ($k=2$ vs $k=8$ chunks):

```bash
python demo_rag.py
```

### B. Mid-Semester Project Evaluation & Gating
Demonstrates baseline vs candidate workload comparison, quantitative unit economics, policy enforcement, and autonomous AI investigator root-cause diagnosis:

```bash
python demo_eval.py
```

### C. CI/CD Automated PR Review Bot Simulator
Emulates an automated GitHub Actions PR reviewer (Sweep.dev / CodiumAI style) that evaluates Pull Request economic impact and blocks merge on policy violations:

```bash
python simulate_pr_bot.py
```

---

## Running the Automated Test Suite

Run the complete test suite (39 unit and integration tests covering analysis, execution, RAG pipeline, LLMs, distributed tracing, and outcome economics):

```bash
python -m unittest discover -v
```

---

## Academic Research Paper & Methodology

CostGuard's theoretical foundations and empirical benchmarks are published in the accompanying research paper:

📄 **[CostGuard Research Paper: Pre-Deployment Economic Regression Testing, Distributed OpenTelemetry Profiling, and Effective Outcome Economics for Agentic AI Workloads](docs/RESEARCH_PAPER.md)**

Key contributions:
- Mathematical derivation of **Effective Cost per Successful Outcome (ECSO)** and **Wasted Capital**.
- Distributed parent-child OTel span hierarchy for multi-agent loops and compound AI systems.
- Benchmark analysis of context window inflation regressions ($k=2 \rightarrow k=8$).
- Multi-tier query caching architecture reducing dashboard response times from multi-second scans to **<15ms**.

---

## Cloud-Native Project Automation: Kubernetes & Red Hat OpenShift

CostGuard includes enterprise-ready declarative infrastructure manifests:

### A. Kubernetes Deployment
Located in [`k8s/`](k8s/):
- **`k8s/deployment.yaml`**: CostGuard API deployment with dual replicas, liveness/readiness probes, and PostgreSQL container.
- **`k8s/service.yaml` & `k8s/ingress.yaml`**: ClusterIP and NGINX ingress controller configuration.
- **`k8s/hpa.yaml`**: Horizontal Pod Autoscaler targeting 70% CPU and 80% Memory utilization.
- **`k8s/otel-collector.yaml`**: OpenTelemetry Collector sidecar with OTLP receivers and Prometheus exporter.

Deploy to any Kubernetes cluster:
```bash
kubectl apply -f k8s/namespace.yaml
kubectl apply -f k8s/
```

### B. Red Hat OpenShift Deployment Automation
Located in [`openshift/`](openshift/):
- **`openshift/buildconfig.yaml`**: OpenShift BuildConfig with automated Git Source-to-Image (S2I) / Docker build triggers.
- **`openshift/route.yaml`**: OpenShift Route with Edge TLS termination and automatic SSL certificate handling.
- **`openshift/deploy.sh`**: One-command automated deployment script via the `oc` CLI.

Deploy to OpenShift:
```bash
chmod +x openshift/deploy.sh
./openshift/deploy.sh
```

---

## OpenTelemetry Standard Integration

CostGuard natively supports OpenTelemetry specifications:
- **W3C Distributed Trace Context**: Automatically extracts and injects standard `traceparent` headers (`00-{trace_id}-{span_id}-01`) across all HTTP calls.
- **OTel Collector Sidecar**: Configured via `otel-collector-config.yaml` to stream traces to Jaeger, SigNoz, or Prometheus.
- **One-Click OTel JSON Export**: Available at `/api/v1/observability/otel`.

---

## Android Mobile Application

CostGuard features a dedicated native mobile application built with **Kotlin**, **Jetpack Compose**, and **Material 3**:

📱 **[CostGuard Android App Documentation](android/README.md)**

Features:
- **Real-Time Telemetry Dashboard**: Live KPI cards for P95 Latency, Streaming TTFT, Token Throughput, and ECSO.
- **Distributed Trace Explorer**: Inspect OpenTelemetry traces and span waterfall flame bars directly on mobile.
- **CI/CD Policy Regression Alerts**: Immediate notification feed when Pull Requests breach economic regression limits.
- **Dynamic Endpoint Configuration**: Connect to local dev server (`10.0.2.2:8001`), Wi-Fi IP, or production OpenShift Route.

---

## REST API Overview

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/health` | Service health check |
| `POST` | `/api/v1/llm/chat` | Execute chat completion with automated observability tracing |
| `GET` | `/api/v1/observability/traces` | List recent distributed traces and span hierarchies |
| `GET` | `/api/v1/observability/traces/{id}`| Retrieve detailed span waterfall for a specific trace |
| `GET` | `/api/v1/observability/metrics` | Retrieve aggregated latency, TTFT, token throughput & error rates |
| `GET` | `/api/v1/observability/otel` | Export traces in OpenTelemetry standard JSON format |
| `POST` | `/compare` | Stateless economic regression comparison for two workloads |
| `GET` | `/api/v1/comparisons` | List saved workload comparisons |
| `POST` | `/api/v1/scenarios` | Evaluate hypothetical repricing scenarios |

---

## Docker Compose (Optional Full Stack)

For containerized environments with PostgreSQL and the Bifrost gateway:

```bash
# Copy local configuration
cp .env.example .env

# Start demo profile
COSTGUARD_DEMO=1 docker compose --profile demo up -d --build
```

---

## License

This project is licensed under the Apache 2.0 License.
