import java.time.DateTimeException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Scanner

fun inputEmployeeDetails(scanner: Scanner, manager: EmployeeManager) {
    print("Enter First Name: ")
    val fn = scanner.nextLine().trim()
    if (fn.isEmpty()) {
        println("First name cannot be empty.")
        return
    }

    print("Enter Last Name: ")
    val ln = scanner.nextLine().trim()
    if (ln.isEmpty()) {
        println("Last name cannot be empty.")
        return
    }

    println("Choose Role:")
    Role.entries.forEachIndexed { i, v -> println("${i + 1}. $v") }
    val rIdx = scanner.nextLine().trim().toIntOrNull()?.minus(1) ?: -1
    val role = Role.entries.getOrNull(rIdx)
    if (role == null) {
        println("Invalid role.")
        return
    }

    println("Choose Department:")
    Department.entries.forEachIndexed { i, v -> println("${i + 1}. $v") }
    val dIdx = scanner.nextLine().trim().toIntOrNull()?.minus(1) ?: -1
    val dept = Department.entries.getOrNull(dIdx)
    if (dept == null) {
        println("Invalid department.")
        return
    }

    print("Enter ReportingTo ID (or 0): ")
    val reportingTo = scanner.nextLine().trim()

    println(manager.addEmployee(fn, ln, role, dept, reportingTo))
}

fun handleCheckIn(scanner: Scanner, manager: EmployeeManager) {
    print("Enter Employee ID: ")
    val id = scanner.nextLine().trim()
    if (id.isEmpty()) {
        println("Employee ID cannot be empty.")
        return
    }

    print("Enter Check-in DateTime (yyyy-MM-dd HH:mm): ")
    val dtStr = scanner.nextLine().trim()
    try {
        val dt = LocalDateTime.parse(dtStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
        println(manager.checkIn(id, dt))
    } catch (_: DateTimeException) {
        println("Invalid datetime format.")
    }
}

fun handleCheckOut(scanner: Scanner, manager: EmployeeManager) {
    print("Enter Employee ID: ")
    val id = scanner.nextLine().trim()
    if (id.isEmpty()) {
        println("Employee ID cannot be empty.")
        return
    }

    print("Enter Check-out DateTime (yyyy-MM-dd HH:mm): ")
    val dtStr = scanner.nextLine().trim()
    try {
        val dt = LocalDateTime.parse(dtStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
        println(manager.checkOut(id, dt))
    } catch (_: DateTimeException) {
        println("Invalid datetime format.")
    }
}

fun printAttendanceLog(scanner: Scanner, manager: EmployeeManager) {
    print("Enter date to view log (yyyy-MM-dd): ")
    val dateStr = scanner.nextLine().trim()
    try {
        val date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        println(manager.printLog(date))
    } catch (_: DateTimeException) {
        println("Invalid date.")
    }
}

fun showWorkingHoursSummary(scanner: Scanner, manager: EmployeeManager) {
    print("Enter Start Date (yyyy-MM-dd): ")
    val startStr = scanner.nextLine().trim()
    print("Enter End Date (yyyy-MM-dd): ")
    val endStr = scanner.nextLine().trim()
    try {
        val start = LocalDate.parse(startStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        val end = LocalDate.parse(endStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        println(manager.workingHoursSummary(start, end))
    } catch (_: DateTimeException) {
        println("Invalid date format.")
    }
}

fun handleUpdateEmployee(scanner: Scanner, manager: EmployeeManager) {
    print("Enter Employee ID to update: ")
    val id = scanner.nextLine().trim()
    if (id.isEmpty()) {
        println("Employee ID cannot be empty.")
        return
    }
    println(manager.updateEmployee(id, scanner))
}

fun handleDeleteEmployee(scanner: Scanner, manager: EmployeeManager) {
    print("Enter Employee ID to delete: ")
    val id = scanner.nextLine().trim()
    if (id.isEmpty()) {
        println("Employee ID cannot be empty.")
        return
    }
    println(manager.deleteEmployee(id))
}

fun handleListCurrentlyCheckedIn(manager: EmployeeManager) {
    println("Employees Currently Checked-in without Check-out:")
    println(manager.listCurrentlyCheckedInEmployees())
}

fun main() {
    val manager = EmployeeManager()
    val scanner = Scanner(System.`in`)
    println(manager.preloadSampleEmployees())
    while (true) {
        println(
            """
                Employee Attendance System
             1. Add Employee
             2. List Employees
             3. Check-in
             4. Check-out
             5. Print Attendance Log
             6. Update Employees
             7. Delete Employees
             8. List of Employees Currently CheckedIn 
             9. Working Hours Summary
            10. Exit
        """.trimMargin()
        )
        print("Choose option: ")
        when (scanner.nextLine().trim()) {
            "1" -> inputEmployeeDetails(scanner, manager)
            "2" -> println(manager.listEmployees())
            "3" -> handleCheckIn(scanner, manager)
            "4" -> handleCheckOut(scanner, manager)
            "5" -> printAttendanceLog(scanner, manager)
            "6" -> handleUpdateEmployee(scanner, manager)
            "7" -> handleDeleteEmployee(scanner, manager)
            "8" -> handleListCurrentlyCheckedIn(manager)
            "9" -> showWorkingHoursSummary(scanner, manager)
            "10" -> break
            else -> println("Invalid choice.")
        }
    }
}