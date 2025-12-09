package com.example.mobileappdevelopment.data

// 앱이 켜져있는 동안만 유지되는 가짜 메모리 DB입니다.
object FakeDb {
    // 저장소 리스트
    val reports = mutableListOf<Report>()

    // ID 자동 생성을 위한 변수
    var nextId = 1000

    // 초기 더미 데이터 (관리자 화면이 썰렁하지 않게 미리 넣어둠)
    init {
        reports.add(
            Report(
                id = (nextId++).toString(),
                encryptedContent = "dummy_content",
                ipfsCid = "QmTestHash123",
                txHash = "0xTestTxHash...",
                title = "사내 비품 무단 반출 신고",
                description = "3층 탕비실에서 커피머신을 집에 가져가는 것을 목격했습니다.",
                category = ReportCategory.OTHER,
                department = "인사팀",
                date = "2025-12-01",
                status = ReportStatus.PENDING,
                priority = ReportPriority.HIGH
            )
        )
    }
}