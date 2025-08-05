class EmployeeList : ArrayList<Employee>() {
    override fun add(element: Employee): Boolean {
        return if (element.validate()) super.add(element) else false
    }
}