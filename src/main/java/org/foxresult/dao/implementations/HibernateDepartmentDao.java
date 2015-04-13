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

    public Class<Department> getEntityClassType() {
        return Department.class;
    }

    @Override
    public Logger getLogger() {
        return logger;
    }

    public HibernateDepartmentDao() {  }

    public Integer getPK(Department object) {
        return object.getId();
    }

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