package com.costguard.android.data.model

import com.google.gson.annotations.SerializedName

data class TraceItem(
    @SerializedName("trace_id") val traceId: String,
    @SerializedName("name") val name: String,
    @SerializedName("latency_ms") val latencyMs: Double,
    @SerializedName("status") val status: String,
    @SerializedName("span_count") val spanCount: Int,
    @SerializedName("total_tokens") val totalTokens: Long,
    @SerializedName("total_cost_usd") val totalCostUsd: Double,
    @SerializedName("spans") val spans: List<SpanItem> = emptyList()
)

data class SpanItem(
    @SerializedName("span_id") val spanId: String,
    @SerializedName("trace_id") val traceId: String,
    @SerializedName("parent_span_id") val parentSpanId: String?,
    @SerializedName("name") val name: String,
    @SerializedName("kind") val kind: String,
    @SerializedName("latency_ms") val latencyMs: Double,
    @SerializedName("status") val status: String,
    @SerializedName("attributes") val attributes: Map<String, Any> = emptyMap()
)

data class PolicyAlert(
    val title: String,
    val prNumber: String,
    val severity: String,
    val message: String,
    val deltaCostPercent: Double,
    val timestamp: String
)
