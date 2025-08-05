import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Scanner

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
        val error = att.isValidCheckIn(attendanceList)
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
        val att = Attendance(id, time)
        att.checkOut = time
        val error = att.isValidCheckOut(attendanceList)
        if (error.isNotEmpty()) {
            return "Check-out failed: $error"
        }
        val existing = attendanceList.findLast {
            it.employeeId == id &&
                    it.checkIn.toLocalDate() == time.toLocalDate() &&
                    it.checkOut == null
        }
        existing?.checkOut = time
        return "Checked out at ${time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))}"
    }

    fun printLog(date: LocalDate): String {
        val found = attendanceList.filter { it.checkIn.toLocalDate() == date }
        return if (found.isEmpty()) "No logs for $date"
        else found.joinToString("\n") { it.toString() }
    }

    fun updateEmployee(id: String, scanner: Scanner): String {
        val emp = employeeList.find { it.id == id } ?: return "Employee ID not found."

        print("Enter new First Name: ")
        val fn = scanner.nextLine()

        print("Enter new Last Name: ")
        val ln = scanner.nextLine()

        println("Select Role:")
        Role.entries.forEachIndexed { index, role -> println("${index + 1}. $role") }
        val rIdx = scanner.nextLine().toIntOrNull()?.minus(1)
        val role = rIdx?.let { Role.entries.getOrNull(it) } ?: return "Invalid role selected."

        println("Select Department:")
        Department.entries.forEachIndexed { index, dept -> println("${index + 1}. $dept") }
        val dIdx = scanner.nextLine().toIntOrNull()?.minus(1)
        val dept = dIdx?.let { Department.entries.getOrNull(it) } ?: return "Invalid department selected."

        print("Enter new Reporting Manager ID: ")
        val reportingTo = scanner.nextLine()

        emp.firstName = fn
        emp.lastName = ln
        emp.role = role
        emp.department = dept
        emp.reportingTo = reportingTo

        return "Employee updated successfully."
    }

    fun deleteEmployee(id: String): String {
        val emp = employeeList.find { it.id == id } ?: return "Employee ID not found."
        employeeList.remove(emp)
        return "Employee deleted successfully."
    }

    fun listCurrentlyCheckedInEmployees(): String {
        val pending = attendanceList.filter { it.checkOut == null }
        return if (pending.isEmpty()) "No employees are currently checked-in."
        else "Currently checked-in employees:\n" + pending.joinToString("\n") { it.toString() }
    }

    fun workingHoursSummary(startDate: LocalDate, endDate: LocalDate): String {
        if (startDate.isAfter(endDate)) {
            return "Start date cannot be after end date."
        }

        val filtered = attendanceList.filter {
            it.checkIn.toLocalDate() in startDate..endDate && it.checkOut != null
        }

        if (filtered.isEmpty()) {
            return "No attendance records found in this date range."
        }

        val summary = mutableMapOf<String, Long>()

        for (record in filtered) {
            val duration = java.time.Duration.between(record.checkIn, record.checkOut)
            summary[record.employeeId] = summary.getOrDefault(record.employeeId, 0L) + duration.toMinutes()
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

        messages.add("Sample employees preloaded.")
        return messages.joinToString("\n")
    }
}
