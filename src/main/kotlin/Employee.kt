enum class Role {
    HR, ENGINEER, MANAGER, SALES, DEVELOPER, INTERN
}

enum class Department {
    ADMIN, TECH, FINANCE, SUPPORT, ENGINEERING, HR, SALES, MARKETING
}

class Employee(
    var firstName: String,
    var lastName: String,
    var role: Role,
    var department: Department,
    var reportingTo: String
) {
    var id: String = ""
    companion object {
        private var counter = 2
    }
    fun validate(): Boolean {
        val isValid= firstName.isNotBlank() &&
                lastName.isNotBlank() &&
                (reportingTo == "0" || Regex("^E\\d{3}$").matches(reportingTo))
        if(isValid && id.isEmpty()){
            id="E" + counter++.toString().padStart(3, '0')
        }
        return isValid
    }

    fun getValidationError(): String {
        if (firstName.isBlank()) return "First name cannot be blank"
        if (lastName.isBlank()) return "Last name cannot be blank"
        if (reportingTo != "0" && !Regex("^E\\d{3}$").matches(reportingTo)) {
            return "ReportingTo must be 0 or a valid employee ID (E001 format)"
        }
        return ""
    }

    override fun toString(): String {
        return "ID: $id  Name: $firstName $lastName  Role: $role  Dept: $department  Reports To: $reportingTo"
    }
}