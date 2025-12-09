package com.example.mobileappdevelopment.data

data class Report(
    val id: String,
    val encryptedContent: String? = null,
    val ipfsCid: String? = null,
    val txHash: String? = null,
    val category: ReportCategory = ReportCategory.OTHER,
    val title: String = "암호화된 제보",
    val description: String = encryptedContent ?: "내용을 확인하려면 복호화가 필요합니다.",
    val department: String = "",
    val date: String = "",
    val submittedAt: String = "",
    val status: ReportStatus = ReportStatus.PENDING,
    val priority: ReportPriority = ReportPriority.MEDIUM,
    val notes: String = ""
)

data class ReportRequest(
    val encryptedContent: String,
    val zkProof: String,
    val nullifierHash: String,
    val root: String,
    val ipfsCid: String?,
    val txHash: String?
)