import com.revature.Data.CostumerRepo;
import com.revature.Person.Costumer;
import com.revature.Utils.ConnectionUtil;
import com.revature.Utils.PostgresConnectionUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Properties;

public class CostumerRepoTest {
    Costumer costumer;
    CostumerRepo repo;
    Properties dbProps;
    ConnectionUtil util;

    @Before
    public void init() {
        try {
            dbProps = new Properties();
            dbProps.load(new FileReader(ClassLoader.getSystemClassLoader().getResource("db.properties").getFile()));
            util = new PostgresConnectionUtil(dbProps);
            repo = new CostumerRepo();
        } catch (SQLException | IOException throwable) {
        }
        costumer = new Costumer("Tony",  "Bennett", 123456999, "jazztony","password", LocalDate.parse("1926-08-03"));
    }

    @Test
    public void CanSaveCostumer(){
        try{
            util = new PostgresConnectionUtil(dbProps);
            repo.setConnection(util);
            repo.save(costumer);

            Costumer fromDB = repo.getById(123456999);
            Assert.assertEquals(fromDB.getFirstName(), "Tony");
            Assert.assertEquals(fromDB.getLastName(), "Bennett");
            Assert.assertEquals(fromDB.getSSN(), 123456999);
            Assert.assertEquals(fromDB.getUserName(), "jazztony");
            Assert.assertEquals(fromDB.getPassword(), "password");
            Assert.assertEquals(fromDB.getDOB().toString(), "1926-08-03");

        } catch (Exception e) {
        }
    }

    @Test
    public void canGetById(){
        try{
            util = new PostgresConnectionUtil(dbProps);
            repo.setConnection(util);

            Costumer fromDB = repo.getById(123456999);
            Assert.assertEquals(fromDB.getFirstName(), "Tony");
            Assert.assertEquals(fromDB.getLastName(), "Bennett");
            Assert.assertEquals(fromDB.getSSN(), 123456999);
            Assert.assertEquals(fromDB.getUserName(), "jazztony");
            Assert.assertEquals(fromDB.getPassword(), "password");
            Assert.assertEquals(fromDB.getDOB().toString(), "1926-08-03");

        } catch (Exception e) {
        }
    }
}
