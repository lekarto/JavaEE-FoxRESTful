package org.foxresult.dao.implementations;

import org.foxresult.dao.interfaces.DepartmentDao;
import org.foxresult.entity.Department;
import org.foxresult.entity.Employee;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.foxresult.dao.interfaces.DaoFactory;
import org.foxresult.dao.interfaces.EmployeeDao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HibernateDaoFactory implements DaoFactory {
    private static String HIBERNATE_CFG = "hibernate.cfg.xml";
    private static SessionFactory sessionFactory = buildSessionFactory();

    static {
        List<Department> dep = new ArrayList<>(4);
        dep.add(new Department(1, "Developers"));
        dep.add(new Department(2, "QA"));
        dep.add(new Department(3, "Managers"));
        dep.add(new Department(4, "Support"));
        List<Employee> data = new ArrayList<>(6);
        data.add(new Employee("Sergey", "Fedorov", Employee.MALE, 100f, dep.get(0)));
        data.add(new Employee("Иван", "Демидов", Employee.MALE, 120f, dep.get(0)));
        data.add(new Employee("عبد", "الحسيب", Employee.MALE, 140f, dep.get(1)));
        data.add(new Employee("Angelina", "Feofilaktova", Employee.FEMALE, 160f, dep.get(2)));
        data.add(new Employee("湧", "阮", Employee.FEMALE, 180f, dep.get(2)));
        data.add(new Employee("Test", "Ivanovna", Employee.FEMALE, 180f, null));
        getDepartmentDao().setAll(dep);
        getEmployeeDao().setAll(data);
    }

    private static SessionFactory buildSessionFactory() {
        Configuration configuration = new Configuration().addResource(HIBERNATE_CFG).configure();
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(
                configuration.getProperties()).build();
        return configuration.buildSessionFactory(serviceRegistry);
    }
    
    public static SessionFactory getSessionFactory() throws SQLException {
        return sessionFactory;
    }

    public static EmployeeDao getEmployeeDao() { return new HibernateEmployeeDao(); }
    
    public static DepartmentDao getDepartmentDao() { return new HibernateDepartmentDao(); }
}
