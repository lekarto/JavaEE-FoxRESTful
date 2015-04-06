package org.foxresult.resources;

import org.foxresult.dao.implementations.HibernateDaoFactory;
import org.foxresult.entity.Department;
import org.foxresult.entity.wrapper.DepartmentWrapper;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("/departments")
public class DepartmentResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<DepartmentWrapper> getAll() {
        return wrap(HibernateDaoFactory.getDepartmentDao().getAll());
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public DepartmentWrapper get(@PathParam("id") int id) {
        return wrap(HibernateDaoFactory.getDepartmentDao().getByPK(id));
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/names")
    public List<String> getDepartmentsNames() {
        return HibernateDaoFactory.getDepartmentDao().getDepartmentsName();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(DepartmentWrapper dw) {
        if (HibernateDaoFactory.getDepartmentDao().persist(unwrap(dw))) {
            String result = "Department saved id: " + dw.getId();
            return Response.status(201).entity(result).build();
        } else {
            String result = "Server error";
            return Response.status(500).entity(result).build();
        }
    }

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
                HibernateDaoFactory.getDepartmentDao().update(unwrap(department))) {
            String result = "Updated department id: " + department.getId();
            return Response.status(200).entity(result).build();
        } else {
            String result = "Wrong format. RequestId = "+id+" not equal ObjectId ="+department.getId()+".";
            return Response.status(415).entity(result).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") int id) {
        if ((HibernateDaoFactory.getEmployeeDao().dismissEmployeesFromDepartment(id)) &&
                (HibernateDaoFactory.getDepartmentDao().deleteByPK(id))) {
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