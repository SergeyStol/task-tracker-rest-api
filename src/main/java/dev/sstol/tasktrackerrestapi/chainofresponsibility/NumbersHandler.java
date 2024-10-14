//package dev.sstol.tasktrackerrestapi.chainofresponsibility;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Scanner;
//
//import static java.lang.System.in;
//
///**
// * @author Sergey Stol
// * 2024-10-09
// */
//public class NumbersHandler {
//   public static void main(String[] args) {
//      Scanner scanner = new Scanner(in);
//      int value = scanner.nextInt();
//
//      checkNumber(value);
//   }
//   static void checkNumber(int value) {
//      List<Handler> checkers = new ArrayList<>();
//      checkers.add(new EvenOddHandler());
//      checkers.add(new NegativePositiveHandler());
//      checkers.add(new PrimeNumberHandler());
//      checkers.forEach(handler -> handler.handle(value));
//   }
//}
//
//interface Handler {
//   void handle(int number);s
//}
//
//class EvenOddHandler implements Handler {
//   @Override
//   public void handle(int number) {
//      if (number % 2 == 0) {
//         System.out.println("The value is even");
//      } else {
//         System.out.println("The value is odd");
//      }
//   }
//}
//
//class NegativePositiveHandler implements Handler {
//   @Override
//   public void handle(int number) {
//      if (number < 0) {
//         System.out.println("The value is negative");
//      } else {
//         System.out.println("The value is positive");
//      }
//   }
//}
//
//class PrimeNumberHandler implements Handler {
//   @Override
//   public void handle(int number) {
//      if (isPrime(number)) {
//         System.out.println("The value is prime");
//      } else {
//         System.out.println("The value is not prime");
//      }
//   }
//
//   private boolean isPrime(int value) {
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
//}