package com.game.project.kalahgame.service;

import com.game.project.kalahgame.model.GameBoard;
import com.game.project.kalahgame.model.Pit;
import com.game.project.kalahgame.repository.PitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Class for @{@link Pit} related actions
 */
@Service
@Transactional
public class PitService {

    private PitRepository pitRepository;

    /**
     * PitService constructor
     *
     * @param pitRepository @{@link PitRepository} dependency
     */
    @Autowired
    public PitService(PitRepository pitRepository) {
        this.pitRepository = pitRepository;
    }

    /**
     * Function to create a Pit
     *
     * @param board @{@link GameBoard} to create on
     * @param pitType @{@link Pit.PitType} of the Pit to create
     * @param position of the Pit
     * @param nrOfStones number of stones in the Pit
     * @return @{@link Pit} created
     */
    public Pit createPit(GameBoard board, Pit.PitType pitType, int position, int nrOfStones) {
        // Create Pit
        Pit pit = new Pit(board, position, nrOfStones, pitType);

        // Save Pit
        pitRepository.save(pit);

        return pit;
    }

    /**
     * Function to update the number of stones on a Pit
     *
     * @param board @{@link GameBoard} the Pit is on
     * @param position of the Pit
     * @param nrOfStones new number of stones in the Pit
     * @return @{@link Pit} that was updated
     */
    public Pit updatePitNumberOfStones(GameBoard board, int position, int nrOfStones) {
        // Retrieve Pit
        Pit pit = getPitByBoardAndPosition(board, position);

        // Update Pit
        pit.setStoneCount(nrOfStones);

        // Save Pit
        pitRepository.save(pit);

        return pit;
    }

    /**
     * Function to update the number of stones on a Pit by a certain amount
     *
     * @param board @{@link GameBoard} the Pit is on
     * @param position of the Pit
     * @param amount number of stones to add in the Pit
     * @return @{@link Pit} that was updated
     */
    public Pit updatePitNumberOfStonesByAmount(GameBoard board, int position, int amount) {
        // Retrieve Pit
        Pit pit = getPitByBoardAndPosition(board, position);

        // Update Pit
        int currentAmount = pit.getStoneCount();
        int newAmount = currentAmount + amount;
        pit.setStoneCount(newAmount);

        // Save Pit
        pitRepository.save(pit);

        return pit;
    }

    /**
     * Function to update the number of stones on a Pit by one
     *
     * @param board @{@link GameBoard} the Pit is on
     * @param position of the Pit
     * @return @{@link Pit} that was updated
     */
    public Pit updatePitNumberOfStonesByOne(GameBoard board, int position) {
        // Retrieve Pit
        Pit pit = getPitByBoardAndPosition(board, position);

        // Update Pit
        int currentAmount = pit.getStoneCount();
        currentAmount++;
        pit.setStoneCount(currentAmount);

        // Save Pit
        pitRepository.save(pit);

        return pit;
    }

    /**
     * Function to get the number of stones by GameBoard and position
     *
     * @param board @{@link GameBoard} to get info from
     * @param position of the Pit to get info from
     * @return The number of stones on the Pit
     */
    public int getPitNumberOfStonesByBoardAndPosition(GameBoard board, int position) {
        Pit pit = getPitByBoardAndPosition(board, position);
        return pit.getStoneCount();
    }

    /**
     * Function to retrieve a Pit by @{@link GameBoard} and position
     *
     * @param board @{@link GameBoard} to get Pit from
     * @param position of the Pit
     * @return @{@link Pit} matching params
     */
    public Pit getPitByBoardAndPosition(GameBoard board, int position) {
        return pitRepository.findByGameBoardAndPosition(board, position);
    }
}
