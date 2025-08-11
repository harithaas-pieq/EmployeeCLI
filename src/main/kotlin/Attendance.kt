import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.Duration
class Attendance(
    var employeeId: String,
    var checkIn: LocalDateTime
) {
    var checkOut: LocalDateTime? = null
    var workingHours: Duration? = null

    fun checkout(time: LocalDateTime): String {
        if (time <= checkIn) {
            return "Check-out cannot be before or same as check-in."
        }
        this.checkOut = time
        this.workingHours = Duration.between(checkIn, checkOut)
        return "Checked out at ${time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))}"
    }

    override fun toString(): String {
        val checkInTime = checkIn.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
        val checkOutTime = checkOut?.format(DateTimeFormatter.ofPattern("HH:mm")) ?: "N/A"
        val hoursWorked = workingHours?.let { "${it.toHours()}h ${it.toMinutesPart()}m" } ?: "N/A"
        return "ID: $employeeId  Check-in: $checkInTime  Check-out: $checkOutTime  Hours Worked: $hoursWorked"
    }
}