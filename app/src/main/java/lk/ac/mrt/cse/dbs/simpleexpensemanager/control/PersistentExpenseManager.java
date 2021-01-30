package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.DBAccess;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.InMemoryAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.InMemoryTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class PersistentExpenseManager extends ExpenseManager {
    private Context context;
    private DBAccess dbAccess;

    public PersistentExpenseManager(Context context) {

        this.context = context;
        this.dbAccess = new DBAccess(context);
        setup();
    }

    @Override
    public void updateAccountBalance(String accountNo, int day, int month, int year, ExpenseType expenseType, String amount)
            throws InvalidAccountException {

        SQLiteDatabase db = dbAccess.getWritableDatabase();

        db.beginTransaction();     //to keep DB in consistent state, grouping transactions.
        try {
            super.updateAccountBalance(accountNo, day, month, year, expenseType, amount);  //there are many transactions inside this.
            db.setTransactionSuccessful();
        } catch (Exception e){
            throw e;
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public void setup() {
        /*** Begin generating dummy data for In-Memory implementation ***/

        /*TransactionDAO inMemoryTransactionDAO = new InMemoryTransactionDAO();
        setTransactionsDAO(inMemoryTransactionDAO);

        AccountDAO inMemoryAccountDAO = new InMemoryAccountDAO();
        AccountDAO persistent = new PersistentAccountDAO(context);

        setAccountsDAO(persistent);
        //setAccountsDAO(inMemoryAccountDAO);
         */

        AccountDAO persistentADO = new PersistentAccountDAO(dbAccess);
        TransactionDAO persistentTDO = new PersistentTransactionDAO(dbAccess);

        setAccountsDAO(persistentADO);
        setTransactionsDAO(persistentTDO);

        // dummy data
        Account dummyAcct1 = new Account("12345A", "Yoda Bank", "Anakin Skywalker", 10000.0);
        Account dummyAcct2 = new Account("78945Z", "Clone BC", "Obi-Wan Kenobi", 80000.0);
        getAccountsDAO().addAccount(dummyAcct1);
        getAccountsDAO().addAccount(dummyAcct2);

        /*** End ***/
    }
}
