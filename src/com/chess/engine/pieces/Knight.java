package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.boards.BoardUtils;
import com.chess.engine.boards.Board;
import com.chess.engine.boards.Move;
import com.chess.engine.boards.Tile;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.chess.engine.boards.BoardUtils.isValidTileCoordinate;
import static com.chess.engine.boards.Move.*;

public class Knight extends Piece {

    private static final int[] CANDIDATE_MOVE_COORDINATES = {-17, -15, -10, -6, 6, 10, 15, 17};

    public Knight(final Alliance pieceAlliance,final int piecePosition) {
        super(PieceType.KNIGHT,piecePosition, pieceAlliance);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {

        final List<Move> legalMoves = new ArrayList<>();

        for (final int currentCandidate : CANDIDATE_MOVE_COORDINATES) {
            final int candidateDestinationCoordinate = this.piecePosition + currentCandidate;
            if (isFirstColumnExclusion(this.piecePosition, currentCandidate) ||
                    isSecondColumnExclusion(this.piecePosition, currentCandidate) ||
                    isSeventhColumnExclusion(this.piecePosition, currentCandidate) ||
                    isEighthColumnExclusion(this.piecePosition, currentCandidate)) {
                continue;
            }

            if (isValidTileCoordinate(candidateDestinationCoordinate)) {
                final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                if (!candidateDestinationTile.isTileOccupied()) {
                    legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
                } else {
                    final Piece pieceDestination = candidateDestinationTile.getPiece();
                    final Alliance pieceAlliance = pieceDestination.getPieceAlliance();
                    if (this.pieceAlliance != pieceAlliance) {
                        legalMoves.add(new AttackMove(board,this, candidateDestinationCoordinate,pieceDestination));
                    }
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public Piece movePiece(Move move) {
        return new Knight(move.getMovedPiece().getPieceAlliance(),move.getDestinationCoordinate());
    }

    @Override
    public String toString() {
        return PieceType.KNIGHT.toString();
    }

    private static boolean isFirstColumnExclusion(int currentPosition, int currentCandidate) {
        return BoardUtils.FIRST_COLUMN[currentPosition] && (currentCandidate == -17 || currentCandidate == -10 ||
                currentCandidate == 6 || currentCandidate == 15);
    }

    private static boolean isSecondColumnExclusion(int currentPosition, int currentCandidate) {
        return BoardUtils.SECOND_COLUMN[currentPosition] && (currentCandidate == -10 || currentCandidate == 6);
    }

    private static boolean isSeventhColumnExclusion(int currentPosition, int currentCandidate) {
        return BoardUtils.SEVENTH_COLUMN[currentPosition] && (currentCandidate == -6 || currentCandidate == 10);
    }

    private static boolean isEighthColumnExclusion(int currentPosition, int currentCandidate) {
        return BoardUtils.EIGHTH_COLUMN[currentPosition] && (currentCandidate == -15 || currentCandidate == -6 ||
                currentCandidate == 10 || currentCandidate == 17);
    }
}
