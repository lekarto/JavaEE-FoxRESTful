package org.foxresult.dao.interfaces;

import org.foxresult.entity.Employee;
import org.foxresult.entity.filter.EmployeeFilter;

import java.util.List;

/**
 * Interface for work with Employee DAO objects
 */
public interface EmployeeDao extends GenericDao<Employee, Integer> {

    /**
     * Get filtered list of employees
     * @param filter    Filter for specify needed users
     * @return          Filtered list of employees
     * @see org.foxresult.entity.filter.EmployeeFilter
     * @see org.foxresult.entity.Employee
     */
    public List<Employee> getFilteredEmployees(EmployeeFilter filter);

    /**
     * Dismiss all Employees from specified department
     * @param depID    Department ID from which you want to lay off employees
     * @return         True if all employees was dismissed otherwise false
     */
    public boolean dismissEmployeesFromDepartment(Integer depID);
}
