import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Attendance(
    var employeeId: String,
    var checkIn: LocalDateTime
) {
    var checkOut: LocalDateTime? = null

    fun isValidCheckIn(attendanceList: List<Attendance>): String {
        val today = checkIn.toLocalDate()
        val alreadyCheckedIn = attendanceList.any {
            it.employeeId == employeeId &&
                    it.checkIn.toLocalDate() == today &&
                    it.checkOut == null
        }
        if (alreadyCheckedIn) {
            return "Employee '$employeeId' is already checked in for today."
        }
        return ""
    }
    fun getWorkingHours(): String {
        if (checkOut == null) return ""
        val totalMinutes = java.time.Duration.between(checkIn, checkOut).toMinutes()
        val hours = totalMinutes / 60
        val minutes = totalMinutes % 60
        return "${hours}h ${minutes}m"
    }

    fun isValidCheckOut(attendanceList: List<Attendance>): String {
        if (checkOut == null) return "Check-out time not set."

        val match = attendanceList.find {
            it.employeeId == employeeId &&
                    it.checkIn.toLocalDate() == checkOut?.toLocalDate() &&
                    it.checkOut == null
        } ?: return "No valid check-in found for employee '$employeeId' on this date."

        val checkInTime = match.checkIn.withSecond(0).withNano(0)
        val checkOutTime = checkOut!!.withSecond(0).withNano(0)

        if (checkOutTime.isBefore(checkInTime) || checkOutTime == checkInTime) {
            return "Check-out cannot be before or same as check-in."
        }
        return ""
    }

    override fun toString(): String {
        val checkInTime = checkIn.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
        val checkOutTime = checkOut?.format(DateTimeFormatter.ofPattern("HH:mm")) ?: "N/A"
        val hoursWorked = getWorkingHours().takeIf { it.isNotEmpty() } ?: "N/A"
        return "ID: $employeeId  Check-in: $checkInTime  Check-out: $checkOutTime  Hours Worked: $hoursWorked"
    }

}
