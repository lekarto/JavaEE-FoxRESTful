package org.foxresult.dao.interfaces;

import org.foxresult.entity.Department;

import java.util.List;

/**
 * Interface for work with Department DAO objects
 */
public interface DepartmentDao extends GenericDao<Department, Integer> {
    /**
     * Get Department entry from DB by it's name
     * @param name    Name of the department
     * @return        Department object if was found department with selected name
     * @see org.foxresult.entity.Department
     */
    public Department getDepartmentByName(String name);

    /**
     * Get all names of departments
     * @return  List of all department names
     */
    public List<String> getDepartmentsName();

}
