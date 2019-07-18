package com.game.project.kalahgame.service;

import com.game.project.kalahgame.model.Game;
import com.game.project.kalahgame.model.GameBoard;
import com.game.project.kalahgame.model.Pit;
import com.game.project.kalahgame.model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Class to handle player actions.
 */
@Service
@Transactional
public class PlayService {

    private GameService gameService;
    private BoardService boardService;
    private PitService pitService;

    // Game constants
    public static final int NR_OF_PITS = 14;
    public static final int P1_LOWER_BOUNDARY = 1;
    public static final int P1_UPPER_BOUNDARY = 6;
    public static final int P2_LOWER_BOUNDARY = 8;
    public static final int P2_UPPER_BOUNDARY = 13;
    public static final int P1_STORE = 7;
    public static final int P2_STORE = 14;

    /**
     * PlayService constructor
     *
     * @param gameService @{@link GameService} dependency
     * @param boardService @{@link BoardService} dependency
     * @param pitService @{@link PitService} dependency
     */
    @Autowired
    public PlayService(GameService gameService, BoardService boardService, PitService pitService) {
        this.gameService = gameService;
        this.boardService = boardService;
        this.pitService = pitService;
    }

    /**
     * Function to check if it is the Player's turn
     *
     * @param game @{@link Game} to check for
     * @param player @{@link Player} to check for
     * @return boolean of the result
     */
    public boolean isTurn(Game game, Player player) {
        return game.getPlayerTurn() == player;
    }

    /**
     * Function to do a Player Move
     *
     * @param game @{@link Game} to do move for
     * @param player @{@link Player} to do move for
     * @param position to move
     * @return @{@link GameBoard} after the move
     */
    public GameBoard doMove(Game game, Player player, int position) {
        GameBoard board = boardService.getBoardByGame(game);

        // Check turn + game is Active
        if(isTurn(game, player) && game.getState() != Game.State.GAME_OVER) {
            // P1
            if(player == game.getFirstPlayer()) {
                // If P1 handle P1 move
                board = handleFirstPlayerMove(game, position);
            }
            else {
                // If P2 handle P2 move
                board = handleSecondPlayerMove(game, position);
            }
        } // else do nothing

        return board;
    }

    /**
     * Function to retrieve the Score for a Player
     *
     * @param game @{@link Game} to retrieve Score for
     * @param player @{@link Player} to retrieve Score for
     * @return the Score
     */
    public int getScore(Game game, Player player) {
        // Get board
        GameBoard board = boardService.getBoardByGame(game);

        // Score is nr of stones in Store

        // P1
        if(player == game.getFirstPlayer()) {
            return pitService.getPitNumberOfStonesByBoardAndPosition(board, P1_STORE);
        }
        // P2
        else {
            return pitService.getPitNumberOfStonesByBoardAndPosition(board, P2_STORE);
        }
    }

    /**
     * Function to handle a P1 move
     *
     * @param game @{@link Game} of the game to move for
     * @param position to move
     * @return @{@link GameBoard} after move
     */
    public GameBoard handleFirstPlayerMove(Game game, int position) {
        // Get GameBoard
        GameBoard board = boardService.getBoardByGame(game);

        // Check if pit not empty
        int nrOfStones = pitService.getPitNumberOfStonesByBoardAndPosition(board, position);

        // Validate pit position is >= 1 and <= 6
        if(position >= P1_LOWER_BOUNDARY && position <= P1_UPPER_BOUNDARY && nrOfStones > 0) {
            // Do Move
            int index = sowStones(board, position, P2_UPPER_BOUNDARY, false);

            // Check capture
            checkCapture(board, index, P1_LOWER_BOUNDARY, P1_UPPER_BOUNDARY, P1_STORE);

            // Check turn
            if (index != P1_STORE) {
                // Switch turn
                gameService.switchTurn(game.getSecondPlayer(), game.getId());
            }

            // Check game finished
            boolean isFinished = checkFinished(game, board);

            if(isFinished) {
                emptyAllPits(board);
                gameService.updateGameState(game, Game.State.GAME_OVER);
            }
        }

        return board;
    }

    /**
     * Function to handle a P2 move
     *
     * @param game @{@link Game} of the game to move for
     * @param position to move
     * @return @{@link GameBoard} after move
     */
    public GameBoard handleSecondPlayerMove(Game game, int position) {
        // Get GameBoard
        GameBoard board = boardService.getBoardByGame(game);

        // Check if pit not empty
        int nrOfStones = pitService.getPitNumberOfStonesByBoardAndPosition(board, position);

        // Validate pit position is >= 8 and <= 13
        if(position >= P2_LOWER_BOUNDARY && position <= P2_UPPER_BOUNDARY && nrOfStones > 0) {
            // Do Move
            int index = sowStones(board, position, P2_STORE, true);

            // Check capture
            checkCapture(board, index, P2_LOWER_BOUNDARY, P2_UPPER_BOUNDARY, P2_STORE);

            // Check turn
            if (index != P2_STORE) {
                // Switch turn
                gameService.switchTurn(game.getFirstPlayer(), game.getId());
            }

            // Check game finished
            boolean isFinished = checkFinished(game, board);

            if(isFinished) {
                emptyAllPits(board);
                gameService.updateGameState(game, Game.State.GAME_OVER);
            }
        }

        return board;
    }

