package org.foxresult.dao.interfaces;

import org.foxresult.entity.Department;

import java.util.List;


public interface DepartmentDao extends GenericDao<Department, Integer> {
    public Department getDepartmentByName(String name);
    public List<String> getDepartmentsName();

}
