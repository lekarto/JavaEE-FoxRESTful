package org.foxresult.dao.implementations;

import org.foxresult.dao.abstraction.AbstractDao;
import org.foxresult.dao.interfaces.EmployeeDao;
import org.foxresult.entity.Employee;
import org.foxresult.entity.filter.EmployeeFilter;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HibernateEmployeeDao extends AbstractDao<Employee, Integer> implements EmployeeDao {

    public Class<Employee> getClassType() {
        return Employee.class;
    }

    public Integer getPK(Employee object) {
        return object.getId();
    }
    
    public List<Employee> getFilteredEmployees(EmployeeFilter filter) {
        List<Employee> employees = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Criteria dbRequest = session.createCriteria(Employee.class);
            if ((filter.department != null) && !filter.department.equals("")) {
                dbRequest.add(Restrictions.like("department.name", filter.department));
            }
            if (filter.sex != null) {
                dbRequest.add(Restrictions.eq("sex", filter.sex));
            }
            if (filter.minSalary != null) {
                dbRequest.add(Restrictions.ge("salary", filter.minSalary));
            }
            if (filter.maxSalary != null) {
                dbRequest.add(Restrictions.le("salary", filter.maxSalary));
            }
            employees = dbRequest.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            closeSession(session);
        }
        return employees;
    }

    @Override
    public boolean dismissEmployeesFromDepartment(Integer depID) {
        if (depID == null) return false;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            List<Employee> employees = session.createCriteria(Employee.class).
                            add(Restrictions.eq("department.id", depID)).list();
            for (Employee emp : employees) {
                emp.setDepartment(null);
                System.out.println(emp.getId());
                if (!update(emp))
                    return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            closeSession(session);
        }
        return true;
    }
}
