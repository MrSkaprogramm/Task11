import com.andersen.tr.model.Car;
import com.andersen.tr.model.CarType;
import com.andersen.tr.service.impl.PersonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class AppControllerTest {
    private MockMvc mockMvc;
    private PersonService personService;

    @Autowired
    public AppControllerTest(MockMvc mockMvc, PersonService personService) {
        this.mockMvc = mockMvc;
        this.personService = personService;
    }

    @Test
    public void testGetSingleCar() throws Exception {
        // Arrange
        Car car = new Car();
        car.setId(1);
        car.setBrand("Toyota Camry");
        car.setType(CarType.SEDAN);
        when(personService.getSingleCar()).thenReturn(car);

        // Act
        MockHttpServletResponse response = mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/cars/1/"))
                .andReturn()
                .getResponse();

        // Assert
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString())
                .contains("Toyota Camry");
    }
}
