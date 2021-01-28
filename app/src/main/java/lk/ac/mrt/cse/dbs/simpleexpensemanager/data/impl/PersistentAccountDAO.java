package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;
import android.database.sqlite.*;

public class PersistentAccountDAO extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Expenses.db";
    public static final String SQL_CREATE_ENTRIES = "CREATE TABLE `account` (\n" +
            "  `id` int NOT NULL,\n" +
            "  `acc_no` varchar(10) NOT NULL,\n" +
            "  `bank` varchar(30) NOT NULL,\n" +
            "  `holder` varchar(50) NOT NULL,\n" +
            "  `balance` decimal(5,2) NOT NULL,\n" +
            "  PRIMARY KEY (`id`)\n" +
            ")";

    public PersistentAccountDAO(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){}


}
