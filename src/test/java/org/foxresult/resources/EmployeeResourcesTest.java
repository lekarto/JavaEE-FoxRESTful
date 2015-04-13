package org.foxresult.resources;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.spi.spring.container.servlet.SpringServlet;
import com.sun.jersey.test.framework.AppDescriptor;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.foxresult.entity.Employee;
import resources.HTTPStatus;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.request.RequestContextListener;

import javax.ws.rs.core.MediaType;
import java.io.IOException;

@ContextConfiguration(value = "classpath:spring-test.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class EmployeeResourcesTest extends JerseyTest {

    private String EMPLOYEE_FIRST_NAME = "Oland";
    private String EMPLOYEE_LAST_NAME = "Pfapfer";

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    protected AppDescriptor configure() {
        return new WebAppDescriptor.Builder("org.foxresult.resources")
                .contextParam("contextConfigLocation", "classpath:spring-test.xml")
                .initParam("com.sun.jersey.api.json.POJOMappingFeature", "true")
                .servletClass(SpringServlet.class)
                .contextListenerClass(ContextLoaderListener.class)
                .requestListenerClass(RequestContextListener.class)
                .build();
    }

    @Test
    public void getAllTest() {
        WebResource webResource = resource().path("/employees");
        ClientResponse response = webResource
                .accept(MediaType.APPLICATION_JSON)
                .get(ClientResponse.class);
        Assert.assertNotNull(response);
        Assert.assertTrue((response.getStatus() == HTTPStatus.RESPONSE_STATUS_OK) &&
                response.hasEntity());
        try {
            JSONArray jsonArray = new JSONArray(response.getEntity(String.class));
            Assert.assertTrue(jsonArray.length() > 0);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void getAllWrongTest() {
        WebResource webResource = resource().path("/employees");
        ClientResponse response = webResource
                .accept(MediaType.APPLICATION_JSON)
                .delete(ClientResponse.class);
        Assert.assertNotNull(response);
        Assert.assertTrue(response.getStatus() == HTTPStatus.RESPONSE_STATUS_NOT_ALLOWED);
    }

    @Test
    public void getByIDTest() {
        WebResource webResource = resource().path("/employees/1");
        ClientResponse response = webResource
                .accept(MediaType.APPLICATION_JSON)
                .get(ClientResponse.class);
        Assert.assertNotNull(response);
        Assert.assertTrue((response.getStatus() == HTTPStatus.RESPONSE_STATUS_OK) &&
                response.hasEntity());
        try {
            JSONObject jsonObject = new JSONObject(response.getEntity(String.class));
            Assert.assertEquals(1, jsonObject.get("id"));
            Assert.assertEquals("Sergey", jsonObject.get("firstName"));
            Assert.assertEquals("Fedorov", jsonObject.get("lastName"));
            Assert.assertEquals(true, jsonObject.get("sex"));
            Assert.assertEquals(100d, jsonObject.get("salary"));
            Assert.assertEquals("Developers", jsonObject.get("department"));
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void getByIDWrongTest() {
        WebResource webResource = resource().path("/employees/10305");
        ClientResponse response = webResource
                .accept(MediaType.APPLICATION_JSON)
                .get(ClientResponse.class);
        Assert.assertNotNull(response);
        Assert.assertTrue(response.getStatus() == HTTPStatus.RESPONSE_STATUS_NO_DATA);
    }

    @Test
    public void createTest() throws IOException {
        Employee emp = new Employee(EMPLOYEE_FIRST_NAME, EMPLOYEE_LAST_NAME, Employee.MALE, 1234567f);
        WebResource webResource = resource().path("/employees");
        ClientResponse response = webResource.type(MediaType.APPLICATION_JSON).
                post(ClientResponse.class, mapper.writeValueAsString(emp));
        Assert.assertNotNull(response);
        Assert.assertTrue(response.getStatus() == HTTPStatus.RESPONSE_STATUS_CREATED &&
                response.hasEntity());
        String answer = response.getEntity(String.class);
        Assert.assertTrue(answer.matches("Employee saved id: [\\d]"));
    }

    @Test
    public void createWrongTest() throws IOException {
        Employee emp = new Employee();
        WebResource webResource = resource().path("/employees");
        ClientResponse response = webResource.type(MediaType.APPLICATION_JSON).
                post(ClientResponse.class, mapper.writeValueAsString(emp));
        Assert.assertNotNull(response);
        Assert.assertTrue(response.getStatus() == HTTPStatus.RESPONSE_STATUS_SERVER_ERROR &&
                response.hasEntity());
        String answer = response.getEntity(String.class);
        Assert.assertTrue(answer.matches("Server error"));
    }

    @Test
    public void createUpdateTest() throws IOException {
        Employee emp = new Employee(EMPLOYEE_FIRST_NAME, EMPLOYEE_LAST_NAME, Employee.MALE, 1234567f);
        emp.setId(create(emp));
        emp.setFirstName("Changuen");

        WebResource webResource = resource().path("/employees/"+emp.getId());
        ClientResponse response = webResource.type(MediaType.APPLICATION_JSON).
                put(ClientResponse.class, mapper.writeValueAsString(emp));
        Assert.assertNotNull(response);
        Assert.assertTrue(response.getStatus() == HTTPStatus.RESPONSE_STATUS_OK);
    }

    @Test
    public void updateWrongTest() throws IOException {
        Employee emp = new Employee(100, EMPLOYEE_FIRST_NAME, EMPLOYEE_LAST_NAME, Employee.MALE, 1234567f, null);
        WebResource webResource = resource().path("/employees/5");
        ClientResponse response = webResource.type(MediaType.APPLICATION_JSON).
                put(ClientResponse.class, mapper.writeValueAsString(emp));
        Assert.assertNotNull(response);
        Assert.assertTrue(response.getStatus() == HTTPStatus.RESPONSE_STATUS_UNSUPPORTED_MEDIA_TYPE &&
                response.hasEntity());
    }

    @Test
    public void updateNullTest() throws IOException {
        WebResource webResource = resource().path("/employees/5");
        ClientResponse response = webResource.type(MediaType.APPLICATION_JSON).
                put(ClientResponse.class, mapper.writeValueAsString(null));
        Assert.assertNotNull(response);
        Assert.assertTrue(response.getStatus() == HTTPStatus.RESPONSE_STATUS_BAD_REQUEST &&
                response.hasEntity());
    }

    @Test
    public void createDeleteTest() throws IOException {
        Employee emp = new Employee(EMPLOYEE_FIRST_NAME, EMPLOYEE_LAST_NAME, Employee.MALE, 1234567f);
        emp.setId(create(emp));

        WebResource webResource = resource().path("/employees/"+emp.getId());
        ClientResponse response = webResource.type(MediaType.APPLICATION_JSON).
                delete(ClientResponse.class);
        Assert.assertNotNull(response);
        Assert.assertTrue(response.getStatus() == HTTPStatus.RESPONSE_STATUS_CREATED);

        response = webResource.type(MediaType.APPLICATION_JSON).
                delete(ClientResponse.class);
        Assert.assertNotNull(response);
        Assert.assertTrue(response.getStatus() == HTTPStatus.RESPONSE_STATUS_NOT_FOUND);
    }

    private int create(Employee emp) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        WebResource webResource = resource().path("/employees");
        ClientResponse response = webResource.type(MediaType.APPLICATION_JSON).
                post(ClientResponse.class, mapper.writeValueAsString(emp));
        Assert.assertNotNull(response);
        Assert.assertTrue(response.getStatus() == HTTPStatus.RESPONSE_STATUS_CREATED &&
                response.hasEntity());
        String answer = response.getEntity(String.class);
        return Integer.valueOf(answer.replace("Employee saved id: ", ""));
    }

}
