package com.game.project.kalahgame.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

/**
 * Pit domain class
 */
@Entity
public class Pit {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private int id;

    public enum PitType {
        SMALL,
        LARGE
    }

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "board_id", nullable = false)
    private GameBoard gameBoard;

    @Column(name = "position", nullable = false)
    private int position;

    @Column(name = "stone_count", nullable = false)
    private int stoneCount;

    @Enumerated(EnumType.STRING)
    private PitType pitType;

    public Pit(GameBoard gameBoard, int position, int numberOfStones, PitType pitType) {
        this.gameBoard = gameBoard;
        this.position = position;
        this.stoneCount = numberOfStones;
        this.pitType = pitType;
    }

    public Pit(){

    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public GameBoard getGameBoard() {
        return gameBoard;
    }

    public void setGameBoard(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getStoneCount() {
        return stoneCount;
    }

    public void setStoneCount(int stoneCount) {
        this.stoneCount = stoneCount;
    }

    public PitType getPitType() {
        return pitType;
    }

    public void setPitType(PitType pitType) {
        this.pitType = pitType;
    }
}
