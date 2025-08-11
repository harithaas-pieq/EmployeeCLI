import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.Duration

class EmployeeManager {
    private val employeeList = EmployeeList()
    private val attendanceList = AttendanceList()

    fun addEmployee(
        firstName: String,
        lastName: String,
        role: Role,
        department: Department,
        reportingTo: String
    ): String {
        val emp = Employee(firstName, lastName, role, department, reportingTo)
        if (!emp.validate()) {
            return emp.getValidationError()
        }
        employeeList.add(emp)
        return "Employee added: ${emp.id}"
    }

    fun listEmployees(): String {
        return if (employeeList.isEmpty()) "No employees found."
        else employeeList.joinToString("\n") { it.toString() }
    }

    fun checkIn(id: String, time: LocalDateTime): String {
        if (employeeList.none { it.id == id }) {
            return "Invalid employee ID."
        }
        val att = Attendance(id, time)
        val error = attendanceList.isValidCheckIn(att)
        return if (error.isNotEmpty()) error
        else {
            attendanceList.add(att)
            "Checked in at ${time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))}"
        }
    }

    fun checkOut(id: String, time: LocalDateTime): String {
        if (employeeList.none { it.id == id }) {
            return "Invalid employee ID."
        }

        val (error, attendance) = attendanceList.validateCheckOut(id, time)
        if (error.isNotEmpty()) {
            return "Check-out failed: $error"
        }

        return attendance!!.checkout(time)
    }

    fun printLog(date: LocalDate): String {
        val found = attendanceList.filter { it.checkIn.toLocalDate() == date }
        return if (found.isEmpty()) "No logs for $date"
        else found.joinToString("\n") { it.toString() }
    }

    fun deleteEmployee(id: String): String {
        return if (employeeList.delete(id)) "Employee deleted."
        else "Employee not found."
    }

    fun deleteAttendanceLog(employeeId: String, checkIn: LocalDateTime): String {
        val log = attendanceList.find {
            it.employeeId == employeeId && it.checkIn == checkIn
        } ?: return "No matching attendance log found."

        return if (attendanceList.delete(log)) {
            "Attendance log deleted successfully."
        } else {
            "Failed to delete attendance log."
        }
    }

    fun listCurrentlyCheckedInEmployees(): String {
        val pending = attendanceList.filter { it.checkOut == null }
        return if (pending.isEmpty()) "No employees are currently checked-in."
        else "Currently checked-in employees:\n" + pending.joinToString("\n") { it.toString() }
    }

    fun workingHoursSummary(startDate: LocalDate, endDate: LocalDate): String {
        val summary = attendanceList.getWorkingHoursSummary(startDate, endDate)
        if (summary.isEmpty()) {
            return "No attendance records found in this date range."
        }
        val result = StringBuilder("Working Hours Summary from $startDate to $endDate:\n")
        summary.forEach { (id, totalMinutes) ->
            val emp = employeeList.find { it.id == id }
            val hours = totalMinutes / 60
            val mins = totalMinutes % 60
            val name = emp?.let { "${it.firstName} ${it.lastName}" } ?: "Unknown"
            result.append("ID: $id | Name: $name | Worked: ${hours}h ${mins}m\n")
        }
        return result.toString().trim()
    }

    fun preloadSampleEmployees(): String {
        val messages = mutableListOf<String>()
        val admin = Employee("Admin", "User", Role.MANAGER, Department.ADMIN, "0")
        admin.id = "E001"
        if (admin.validate()) {
            employeeList.add(admin)
            messages.add("Admin created with ID: ${admin.id}")
        }

        val sampleEmployees = listOf(
            Employee("Emma", "Watson", Role.DEVELOPER, Department.ENGINEERING, "E001"),
            Employee("John", "Doe", Role.HR, Department.HR, "E001"),
            Employee("Ravi", "P", Role.MANAGER, Department.SALES, "E001"),
            Employee("Divya", "S", Role.INTERN, Department.MARKETING, "E001"),
            Employee("John", "Smith", Role.DEVELOPER, Department.ENGINEERING, "E001")
        )

        for (emp in sampleEmployees) {
            if (emp.validate()) {
                employeeList.add(emp)
            } else {
                messages.add("Error in preloaded employee: ${emp.getValidationError()}")
            }
        }

        return messages.joinToString("\n")
    }
}
