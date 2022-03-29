package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.boards.Board;
import com.chess.engine.boards.BoardUtils;
import com.chess.engine.boards.Move;
import com.chess.engine.boards.Tile;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.chess.engine.boards.BoardUtils.isValidTileCoordinate;

public class King extends Piece{

    private static final int[] CANDIDATE_MOVE_COORDINATES = {-9,-8,-7,-1,1,7,8,9};

    public King(Alliance pieceAlliance,final int piecePosition) {
        super(PieceType.KING,piecePosition, pieceAlliance);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {

        List<Move> legalMoves = new ArrayList<>();

        for (final int currentCoordinateOffset : CANDIDATE_MOVE_COORDINATES) {
            final int candidateDestinationCoordinate = this.piecePosition + currentCoordinateOffset;
            if (isFirstColumnExclusion(this.piecePosition, currentCoordinateOffset) ||
                    isEighthColumnExclusion(this.piecePosition, currentCoordinateOffset)) {
                continue;
            }
            if (isValidTileCoordinate(candidateDestinationCoordinate)) {
                final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                if (!candidateDestinationTile.isTileOccupied()) {
                    legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));
                } else {
                    final Piece pieceDestination = candidateDestinationTile.getPiece();
                    final Alliance pieceAlliance = pieceDestination.getPieceAlliance();
                    if (this.pieceAlliance != pieceAlliance) {
                        legalMoves.add(new Move.AttackMove(board, this, candidateDestinationCoordinate, pieceDestination));
                    }
                }
            }
        }

        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public Piece movePiece(Move move) {
        return new King(move.getMovedPiece().getPieceAlliance(),move.getDestinationCoordinate());
    }

    @Override
    public String toString() {
        return PieceType.KING.toString();
    }

    private static boolean isFirstColumnExclusion(int currentPosition, int currentCandidate) {
        return BoardUtils.FIRST_COLUMN[currentPosition] && (currentCandidate == -9 || currentCandidate == -1 ||
                currentCandidate == 7);
    }

    private static boolean isEighthColumnExclusion(int currentPosition, int currentCandidate) {
        return BoardUtils.EIGHTH_COLUMN[currentPosition] && (currentCandidate == -7 || currentCandidate == 1 ||
                currentCandidate == 9);
    }
}
