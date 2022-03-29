package com.chess.engine;

import com.chess.engine.boards.Board;

public class JChess {
    public static void main(String[] args) {
        Board board = Board.createStandartBoard();
        System.out.println(board);
    }
}
