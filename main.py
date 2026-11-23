import time
from fastapi import FastAPI, Request
from fastapi.responses import Response

from costguard import ComparisonReport, ComparisonRequest, compare_workloads
from costguard.api import router
from costguard.observability import extract_w3c_trace_context, inject_w3c_trace_context, tracer
from costguard.web import router as web_router

app = FastAPI(
    title="CostGuard API",
    description="Economic regression analysis and OpenTelemetry observability for AI workloads",
)


@app.middleware("http")
async def otel_tracing_middleware(request: Request, call_next):
    # Extract distributed W3C TraceContext if forwarded from upstream gateway/ingress
    trace_id, parent_id = extract_w3c_trace_context(dict(request.headers))
    path = request.url.path

    # Track requests with OpenTelemetry spans
    with tracer.start_span(f"http_{request.method}_{path}", kind="workflow") as span:
        span.set_attribute("http.method", request.method)
        span.set_attribute("http.url", str(request.url))
        span.set_attribute("http.route", path)
        if parent_id:
            span.set_attribute("otel.parent_span_id", parent_id)

        try:
            response = await call_next(request)
            span.set_attribute("http.status_code", response.status_code)
            # Inject W3C traceparent header for client/gateway context propagation
            w3c_headers = inject_w3c_trace_context(span.trace_id, span.span_id)
            for k, v in w3c_headers.items():
                response.headers[k] = v
            return response
        except Exception as exc:
            span.finish(status="error", error=str(exc))
            raise exc


app.include_router(router)
app.include_router(web_router)


@app.get("/health")
def health() -> dict[str, str]:
    return {"status": "ok"}


@app.post("/compare", response_model=ComparisonReport)
def compare(request: ComparisonRequest) -> ComparisonReport:
    return compare_workloads(request)
