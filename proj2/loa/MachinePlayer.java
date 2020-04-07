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
   /** private int findMove(Board board, int depth, boolean saveMove,
                         int sense, int alpha, int beta) {
        int bestScore = 0;
        if (sense == 1) {
            bestScore = -INFTY;
        }
        Move tempMove = null;
        if (depth == 0 || board.gameOver()) {
            return heuristic(board);
        }

        for (Move move : board.legalMoves()) {
            //System.out.println("sense + move: " +sense +":"+move); //fixme

            board.makeMoveTest(move);
            int tempScore = findMove(board, depth - 1, false, -1 * sense, alpha, beta);
            board.retract();

            if (sense == 1) {
                alpha = Math.max(tempScore, alpha);
            } else {
                beta = Math.min(tempScore, beta);
            }


            if (tempScore > bestScore && sense == 1) {
                bestScore = tempScore;
                tempMove = move;

            } else if (tempScore < bestScore && sense == -1) {
                bestScore = tempScore;
                tempMove = move;
            }
            if (alpha >= beta) {
                break;
            }
        }
        if (saveMove) {
            _foundMove = tempMove;
            System.out.println("foundmove: " +_foundMove);
        }
        return bestScore;
    } **/
    private int findMove(Board board, int depth, boolean saveMove,
                         int sense, int alpha, int beta) {
        if (board.gameOver() || depth == 0) {
            return heuristic(board);
        }
        for (Move m : board.legalMoves()) {
            board.makeMove(m);
            int score = findMove(board, depth - 1, false,
                    sense * (-1), alpha, beta);
            board.retract();
            if (sense == 1) {
                if (score > alpha) {
                    alpha = score;
                    if (saveMove) {
                        _foundMove = m;
                    }
                }
            } else {
                if (score < beta) {
                    beta = score;
                    if (saveMove) {
                        _foundMove = m;
                    }
                }
            }
            if (alpha >= beta) {
                break;
            }
        }
        if (sense == 1) {
            return alpha;
        } else {
            return beta;
        }
    }


    /** Return a search depth for the current position. */
    private int chooseDepth() {
        return 3;
    }

    private int heuristic(Board board) {
        if (board.piecesContiguous(WP)) {
            return WINNING_VALUE;
        } else if (board.piecesContiguous(BP)) {
            return -WINNING_VALUE;
        }

        List white = board.getRegionSizes(WP);
        List black = board.getRegionSizes(BP);
        int numWhiteRegions = white.size();
        int numBLackRegions = black.size();

        return numWhiteRegions - numBLackRegions;
    }

    /** Used to convey moves discovered by findMove. */
    private Move _foundMove;

}
