/* Skeleton Copyright (C) 2015, 2020 Paul N. Hilfinger and the Regents of the
 * University of California.  All rights reserved. */
package loa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Formatter;
import java.util.List;

import java.util.regex.Pattern;

import static loa.Piece.*;
import static loa.Square.*;

/** Represents the state of a game of Lines of Action.
 *  @author Jessica Yang
 */
class Board {

    /** Default number of moves for each side that results in a draw. */
    static final int DEFAULT_MOVE_LIMIT = 60;

    /** Pattern describing a valid square designator (cr). */
    static final Pattern ROW_COL = Pattern.compile("^[a-h][1-8]$");

    /** A Board whose initial contents are taken from INITIALCONTENTS
     *  and in which the player playing TURN is to move. The resulting
     *  Board has
     *        get(col, row) == INITIALCONTENTS[row][col]
     *  Assumes that PLAYER is not null and INITIALCONTENTS is 8x8.
     *
     *  CAUTION: The natural written notation for arrays initializers puts
     *  the BOTTOM row of INITIALCONTENTS at the top.
     */
    Board(Piece[][] initialContents, Piece turn) {
        initialize(initialContents, turn);
    }


    /** A new board in the standard initial position. */
    Board() {
        this(INITIAL_PIECES, BP);
    }

    /** A Board whose initial contents and state are copied from
     *  BOARD. */
    Board(Board board) {
        this();
        copyFrom(board);
    }


    /** Set my state to CONTENTS with SIDE to move. */
    void initialize(Piece[][] contents, Piece side) {
        _moves.clear();
        for (int i = 0; i < contents.length; i++) {
            for (int j = 0; j < contents[i].length; j++) {
                Square curr = sq(j, i);
                set(curr, contents[i][j]);
            }
        }

        _turn = side;
        _moveLimit = DEFAULT_MOVE_LIMIT;
        _winnerKnown = false;
        _subsetsInitialized = false;
    }

    /** Set me to the initial configuration. */
    void clear() {
        initialize(INITIAL_PIECES, BP);
        _winnerKnown = false;
        _subsetsInitialized = false;
    }

    /** Set my state to a copy of BOARD. */
    void copyFrom(Board board) {
        if (board == this) {
            return;
        }

        _moves.clear();
        _moves.addAll(board._moves);
        for (Square sq : ALL_SQUARES) {
            _board[sq.index()] = board.get(sq);
        }
        _turn = board._turn;
        _winnerKnown = board._winnerKnown;
        _subsetsInitialized = board._subsetsInitialized;

    }

    /** Return the contents of the square at SQ. */
    Piece get(Square sq) {
        return _board[sq.index()];
    }

    /** Set the square at SQ to V and set the side that is to move next
     *  to NEXT, if NEXT is not null. */
    void set(Square sq, Piece v, Piece next) {
        _board[sq.index()] = v;
        if (next != null) {
            _turn = next;
        }

    }

    /** Set the square at SQ to V, without modifying the side that
     *  moves next. */
    void set(Square sq, Piece v) {
        set(sq, v, null);
    }

    /** Set limit on number of moves by each side that results in a tie to
     *  LIMIT, where 2 * LIMIT > movesMade(). */
    void setMoveLimit(int limit) {
        if (2 * limit <= movesMade()) {
            throw new IllegalArgumentException("move limit too small");
        }
        _moveLimit = 2 * limit;
    }

    /** Assuming isLegal(MOVE), make MOVE. Assumes MOVE.isCapture()
     *  is false. */
    void makeMove(Move move) {

        assert isLegal(move);

        Square from = move.getFrom();
        Square to = move.getTo();

        if (get(to) == _turn.opposite()) {
            move = Move.mv(from, to, true);
        }
        _moves.add(move);
        set(to, _turn, turn());
        set(from, EMP);


        _turn = _turn.opposite();
        _subsetsInitialized = false;


    }

    /** make MOVE for testing purposes.*/
    void makeMoveTest(Move move) {

        Square from = move.getFrom();
        Square to = move.getTo();

        if (get(to) == _turn.opposite()) {
            move = Move.mv(from, to, true);
        }
        _moves.add(move);
        set(to, _turn, turn());
        set(from, EMP);


        _turn = _turn.opposite();
        _subsetsInitialized = false;


    }

