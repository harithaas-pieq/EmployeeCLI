import java.time.LocalDateTime

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
    //get object after validation from this fn and pass it to attendance then do checkout
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

    fun delete(log: Attendance): Boolean {
        return this.remove(log)
    }

}
