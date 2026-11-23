package com.costguard.android.data.model

import com.google.gson.annotations.SerializedName

data class TelemetryMetrics(
    @SerializedName("total_traces") val totalTraces: Int = 0,
    @SerializedName("total_spans") val totalSpans: Int = 0,
    @SerializedName("mean_latency_ms") val meanLatencyMs: Double = 0.0,
    @SerializedName("p50_latency_ms") val p50LatencyMs: Double = 0.0,
    @SerializedName("p95_latency_ms") val p95LatencyMs: Double = 0.0,
    @SerializedName("avg_ttft_ms") val avgTtftMs: Double = 0.0,
    @SerializedName("total_prompt_tokens") val totalPromptTokens: Long = 0,
    @SerializedName("total_completion_tokens") val totalCompletionTokens: Long = 0,
    @SerializedName("total_tokens") val totalTokens: Long = 0,
    @SerializedName("tokens_per_second") val tokensPerSecond: Double = 0.0,
    @SerializedName("error_rate_percent") val errorRatePercent: Double = 0.0,
    @SerializedName("total_cost_usd") val totalCostUsd: Double = 0.0
)

data class HealthResponse(
    @SerializedName("status") val status: String = "ok"
)
