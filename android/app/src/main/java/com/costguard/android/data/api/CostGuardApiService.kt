package com.costguard.android.data.api

import com.costguard.android.data.model.HealthResponse
import com.costguard.android.data.model.TelemetryMetrics
import com.costguard.android.data.model.TraceItem
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CostGuardApiService {

    @GET("health")
    async suspend fun getHealth(): HealthResponse

    @GET("api/v1/observability/metrics")
    suspend fun getTelemetryMetrics(): TelemetryMetrics

    @GET("api/v1/observability/traces")
    suspend fun getTraces(
        @Query("limit") limit: Int = 25,
        @Query("status") status: String? = null
    ): List<TraceItem>

    @GET("api/v1/observability/traces/{trace_id}")
    suspend fun getTraceDetail(
        @Path("trace_id") traceId: String
    ): TraceItem
}
