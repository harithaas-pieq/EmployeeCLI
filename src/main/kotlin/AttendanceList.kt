import java.time.LocalDateTime
import java.time.LocalDate

class AttendanceList : ArrayList<Attendance>() {

    fun isValidCheckIn(newAttendance: Attendance): String {
        val today = newAttendance.checkIn.toLocalDate()
        val alreadyCheckedIn = this.any {
            it.employeeId == newAttendance.employeeId &&
                    it.checkIn.toLocalDate() == today &&
                    it.checkOut == null
        }

        return if (alreadyCheckedIn) {
            "Employee '${newAttendance.employeeId}' is already checked in for today."
        } else {
            ""
        }
    }

    fun validateCheckOut(employeeId: String, checkOutTime: LocalDateTime): Pair<String, Attendance?> {
        val match = this.findLast {
            it.employeeId == employeeId &&
                    it.checkIn.toLocalDate() == checkOutTime.toLocalDate() &&
                    it.checkOut == null
        } ?: return "No valid check-in found for employee '$employeeId' on this date." to null

        if (checkOutTime <= match.checkIn) {
            return "Check-out cannot be before or same as check-in." to null
        }

        return "" to match
    }

    override fun add(element: Attendance): Boolean {
        return super.add(element)
    }

    fun hasAlreadyCheckedIn(employeeId: String, date: LocalDate): Boolean {
        return this.any {
            it.employeeId == employeeId &&
                    it.checkIn.toLocalDate() == date &&
                    it.checkOut == null
        }
    }

    fun hasAlreadyCheckedOut(employeeId: String, date: LocalDate): Boolean {
        return this.any {
            it.employeeId == employeeId &&
                    it.checkIn.toLocalDate() == date &&
                    it.checkOut != null
        }
    }

    fun delete(log: Attendance): Boolean {
        return this.remove(log)
    }

    override fun toString(): String {
        if (this.isEmpty()) return "No attendance records found."
        return this.joinToString("\n") { it.toString() }
    }

    fun getWorkingHoursSummary(startDate: LocalDate, endDate: LocalDate): Map<String, Long> {
        if (startDate.isAfter(endDate)) {
            return emptyMap()
        }
        val summary = mutableMapOf<String, Long>()
        this.filter { it.checkIn.toLocalDate() in startDate..endDate && it.checkOut != null }
            .forEach { record ->
                record.workingHours?.let { duration ->
                    summary[record.employeeId] = summary.getOrDefault(record.employeeId, 0L) + duration.toMinutes()
                }
            }
        return summary
    }
}
