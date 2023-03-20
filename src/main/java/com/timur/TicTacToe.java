package com.timur;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class TicTacToe {
    private final List<List<Optional<String>>> board;
    public static final int ROW_COUNT = 3;
    public static final int COLUMN_COUNT = 3;


    public TicTacToe() {
        this.board = new ArrayList<>();
        for (int row = 0; row < ROW_COUNT; row++) {
            List<Optional<String>> boardRow = new ArrayList<>();
            for (int column = 0; column < COLUMN_COUNT; column++) {
                boardRow.add(Optional.empty());
            }
            this.board.add(boardRow);
        }
    }

    public boolean makeMove(int row, int column, String player) throws Exception {
        this.validatePlayer(player);
        this.validateBoardPosition(row, column);
        this.validateMove(row, column);
        this.board.get(row).set(column, Optional.of(player));
        return true;
    }

    public boolean makeRandomMove(String player) throws Exception {
        List<Integer[]> availableSquares = this.getAvailableSquares();
        Random rand = new Random();
        Integer[] randomSquare = availableSquares.get(rand.nextInt(availableSquares.size()));
        return this.makeMove(randomSquare[0], randomSquare[1], player);
    }

    protected Optional<String> checkDiagonalWin() {
        Optional<String> centerSquare = this.board.get(1).get(1);
        if (!centerSquare.isPresent()) {
            return Optional.empty();
        }
        if (centerSquare.equals(this.board.get(0).get(0)) && centerSquare.equals(this.board.get(2).get(2))) {
            return centerSquare;
        } else if (centerSquare.equals(this.board.get(0).get(2)) && centerSquare.equals(this.board.get(2).get(0))) {
            return centerSquare;
        }
        return Optional.empty();
    }

    protected Optional<String> checkRowWin() {
        for (int row = 0; row < ROW_COUNT; row++) {
            if (this.board.get(row).get(0).equals(this.board.get(row).get(1)) && this.board.get(row).get(0).equals(this.board.get(row).get(2))) {
                return this.board.get(row).get(0);
            }
        }
        return Optional.empty();
    }

    protected Optional<String> checkColumnWin() {
        for (int column = 0; column < COLUMN_COUNT; column++) {
            if (this.board.get(0).get(column).equals(this.board.get(1).get(column)) && this.board.get(0).get(column).equals(this.board.get(2).get(column))) {
                return this.board.get(0).get(column);
            }
        }
        return Optional.empty();
    }

    public Optional<String> checkWin() {
        List<Optional<String>> winners = new ArrayList<>();
        winners.add(this.checkDiagonalWin());
        winners.add(this.checkRowWin());
        winners.add(this.checkColumnWin());
        for (Optional<String> winner : winners) {
            if (winner.isPresent()) {
                return winner;
            }
        }

        int availableSquares = this.calculateAvailableSquares();
        if (availableSquares == 0) {
            return Optional.of("TIE GAME");
        }

        return Optional.empty();
    }

    public void displayBoard() {
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < ROW_COUNT; row++) {
            for (int column = 0; column < COLUMN_COUNT; column++) {
                Optional<String> player = this.board.get(row).get(column);
                sb.append(player.orElse(" "));
                if (column != 2) {
                    sb.append(" | ");
                }
            }
            sb.append("\n--- --- ---\n");
        }
        System.out.println(sb);
    }

    public List<Integer[]> getAvailableSquares() {
        List<Integer[]> availableSquares = new ArrayList<>();
        for (int row = 0; row < ROW_COUNT; row++) {
            for (int column = 0; column < COLUMN_COUNT; column++) {
                if (!this.board.get(row).get(column).isPresent()) {
                    Integer[] position = {row, column};
                    availableSquares.add(position);
                }
            }
        }
        return availableSquares;
    }

    public int calculateAvailableSquares() {
        return this.getAvailableSquares().size();
    }

    public void validatePlayer(String player) throws Exception {
        if (!player.equals("X") && !player.equals("O")) {
            throw new Exception(String.format("Bad player %s", player));
        }
    }

    public void validateBoardPosition(int row, int column) throws Exception {
        if (row < 0 || row > 2 || column < 0 || column > 2) {
            throw new Exception(String.format("Illegal board position %d, %d", row, column));
        }
    }

    public void validateMove(int row, int column) throws Exception {
        if (this.board.get(row).get(column).isPresent()) {
            throw new Exception(String.format("Board position %d, %d occupied", row, column));
        }
    }
}
