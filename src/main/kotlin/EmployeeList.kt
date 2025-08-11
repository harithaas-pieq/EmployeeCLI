class EmployeeList : ArrayList<Employee>() {
    override fun add(element: Employee): Boolean {
        return if (element.validate()) super.add(element) else false
    }
    fun delete(id: String): Boolean {
        val emp = this.find { it.id == id } ?: return false
        return super.remove(emp)
    }
    fun employeeExists(id: String): Boolean {
        return this.any { it.id == id }
    }
}