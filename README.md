# SimpleExpenseManager
This is an android based basic expense manager application which will be used as a lab assignment for CS3042 - Database Systems course module.

## Changes Implemented:
Persistent database was added using SQLite. Android's SQLiteDatabase and SQLiteHelper classes were used for this.
Database is initialized and accessed through DBAccess class, which is used by the PersistentAccountDAO, PersistentTransactionDAO and PersistentExpenseManager classes.

Added the ability to delete account by implementing the removeAccount method of AccountDAO interface. Also added a method to TransactionDAO interface which allows to delete all transactions linked to a particular account. 

Certain groups of actions which were executed together had the possibility of leaving the database in an inconsistent state if the execution failed halfway. The ExpenseManager class had methods (updateAccountBalance and removeAccount) which called such actions together without allowing for such a situation. Those methods were overriden in the PersistentExpenseManager class, where the methods of Android's SQLiteDatabase class, beginTransaction(), setTransactionSuccessful(), and endTransaction() were used to ensure consistency.

When a new account is inserted, or an old account is deleted, the dropdown lists of accounts and the transaction logs shown on the screen must be updated. Buttons were added at the base of each screen to manually refresh those views. If, without refreshing with these buttons, some action is attempted using an account which is no longer in the system, an error message will be displayed.



## Description
During this assignment we will be self-learning how to use an embedded database in an android application. This project is an android application that act as a skeleton. Following is the structure of the application.

![Project Structure](https://photos-6.dropbox.com/t/2/AABUCZrAwldw_NV3Ifvnp7oC4gSKnSgQpobs4XewmqyOfw/12/9253042/png/32x32/3/1449064800/0/2/simpleexpensemanager_project_structure.png/EICo8gYY42wgAigC/W5KIhq-w862dGqauYIvo4akPhVyFsdz4o9X_TOOCnjA?size_mode=3&size=1024x768)

The current implementation is non-persistent. Therefore all the account information and transactions are stored in the memory and once the application is closed, the information is lost.

Your task is to make the storage of account information and transactions persistent, using an embedded database such as SQLite as the persistent storage.

In order to achieve this you should implement the two interfaces, [`AccountDAO`](/app/src/main/java/lk/ac/mrt/cse/dbs/simpleexpensemanager/data/AccountDAO.java) and [`TransactionDAO`](/app/src/main/java/lk/ac/mrt/cse/dbs/simpleexpensemanager/data/TransactionDAO.java). These two interfaces follow the “Data Access Object” Design pattern [1](http://www.oracle.com/technetwork/java/dataaccessobject-138824.html) [2](http://www.tutorialspoint.com/design_pattern/data_access_object_pattern.htm). You can refer the current In-Memory implementation ([`InMemoryAccountDAO`](/app/src/main/java/lk/ac/mrt/cse/dbs/simpleexpensemanager/data/impl/InMemoryAccountDAO.java), [`InMemoryTransactionDAO`](/app/src/main/java/lk/ac/mrt/cse/dbs/simpleexpensemanager/data/impl/InMemoryTransactionDAO.java)) to get an idea of the current implementation.

After you have implemented the two interfaces, you can use these implementations to setup the application to use the persistent storage instead of the existing in-memory storage. In order to do that you should implement the `setup()` method of the abstract class [`ExpenseManager`](/app/src/main/java/lk/ac/mrt/cse/dbs/simpleexpensemanager/control/ExpenseManager.java). You can refer the current concrete implementation ([`InMemoryDemoExpenseManager`](/app/src/main/java/lk/ac/mrt/cse/dbs/simpleexpensemanager/control/InMemoryDemoExpenseManager.java)) of this class to get an idea.

After you have completed implementation of the [`ExpenseManager`](/app/src/main/java/lk/ac/mrt/cse/dbs/simpleexpensemanager/control/ExpenseManager.java) class, go to [`MainActivity`](/app/src/main/java/lk/ac/mrt/cse/dbs/simpleexpensemanager/ui/MainActivity.java) class in the ui package and change the existing implementation to your implementation.

eg:

Current implementation
``` Java
/***  Begin generating dummy data for In-Memory implementation  ***/
expenseManager = new InMemoryDemoExpenseManager();
/*** END ***/
```

Your implementation
``` Java
/***  Setup the persistent storage implementation  ***/
expenseManager = new PersistentExpenseManager(context);
/*** END ***/
```

You can make improvements to the project as you require. However this project is designed to act as a skeleton and minimize involvement in other components such as the UI. Therefore your main effort should be focused on implementing the persistent storage using an embedded database.

## Instructions
1. Fork the GitHub project - https://github.com/GayashanNA/SimpleExpenseManager
2. Clone your fork of the above project into your android studio IDE.
3. Implement the two interfaces
  * [`AccountDAO`](/app/src/main/java/lk/ac/mrt/cse/dbs/simpleexpensemanager/data/AccountDAO.java)
  * [`TransactionDAO`](/app/src/main/java/lk/ac/mrt/cse/dbs/simpleexpensemanager/data/TransactionDAO.java)
4. Extend and implement the `setup()` method of the abstract class [`ExpenseManager`](/app/src/main/java/lk/ac/mrt/cse/dbs/simpleexpensemanager/control/ExpenseManager.java).
5. Change current implementation to your implementation in the [`MainActivity`](/app/src/main/java/lk/ac/mrt/cse/dbs/simpleexpensemanager/ui/MainActivity.java) ui class.

  Current implementation
  ```Java
  /***  Begin generating dummy data for In-Memory implementation  ***/
  expenseManager = new InMemoryDemoExpenseManager();
  /*** END ***/
  ```
  Your implementation
  ```Java
  /***  Setup the persistent storage implementation  ***/
  expenseManager = new PersistentExpenseManager(context);
  /*** END ***/
  ```
6. Commit your code and push to your forked repository in GitHub.
7. Download your project as a Zip from GitHub and submit as the completed assignment.

