import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.Duration
class Attendance(
    var employeeId: String,
    var checkIn: LocalDateTime
) {
    var checkOut: LocalDateTime? = null
    var workingHours: Duration = Duration.ZERO

//    fun getWorkingHours(): String {
//        if (checkOut == null) return ""
//        val totalMinutes = java.time.Duration.between(checkIn, checkOut).toMinutes()
//        val hours = totalMinutes / 60
//        val minutes = totalMinutes % 60
//        return "${hours}h ${minutes}m"
//    }

    fun checkout(time: LocalDateTime): String {
        if (time <= checkIn) {
            return "Check-out cannot be before or same as check-in."
        }
        this.checkOut = time
        this.workingHours=Duration.between(checkIn, checkOut)
        return "Checked out at ${time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))}"
    }

    override fun toString(): String {
        val checkInTime = checkIn.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
        val checkOutTime = checkOut?.format(DateTimeFormatter.ofPattern("HH:mm")) ?: "N/A"
        val hoursWorked = "${workingHours.toHours()}h ${workingHours.toMinutesPart()}m"
        return "ID: $employeeId  Check-in: $checkInTime  Check-out: $checkOutTime  Hours Worked: $hoursWorked"
    }

}
