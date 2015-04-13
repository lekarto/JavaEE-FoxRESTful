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
import org.foxresult.entity.Department;
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
public class DepartmentResourceTest extends JerseyTest {

    private static String DEPARTMENT_TEST_NAME = "Test Department";

    private static int RESPONSE_STATUS_OK = 200;
    private static int RESPONSE_STATUS_CREATED = 201;
    private static int RESPONSE_STATUS_NO_DATA = 204;
    private static int RESPONSE_STATUS_BAD_REQUEST = 400;
    private static int RESPONSE_STATUS_NOT_FOUND = 404;
    private static int RESPONSE_STATUS_NOT_ALLOWED = 405;
    private static int RESPONSE_STATUS_UNSUPPORTED_MEDIA_TYPE = 415;
    private static int RESPONSE_STATUS_SERVER_ERROR = 500;

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
        WebResource webResource = resource().path("/departments");
        ClientResponse response = webResource
                .accept(MediaType.APPLICATION_JSON)
                .get(ClientResponse.class);
        Assert.assertNotNull(response);
        Assert.assertTrue((response.getStatus() == RESPONSE_STATUS_OK) &&
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
        WebResource webResource = resource().path("/departments");
        ClientResponse response = webResource
                .accept(MediaType.APPLICATION_JSON)
                .delete(ClientResponse.class);
        Assert.assertNotNull(response);
        Assert.assertTrue(response.getStatus() == RESPONSE_STATUS_NOT_ALLOWED);
    }

    @Test
    public void getByIDTest() {
        WebResource webResource = resource().path("/departments/1");
        ClientResponse response = webResource
                .accept(MediaType.APPLICATION_JSON)
                .get(ClientResponse.class);
        Assert.assertNotNull(response);
        Assert.assertTrue((response.getStatus() == RESPONSE_STATUS_OK) &&
                           response.hasEntity());
        try {
            JSONObject jsonObject = new JSONObject(response.getEntity(String.class));
            System.out.println(jsonObject);
            System.out.println(jsonObject.get("id"));
            Assert.assertEquals(1, jsonObject.get("id"));
            Assert.assertEquals("Developers", jsonObject.get("name"));
            Assert.assertEquals(110d, jsonObject.get("averageSalary"));
            Assert.assertEquals(2, jsonObject.get("employeeCount"));
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void getByIDWrongTest() {
        WebResource webResource = resource().path("/departments/10305");
        ClientResponse response = webResource
                .accept(MediaType.APPLICATION_JSON)
                .get(ClientResponse.class);
        Assert.assertNotNull(response);
        Assert.assertTrue(response.getStatus() == RESPONSE_STATUS_NO_DATA);
    }

    @Test
    public void getDepartmentsNamesTest() {
        WebResource webResource = resource().path("/departments/names");
        ClientResponse response = webResource
                .accept(MediaType.APPLICATION_JSON)
                .get(ClientResponse.class);
        Assert.assertNotNull(response);
        Assert.assertTrue(response.getStatus() == RESPONSE_STATUS_OK &&
                          response.hasEntity());
        try {
            JSONArray array = new JSONArray(response.getEntity(String.class));
            Assert.assertTrue(array.length() > 0);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void getDepartmentsNamesWrongTest() {
        WebResource webResource = resource().path("/departments/names");
        ClientResponse response = webResource
                .accept(MediaType.APPLICATION_JSON)
                .put(ClientResponse.class);
        Assert.assertNotNull(response);
        Assert.assertTrue(response.getStatus() == RESPONSE_STATUS_NOT_ALLOWED);
    }

    @Test
    public void createTest() throws IOException {
        Department dep = new Department(DEPARTMENT_TEST_NAME);
        WebResource webResource = resource().path("/departments");
        ClientResponse response = webResource.type(MediaType.APPLICATION_JSON).
                post(ClientResponse.class, mapper.writeValueAsString(dep));
        Assert.assertNotNull(response);
        Assert.assertTrue(response.getStatus() == RESPONSE_STATUS_CREATED &&
                response.hasEntity());
        String answer = response.getEntity(String.class);
        Assert.assertTrue(answer.matches("Department saved id: [\\d]"));
    }

    @Test
    public void createWrongTest() throws IOException {
        Department dep = new Department();
        WebResource webResource = resource().path("/departments");
        ClientResponse response = webResource.type(MediaType.APPLICATION_JSON).
                post(ClientResponse.class, mapper.writeValueAsString(dep));
        Assert.assertNotNull(response);
        Assert.assertTrue(response.getStatus() == RESPONSE_STATUS_SERVER_ERROR &&
                response.hasEntity());
        String answer = response.getEntity(String.class);
        Assert.assertTrue(answer.matches("Server error"));
    }

    @Test
    public void createUpdateTest() throws IOException {
        Department dep = new Department(DEPARTMENT_TEST_NAME);
        dep.setId(create(dep));
        dep.setName("Changed Department Name");

        WebResource webResource = resource().path("/departments/"+dep.getId());
        ClientResponse response = webResource.type(MediaType.APPLICATION_JSON).
                put(ClientResponse.class, mapper.writeValueAsString(dep));
        Assert.assertNotNull(response);
        Assert.assertTrue(response.getStatus() == RESPONSE_STATUS_OK);
    }

    @Test
    public void updateWrongTest() throws IOException {
        Department dep = new Department(100, DEPARTMENT_TEST_NAME);
        WebResource webResource = resource().path("/departments/5");
        ClientResponse response = webResource.type(MediaType.APPLICATION_JSON).
                put(ClientResponse.class, mapper.writeValueAsString(dep));
        Assert.assertNotNull(response);
        Assert.assertTrue(response.getStatus() == RESPONSE_STATUS_UNSUPPORTED_MEDIA_TYPE &&
                response.hasEntity());
    }

    @Test
    public void updateNullTest() throws IOException {
        WebResource webResource = resource().path("/departments/5");
        ClientResponse response = webResource.type(MediaType.APPLICATION_JSON).
                put(ClientResponse.class, mapper.writeValueAsString(null));
        Assert.assertNotNull(response);
        Assert.assertTrue(response.getStatus() == RESPONSE_STATUS_BAD_REQUEST &&
                response.hasEntity());
    }

    @Test
    public void createDeleteTest() throws IOException {
        Department dep = new Department(DEPARTMENT_TEST_NAME);
        dep.setId(create(dep));

        WebResource webResource = resource().path("/departments/"+dep.getId());
        ClientResponse response = webResource.type(MediaType.APPLICATION_JSON).
                delete(ClientResponse.class);
        Assert.assertNotNull(response);
        Assert.assertTrue(response.getStatus() == RESPONSE_STATUS_CREATED);

        response = webResource.type(MediaType.APPLICATION_JSON).
                delete(ClientResponse.class);
        Assert.assertNotNull(response);
        Assert.assertTrue(response.getStatus() == RESPONSE_STATUS_NOT_FOUND);
    }

    private int create(Department dep) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        WebResource webResource = resource().path("/departments");
        ClientResponse response = webResource.type(MediaType.APPLICATION_JSON).
                post(ClientResponse.class, mapper.writeValueAsString(dep));
        Assert.assertNotNull(response);
        Assert.assertTrue(response.getStatus() == RESPONSE_STATUS_CREATED &&
                response.hasEntity());
        String answer = response.getEntity(String.class);
        return Integer.valueOf(answer.replace("Department saved id: ", ""));
    }
}