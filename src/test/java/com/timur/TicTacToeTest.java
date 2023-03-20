package com.timur;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TicTacToeTest {
    private TicTacToe ttt;
    private static final String defaultPlayer = "X";

    private void occupyBoard(String player) throws Exception {
        for (int row = 0; row < TicTacToe.ROW_COUNT; row++) {
            for (int column = 0; column < TicTacToe.COLUMN_COUNT; column++) {
                this.ttt.makeMove(row, column, player);
            }
        }
    }

    private void occupyDiagonalLeftToRight(String player) throws Exception {
        for (int row = 0; row < TicTacToe.ROW_COUNT; row++) {
            this.ttt.makeMove(row, row, player);
        }
    }

    private void occupyDiagonalRightToLeft(String player) throws Exception {
        for (int row = 0; row < TicTacToe.ROW_COUNT; row++) {
            this.ttt.makeMove(2 - row, row, player);
        }
    }

    private void occupyRow(int rowNumber, String player) throws Exception {
        for (int column = 0; column < TicTacToe.COLUMN_COUNT; column++) {
            this.ttt.makeMove(rowNumber, column, player);
        }
    }

    private void occupyColumn(int columnNumber, String player) throws Exception {
        for (int row = 0; row < TicTacToe.ROW_COUNT; row++) {
            this.ttt.makeMove(row, columnNumber, player);
        }
    }

    private void makeTie(String playerOne, String playerTwo) throws Exception {
        String currentPlayer = playerOne;
        // Start from 1 to make a tie
        int currentPlayerMoveCount = 1;
        for (int row = 0; row < TicTacToe.ROW_COUNT; row++) {
            for (int column = 0; column < TicTacToe.COLUMN_COUNT; column++) {
                // If a player made 3 moves in a row, switch the player
                if (currentPlayerMoveCount > 2) {
                    currentPlayer = currentPlayer.equals(playerOne) ? playerTwo : playerOne;
                    currentPlayerMoveCount = 0;
                }
                this.ttt.makeMove(row, column, currentPlayer);
                currentPlayerMoveCount++;
            }
        }
    }


    @BeforeEach
    void setUp() {
        this.ttt = new TicTacToe();
    }

    @Test
    void validatePlayerWhenInvalidThenThrowsException() {
        // Arrange
        String invalidPlayer = "C";
        String expectedErrorMessage = String.format("Bad player %s", invalidPlayer);

        // Act
        Exception exception = assertThrows(Exception.class, () -> this.ttt.validatePlayer(invalidPlayer));

        // Assert
        assertEquals(expectedErrorMessage, exception.getMessage());
    }

    @Test
    void validatePlayerWhenValidThenDoesNotThrowException() {
        // Assert
        assertDoesNotThrow(() -> this.ttt.validatePlayer(defaultPlayer));
    }

    @Test
    void validateBoardPositionWhenInvalidThenThrowsException() {
        // Arrange
        int invalidRow = -1;
        int invalidColumn = -1;
        String expectedErrorMessage = String.format("Illegal board position %d, %d", invalidRow, invalidColumn);

        // Act
        Exception exception = assertThrows(Exception.class, () -> this.ttt.validateBoardPosition(invalidRow, invalidColumn));

        // Assert
        assertEquals(expectedErrorMessage, exception.getMessage());
    }

    @Test
    void validateBoardPositionWhenValidThenDoesNotThrowException() {
        // Arrange
        int validRow = 0;
        int validColumn = 0;

        // Assert
        assertDoesNotThrow(() -> this.ttt.validateBoardPosition(validRow, validColumn));
    }

    @Test
    void validateMoveWhenOccupiedThenThrowsException() throws Exception {
        // Arrange
        int occupiedRow = 0;
        int occupiedColumn = 0;
        this.ttt.makeMove(occupiedRow, occupiedColumn, "O");
        String expectedErrorMessage = String.format("Board position %d, %d occupied", occupiedRow, occupiedColumn);

        // Act
        Exception exception = assertThrows(Exception.class, () -> this.ttt.validateMove(occupiedRow, occupiedColumn));

        // Assert
        assertEquals(expectedErrorMessage, exception.getMessage());
    }

    @Test
    void validateMoveWhenUnoccupiedThenDoesNotThrowException() {
        // Arrange
        int unoccupiedRow = 0;
        int unoccupiedColumn = 0;

        // Assert
        assertDoesNotThrow(() -> this.ttt.validateMove(unoccupiedRow, unoccupiedColumn));
    }

    @Test
    void makeMoveWhenValidReturnsTrue() throws Exception {
        // Arrange
        int validRow = 0;
        int validColumn = 0;

        // Act
        boolean isMoveMade = this.ttt.makeMove(validRow, validColumn, defaultPlayer);

        // Assert
        assertTrue(isMoveMade);
    }

    @Test
    void makeMoveWhenInvalidThrowsException() {
        // Arrange
        int invalidRow = -1;
        int invalidColumn = -1;
        String invalidPlayer = "";

        // Assert
        assertThrows(Exception.class, () -> this.ttt.makeMove(invalidRow, invalidColumn, invalidPlayer));
    }


    @Test
    void getAvailableSquaresWhenSquaresAvailableReturnsList() {
        // Act
        List<Integer[]> availableSquares = this.ttt.getAvailableSquares();

        // Assert
        assertFalse(availableSquares.isEmpty());
    }

    @Test
    void getAvailableSquaresWhenNoSquaresAvailableReturnsEmptyList() throws Exception {
        // Arrange
        this.occupyBoard(defaultPlayer);

        // Act
        List<Integer[]> availableSquares = this.ttt.getAvailableSquares();

        // Assert
        assertTrue(availableSquares.isEmpty());
    }

    @Test
    void calculateAvailableSquaresWhenSquaresAvailableReturnsSize() {
        // Arrange
        int expectedSquareCount = TicTacToe.ROW_COUNT * TicTacToe.COLUMN_COUNT;

        // Act
        int availableSquareCount = this.ttt.calculateAvailableSquares();

        // Assert
        assertEquals(expectedSquareCount, availableSquareCount);
    }

    @Test
    void calculateAvailableSquaresWhenNoSquaresAvailableReturnsZero() throws Exception {
        // Arrange
        int expectedSquareCount = 0;
        this.occupyBoard(defaultPlayer);

        // Act
        int availableSquareCount = this.ttt.calculateAvailableSquares();

        // Assert
        assertEquals(expectedSquareCount, availableSquareCount);
    }

    @Test
    void makeRandomMoveWhenSquaresAvailableReturnsTrue() throws Exception {
        // Act
        boolean isMoveMade = this.ttt.makeRandomMove("X");

        // Assert
        assertTrue(isMoveMade);
    }

    @Test
    void checkDiagonalWinWhenDiagonalIsOccupiedLeftToRightReturnsWinner() throws Exception {
        // Arrange
        this.occupyDiagonalLeftToRight(defaultPlayer);

        // Act
        Optional<String> winner = this.ttt.checkDiagonalWin();

        // Assert
        assertTrue(winner.isPresent());
        assertEquals(defaultPlayer, winner.get());
    }

    @Test
    void checkDiagonalWinWhenDiagonalIsOccupiedRightToLeftReturnsWinner() throws Exception {
        // Arrange
        this.occupyDiagonalRightToLeft(defaultPlayer);

        // Act
        Optional<String> winner = this.ttt.checkDiagonalWin();

        // Assert
        assertTrue(winner.isPresent());
        assertEquals(defaultPlayer, winner.get());
    }

    @Test
    void checkDiagonalWinWhenCenterSquareIsEmptyReturnsEmptyOptional() {
        // Act
        Optional<String> winner = this.ttt.checkDiagonalWin();

        // Assert
        assertFalse(winner.isPresent());
    }

    @Test
    void checkDiagonalWinWhenCenterSquareIsNotEmptyAndDiagonalsAreNotOccupiedReturnsEmptyOptional() throws Exception {
        // Arrange
        this.ttt.makeMove(TicTacToe.ROW_COUNT / 2, TicTacToe.COLUMN_COUNT / 2, defaultPlayer);

        // Act
        Optional<String> winner = this.ttt.checkDiagonalWin();

        // Assert
        assertFalse(winner.isPresent());
    }

    @Test
    void checkRowWinWhenRowIsOccupiedReturnsWinner() throws Exception {
        // Arrange
        this.occupyRow(0, defaultPlayer);

        // Act
        Optional<String> winner = this.ttt.checkRowWin();

        // Assert
        assertTrue(winner.isPresent());
        assertEquals(defaultPlayer, winner.get());
    }

    @Test
    void checkRowWinWhenRowIsNotOccupiedReturnsEmptyOptional() {
        // Act
        Optional<String> winner = this.ttt.checkRowWin();

        // Assert
        assertFalse(winner.isPresent());
    }


    @Test
    void checkColumnWinWhenColumnIsOccupiedReturnsWinner() throws Exception {
        // Arrange
        this.occupyColumn(0, defaultPlayer);

        // Act
        Optional<String> winner = this.ttt.checkColumnWin();

        // Assert
        assertTrue(winner.isPresent());
        assertEquals(defaultPlayer, winner.get());
    }

    @Test
    void checkColumnWinWhenColumnIsNotOccupiedReturnsEmptyOptional() {
        // Act
        Optional<String> winner = this.ttt.checkColumnWin();

        // Assert
        assertFalse(winner.isPresent());
    }

    @Test
    void checkWinWhenBoardIsEmptyReturnsEmptyOptional() {
        // Act
        Optional<String> winner = this.ttt.checkWin();

        // Assert
        assertFalse(winner.isPresent());
    }

    @Test
    void checkWinWhenBoardIsNotEmptyAndNoWinnerReturnsTie() throws Exception {
        // Arrange
        String expectedResult = "TIE GAME";
        String playerTwo = "O";
        this.makeTie(defaultPlayer, playerTwo);

        // Act
        Optional<String> winner = this.ttt.checkWin();

        // Assert
        assertTrue(winner.isPresent());
        assertEquals(expectedResult, winner.get());
    }

    @Test
    void checkWinWhenWinnerIsPresentReturnsWinner() throws Exception {
        // Arrange
        this.occupyRow(0, defaultPlayer);

        // Act
        Optional<String> winner = this.ttt.checkWin();

        // Assert
        assertTrue(winner.isPresent());
        assertEquals(defaultPlayer, winner.get());
    }
}