//////////////////// ALL ASSIGNMENTS INCLUDE THIS SECTION /////////////////////
//
// Title: Account.java
// Files: TransactionGroup.java, ExceptionalBankingTests.java
// Course: CS300 Fall 2018
//
// Author: Reece Lardy
// Email: RLardy@wisc.edu
// Lecturer's Name: Alexander Brooks
//
///////////////////////////// CREDIT OUTSIDE HELP /////////////////////////////
//
// Students who get help from sources other than their partner must fully
// acknowledge and credit those sources of help here. Instructors and TAs do
// not need to be credited here, but tutors, friends, relatives, room mates,
// strangers, and others do. If you received no outside help from either type
// of source, then please explicitly indicate NONE.
//
// Persons: (identify each person and describe their help in detail)
// Online Sources: (identify each URL and describe their assistance in detail)
//
/////////////////////////////// 80 COLUMNS WIDE ///////////////////////////////
import java.io.File;
import java.util.zip.DataFormatException;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * This class contains methods to access information in account object. Information of account
 * object includes transaction groups, transactions, balance, overdrafts, and ID
 */
public class Account {

  private static final int MAX_GROUPS = 10000; // sets max number of groups equal to 10,000
  private static int nextUniqueId = 1000; // sets nextUniqueId equal to 1,000
  private String name; // initializes a string to hold name
  private final int UNIQUE_ID; // initializes an int to be used as a unique ID
  private TransactionGroup[] transactionGroups; // creates an array of transaction groups
  private int transactionGroupsCount; // initializes variable to hold count of transactiongroups

  /**
   * Creates an account with the inputted name that holds a unique ID and numerous transactions of
   * various amounts
   * 
   * @param name represents the name of the account
   */
  public Account(String name) {
    this.name = name; // sets account name to input name
    this.UNIQUE_ID = Account.nextUniqueId; // sets unique id to next available unique id
    Account.nextUniqueId++; // increment next unique id
    this.transactionGroups = new TransactionGroup[MAX_GROUPS]; // creates transaction groups array
    this.transactionGroupsCount = 0; // set count of account transaction groups to 0

  }

  /**
   * Iterates through a file to find an account name, ID, and transactions with amounts
   * 
   * @param file represents a file containing account information
   */
  public Account(File file) throws FileNotFoundException {
    // NOTE: THIS METHOD SHOULD NOT BE CALLED MORE THAN ONCE, BECAUSE THE
    // RESULTING BEHAVIOR IS NOT DEFINED WITHIN THE JAVA SPECIFICATION ...
    if (!file.exists()) {
      throw new FileNotFoundException("File not found: " + file.getName());
    }
    Scanner in = new Scanner(file);
    // ... THIS WILL BE UPDATED TO READ FROM A FILE INSTEAD OF SYSTEM.IN.

    this.name = in.nextLine(); // set account name to first line of input
    this.UNIQUE_ID = Integer.parseInt(in.nextLine()); // set unique id to int of next line of input
    Account.nextUniqueId = this.UNIQUE_ID + 1; // set account next unique id to the current uniqueID
                                               // + 1
    this.transactionGroups = new TransactionGroup[MAX_GROUPS]; // creates transaction groups array
                                                               // of size max_groups
    this.transactionGroupsCount = 0; // set count of account transaction groups to 0
    String nextLine = "";
    while (in.hasNextLine()) // while there is more input in the file
      try {
        this.addTransactionGroup(in.nextLine()); // add nextline of input to account transaction
                                                 // group
                                                 // array
      } catch (DataFormatException e) {
        System.err.println(e.getMessage()); // print error message
      }
    in.close(); // close input
  }

  /**
   * Returns the unique ID of the account object
   * 
   * @return account.UniqueID represents the unique ID of the account object
   */
  public int getId() {
    return this.UNIQUE_ID; // returns account's unique ID
  }

  /**
   * Adds transaction group to account object
   * 
   * @param command represents a string containing transactions
   */
  public void addTransactionGroup(String command) throws DataFormatException {
    String[] parts = command.split(" "); // splits input by spaces and puts it into string array
    int[] newTransactions = new int[parts.length]; // creates transactions array with same size as
                                                   // string array
    for (int i = 0; i < parts.length; i++) // iterates through transaction array and string array
      try {
        newTransactions[i] = Integer.parseInt(parts[i]); // sets transaction value equal to int
                                                         // value
                                                         // of string array
      } catch (Exception e) {
        throw new DataFormatException(
            "addTransactionGroup requires string commands that contain only space separated integer values");
      }
    TransactionGroup t = new TransactionGroup(newTransactions); // creates transaction array

    this.transactionGroups[this.transactionGroupsCount] = t; // puts transactions array values into
                                                             // account transactions array
    this.transactionGroupsCount++; // increment account transactiongroups count
  }

  /**
   * Returns transaction count of all transaction groups in account object
   * 
   * @return transactionCount represents the total amount of transactions in account object.
   */
  public int getTransactionCount() {
    int transactionCount = 0; // initialize transaction count and set it to 0
    for (int i = 0; i < this.transactionGroupsCount; i++) // iterate through account
                                                          // transactiongroupcount
      transactionCount += this.transactionGroups[i].getTransactionCount(); // increment transaction
                                                                           // count by transaction
                                                                           // count in account
                                                                           // transactiongroup array
    return transactionCount; // return transaction count
  }

  /**
   * Gets the transaction amount of a given transaction
   * 
   * @param index represents a reference to the transaction which we are finding amount of
   * @return transaction amount of transaction at index, if it cannot be found, return -1
   */
  public int getTransactionAmount(int index) throws IndexOutOfBoundsException {
    int transactionCount = 0; // initialize transaction count and set it to 0
    for (int i = 0; i < this.transactionGroupsCount; i++) { // iterate through account
                                                            // transactiongroup count
      int prevTransactionCount = transactionCount; // set new previoustransactioncount equal to
                                                   // transaction count
      transactionCount += this.transactionGroups[i].getTransactionCount(); // add account's
                                                                           // transactioncount to
                                                                           // transactioncount
      if (transactionCount > index) { // if transactionCount is greater than input index
        index -= prevTransactionCount; // subtract previous transaction count from index
        return this.transactionGroups[i].getTransactionAmount(index); // return account's
      }
    }
    throw new IndexOutOfBoundsException(
        "Error: Index: " + index + ", Number of transactions: " + transactionCount); // throws error
  }

  /**
   * Gets the current balance of account object
   * 
   * @return balance represents the total balance in account object
   */
  public int getCurrentBalance() {
    int balance = 0; // initialize balance to 0
    int size = this.getTransactionCount(); // initialize size to account transaction count
    for (int i = 0; i < size; i++) // iterate through size
      balance += this.getTransactionAmount(i); // add account transaction amounts of account to
                                               // balance
    return balance; // return balance
  }

  /**
   * Gets the number of overdrafts in account object
   * 
   * @return overdraftCount which represents the total number of overdrafts in the account object
   */
  public int getNumberOfOverdrafts() {
    int balance = 0; // initialize balance to 0
    int overdraftCount = 0; // initialize overdraftcount to 0
    int size = this.getTransactionCount(); // initialize size to account transaction count
    for (int i = 0; i < size; i++) { // iterate through size
      int amount = this.getTransactionAmount(i); // initializes amount to account transaction amount
                                                 // at i
      balance += amount; // add amount to balance
      if (balance < 0 && amount < 0) // if balance and amount are less than 0
        overdraftCount++; // incement overdraft count
    }
    return overdraftCount; // return overdraft count
  }

}
