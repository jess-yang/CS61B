/* Skeleton Copyright (C) 2015, 2020 Paul N. Hilfinger and the Regents of the
 * University of California.  All rights reserved. */
package loa;

import java.util.List;

import static loa.Piece.*;
import static loa.Square.ALL_SQUARES;

/** An automated Player.
 *  @author Jessica Yang
 */
class MachinePlayer extends Player {

    /** A position-score magnitude indicating a win (for white if positive,
     *  black if negative). */
    private static final int WINNING_VALUE = Integer.MAX_VALUE - 20;
    /** A magnitude greater than a normal value. */
    private static final int INFTY = Integer.MAX_VALUE;

    /** A new MachinePlayer with no piece or controller (intended to produce
     *  a template). */
    MachinePlayer() {
        this(null, null);
    }

    /** A MachinePlayer that plays the SIDE pieces in GAME. */
    MachinePlayer(Piece side, Game game) {
        super(side, game);
    }

    @Override
    String getMove() {
        Move choice;

        assert side() == getGame().getBoard().turn();
        int depth;
        choice = searchForMove();
        getGame().reportMove(choice);
        return choice.toString();
    }

    @Override
    Player create(Piece piece, Game game) {
        return new MachinePlayer(piece, game);
    }

    @Override
    boolean isManual() {
        return false;
    }

    /** Return a move after searching the game tree to DEPTH>0 moves
     *  from the current position. Assumes the game is not over. */
    private Move searchForMove() {
        Board work = new Board(getBoard());
        int value;
        assert side() == work.turn();
        _foundMove = null;
        if (side() == WP) {
            value = findMove(work, chooseDepth(), true, 1, -INFTY, INFTY);
        } else {
            value = findMove(work, chooseDepth(), true, -1, -INFTY, INFTY);
        }
        return _foundMove;
    }

    /** Find a move from position BOARD and return its value, recording
     *  the move found in _foundMove iff SAVEMOVE. The move
     *  should have maximal value or have value > BETA if SENSE==1,
     *  and minimal value or value < ALPHA if SENSE==-1. Searches up to
     *  DEPTH levels.  Searching at level 0 simply returns a static estimate
     *  of the board value and does not set _foundMove. If the game is over
     *  on BOARD, does not set _foundMove. */
    private int findMove(Board board, int depth, boolean saveMove,
                         int sense, int alpha, int beta) {
        // FIXME
        int bestscore = 0;
        Move moveCopy = Move.mv("a2-b2");
        if (depth == 0) {
            return heuristic(board, sense);
        }
        for (Move move : board.legalMoves()) {
            int tempScore = 0;
            Board copy = new Board(board);
            copy.makeMove(move);
            tempScore = findMove(copy, depth - 1, true, sense, alpha, beta);
            if (tempScore > bestscore) {
                bestscore = tempScore;
                moveCopy = move;
            }

            if (sense == 1) {
                alpha = Math.max(tempScore, alpha);
            } else {
                beta = Math.min(tempScore, beta);
            }
            if (alpha > beta) {
                //fixme prune
            }
        }
        if (saveMove) {
            _foundMove = moveCopy;
        }

        return bestscore;
    }

    /** Return a search depth for the current position. */
    private int chooseDepth() {
        return 1;  // FIXME
    }

    private int heuristic(Board board, int sense) {
        Piece side;
        if (sense == 1) {
            side = WP;
        } else {
            side = BP;
        }
        int totalNumWhite = 0;
        int totalNumBlack = 0;
        for (Square sq : ALL_SQUARES) {
            Piece piece = board.get(sq);
            if (piece == BP) {
                totalNumBlack++;
            } else if (piece == WP) {
                totalNumWhite++;
            }
        }
        if (side == WP) {
            if (totalNumWhite > totalNumBlack) {
                return -10;
            } else if (totalNumBlack == totalNumWhite){
                return 0;
            } else {
                return 10;
            }
        } else {
            if (totalNumWhite > totalNumBlack) {
                return 10;
            } else if (totalNumBlack == totalNumWhite){
                return 0;
            } else {
                return -10;
            }
        }
    }

    /** Used to convey moves discovered by findMove. */
    private Move _foundMove;

}
