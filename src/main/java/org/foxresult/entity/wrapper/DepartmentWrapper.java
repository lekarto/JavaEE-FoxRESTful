package org.foxresult.entity.wrapper;

import org.foxresult.entity.Department;
import org.foxresult.entity.Employee;
import org.hibernate.Hibernate;

/**
 * Wrapper for Department class.
 * This class uses to communicate with the customer
 * @see org.foxresult.entity.Department
 */
public class DepartmentWrapper {
    private Integer id;
    private String name;
    private Float averageSalary = 0f;
    private Integer employeeCount = 0;

    public DepartmentWrapper() {  }
    
    public DepartmentWrapper(Department department) {
        id = department.getId();
        name = department.getName();
        employeeCount = department.getEmployees().size();
        for (Employee employee : department.getEmployees()) {
            Hibernate.initialize(department.getEmployees());
            averageSalary += employee.getSalary();            
        }
        if (employeeCount != 0) {
            averageSalary /= employeeCount;
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getAverageSalary() {
        return averageSalary;
    }

    public void setAverageSalary(Float averageSalary) {
        this.averageSalary = averageSalary;
    }

    public Integer getEmployeeCount() {
        return employeeCount;
    }

    public void setEmployeeCount(Integer employeeCount) {
        this.employeeCount = employeeCount;
    }
}