    /**
     * Function to sow the stones one by one until empty
     *
     * @param board @{@link GameBoard} of the game
     * @param position To start sowing from
     * @param upper Max boundary to start at index 1 again
     * @param skipP1Store boolean if needs to skip P1 store (i.e. is P2)
     * @return The index of the @{@link Pit} last sowed on
     */
    public int sowStones(GameBoard board, int position, int upper, boolean skipP1Store) {
        // Get nr of stones from startPit and empty
        int amount = pitService.getPitNumberOfStonesByBoardAndPosition(board, position);
        pitService.updatePitNumberOfStones(board, position, 0);

        // Start on pos + 1
        int index = position + 1;

        // Start Sowing
        while (amount != 0) {
            if(index > upper) {
                index = P1_LOWER_BOUNDARY;
            }
            else if (skipP1Store && index == P1_STORE) {
                // Skip P1 store
                index = P2_LOWER_BOUNDARY;
            }

            // Add stone for every pit
            pitService.updatePitNumberOfStonesByOne(board, index);

            // Next index, lower amount
            index++;
            amount--;
        }

        index--; // Save last position checked
        return index;
    }

    /**
     * Function to check for and execute capture of stones across
     *
     * @param board @{@link GameBoard} of the game
     * @param index Of the @{@link Pit} last sowed on
     * @param lower Lower boundary of current Player's small pits
     * @param upper Upper boundary of current Player's small pits
     * @param store Current Player's store
     */
    public void checkCapture(GameBoard board, int index, int lower, int upper, int store) {
        // Check if between boundaries, i.e. landed on his own house and if the house was empty
        if(index >= lower && index <= upper
                && pitService.getPitNumberOfStonesByBoardAndPosition(board, index) == 1) {
            // Capture stones across
            int indexAcross = NR_OF_PITS - index;
            int amountAcross = pitService.getPitNumberOfStonesByBoardAndPosition(board, indexAcross);

            if(amountAcross > 0) {
                // Empty own house + across
                pitService.updatePitNumberOfStones(board, indexAcross, 0);
                pitService.updatePitNumberOfStones(board, index, 0);

                // Add to store
                pitService.updatePitNumberOfStonesByAmount(board, store, (amountAcross + 1));
            }
        }
    }

    /**
     * Function to check if the game is finished
     *
     * @param game @{@link Game} to check for
     * @param board @{@link GameBoard} to check for
     * @return boolean if the game is finished
     */
    public boolean checkFinished(Game game, GameBoard board) {

        boolean isFinished = true;

        int lower;
        int upper;

        // If current Player has no stones left game is Finished
        if(game.getPlayerTurn() == game.getFirstPlayer()) {
            lower = P1_LOWER_BOUNDARY;
            upper = P1_UPPER_BOUNDARY;
        }
        else {
            lower = P2_LOWER_BOUNDARY;
            upper = P2_UPPER_BOUNDARY;
        }

        // Check Player small pits for stones
        for (int i = lower; i <= upper; i++) {
            if (pitService.getPitNumberOfStonesByBoardAndPosition(board, i) > 0) {
                isFinished = false;
                break;
            }
        }

        return isFinished;
    }

    /**
     * Function to empty all pits when game is finished
     *
     * @param board @{@link GameBoard} to empty Pits for
     */
    public void emptyAllPits(GameBoard board) {
        // Empty P1 small pits
        for (int i = P1_LOWER_BOUNDARY; i <= P1_UPPER_BOUNDARY; i++) {
            int tmpAmount = pitService.getPitNumberOfStonesByBoardAndPosition(board, i);
            if (tmpAmount > 0) {
                pitService.updatePitNumberOfStones(board, i, 0);
                pitService.updatePitNumberOfStonesByAmount(board, P1_STORE, tmpAmount);
            }
        }

        // Empty P2 small pits
        for (int i = P2_LOWER_BOUNDARY; i <= P2_UPPER_BOUNDARY; i++) {
            int tmpAmount = pitService.getPitNumberOfStonesByBoardAndPosition(board, i);
            if (tmpAmount > 0) {
                pitService.updatePitNumberOfStones(board, i, 0);
                pitService.updatePitNumberOfStonesByAmount(board, P2_STORE, tmpAmount);
            }
        }
    }
}
