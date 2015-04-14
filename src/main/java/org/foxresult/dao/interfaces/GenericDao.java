package org.foxresult.dao.interfaces;

import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.List;

/**
 * Generic DAO interface DAO entries
 * Consist standard CRUD operations.
 * @param <T>     The class that the code will be operating on.
 * @param <PK>    The class of the ID column associated with the class.
 */
public interface GenericDao<T, PK extends Serializable> {

    /**
     * Create a new record in the DB
     * @param object    Entity which must be stored
     * @return          True if entity was successfully created in the DB, else false will be return
     */
    public boolean persist(T object);

    /**
     * Get entity T from DB by private key.
     * @param key    Private key of entity which will need to read from DB
     * @return       T class if entity was found in DB, null if DB if requested entity was not found.
     */
    public T getByPK(PK key);

    /**
     * Update existing Entity
     * @param object    Entity for updating
     * @return          True if entity was updated, otherwise false
     */
    public boolean update(T object);

    /**
     * Get all entities from DB.
     * @return  List of entities.
     */
    public List<T> getAll();
    public boolean setAll(List<T> entries);

    /**
     * Delete an object from the database that has an id.
     * @param id    ID identifier for row which need delete.
     * @return      True if delete was successful.
     */
    public boolean deleteByPK(PK id);

    /**
     * Returns the class of the DAO.
     * @return Entity classtype: T.class
     */
    public Class<T> getEntityClassType();

    /**
     *
     * @return Returns logger object
     */
    public Logger getLogger();

    /**
     * Returns private key of the entity
     * @param object    Entity which have primary key
     * @return          Private key of the entity
     */
    public PK getPK(T object);
}
