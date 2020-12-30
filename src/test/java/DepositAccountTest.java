import com.revature.Account.CheckingAccount;
import com.revature.Account.DepositAccount;
import com.revature.Account.SavingsAccount;
import com.revature.Person.Costumer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

public class DepositAccountTest {
    DepositAccount chkAcct;
    DepositAccount svgsAcct;
    Costumer costumer;

    @Before
    public void init() {
        costumer = new Costumer("Tony",  "Bennett", 123456789, "jazztony","password", LocalDate.parse("1926-08-03"));
        chkAcct = new CheckingAccount(12345, 500, costumer);
        svgsAcct = new SavingsAccount(12356, 1000, costumer);
    }

    @Test
    public void canCreateNewCheckingAccount() {
        Assert.assertNotNull(chkAcct);
        Assert.assertTrue(chkAcct instanceof CheckingAccount);
    }

    @Test
    public void canCreateNewSavingsAccount() {
        Assert.assertNotNull(svgsAcct);
        Assert.assertTrue(svgsAcct instanceof SavingsAccount);
    }

    @Test
    public void DepositToCheckingAccount(){
        double expectedBalance = chkAcct.getBalance() + 150.50;
        chkAcct.Deposit(150.50, false, 0);
        Assert.assertEquals(expectedBalance, chkAcct.getBalance(), 0.0000001);
    }

    @Test
    public void DepositToSavingsAccount(){
        double expectedBalance = svgsAcct.getBalance() + 150.50;
        svgsAcct.Deposit(150.50, false, 0);
        Assert.assertEquals(expectedBalance, svgsAcct.getBalance(), 0.0000001);
    }

    @Test
    public void WithdrawalFromCheckingAccount(){
        double expectedBalance = chkAcct.getBalance() - 50.00;
        chkAcct.Withdraw(50.00, false, 0);
        Assert.assertEquals(expectedBalance, chkAcct.getBalance(), 0.0000001);
    }

    @Test
    public void WithdrawalFromSavingsAccount(){
        double expectedBalance = svgsAcct.getBalance() - 50.00;
        svgsAcct.Withdraw(50.00, false, 0);
        Assert.assertEquals(expectedBalance, svgsAcct.getBalance(), 0.0000001);
    }

    @Test
    public void TransferFromCheckingToSavings(){
        double chkBalance = chkAcct.getBalance() - 125.00;
        double svgsBalance = svgsAcct.getBalance() + 125.00;
        chkAcct.Transfer(svgsAcct, 125.00);
        Assert.assertEquals(chkBalance, chkAcct.getBalance(), 0.0000001);
        Assert.assertEquals(svgsBalance, svgsAcct.getBalance(), 0.0000001);
    }

    @Test
    public void TransferFromSavingsToChecking(){
        double chkBalance = chkAcct.getBalance() + 125.00;
        double svgsBalance = svgsAcct.getBalance() - 125.00;
        svgsAcct.Transfer(chkAcct, 125.00);
        Assert.assertEquals(chkBalance, chkAcct.getBalance(), 0.0000001);
        Assert.assertEquals(svgsBalance, svgsAcct.getBalance(), 0.0000001);
    }
}
