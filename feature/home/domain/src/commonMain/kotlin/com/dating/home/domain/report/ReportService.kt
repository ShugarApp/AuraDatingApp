package com.dating.home.domain.report

import com.dating.core.domain.util.DataError
import com.dating.core.domain.util.Result

interface ReportService {
    suspend fun reportUser(
        userId: String,
        reason: ReportReason,
        description: String?,
        // Módulo 2 — contextual evidence (RN-2.1)
        messageId: String? = null,
        matchId: String? = null,
        matchIntention: String? = null,
        category: String? = null
    ): Result<ReportResult, DataError.Remote>
}

enum class ReportReason {
    HARASSMENT,
    FAKE_PROFILE,
    INAPPROPRIATE_CONTENT,
    UNDERAGE,
    SPAM,
    SCAM,
    OTHER
}

data class ReportResult(
    val id: String,
    val message: String
)
