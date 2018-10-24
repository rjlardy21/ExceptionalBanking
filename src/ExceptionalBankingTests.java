//////////////////// ALL ASSIGNMENTS INCLUDE THIS SECTION /////////////////////
//
// Title: ExceptionalBankingTests.java
// Files: TransactionGroup.java, Account.java
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
import java.util.zip.DataFormatException;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * This class contains test methods to check the correctness of methods in Account.java and
 * TransactionGroup.java
 */
public class ExceptionalBankingTests {
  /**
   * The main method calls all tests methods and prints method failures.
   */
  public static void main(String[] args) {
    try {
      int fails = 0;
      if (!testAccountBalance()) { // if test fails
        System.out.println("AccountBalance Test failed.");
        fails++; // increment fails
      }
      if (!testOverdraftCount()) { // if test fails
        System.out.println("OverdraftCount Test failed.");
        fails++;
      }
      if (!testTransactionGroupEmpty()) { // if test fails
        System.out.println("TransactiongGroupEmpty Test failed.");
        fails++;
      }
      if (!testTransactionGroupInvalidEncoding()) { // if test fails
        System.out.println("TransactionGroupInvalidEncoding Test failed.");
        fails++;
      }
      if (!testAccountAddNegativeQuickWithdraw()) { // if test fails
        System.out.println("AccountAddNegativeQuickWithdraw Test failed.");
        fails++;
      }
      if (!testAccountBadTransactionGroup()) { // if test fails
        System.out.println("AccountBadTransactionGroup Test failed.");
        fails++;
      }
      if (!testAccountIndexOutOfBounds()) { // if test fails
        System.out.println("AccountIndexOutOfBounds Test failed.");
        fails++;
      }
      if (!testAccountMissingFile()) { // if test fails
        System.out.println("AccountMissingFile Test failed.");
        fails++;
      }
      if (fails == 0) { // if no test fails
        System.out.println("All tests passed!");
      }
    } catch (DataFormatException e) {
      System.err.println(e.getMessage());
    } catch (IndexOutOfBoundsException e1) {
      System.err.println(e1.getMessage());
    } catch (OutOfMemoryError e2) {
      System.err.println(e2.getMessage());
    }
  }

  /**
   * Creates transaction groups of each encoding type and compares the account balance to the
   * expected value
   * 
   * @throws DataFormatException
   * @returns boolean representing whether or not the test passed
   */
  public static boolean testAccountBalance() throws DataFormatException {
    Account test = new Account("test"); // creates new account object
    test.addTransactionGroup("0 1 1"); // binary deposit of $2
    test.addTransactionGroup("1 20 15 10"); // integer deposit of $45
    test.addTransactionGroup("2 4 0 0 0"); // quick withdraw to remove $80
    test.addTransactionGroup("1 80"); // integer deposit of $80
    if (test.getCurrentBalance() == 47) { // compares account balance to expected value
      return true;
    }
    return false;
  }

  /**
   * Creates transaction groups of each encoding type and compares the account overdraftcount to the
   * expected value
   * 
   * @throws DataFormatException
   * @returns boolean representing whether or not the test passed
   */
  public static boolean testOverdraftCount() throws DataFormatException {
    Account test = new Account("test"); // creates new account object
    test.addTransactionGroup("0 1 1"); // binary deposit of $2
    test.addTransactionGroup("1 20 15 10"); // integer deposit of $45
    test.addTransactionGroup("2 4 0 0 0"); // quick withdraw to remove $80
    test.addTransactionGroup("1 80"); // integer deposit of $80
    if (test.getNumberOfOverdrafts() == 2) { // compares account overdrafts to expected value
      return true;
    }
    return false;
  }

  /**
   * This method checks whether the TransactionGroup constructor throws an exception with an
   * appropriate message, when it is passed an empty int[].
   * 
   * @return true when test verifies correct functionality, and false otherwise.
   */
  public static boolean testTransactionGroupEmpty() {
    try {
      TransactionGroup test = new TransactionGroup(null); // creates empty transaction group
    } catch (DataFormatException e) {
      System.out.println(e.getMessage()); // print error message
      return true;
    }
    return false;
  }

  /**
   * This method checks whether the TransactionGroup constructor throws an exception with an
   * appropriate message, when then first int in the provided array is not 0, 1, or 2.
   * 
   * @return true when test verifies correct functionality, and false otherwise.
   */
  public static boolean testTransactionGroupInvalidEncoding() {
    try {
      Account test = new Account("test"); // creates new account object
      test.addTransactionGroup("3 4 2 9"); // add transaction group with invalid encoding
    } catch (DataFormatException e) {
      System.out.println(e.getMessage()); // print error message
      return true;
    }
    return false;
  }

  /**
   * This method checks whether the Account.addTransactionGroup method throws an exception with an
   * appropriate message, when it is passed a quick withdraw encoded group that contains negative
   * numbers of withdraws.
   * 
   * @return true when test verifies correct functionality, and false otherwise.
   */
  public static boolean testAccountAddNegativeQuickWithdraw() {
    try {
      Account test = new Account("test"); // creates new account object
      test.addTransactionGroup("2 2 -3 2 0"); // add transaction group with negative quick withdraw
    } catch (DataFormatException e) {
      System.out.println(e.getMessage()); // print error message
      return true;
    }
    return false;
  }

  /**
   * This method checks whether the Account.addTransactionGroup method throws an exception with an
   * appropriate message, when it is passed a string with space separated English words (non-int).
   * 
   * @return true when test verifies correct functionality, and false otherwise.
   */
  public static boolean testAccountBadTransactionGroup() {
    try {
      Account test = new Account("test"); // create new account object
      test.addTransactionGroup("1 jetski 5 7"); // add transaction group with a string
    } catch (DataFormatException e) {
      System.out.println(e.getMessage()); // print error message
      return true;
    }
    return false;
  }

  /**
   * This method checks whether the Account.getTransactionAmount method throws an exception with an
   * appropriate message, when it is passed an index that is out of bounds.
   * 
   * @return true when test verifies correct functionality, and false otherwise.
   */
  public static boolean testAccountIndexOutOfBounds() throws DataFormatException {
    try {
      Account test = new Account("test"); // create new account object
      test.addTransactionGroup("2 1 1 0 0"); // add a transaction group with two transactions
      test.getTransactionAmount(2); // get transaction amount at index 2
    } catch (IndexOutOfBoundsException e1) {
      System.out.println(e1.getMessage()); // print error message
      return true;
    }
    return false;
  }

  /**
   * This method checks whether the Account constructor that takes a File parameter throws an
   * exception with an appropriate message, when it is passed a File object that does not correspond
   * to an actual file within the file system.
   * 
   * @return true when test verifies correct functionality, and false otherwise.
   */
  public static boolean testAccountMissingFile() {
    File file = new File("a"); // creates file object
    try {
      Account test = new Account(file); // create account object from file
    } catch (FileNotFoundException e) {
      System.out.println(e.getMessage()); // print error message
      return true;
    }
    return false;
  }
}