    /** Retract (unmake) one move, returning to the state immediately before
     *  that move.  Requires that movesMade () > 0. */
    void retract() {
        assert movesMade() > 0;
        Move removed = _moves.remove(_moves.size() - 1);

        boolean isCapture = removed.isCapture();

        Square removedTo = removed.getTo();
        Square removedOrigin = removed.getFrom();

        if (isCapture) {
            _board[removedTo.index()] = _turn;
            _board[removedOrigin.index()] = _turn.opposite();
        } else {
            _board[removedTo.index()] = EMP;
        }

        _winnerKnown = false;
        _turn = _turn.opposite();
        _subsetsInitialized = false;

    }


    /** Return the Piece representing who is next to move. */
    Piece turn() {
        return _turn;
    }

    /** Return true iff FROM - TO is a legal move for the player currently on
     *  move. */
    boolean isLegal(Square from, Square to) {
        if (gameOver()) {
            return false;
        } else if (!Arrays.asList(ALL_SQUARES).contains(to)) {
            return false;
        } else if (from.distance(to) != pieceInLine(from, to)) {
            return false;
        } else if (blocked(from, to)) {
            return false;
        }
        return true;
    }

    /** Return the number of pieces in the line of action,
     * including the from piece. Added by Jessica.
     * @param to its square to
     * @param from  its square from.*/
    int pieceInLine(Square from, Square to) {
        int numOfPieces = 0;
        int direction = from.direction(to);
        int oppositeDirection = (direction + 4) % 8;
        for (int step = 1; step < BOARD_SIZE; step++) {
            Square current = from.moveDest(direction, step);
            if (current != null && get(current) != EMP) {
                numOfPieces++;
            }
            Square currentOther = from.moveDest(oppositeDirection, step);
            if (currentOther != null && get(currentOther) != EMP) {
                numOfPieces++;
            }
        }

        return numOfPieces + 1;

    }

    /** Return true iff MOVE is legal for the player currently on move.
     *  The isCapture() property is ignored. */
    boolean isLegal(Move move) {
        return isLegal(move.getFrom(), move.getTo());
    }

    /** Return a sequence of all legal moves from this position. */
    List<Move> legalMoves() {
        List<Move> ret = new ArrayList<Move>();
        for (Square sq : ALL_SQUARES) {
            Piece piece = get(sq);
            if (piece != EMP && piece == _turn) {
                for (int dir = 0; dir < 8; dir++) {
                    for (int step = 1; step < BOARD_SIZE; step++) {
                        Square possible = sq.moveDest(dir, step);
                        if (possible != null && isLegal(sq, possible)) {
                            Move valid = Move.mv(sq, possible);
                            ret.add(valid);
                        }
                    }
                }
            }
        }
        return ret;
    }

    /** Return true iff the game is over (either player has all his
     *  pieces continguous or there is a tie). */
    boolean gameOver() {
        return winner() != null;
    }

    /** Return true iff SIDE's pieces are continguous. */
    boolean piecesContiguous(Piece side) {
        return getRegionSizes(side).size() == 1;
    }

    /** Return the winning side, if any.  If the game is not over, result is
     *  null.  If the game has ended in a tie, returns EMP. */
    Piece winner() {
        if (!_winnerKnown) {

            boolean blackCont = piecesContiguous(BP);
            boolean whiteCont = piecesContiguous(WP);

            if (blackCont || whiteCont) {
                _winnerKnown = true;
                if (blackCont && whiteCont) {
                    _winner = turn().opposite();
                }
                if (whiteCont) {
                    _winner = WP;
                } else {
                    _winner = BP;
                }
            } else if (movesMade() >= _moveLimit) {
                _winner = EMP;
                _winnerKnown = true;
            } else {
                _winner = null;
                return null;
            }

        }
        return _winner;
    }

    /** Return the total number of moves that have been made (and not
     *  retracted).  Each valid call to makeMove with a normal move increases
     *  this number by 1. */
    int movesMade() {
        return _moves.size();
    }

    @Override
    public boolean equals(Object obj) {
        Board b = (Board) obj;
        return Arrays.deepEquals(_board, b._board) && _turn == b._turn;
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(_board) * 2 + _turn.hashCode();
    }

