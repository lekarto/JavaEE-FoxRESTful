package org.foxresult.dao.implementations;

import org.apache.log4j.Logger;
import org.foxresult.dao.abstraction.AbstractDao;
import org.foxresult.dao.interfaces.DepartmentDao;
import org.foxresult.entity.Department;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class HibernateDepartmentDao extends AbstractDao<Department, Integer> implements DepartmentDao {

    final static Logger logger = Logger.getLogger(HibernateDepartmentDao.class);

    /**
     * Returns Department class
     * @return Department class: Department.class
     * @see org.foxresult.entity.Department
     */
    public Class<Department> getEntityClassType() {
        return Department.class;
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

    public HibernateDepartmentDao() {  }

    /**
     * Returns Department private key
     * @param object    Department class
     * @return          Private key of the entity
     * @see org.foxresult.entity.Department
     */
    public Integer getPK(Department object) {
        return object.getId();
    }

    /**
     * Get Department entry from DB by it's name
     * @param name    Name of the department
     * @return        Department object if was found department with selected name
     * @see org.foxresult.entity.Department
     */
    @Override
    public Department getDepartmentByName(String name) {
        Department department = null;
        Session session = null;
        if ((name == null) || name.equals(""))
            return null;
        try {
            session = sessionFactory.openSession();
            department = (Department)session.createCriteria(Department.class).
                                             add(Restrictions.eq("name", name)).
                                             uniqueResult();
        } catch (Exception e) {
            logger.error("getDepartmentByName exception!", e);
            return null;
        } finally {
            closeSession(session);
        }
        return department;
    }

    /**
     * Get all names of departments
     * @return  List of all department names
     */
    @Override
    public List<String> getDepartmentsName() {
        List<String> depNames = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            depNames = session.createCriteria(Department.class).
                               setProjection(Projections.property("name")).list();
        } catch (Exception e) {
            logger.error("getDepartmentsName exception!", e);
            return null;
        } finally {
            closeSession(session);
        }
        return depNames;
    }
}