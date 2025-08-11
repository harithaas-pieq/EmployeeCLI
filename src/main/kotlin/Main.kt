import java.time.DateTimeException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Scanner

fun inputEmployeeDetails(scanner: Scanner, manager: EmployeeManager) {
    print("Enter First Name: ")
    val firstname = scanner.nextLine().trim()
    if (firstname.isEmpty()) {
        println("First name cannot be empty.")
        return
    }
    //error validation not to be done here
    print("Enter Last Name: ")
    val lastname = scanner.nextLine().trim()
    if (lastname.isEmpty()) {
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
    val department = Department.entries.getOrNull(dIdx)
    if (department == null) {
        println("Invalid department.")
        return
    }

    print("Enter ReportingTo ID (or 0): ")
    val reportingTo = scanner.nextLine().trim()

    println(manager.addEmployee(firstname, lastname, role, department, reportingTo))
}

fun handleCheckIn(scanner: Scanner, manager: EmployeeManager) {
    print("Enter Employee ID: ")
    val id = scanner.nextLine().trim()
    if (id.isEmpty()) {
        println("Employee ID cannot be empty.")
        return
    }

    print("Enter Check-in DateTime (yyyy-MM-dd HH:mm) or press Enter for current: ")
    val dtStr = scanner.nextLine().trim()
    val dt = if (dtStr.isEmpty()) {
        LocalDateTime.now()
    } else {
        try {
            LocalDateTime.parse(dtStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
        } catch (_: DateTimeException) {
            println("Invalid datetime format.")
            return
        }
    }
    println(manager.checkIn(id, dt))
}

fun handleDeleteAttendanceLog(scanner: Scanner, manager: EmployeeManager) {
    print("Enter Employee ID: ")
    val id = scanner.nextLine().trim()

    print("Enter Check-in DateTime of the log to delete (yyyy-MM-dd HH:mm): ")
    val dtStr = scanner.nextLine().trim()
    val checkIn = try {
        LocalDateTime.parse(dtStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
    } catch (_: DateTimeException) {
        println("Invalid datetime format.")
        return
    }

    val result = manager.deleteAttendanceLog(id,checkIn)
    println(result)
}

fun handleCheckOut(scanner: Scanner, manager: EmployeeManager) {
    print("Enter Employee ID: ")
    val id = scanner.nextLine().trim()
    if (id.isEmpty()) {
        println("Employee ID cannot be empty.")
        return
    }

    print("Enter Check-out DateTime (yyyy-MM-dd HH:mm) or press Enter for current: ")
    val dtStr = scanner.nextLine().trim()
    val dt = if (dtStr.isEmpty()) {
        LocalDateTime.now()
    } else {
        try {
            LocalDateTime.parse(dtStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
        } catch (_: DateTimeException) {
            println("Invalid datetime format.")
            return
        }
    }
    println(manager.checkOut(id, dt))
}

fun printAttendanceLog(scanner: Scanner, manager: EmployeeManager) {
    print("Enter date to view log (yyyy-MM-dd): ")
    val dateStr = scanner.nextLine().trim()
    val date = if (dateStr.isEmpty()) {
        LocalDate.now()
    } else {
        try {
            LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        } catch (_: DateTimeException) {
            println("Invalid date.")
            return
        }
    }
    println(manager.printLog(date))
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
             6. Delete Employees
             7. List of Employees Currently CheckedIn 
             8. Working Hours Summary
             9. Delete Attendance Log
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
            "6" -> handleDeleteEmployee(scanner, manager)
            "7" -> handleListCurrentlyCheckedIn(manager)
            "8" -> showWorkingHoursSummary(scanner, manager)
            "9" -> handleDeleteAttendanceLog(scanner, manager)
            else -> println("Invalid choice.")
        }
    }
}