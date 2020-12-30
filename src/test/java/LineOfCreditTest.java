import com.revature.Account.LineOfCreditAccount;
import com.revature.Person.Costumer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

public class LineOfCreditTest {
    LineOfCreditAccount account;
    Costumer costumer;

    @Before
    public void init() {
        costumer = new Costumer("Tony","Bennett", 123456789, "jazztony","password", LocalDate.parse("1926-08-03"));
        account = new LineOfCreditAccount(123456, costumer, 1000, 9.5);
    }

    @Test
    public void canCreateNewLineOfCreditAccount() {
        Assert.assertNotNull(account);
        Assert.assertTrue(account instanceof LineOfCreditAccount);
    }
}
