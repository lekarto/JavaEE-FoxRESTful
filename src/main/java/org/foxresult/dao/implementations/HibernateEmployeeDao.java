package org.foxresult.dao.implementations;

import org.apache.log4j.Logger;
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

    final static Logger logger = Logger.getLogger(HibernateEmployeeDao.class);

    /**
     * Returns Employee DAO class.
     * @return Entity classtype: T.class
     * @see org.foxresult.entity.Employee
     */
    public Class<Employee> getEntityClassType() {
        return Employee.class;
    }

    /**
     * Get logger
     * @return Returns logger object
     * @see org.apache.log4j.Logger
     */
    @Override
    public Logger getLogger() {
        return logger;
    }

    /**
     * Returns Employee private key
     * @param object    Employee class
     * @return          Private key of the entity
     * @see org.foxresult.entity.Employee
     */
    public Integer getPK(Employee object) {
        return object.getId();
    }

    /**
     * Get filtered list of employees
     * @param filter    Filter for specify needed users
     * @return          Filtered list of employees
     *                  If filter is null will be return null
     * @see org.foxresult.entity.filter.EmployeeFilter
     * @see org.foxresult.entity.Employee
     */
    public List<Employee> getFilteredEmployees(EmployeeFilter filter) {
        if (filter == null)
            return null;
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
            logger.error("getFilteredEmployees exception!", e);
            return null;
        } finally {
            closeSession(session);
        }
        return employees;
    }

    /**
     * Dismiss all Employees from specified department
     * @param depID    Department ID from which you want to lay off employees
     * @return         True if all employees was dismissed otherwise false
     *                 If depID is null will be return null.
     */
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
            logger.error("dismissEmployeesFromDepartment exception!", e);
            return false;
        } finally {
            closeSession(session);
        }
        return true;
    }
}
