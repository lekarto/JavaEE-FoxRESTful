package org.foxresult.resources;

import org.foxresult.dao.interfaces.DepartmentDao;
import org.foxresult.dao.interfaces.EmployeeDao;
import org.foxresult.entity.Department;
import org.foxresult.entity.Employee;
import org.foxresult.entity.wrapper.EmployeeWrapper;
import org.foxresult.entity.filter.EmployeeFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.ArrayList;
import java.util.List;

@Component
@Path("/employees")
public class EmployeeResource {

    @Autowired
    protected EmployeeDao employeeDao;
    @Autowired
    protected DepartmentDao departmentDao;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(@Context UriInfo uriInfo) {
        MultivaluedMap<String, String> queryParams = uriInfo.getQueryParameters();
        if (queryParams.size() > 0) {
            return getFilteredEmployees(queryParams);
        } else {
            return  Response.ok(wrap(employeeDao.getAll())).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public EmployeeWrapper get(@PathParam("id") int id) {
        return wrap(employeeDao.getByPK(id));
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(EmployeeWrapper employee) {
        Employee emp = unwrap(employee);
        if ((emp != null) &&
                (emp.getFirstName()!=null) && (emp.getLastName()!=null) &&
                employeeDao.persist(emp)) {
            String result = "Employee saved id: " + emp.getId();
            return Response.status(201).entity(result).build();
        } else {
            String result = "Server error";
            return Response.status(500).entity(result).build();
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response update(EmployeeWrapper employee, @PathParam("id") int id) {
        if (employee == null) {
            return Response.status(400).entity("No data").build();
        }
        if (employee.getId() == null) {
            employee.setId(id);
        }
        if (id == employee.getId()) {
            if (employeeDao.update(unwrap(employee))) {
                String result = "Updated employee id: " + employee.getId();
                return Response.status(200).entity(result).build();
            } else {
                return Response.status(500).entity("Something goes wrong. Try later.").build();
            } 
        } else {
            String result = "Wrong format. RequestId = "+id+" not equal ObjectId = "+employee.getId()+".";
            return Response.status(415).entity(result).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") int id) {
        if (employeeDao.deleteByPK(id)) {
            String result = "Employee deleted id: " + id;
            return Response.status(201).entity(result).build();
        } else {
            String result = "Employee not found: " + id;
            return Response.status(404).entity(result).build();
        }
    }

    private Response getFilteredEmployees(MultivaluedMap<String, String> params) {
        EmployeeFilter filter = new EmployeeFilter();
        String dep = params.getFirst(EmployeeFilter.DEPARTMENT);
        if (!filter.parseDepartment(dep)) {
            return Response.status(400).entity("Parameter '" + EmployeeFilter.DEPARTMENT + "' not valid").build();
        }

        String sex = params.getFirst(EmployeeFilter.SEX);
        if (!filter.parseSex(sex)) {
            return Response.status(400).entity("Parameter '" + EmployeeFilter.SEX + "' not valid").build();
        }

        String minSalary = params.getFirst(EmployeeFilter.MIN_SALARY);
        if (!filter.parseMinSalary(minSalary)) {
            return Response.status(400).entity("Parameter '" +EmployeeFilter.MIN_SALARY + "' not valid").build();
        }

        String maxSalary = params.getFirst(EmployeeFilter.MAX_SALARY);
        if (!filter.parseMaxSalary(maxSalary)) {
            return Response.status(400).entity("Parameter '" + EmployeeFilter.MAX_SALARY + "' not valid").build();
        }
        return Response.ok(wrap(employeeDao.getFilteredEmployees(filter))).build();
    }

    private EmployeeWrapper wrap(Employee emp) {
        return  (emp == null)? null : new EmployeeWrapper(emp);
    }

    private List<EmployeeWrapper> wrap(List<Employee> employees) {
        if (employees == null)
            return null;
        List<EmployeeWrapper> wrappedEmployees = new ArrayList<>(employees.size());
        for (Employee emp : employees) {
            wrappedEmployees.add(new EmployeeWrapper(emp));
        }
        return wrappedEmployees;
    }

    private Employee unwrap(EmployeeWrapper ew) {
        if (ew == null) return null;
        Department dep = null;
        if ( (ew.getDepartment() != null)) {
            dep = departmentDao.getDepartmentByName(ew.getDepartment());
        }
        return new Employee(ew.getId(), ew.getFirstName(), ew.getLastName(),
                            ew.getSex(), ew.getSalary(), dep);
    }

    private List<Employee> unwrap(List<EmployeeWrapper> employees) {
        if (employees == null)
            return null;
        List<Employee> emps = new ArrayList<>(employees.size());
        for (EmployeeWrapper employee : employees) {
            emps.add(unwrap(employee));
        }
        return emps;
    }
}