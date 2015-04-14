package org.foxresult.resources;

import org.foxresult.dao.interfaces.DepartmentDao;
import org.foxresult.dao.interfaces.EmployeeDao;
import org.foxresult.entity.Department;
import org.foxresult.entity.wrapper.DepartmentWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * REST service for get or set info about Departments
 * Client will get data in JSON format
 */
@Component
@Path("/departments")
public class DepartmentResource {

    @Autowired
    protected DepartmentDao departmentDao;
    @Autowired
    protected EmployeeDao employeeDao;

    /**
     * Get all Departments
     * @return  List of all departments.
     * @see org.foxresult.entity.wrapper.DepartmentWrapper
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<DepartmentWrapper> getAll() {
        return wrap(departmentDao.getAll());
    }

    /**
     * Get Department by id
     * @param id    Needed Department ID
     * @return      Department info
     * @see org.foxresult.entity.wrapper.DepartmentWrapper
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public DepartmentWrapper get(@PathParam("id") int id) {
        return wrap(departmentDao.getByPK(id));
    }

    /**
     * Get all Department names
     * @return  List of the department names
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/names")
    public List<String> getDepartmentsNames() {
        return departmentDao.getDepartmentsName();
    }

    /**
     * Create new Department
     * @param dw    Department info
     * @return      HTTP status 201 if Department was created otherwise HTTP status 500
     * @see org.foxresult.entity.wrapper.DepartmentWrapper
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(DepartmentWrapper dw) {
        Department dep = unwrap(dw);
        if ((dep.getName() != null) && departmentDao.persist(dep)) {
            String result = "Department saved id: " + dep.getId();
            return Response.status(201).entity(result).build();
        } else {
            String result = "Server error";
            return Response.status(500).entity(result).build();
        }
    }

    /**
     * Update existing Department
     * @param department    New department info
     * @param id            Department ID which need to be updated
     * @return              HTTP status 400 if request body is null.
     *                      HTTP status 415 if request ID and Department ID is not equal
     *                      HTTP 200 if department was updated
     * @see org.foxresult.entity.wrapper.DepartmentWrapper
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response update(DepartmentWrapper department, @PathParam("id") int id) {
        if (department == null) {
            return Response.status(400).entity("No data").build();
        }
        if (department.getId() == null) {
            department.setId(id);
        }
        if ((id == department.getId()) &&
                departmentDao.update(unwrap(department))) {
            String result = "Updated department id: " + department.getId();
            return Response.status(200).entity(result).build();
        } else {
            String result = "Wrong format. RequestId = "+id+" not equal ObjectId ="+department.getId()+".";
            return Response.status(415).entity(result).build();
        }
    }

    /**
     * Delete existing Department
     * @param id            Department ID which need to be deleted
     * @return              HTTP status 404 if department with specified ID was not found
     *                      HTTP 200 if department was deleted
     * @see org.foxresult.entity.wrapper.DepartmentWrapper
     */
    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") int id) {
        if ((employeeDao.dismissEmployeesFromDepartment(id)) &&
                (departmentDao.deleteByPK(id))) {
            String result = "Department deleted id: " + id;
            return Response.status(201).entity(result).build();
        } else {
            String result = "Department not found: " + id;
            return Response.status(404).entity(result).build();
        }
    }

    private DepartmentWrapper wrap(Department dep) {
        return  (dep == null)? null : new DepartmentWrapper(dep);
    }

    private List<DepartmentWrapper> wrap(List<Department> departments) {
        if (departments == null)
            return null;
        List<DepartmentWrapper> wrappedDepartments = new ArrayList<>(departments.size());
        for (Department department : departments) {
            wrappedDepartments.add(new DepartmentWrapper(department));
        }
        return wrappedDepartments;
    }

    private Department unwrap(DepartmentWrapper dw) {
        return (dw == null)? null : new Department(dw.getId(), dw.getName());
    }

    private List<Department> unwrap(List<DepartmentWrapper> departments) {
        if (departments == null)
            return null;
        List<Department> deps = new ArrayList<>(departments.size());
        for (DepartmentWrapper department : departments) {
            deps.add(unwrap(department));
        }
        return deps;
    }
}