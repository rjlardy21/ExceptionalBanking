//////////////////// ALL ASSIGNMENTS INCLUDE THIS SECTION /////////////////////
//
// Title: TransactionGroup.java
// Files: ExceptionalBankingTests.java, Account.java
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

/**
 * This class contains methods to operate on transaction groups from account object
 */
public class TransactionGroup {

  private enum EncodingType {
    BINARY_AMOUNT, INTEGER_AMOUNT, QUICK_WITHDRAW
  };

  private EncodingType type;
  private int[] values;

  /**
   * Checks for DataFormatExceptions then encodes the input integer array
   * 
   * @param groupEncoding, an integer array containing transactions
   * @throws DataFormatException
   */
  public TransactionGroup(int[] groupEncoding) throws DataFormatException {
    if (groupEncoding == null) {
      throw new DataFormatException("transaction group encoding cannot be null or empty");
    }
    if (groupEncoding[0] != 0 && groupEncoding[0] != 1 && groupEncoding[0] != 2) {
      throw new DataFormatException(
          "the first element within a transaction group must be 0, 1, or 2");
    }
    for (int i = 1; i < groupEncoding.length; i++) {
      if (groupEncoding[0] == 0 && (groupEncoding[i] != 0 && groupEncoding[i] != 1)) {
        throw new DataFormatException(
            "binary amount transaction groups may only contain 0s and 1s");
      }
      if (groupEncoding[0] == 0 && groupEncoding[i] == 0) {
        throw new DataFormatException("integer amount transaction groups may not contain 0s");
      }
      if (groupEncoding[0] == 2 && groupEncoding.length != 5) {
        throw new DataFormatException("quick withdraw transaction groups must contain 5 elements");
      }
      if (groupEncoding[0] == 2 && groupEncoding[i] < 0) {
        throw new DataFormatException(
            "quick withdraw transaction groups may not contain negative numbers");
      }
    }
    this.type = EncodingType.values()[groupEncoding[0]]; // sets transaction group type equal to the
                                                         // encoding type of the first int in
                                                         // groupEncoding
    this.values = new int[groupEncoding.length - 1]; // creates values array to hold transactions
    for (int i = 0; i < values.length; i++) // iterates through values array
      this.values[i] = groupEncoding[i + 1]; // sets values array to the values of the next group
                                             // encoding element
  }

  /**
   * Calculates the number of transactions in the transaction group
   * 
   * @returns transactionCount, the number of transactions in the transaction group
   */
  public int getTransactionCount() {
    int transactionCount = 0;
    switch (this.type) {
      case BINARY_AMOUNT:
        int lastAmount = -1;
        for (int i = 0; i < this.values.length; i++) { // iterates through transaction values
          if (this.values[i] != lastAmount) { // if the value is not equal to lastAmount
            transactionCount++; // increment transaction count
            lastAmount = this.values[i]; // set lastAmount equal to the value
          }
        }
        break;
      case INTEGER_AMOUNT:
        transactionCount = values.length; // transaction count is equal to the length of value array
        break;
      case QUICK_WITHDRAW:
        for (int i = 0; i < this.values.length; i++) // iterate through values array
          transactionCount += this.values[i]; // add value to transactionCount
    }
    return transactionCount;
  }

  /**
   * Calculates the transaction amount at the input index
   * 
   * @param transactionIndex, representing the transaction of which we are finding the transaction
   *        amount
   * @throws IndexOutOfBoundsException
   * @returns transactionCount, the number of transactions in the transaction group
   */
  public int getTransactionAmount(int transactionIndex) throws IndexOutOfBoundsException {
    if (transactionIndex < this.values.length && transactionIndex >= 0) { // if transaction index is
                                                                          // 0 or higher and less
                                                                          // than the length of
                                                                          // transaction values
      int transactionCount = 0;
      switch (this.type) {
        case BINARY_AMOUNT:
          int lastAmount = -1;
          int amountCount = 0;
          for (int i = 0; i <= this.values.length; i++) { // iterate through values array
            if (i == this.values.length || this.values[i] != lastAmount) { // if i is equal to the
                                                                           // values array length or
                                                                           // value isn't equal to
                                                                           // lastAmount
              if (transactionCount - 1 == transactionIndex) {
                if (lastAmount == 0)
                  return -1 * amountCount;
                else
                  return +1 * amountCount;
              }
              transactionCount++; // increment transactionCount
              lastAmount = this.values[i]; // set lastAmount to the value
              amountCount = 1;
            } else
              amountCount++; // increment amountCount
            lastAmount = this.values[i];// set lastAmount to the value
          }
          break;
        case INTEGER_AMOUNT:
          return this.values[transactionIndex];
        case QUICK_WITHDRAW:
          final int[] QW_AMOUNTS = new int[] {-20, -40, -80, -100};
          for (int i = 0; i < this.values.length; i++) // iterate through values array
            for (int j = 0; j < this.values[i]; j++) // iterate through value
              if (transactionCount == transactionIndex)
                return QW_AMOUNTS[i];
              else
                transactionCount++; // increment transactionCount
      }
    } else {
      throw new IndexOutOfBoundsException(
          "Error: Index: " + transactionIndex + ", Number of transactions: " + this.values.length);
    }
    return -1;
  }
}
