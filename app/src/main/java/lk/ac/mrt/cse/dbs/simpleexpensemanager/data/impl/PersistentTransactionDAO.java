package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.DBAccess;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class PersistentTransactionDAO implements TransactionDAO {

    private DBAccess dbAccess;

    public PersistentTransactionDAO(DBAccess dbAccess){
        this.dbAccess = dbAccess;
    }


    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {

        SQLiteDatabase db = dbAccess.getWritableDatabase();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);

        ContentValues transaction = new ContentValues();
        transaction.put(DBAccess.TransactionTable.COLUMN_ACC_NO,accountNo);
        transaction.put(DBAccess.TransactionTable.COLUMN_DATE, sdf.format(date));
        transaction.put(DBAccess.TransactionTable.COLUMN_TYPE,expenseType.toString());
        transaction.put(DBAccess.TransactionTable.COLUMN_AMOUNT,amount);

        long newRowID = db.insert(DBAccess.TransactionTable.TABLE_NAME,null,transaction);

        //change account balance, add synchronization in application and DB level
        updateAccount(date,accountNo,expenseType,amount);
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {

        SQLiteDatabase db = dbAccess.getReadableDatabase();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-mm-yyyy", Locale.ENGLISH);

        String[] columns = {DBAccess.TransactionTable.COLUMN_ACC_NO,DBAccess.TransactionTable.COLUMN_DATE,
                DBAccess.TransactionTable.COLUMN_TYPE, DBAccess.TransactionTable.COLUMN_AMOUNT};

        Cursor cursor = db.query(DBAccess.TransactionTable.TABLE_NAME,columns,null,null,null,null,null);

        List<Transaction> transactions = new ArrayList<Transaction>();

        while(cursor.moveToNext()){

            Date date = new Date();
            try { date = sdf.parse(cursor.getString(cursor.getColumnIndex(DBAccess.TransactionTable.COLUMN_DATE))); }
            catch (ParseException e) {}

            String acc_no = cursor.getString(cursor.getColumnIndex(DBAccess.TransactionTable.COLUMN_ACC_NO));
            ExpenseType type = (cursor.getString(cursor.getColumnIndex(DBAccess.TransactionTable.COLUMN_TYPE))=="EXPENSE") ? ExpenseType.EXPENSE : ExpenseType.INCOME;
            Double amount = cursor.getDouble(cursor.getColumnIndex(DBAccess.TransactionTable.COLUMN_AMOUNT));

            Transaction newTrans = new Transaction(date,acc_no,type,amount);

            transactions.add(newTrans);
        }

        return transactions;
    }

    private boolean updateAccount(Date date, String accountNo, ExpenseType type, double amount){

        SQLiteDatabase db = dbAccess.getWritableDatabase();

        String[] columns = {DBAccess.AccountTable.COLUMN_BALANCE};

        //want to get current balance of that account
        Cursor cursor = db.query(DBAccess.AccountTable.TABLE_NAME, columns,
                DBAccess.AccountTable.COLUMN_ACC_NO + "= \"" +accountNo+ "\"",
                null,null,null,null);

        cursor.moveToNext();
        Double balance = cursor.getDouble(cursor.getColumnIndex(DBAccess.AccountTable.COLUMN_BALANCE));

        //update that record with the new balance
        Double newBalance = (type==ExpenseType.EXPENSE) ? (balance-amount) : (balance+amount);

        String UPDATE_SQL = "UPDATE " + DBAccess.AccountTable.TABLE_NAME +
                " set " + DBAccess.AccountTable.COLUMN_BALANCE + " = " + newBalance +
                " WHERE " + DBAccess.AccountTable.COLUMN_ACC_NO + " = \"" + accountNo + "\";";

        db.execSQL(UPDATE_SQL);
        return true;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {

        List<Transaction> transactions = getAllTransactionLogs();
        int size = transactions.size();
        if (size <= limit) {
            return transactions;
        }
        // return the last <code>limit</code> number of transaction logs
        return transactions.subList(size - limit, size);
    }
}
