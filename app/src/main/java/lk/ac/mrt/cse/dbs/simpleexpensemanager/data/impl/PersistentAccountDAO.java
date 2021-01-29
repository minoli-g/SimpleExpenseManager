package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.DBAccess;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class PersistentAccountDAO implements AccountDAO{

    private DBAccess dbAccess;

    public PersistentAccountDAO(DBAccess dbAccess) {
        this.dbAccess = dbAccess;
    }

    @Override
    public List<String> getAccountNumbersList() {

        SQLiteDatabase db = dbAccess.getReadableDatabase();
        String[] columns = {DBAccess.AccountTable.COLUMN_ACC_NO};

        Cursor cursor = db.query(DBAccess.AccountTable.TABLE_NAME,columns,null,null,null,null,null);

        List<String> account_nos = new ArrayList<String>();

        while (cursor.moveToNext()){
            account_nos.add(cursor.getString(cursor.getColumnIndex(DBAccess.AccountTable.COLUMN_ACC_NO)));
        }

        cursor.close();
        return account_nos;

    }

    @Override
    public List<Account> getAccountsList() {

        SQLiteDatabase db = dbAccess.getReadableDatabase();
        String[] columns = {DBAccess.AccountTable.COLUMN_ACC_NO,DBAccess.AccountTable.COLUMN_BANK,
                DBAccess.AccountTable.COLUMN_HOLDER, DBAccess.AccountTable.COLUMN_BALANCE};

        Cursor cursor = db.query(DBAccess.AccountTable.TABLE_NAME,columns,null,null,null,null,null);

        List<Account> accounts = new ArrayList<Account>();

        while(cursor.moveToNext()){
            String acc_no = cursor.getString(cursor.getColumnIndex(DBAccess.AccountTable.COLUMN_ACC_NO));
            String bank = cursor.getString(cursor.getColumnIndex(DBAccess.AccountTable.COLUMN_BANK));
            String holder = cursor.getString(cursor.getColumnIndex(DBAccess.AccountTable.COLUMN_HOLDER));
            Double balance = cursor.getDouble(cursor.getColumnIndex(DBAccess.AccountTable.COLUMN_BALANCE));

            Account newAcc = new Account(acc_no,bank,holder,balance);

            accounts.add(newAcc);
        }

        return accounts;

    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        return null;
    }

    @Override
    public void addAccount(Account account) {

        SQLiteDatabase db = dbAccess.getWritableDatabase();

        ContentValues account_info = new ContentValues();
        account_info.put(DBAccess.AccountTable.COLUMN_ACC_NO,account.getAccountNo());
        account_info.put(DBAccess.AccountTable.COLUMN_BANK,account.getBankName());
        account_info.put(DBAccess.AccountTable.COLUMN_HOLDER,account.getAccountHolderName());
        account_info.put(DBAccess.AccountTable.COLUMN_BALANCE,account.getBalance());

        long newRowID = db.insert(DBAccess.AccountTable.TABLE_NAME,null,account_info);

    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {

    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {

    }



}
