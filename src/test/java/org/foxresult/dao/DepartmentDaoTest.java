package org.foxresult.dao;

import org.foxresult.dao.interfaces.DepartmentDao;
import org.foxresult.entity.Department;
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
public class DepartmentDaoTest {
    private String DEPARTMENT_TEST_NAME = "Economic analysis";

    @Autowired
    private DepartmentDao departmentDao;

    @Test
    @Before
    public void testContext() {
        Assert.assertNotNull(departmentDao);
    }

    @Test
    public void persistAndGetDepartmentByNameTest() {
        Department dep = new Department(DEPARTMENT_TEST_NAME);
        Assert.assertTrue(departmentDao.persist(dep));

        Department dep_get = departmentDao.getDepartmentByName(DEPARTMENT_TEST_NAME);
        Assert.assertNotNull(dep_get);
        Assert.assertEquals(dep_get.getName(), DEPARTMENT_TEST_NAME);
    }

    @Test
    public void persistUpdateDeleteTest() {
        Department dep = new Department(DEPARTMENT_TEST_NAME);
        Assert.assertTrue(departmentDao.persist(dep));

        String newName = "Cloned";
        dep.setName(newName);
        departmentDao.update(dep);

        Department dep_updated = departmentDao.getByPK(dep.getId());
        Assert.assertTrue((dep_updated != null) && dep_updated.getName().equals(dep.getName()));

        Assert.assertTrue(departmentDao.deleteByPK(dep_updated.getId()));
        Assert.assertNull(departmentDao.getByPK(dep_updated.getId()));
    }

    @Test
    public void getByPKTest() {
        Department dep = departmentDao.getByPK(1);
        Assert.assertNotNull(dep);
        Assert.assertTrue(dep.getName().equals("Developers"));
    }

    @Test
    public void getAllTest() {
        List deps = departmentDao.getAll();
        Assert.assertNotNull(deps);
        int count = deps.size();
        Assert.assertTrue(count > 0);
    }

    @Test
    public void getDepartmentsNameTest() {
        List<String> depNames = departmentDao.getDepartmentsName();
        Assert.assertNotNull(depNames);
        Assert.assertTrue(depNames.size() > 0);
        String names[] = {"Developers", "QA", "Managers", "Support", "Test"};
        for (String depName : names) {
            Assert.assertTrue(depNames.contains(depName));
        }
    }
}
