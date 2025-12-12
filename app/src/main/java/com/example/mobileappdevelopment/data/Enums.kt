package com.example.mobileappdevelopment.data


enum class UserRole {
    ADMIN, EMPLOYEE
}

enum class EmployeeStatus {
    ACTIVE, INACTIVE
}

enum class ReportCategory(val label: String) {
    HARASSMENT("Harassment"),
    DISCRIMINATION("Discrimination"),
    CORRUPTION("Corruption"),
    SAFETY("Safety Violation"),
    ETHICS("Ethics Violation"),
    OTHER("Other")
}

enum class ReportStatus(val label: String) {
    PENDING("Pending"),
    INVESTIGATING("Investigating"),
    RESOLVED("Resolved"),
    CLOSED("Closed")
}

//TODO Not used.
enum class ReportPriority(val label: String) {
    LOW("Low"),
    MEDIUM("Medium"),
    HIGH("High")
}