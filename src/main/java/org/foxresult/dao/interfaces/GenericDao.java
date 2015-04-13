package org.foxresult.dao.interfaces;

import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.List;

public interface GenericDao<T, PK extends Serializable> {
    public boolean persist(T object);
    public T getByPK(PK key);
    public boolean update(T object);
    public List<T> getAll();
    public boolean setAll(List<T> entries);
    public boolean deleteByPK(PK id);
    public Class<T> getEntityClassType();
    public Logger getLogger();
    public PK getPK(T object);
}