    @Override
    public String toString() {
        Formatter out = new Formatter();
        out.format("===%n");
        for (int r = BOARD_SIZE - 1; r >= 0; r -= 1) {
            out.format("    ");
            for (int c = 0; c < BOARD_SIZE; c += 1) {
                out.format("%s ", get(sq(c, r)).abbrev());
            }
            out.format("%n");
        }
        out.format("Next move: %s%n===", turn().fullName());
        return out.toString();
    }

    /** Return true if a move from FROM to TO is blocked by an opposing
     *  piece or by a friendly piece on the target square. */
    private boolean blocked(Square from, Square to) {
        if (get(to) != EMP && get(to) == _turn) {
            return true;
        }
        int directionOfTo = from.direction(to);

        for (int steps = 1; steps < from.distance(to); steps++) {
            Square current = from.moveDest(directionOfTo, steps);
            Piece currPiece = get(current);
            if (current != null && currPiece == _turn.opposite()) {
                return true;
            }
        }
        return false;
    }

    /** Return the size of the as-yet unvisited cluster of squares
     *  containing P at and adjacent to SQ.  VISITED indicates squares that
     *  have already been processed or are in different clusters.  Update
     *  VISITED to reflect squares counted. */
    private int numContig(Square sq, boolean[][] visited, Piece p) {
        if (p == EMP) {
            return 0;
        } else if (get(sq) != p) {
            return 0;
        } else if (visited[sq.row()][sq.col()]) {
            return 0;
        }

        visited[sq.row()][sq.col()] = true;
        int count = 1;

        for (int dir = 0; dir < 8; dir++) {
            Square nextAdjacent = sq.moveDest(dir, 1);
            if (nextAdjacent == null) {
                return count;
            } else {
                if (get(nextAdjacent) == p) {
                    count += numContig(nextAdjacent, visited, p);
                }
            }

        }
        return count;
    }

    /** Set the values of _whiteRegionSizes and _blackRegionSizes. */
    private void computeRegions() {
        if (_subsetsInitialized) {
            return;
        }
        _whiteRegionSizes.clear();
        _blackRegionSizes.clear();

        boolean[][] visited = new boolean[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                visited[j][i] = false;
            }
        }

        for (Square sq : ALL_SQUARES) {
            int numContig = numContig(sq, visited, BP);
            if (numContig > 0) {
                _blackRegionSizes.add(numContig(sq, visited, BP));
            }

        }

        visited = new boolean[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                visited[j][i] = false;
            }
        }

        for (Square sq : ALL_SQUARES) {
            int numContig = numContig(sq, visited, WP);
            if (numContig > 0) {
                _whiteRegionSizes.add(numContig(sq, visited, WP));
            }
        }


        Collections.sort(_whiteRegionSizes, Collections.reverseOrder());
        Collections.sort(_blackRegionSizes, Collections.reverseOrder());
        _subsetsInitialized = true;
    }

    /** Return the sizes of all the regions in the current union-find
     *  structure for side S. */
    List<Integer> getRegionSizes(Piece s) {
        computeRegions();
        if (s == WP) {
            return _whiteRegionSizes;
        } else {
            return _blackRegionSizes;
        }
    }


    /** The standard initial configuration for Lines of Action (bottom row
     *  first). */
    static final Piece[][] INITIAL_PIECES = {
            { EMP, BP,  BP,  BP,  BP,  BP,  BP,  EMP },
            { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
            { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
            { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
            { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
            { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
            { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
            { EMP, BP,  BP,  BP,  BP,  BP,  BP,  EMP }
    };

    /** Current contents of the board.  Square S is at _board[S.index()]. */
    private final Piece[] _board = new Piece[BOARD_SIZE  * BOARD_SIZE];

    /** List of all unretracted moves on this board, in order. */
    private final ArrayList<Move> _moves = new ArrayList<>();
    /** Current side on move. */
    private Piece _turn;
    /** Limit on number of moves before tie is declared.  */
    private int _moveLimit;
    /** True iff the value of _winner is known to be valid. */
    private boolean _winnerKnown;
    /** Cached value of the winner (BP, WP, EMP (for tie), or null (game still
     *  in progress).  Use only if _winnerKnown. */
    private Piece _winner;

    /** True iff subsets computation is up-to-date. */
    private boolean _subsetsInitialized;

    /** List of the sizes of continguous clusters of pieces, by color. */
    private final ArrayList<Integer>
            _whiteRegionSizes = new ArrayList<>(),
            _blackRegionSizes = new ArrayList<>();
}
