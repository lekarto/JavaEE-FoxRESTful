package org.foxresult.dao.interfaces;

import org.foxresult.entity.Employee;
import org.foxresult.entity.filter.EmployeeFilter;

import java.util.List;

public interface EmployeeDao extends GenericDao<Employee, Integer> {
    public List<Employee> getFilteredEmployees(EmployeeFilter filter);
    public boolean dismissEmployeesFromDepartment(Integer depID);
}
