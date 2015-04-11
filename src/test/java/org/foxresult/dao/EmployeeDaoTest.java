package org.foxresult.dao;

import org.foxresult.dao.interfaces.EmployeeDao;
import org.foxresult.entity.Employee;
import org.foxresult.entity.filter.EmployeeFilter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@ContextConfiguration(value = "classpath:spring-test.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class EmployeeDaoTest {
    private String FIRST_NAME_TEST = "Abdul";
    private String LAST_NAME_TEST  = "Ivanov";
    @Autowired
    EmployeeDao employeeDao;

    @Before
    public void testContext() {
        Assert.assertNotNull(employeeDao);
    }

    @Test
    public void persistAndGetDepartmentByNameTest() {
        Employee emp = new Employee(FIRST_NAME_TEST, LAST_NAME_TEST, Employee.MALE, 123456789f);
        Assert.assertTrue(employeeDao.persist(emp));

        EmployeeFilter filter = new EmployeeFilter();
        filter.sex = Employee.MALE;
        filter.minSalary = 123456789f;
        filter.maxSalary = 123456789f;

        List<Employee> list = employeeDao.getFilteredEmployees(filter);
        Assert.assertTrue((list!=null) && (list.size() == 1));
        Employee emp_get = list.get(0);

        Assert.assertTrue((emp_get.getSex() == emp.getSex()) &&
                          emp_get.getFirstName().equals(emp.getFirstName()) &&
                          emp_get.getLastName().equals(emp.getLastName()) &&
                        (emp_get.getSalary().equals(emp.getSalary())) );
    }
}
