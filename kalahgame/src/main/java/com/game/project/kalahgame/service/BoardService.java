package com.game.project.kalahgame.service;

import com.game.project.kalahgame.model.Game;
import com.game.project.kalahgame.model.GameBoard;
import com.game.project.kalahgame.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Class to handle @{@link GameBoard} related actions
 */
@Service
@Transactional
public class BoardService {

    private BoardRepository boardRepository;

    /**
     * BoardService constructor
     *
     * @param boardRepository @{@link BoardRepository} dependency
     */
    @Autowired
    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    /**
     * Create new board
     *
     * @param game @{@link Game} for which to create GameBoard
     * @return @{@link GameBoard} that has been created
     */
    public GameBoard createNewBoard(Game game) {
        GameBoard board = new GameBoard(game);

        boardRepository.save(board);

        return board;
    }

    /**
     * Retrieve a GameBoard by @{@link Game}
     *
     * @param game @{@link Game} to retrieve GameBoard for
     * @return @{@link GameBoard} matching Game given
     */
    public GameBoard getBoardByGame(Game game) {
        return boardRepository.findByGame(game);
    }

}
