package org.foxresult.dao.abstraction;

import org.foxresult.dao.interfaces.GenericDao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.List;


/**
 * Abstract class for DAO entries
 * Consist standard CRUD operations
 * Implements Generic DAO interface
 * @param <T>
 * @param <PK>
 * @see org.foxresult.dao.interfaces.GenericDao
 */
public abstract class AbstractDao<T, PK extends Serializable> implements GenericDao<T, PK> {

    @Autowired
    protected SessionFactory sessionFactory;

    /**
     * Create a new record in the DB
     * @param object    Entity which must be stored
     * @return          True if entity was successfully created in the DB otherwise false
     */
    public boolean persist(T object) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.save(object);
            session.getTransaction().commit();
        } catch (Exception sql) {
            session.getTransaction().rollback();
            getLogger().error("persist exception!", sql);
            return false;
        } finally {
            closeSession(session);
        }
        return true;
    }

    /**
     * Get entity T from DB by private key.
     * @param key    Private key of entity which will need to read from DB
     * @return       T class if entity was found in DB, null if DB if requested entity was not found.
     *               If key is null will be returned null.
     */
    public T getByPK(PK key) {
        T entry = null;
        Session session = null;
        if (key != null)
            try {
                session = sessionFactory.openSession();
                session.clear();
                entry = (T) session.get(getEntityClassType(), key);
            } catch (Exception sql) {
                getLogger().error("getByPK exception!", sql);
            } finally {
                closeSession(session);
            }
        return entry;
    }

    /**
     * Delete an object from the database that has an id.
     * @param id    ID identifier for row which need delete.
     * @return      True if delete was successful.
     *              If key is null will be returned null.
     */
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
                getLogger().error("deleteByPK exception!", e);
                return false;
            } finally {
                closeSession(session);
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Create new entries in DB from List of entities
     * @param objects   List of entries
     * @return          True if all entries was created otherwise false.
     *                  If objects is null will be returned null.
     */
    public boolean setAll(List<T> objects) {
        try {
            for (T object : objects) {
                if (!persist(object)) {
                    throw new Exception("Can't save to DB employee");
                }
            }
        } catch (Exception e) {
            getLogger().error("setAll exception!", e);
            return false;
        }
        return true;
    }

    /**
     * Get all entities from DB.
     * @return  List of entities.
     */
    public List<T> getAll() {
        List<T> result = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.clear();
            result = session.createCriteria(getEntityClassType()).
                        addOrder(Order.asc("id")).list();
        } catch (Exception e) {
            getLogger().error("getAll exception!", e);
        } finally {
            closeSession(session);
        }
        return result;
    }

    /**
     * Update existing Entity
     * @param object    Entity for updating
     * @return          True if entity was updated, otherwise false.
     *                  if object is null will be return null.
     */
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
                getLogger().error("update exception!", e);
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