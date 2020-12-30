import com.revature.Account.CheckingAccount;
import com.revature.Account.DepositAccount;
import com.revature.Data.CostumerRepo;
import com.revature.Data.DepositAccountRepo;
import com.revature.Person.Costumer;
import com.revature.Utils.ConnectionUtil;
import com.revature.Utils.PostgresConnectionUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;

public class DepositAccountRepoTest {
    Costumer costumer;
    DepositAccount account;
    DepositAccountRepo repo;
    CostumerRepo costumerRepo;
    Properties dbProps;
    ConnectionUtil util;

    @Before
    public void init() {
        try {
            dbProps = new Properties();
            dbProps.load(new FileReader(ClassLoader.getSystemClassLoader().getResource("db.properties").getFile()));
            util = new PostgresConnectionUtil(dbProps);
            repo = new DepositAccountRepo();
            costumerRepo =  new CostumerRepo();
        } catch (SQLException | IOException throwable) {    }

        costumer = new Costumer("Tony",  "Bennett", ThreadLocalRandom.current().nextInt(1000000, 10000000), "jazztony","password", LocalDate.parse("1926-08-03"));
        account = new CheckingAccount(ThreadLocalRandom.current().nextInt(1000000, 10000000), 1000, costumer, LocalDate.now());

    }

    @Test
    public void CanSaveDepositAccount(){
        try{
            util = new PostgresConnectionUtil(dbProps);
            repo.setConnection(util);
            costumerRepo.setConnection(util);
            costumerRepo.save(costumer);
            repo.save(account);


            DepositAccount fromDB = repo.getById(account.getAccountNumber());
            Assert.assertEquals(fromDB.getAccountNumber(), account.getAccountNumber());
            Assert.assertEquals(fromDB.getBalance(), 1000, 000000.1);
            Assert.assertEquals(fromDB.getAccountDOB(), LocalDate.now());
            Assert.assertEquals(fromDB.getOwner().getFirstName(), "Tony");
            Assert.assertEquals(fromDB.getOwner().getLastName(), "Bennett");
            Assert.assertEquals(fromDB.getOwner().getSSN(), 123456999);
            Assert.assertEquals(fromDB.getOwner().getUserName(), "jazztony");
            Assert.assertEquals(fromDB.getOwner().getPassword(), "password");
            Assert.assertEquals(fromDB.getOwner().getDOB().toString(), "1926-08-03");

        } catch (Exception e) {
        }
    }

    @Test
    public void CanGetById(){
        try{
            util = new PostgresConnectionUtil(dbProps);
            repo.setConnection(util);
            costumerRepo.setConnection(util);
            costumerRepo.save(costumer);
            repo.save(account);

            DepositAccount fromDB = repo.getById(account.getAccountNumber());
            Assert.assertEquals(fromDB.getAccountNumber(), account.getAccountNumber());
            Assert.assertEquals(fromDB.getBalance(), 1000, 000000.1);
            Assert.assertEquals(fromDB.getAccountDOB(), LocalDate.now());
            Assert.assertEquals(fromDB.getOwner().getFirstName(), "Tony");
            Assert.assertEquals(fromDB.getOwner().getLastName(), "Bennett");
            Assert.assertEquals(fromDB.getOwner().getSSN(), costumer.getSSN());
            Assert.assertEquals(fromDB.getOwner().getUserName(), "jazztony");
            Assert.assertEquals(fromDB.getOwner().getPassword(), "password");
            Assert.assertEquals(fromDB.getOwner().getDOB().toString(), "1926-08-03");

        } catch (Exception e) {
        }
    }

    @Test
    public void canUpdateAccount(){
        try{
            util = new PostgresConnectionUtil(dbProps);
            repo.setConnection(util);
            costumerRepo.setConnection(util);
            costumerRepo.save(costumer);
            repo.save(account);

            account.Deposit(1250, false, 0);
            repo.save(account);

            DepositAccount fromDB = repo.getById(account.getAccountNumber());
            Assert.assertEquals(fromDB.getAccountNumber(), account.getAccountNumber());
            Assert.assertEquals(fromDB.getBalance(), 2250, 000000.1);
            Assert.assertEquals(fromDB.getAccountDOB(), LocalDate.now());
            Assert.assertEquals(fromDB.getOwner().getFirstName(), "Tony");
            Assert.assertEquals(fromDB.getOwner().getLastName(), "Bennett");
            Assert.assertEquals(fromDB.getOwner().getSSN(), costumer.getSSN());
            Assert.assertEquals(fromDB.getOwner().getUserName(), "jazztony");
            Assert.assertEquals(fromDB.getOwner().getPassword(), "password");
            Assert.assertEquals(fromDB.getOwner().getDOB().toString(), "1926-08-03");

        } catch (Exception e) {
        }
    }
}