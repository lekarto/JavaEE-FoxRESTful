package org.foxresult.dao.abstraction;

import org.foxresult.dao.interfaces.GenericDao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.List;


public abstract class AbstractDao<T, PK extends Serializable> implements GenericDao<T, PK> {

    @Autowired
    protected SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public boolean persist(T object) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.save(object);
            session.getTransaction().commit();
        } catch (Exception sql) {
            session.getTransaction().rollback();
            sql.printStackTrace();
            return false;
        } finally {
            closeSession(session);
        }
        return true;
    }

    public T getByPK(PK key) {
        T entry = null;
        Session session = null;
        if (key != null)
            try {
                session = sessionFactory.openSession();
                session.clear();
                entry = (T) session.get(getClassType(), key);
            } catch (Exception sql) {
                sql.printStackTrace();
            } finally {
                closeSession(session);
            }
        return entry;
    }

    public boolean deleteByPK(PK id) {
        T object = getByPK(id);
        if (object != null) {
            Session session = null;
            try {
                session = sessionFactory.openSession();
                session.beginTransaction();
                session.delete(object);
                session.getTransaction().commit();
            } catch (Exception e) {
                session.getTransaction().rollback();
                e.printStackTrace();
                return false;
            } finally {
                closeSession(session);
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean setAll(List<T> objects) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            for (T object : objects) {
                if (!persist(object)) {
                    throw new Exception("Can't save to DB employee");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            closeSession(session);
        }
        return true;
    }

    public List<T> getAll() {
        List<T> result = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.clear();
            result = session.createCriteria(getClassType()).
                        addOrder(Order.asc("id")).list();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeSession(session);
        }
        return result;
    }

    public boolean update(T object) {
        T getObject = getByPK(getPK(object));
        if (getObject != null) {
            Session session = null;
            try {
                session = sessionFactory.openSession();
                session.beginTransaction();
                session.update(object);

                session.getTransaction().commit();
                session.close();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            } finally {
                closeSession(session);
            }
        } else {
            return false;
        }
    }

    protected void closeSession(Session session) {
        if ((session != null) && (session.isOpen())) {
            session.close();
        }
    }
}