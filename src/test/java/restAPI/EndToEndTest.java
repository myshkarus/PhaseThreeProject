package restAPI;

import org.testng.annotations.Test;
import org.testng.AssertJUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class EndToEndTest {

	RequestSpecification request;

	public EndToEndTest() {
		RestAssured.baseURI = "http://localhost:3000";
		this.request = RestAssured.given();
	}

	@Test
	public void test1() {

		Response response = getAllEmployees();
		AssertJUnit.assertEquals(response.getStatusCode(), 200);

		response = createEmployee("John", 8000);
		int id = response.jsonPath().get("id");
		AssertJUnit.assertEquals(response.getStatusCode(), 201);

		response = getSingleEmployee(id);
		AssertJUnit.assertEquals(response.getStatusCode(), 200);
		AssertJUnit.assertEquals(response.jsonPath().get("name"), "John");

		response = updateEmployee(id, "Smith", 8000);
		AssertJUnit.assertEquals(response.getStatusCode(), 200);

		response = getSingleEmployee(id);
		AssertJUnit.assertEquals(response.getStatusCode(), 200);
		AssertJUnit.assertEquals(response.jsonPath().get("name"), "Smith");

		response = deleteEmployee(id);
		AssertJUnit.assertEquals(response.getStatusCode(), 200);

		response = getSingleEmployee(id);
		AssertJUnit.assertEquals(response.getStatusCode(), 404);

		response = getAllEmployees();
		List<String> employees = response.jsonPath().get("id");
		AssertJUnit.assertFalse(employees.contains(id));
	}

	public Response getAllEmployees() {
		Response response = this.request.get("employees");
		return response;
	}

	public Response getSingleEmployee(int empId) {
		Response response = this.request.get("employees/" + empId);
		return response;
	}

	public Response createEmployee(String name, int salary) {
		Map<String, Object> mapObj = new HashMap<String, Object>();
		mapObj.put("name", name);
		mapObj.put("salary", salary);
		Response response = this.request.contentType(ContentType.JSON).accept(ContentType.JSON).body(mapObj)
				.post("employees/create");
		return response;
	}

	public Response updateEmployee(int empId, String name, int salary) {
		Map<String, Object> mapObj = new HashMap<String, Object>();
		mapObj.put("name", name);
		mapObj.put("salary", salary);
		Response response = this.request.contentType(ContentType.JSON).accept(ContentType.JSON).body(mapObj)
				.patch("employees/update/" + empId);
		return response;
	}

	public Response deleteEmployee(int empId) {
		Response response = this.request.delete("employees/" + empId);
		return response;
	}

}
