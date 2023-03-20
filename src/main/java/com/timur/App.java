package com.timur;

import java.util.Optional;
import java.util.Scanner;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        TicTacToe ttt = new TicTacToe();
        System.out.println("Welcome to TicTacToe!");
        System.out.println("To make a move, enter a board position by choosing an unoccupied row and column.");
        System.out.println("Specify your move as row,col (starting with 0 for the first row and column).\n");
        String player = "X";
        Scanner sc = new Scanner(System.in);
        Optional<String> winner;
        while (true) {
            ttt.displayBoard();
            winner = ttt.checkWin();
            if (winner.isPresent() && winner.get().equals("TIE GAME")) {
                System.out.println("It's a tie!");
                break;
            } else if (winner.isPresent()) {
                System.out.printf("Player %s has won!%n%n", winner.get());
                break;
            }

            if (player.equals("X")) {
                System.out.println("Player X, choose your next move!");
                String[] move = sc.next().split(",");
                int row = Integer.parseInt(move[0]);
                int column = Integer.parseInt(move[1]);
                try {
                    ttt.makeMove(row, column, player);
                } catch (Exception e) {
                    System.out.println(e);
                    continue;
                }
                player = "O";
            } else if (player.equals("O")) {
                System.out.println("Now I will make a move...");
                try {
                    ttt.makeRandomMove(player);
                    player = "X";
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }
    }
}
