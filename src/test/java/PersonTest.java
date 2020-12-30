import com.revature.Person.Costumer;
import com.revature.Person.Employee;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

public class PersonTest {
    Costumer costumer;
    Employee employee;

    @Before
    public void init() {
        costumer = new Costumer("Tony", "Bennett", 123456789, "jazztony","password", LocalDate.parse("1926-08-03"));
        employee = new Employee("Onika", "Maraj", 124578246, "youngmoney", "password", LocalDate.parse("1982-12-08"));
    }

    @Test
    public void CanCreateNewCostumer(){
        Assert.assertNotNull(costumer);
        Assert.assertTrue(costumer instanceof Costumer);
    }

    @Test
    public void CanCreateNewEmployee(){
        Assert.assertNotNull(employee);
        Assert.assertTrue(employee instanceof Employee);
    }
}
