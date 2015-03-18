package org.foxresult.dao.implementations;

import org.foxresult.dao.abstraction.AbstractDao;
import org.foxresult.dao.interfaces.DepartmentDao;
import org.foxresult.entity.Department;
import org.hibernate.Session;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.sql.SQLException;
import java.util.List;

public class HibernateDepartmentDao extends AbstractDao<Department, Integer> implements DepartmentDao {

    public Class<Department> getClassType() {
        return Department.class;
    }

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
            session = HibernateDaoFactory.getSessionFactory().openSession();
            department = (Department)session.createCriteria(Department.class).
                                             add(Restrictions.eq("name", name)).
                                             uniqueResult();
        } catch (SQLException e) {
            e.printStackTrace();
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
            session = HibernateDaoFactory.getSessionFactory().openSession();
            depNames = session.createCriteria(Department.class).
                               setProjection(Projections.property("name")).list();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            closeSession(session);
        }
        return depNames;
    }
}
