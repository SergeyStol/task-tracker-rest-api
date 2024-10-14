package dev.sstol.tasktrackerrestapi.chainofresponsibility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static java.lang.System.in;

/**
 * @author Sergey Stol
 * 2024-10-09
 */
public class NumbersDataCollect  {
   public static void main(String[] args) {
      Scanner scanner = new Scanner(in);
      int value = scanner.nextInt();

      Map<String, Boolean> data = checkNumber(value);
      System.out.println(data);
   }
//   static Map<String, Boolean> checkNumber(int value) {
//      Map<String, Boolean> data = new HashMap<>();
//      data.put("isEven", value % 2 == 0);
//      data.put("isNegative", value < 0);
//      data.put("isPrime", isPrime(value));
//
//      return data;
//   }

//   static private boolean isPrime(int value) {
//      if (value < 2) {
//         return false;
//      }
//      for (int i = 2; i <= Math.sqrt(value); i++) {
//         if (value % i == 0) {
//            return false;
//         }
//      }
//      return true;
//   }

   static Map<String, Boolean> checkNumber(int value) {
      Map<String, Boolean> data = new HashMap<>();
      List<Handler> checkers = new ArrayList<>();
      checkers.add(new EvenOddHandler());
      checkers.add(new NegativePositiveHandler());
      checkers.add(new PrimeNumberHandler());
      checkers.forEach(handler -> handler.handle(value, data));
      return data;
   }
}

interface Handler {
   void handle(int number, Map<String, Boolean> data);
}

class EvenOddHandler implements Handler {
   @Override
   public void handle(int number, Map<String, Boolean> data) {
      data.put("isEven", number % 2 == 0);
   }
}

class NegativePositiveHandler implements Handler {
   @Override
   public void handle(int number, Map<String, Boolean> data) {
      data.put("isNegative", number < 0);
   }
}

class PrimeNumberHandler implements Handler {
   @Override
   public void handle(int number, Map<String, Boolean> data) {
      data.put("isPrime", isPrime(number));
   }

   private boolean isPrime(int value) {
      if (value < 2) {
         return false;
      }
      for (int i = 2; i <= Math.sqrt(value); i++) {
         if (value % i == 0) {
            return false;
         }
      }
      return true;
   }
}
