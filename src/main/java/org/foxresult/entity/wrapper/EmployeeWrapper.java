package org.foxresult.entity.wrapper;

import org.foxresult.entity.Employee;

/**
 * Wrapper for Employee class.
 * This class uses to communicate with the customer
 * @see org.foxresult.entity.Employee
 */
public class EmployeeWrapper {

    private Integer id;
    private String firstName;
    private String lastName;
    private boolean sex;
    private Float salary;
    private String department;

    public static boolean FEMALE = false;
    public static boolean MALE = true;

    public EmployeeWrapper() {}

    public EmployeeWrapper(String firstName, String lastName,
                    boolean sex, Float salary, String department) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.sex = sex;
        this.salary = salary;
        this.department = department;
    }

    public EmployeeWrapper(String firstName, String lastName,
                    boolean sex, Float salary) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.sex = sex;
        this.salary = salary;
    }

    public EmployeeWrapper(Integer id, String firstName, String lastName,
                    boolean sex, Float salary, String department) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.sex = sex;
        this.salary = salary;
        this.department = department;
    }

    public EmployeeWrapper(Employee emp) {
        if (emp != null) {
            id = emp.getId();
            firstName = emp.getFirstName();
            lastName = emp.getLastName();
            sex = emp.getSex();
            salary = emp.getSalary();
            if (emp.getDepartment() != null)
                department = emp.getDepartment().getName();
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean getSex() {
        return sex;
    }

    public boolean Male() {
        return (sex == MALE);
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public Float getSalary() {
        return salary;
    }

    public void setSalary(Float salary) {
        this.salary = salary;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
