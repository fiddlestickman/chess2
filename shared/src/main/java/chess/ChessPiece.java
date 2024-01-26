package chess;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    ChessGame.TeamColor pieceColor;
    ChessPiece.PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type)
    {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN,
        NOTHING
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor()
    {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType()
    {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition)
    {
        ChessPiece piece = board.getPiece(myPosition);
        java.util.ArrayList<ChessMove> legalMoves = new java.util.ArrayList<ChessMove>(0);

        if (piece.getPieceType() == PieceType.NOTHING)
        {
            return legalMoves; //no piece on the square selected
        }

        //makes sure the position given and the board state match
        // they both have a potentially different piece assigned to the same square
        if (board.getPiece(myPosition).getPieceType() != piece.getPieceType()
                || board.getPiece(myPosition).getTeamColor() != piece.getTeamColor())
        {
            throw new RuntimeException("somehow the board and position given don't match");
        }

        if (piece.getPieceType() == PieceType.PAWN)
        {
            legalMoves.addAll(pawnMoves(myPosition, board));//adds all the legal pawn moves to the legal moves
        }
        else if (piece.getPieceType() == PieceType.KNIGHT)
        {
            legalMoves.addAll(knightMoves(myPosition, board));
        }
        else if (piece.getPieceType() == PieceType.BISHOP)
        {

            legalMoves.addAll(bishopMoves(myPosition, board));
        }
        else if (piece.getPieceType() == PieceType.ROOK)
        {
            legalMoves.addAll(rookMoves(myPosition, board));
        }
        else if (piece.getPieceType() == PieceType.QUEEN)
        {
            legalMoves.addAll(queenMoves(myPosition, board));
        }
        else if (piece.getPieceType() == PieceType.KING)
        {
            legalMoves.addAll(kingMoves(myPosition, board));
        }
        else
            return legalMoves; //no chess piece on that square

        return legalMoves;
    }

    //rules for pawn moves
    private Collection<ChessMove> pawnMoves(ChessPosition myPosition, ChessBoard board)
    {
        int row = myPosition.getRow();
        int column = myPosition.getColumn();
        ChessPiece piece = board.getPiece(myPosition);
        java.util.ArrayList<ChessMove> legalMoves = new java.util.ArrayList<ChessMove>(0);

        //moves for white
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE)
        {
            ChessPiece.PieceType promo = PieceType.NOTHING;
            if (row == 7)
            {
                //checks if front of pawn is legal

                ChessPosition temp = new ChessPosition(row+1, column); //selects the position in front of pawn
                promo = chess.ChessPiece.PieceType.QUEEN;
                ChessMove move1 = new ChessMove(myPosition, temp, promo); //states that pawn is not promoting
                promo = chess.ChessPiece.PieceType.ROOK;
                ChessMove move2 = new ChessMove(myPosition, temp, promo); //states that pawn is not promoting
                promo = chess.ChessPiece.PieceType.KNIGHT;
                ChessMove move3 = new ChessMove(myPosition, temp, promo); //states that pawn is not promoting
                promo = chess.ChessPiece.PieceType.BISHOP;
                ChessMove move4 = new ChessMove(myPosition, temp, promo); //states that pawn is not promoting
                ChessPiece test = board.getPiece(temp); //gets the piece on the square in front of pawn
                if (test.getPieceType() == PieceType.NOTHING) //checks that there's no piece in the way
                {
                    legalMoves.add(move1); //adds the move going forward to the legal moves list
                    legalMoves.add(move2); //adds the move going forward to the legal moves list
                    legalMoves.add(move3); //adds the move going forward to the legal moves list
                    legalMoves.add(move4); //adds the move going forward to the legal moves list
                }

                //checks if left front of pawn is legal
                if (column > 1) {
                    temp = new ChessPosition(row + 1, column - 1); //selects the position in front of pawn to the left
                    promo = chess.ChessPiece.PieceType.QUEEN;
                    move1 = new ChessMove(myPosition, temp, promo); //states that pawn is not promoting
                    promo = chess.ChessPiece.PieceType.ROOK;
                    move2 = new ChessMove(myPosition, temp, promo); //states that pawn is not promoting
                    promo = chess.ChessPiece.PieceType.KNIGHT;
                    move3 = new ChessMove(myPosition, temp, promo); //states that pawn is not promoting
                    promo = chess.ChessPiece.PieceType.BISHOP;
                    move4 = new ChessMove(myPosition, temp, promo); //states that pawn is not promoting
                    test = board.getPiece(temp); //gets the piece on the square in front of pawn
                    if (test.getTeamColor() == ChessGame.TeamColor.BLACK) //checks that there's a capturable piece
                    {

                        legalMoves.add(move1); //adds the move going forward to the legal moves list
                        legalMoves.add(move2); //adds the move going forward to the legal moves list
                        legalMoves.add(move3); //adds the move going forward to the legal moves list
                        legalMoves.add(move4); //adds the move going forward to the legal moves list
                    }
                }

                //checks if right front of pawn is legal
                if (column < 8) {
                    temp = new ChessPosition(row + 1, column + 1); //selects the position in front of pawn to the right
                    promo = chess.ChessPiece.PieceType.QUEEN;
                    move1 = new ChessMove(myPosition, temp, promo); //states that pawn is not promoting
                    promo = chess.ChessPiece.PieceType.ROOK;
                    move2 = new ChessMove(myPosition, temp, promo); //states that pawn is not promoting
                    promo = chess.ChessPiece.PieceType.KNIGHT;
                    move3 = new ChessMove(myPosition, temp, promo); //states that pawn is not promoting
                    promo = chess.ChessPiece.PieceType.BISHOP;
                    move4 = new ChessMove(myPosition, temp, promo); //states that pawn is not promoting
                    test = board.getPiece(temp); //gets the piece on the square in front of pawn
                    if (test.getTeamColor() == ChessGame.TeamColor.BLACK) //checks that there's a capturable piece
                    {
                        legalMoves.add(move1); //adds the move going forward to the legal moves list
                        legalMoves.add(move2); //adds the move going forward to the legal moves list
                        legalMoves.add(move3); //adds the move going forward to the legal moves list
                        legalMoves.add(move4); //adds the move going forward to the legal moves list
                    }
                }
            }
            if ( row < 7) {
                //checks if front of pawn is legal
                ChessPosition temp = new ChessPosition(row+1, column); //selects the position in front of pawn
                ChessMove move = new ChessMove(myPosition, temp, promo); //states that pawn is not promoting
                ChessPiece test = board.getPiece(temp); //gets the piece on the square in front of pawn
                if (test.getPieceType() == PieceType.NOTHING) //checks that there's no piece in the way
                    legalMoves.add(move); //adds the move going forward to the legal moves list

                //checks if left front of pawn is legal
                if (column > 1) {
                    temp = new ChessPosition(row + 1, column - 1); //selects the position in front of pawn to the left
                    move = new ChessMove(myPosition, temp, promo); //states that pawn is not promoting
                    test = board.getPiece(temp); //gets the piece on the square in front of pawn
                    if (test.getTeamColor() == ChessGame.TeamColor.BLACK) //checks that there's a capturable piece
                        legalMoves.add(move); //adds the capture move to the legal moves list
                }

                //checks if right front of pawn is legal
                if (column < 8) {
                    temp = new ChessPosition(row + 1, column + 1); //selects the position in front of pawn to the right
                    move = new ChessMove(myPosition, temp, promo); //states that pawn is not promoting
                    test = board.getPiece(temp); //gets the piece on the square in front of pawn
                    if (test.getTeamColor() == ChessGame.TeamColor.BLACK) //checks that there's a capturable piece
                        legalMoves.add(move); //adds the capture move to the legal moves list
                }
            }
            if (row == 2)
            {
                //double move at start
                ChessPosition temp = new ChessPosition(row+2, column); //selects the position two ahead of pawn
                ChessPosition temp2 = new ChessPosition(row+1, column); //checks the spot directly in front of pawn
                ChessMove move = new ChessMove(myPosition, temp, promo); //states that pawn is not promoting
                ChessPiece test = board.getPiece(temp); //gets the piece on the square two steps in front of pawn
                ChessPiece test2 = board.getPiece(temp2); //gets the piece on the square in front of pawn
                if (test.getPieceType() == PieceType.NOTHING && test2.getPieceType() == PieceType.NOTHING) //checks that there's no piece in the way
                    legalMoves.add(move); //adds the double move to the legal moves list

            }
            /*if (row == 5)//en passant possible if pawn is on fifth rank
            {
                ChessMove lastMove = board.getLastMove();
                if (lastMove.getStartPosition().getRow() == 7
                && lastMove.getEndPosition().getRow() == 5
                && lastMove.getStartPosition().getPiece().getPieceType() == PieceType.PAWN) //makes sure that the last move was a double pawn move
                {
                    if(myPosition.getColumn() == lastMove.getEndPosition().getColumn()+1) //if the pawn moved to the right of the pawn
                    {
                        ChessPosition temp = new ChessPosition(row + 1, column + 1); //selects the position in front of pawn to the right
                        ChessMove move = new ChessMove(myPosition, temp, promo); //sets up the capture move
                        legalMoves.add(move); //adds the move to the list
                    }
                    if(myPosition.getColumn() == lastMove.getEndPosition().getColumn()-1) //if the pawn moved to the left of the pawn
                    {
                        ChessPosition temp = new ChessPosition(row + 1, column - 1); //selects the position in front of pawn to the left
                        ChessMove move = new ChessMove(myPosition, temp, promo); //sets up the capture move
                        legalMoves.add(move); //adds the move to the list
                    }
                }
            }*/
        }
        else if (piece.getTeamColor() == ChessGame.TeamColor.BLACK)
        {
            chess.ChessPiece.PieceType promo = PieceType.NOTHING; //sets promo to null unless on seventh rank
            if (row == 2)
            {
                //checks if front of pawn is legal
                ChessPosition temp = new ChessPosition(row-1, column); //selects the position in front of pawn
                promo = chess.ChessPiece.PieceType.QUEEN;
                ChessMove move1 = new ChessMove(myPosition, temp, promo); //states that pawn is not promoting
                promo = chess.ChessPiece.PieceType.ROOK;
                ChessMove move2 = new ChessMove(myPosition, temp, promo); //states that pawn is not promoting
                promo = chess.ChessPiece.PieceType.KNIGHT;
                ChessMove move3 = new ChessMove(myPosition, temp, promo); //states that pawn is not promoting
                promo = chess.ChessPiece.PieceType.BISHOP;
                ChessMove move4 = new ChessMove(myPosition, temp, promo); //states that pawn is not promoting
                ChessPiece test = board.getPiece(temp); //gets the piece on the square in front of pawn
                if (test.getPieceType() == PieceType.NOTHING) //checks that there's no piece in the way
                {
                    legalMoves.add(move1); //adds the move going forward to the legal moves list
                    legalMoves.add(move2); //adds the move going forward to the legal moves list
                    legalMoves.add(move3); //adds the move going forward to the legal moves list
                    legalMoves.add(move4); //adds the move going forward to the legal moves list
                }

                //checks if left front of pawn is legal
                if (column > 1) {
                    temp = new ChessPosition(row - 1, column - 1); //selects the position in front of pawn to the left
                    promo = chess.ChessPiece.PieceType.QUEEN;
                    move1 = new ChessMove(myPosition, temp, promo); //states that pawn is not promoting
                    promo = chess.ChessPiece.PieceType.ROOK;
                    move2 = new ChessMove(myPosition, temp, promo); //states that pawn is not promoting
                    promo = chess.ChessPiece.PieceType.KNIGHT;
                    move3 = new ChessMove(myPosition, temp, promo); //states that pawn is not promoting
                    promo = chess.ChessPiece.PieceType.BISHOP;
                    move4 = new ChessMove(myPosition, temp, promo); //states that pawn is not promoting
                    test = board.getPiece(temp); //gets the piece on the square in front of pawn
                    if (test.getTeamColor() == ChessGame.TeamColor.WHITE) //checks that there's a capturable piece
                    {
                        legalMoves.add(move1); //adds the move going forward to the legal moves list
                        legalMoves.add(move2); //adds the move going forward to the legal moves list
                        legalMoves.add(move3); //adds the move going forward to the legal moves list
                        legalMoves.add(move4); //adds the move going forward to the legal moves list
                    }
                }

                //checks if right front of pawn is legal
                if (column < 8) {
                    temp = new ChessPosition(row - 1, column + 1); //selects the position in front of pawn to the right
                    promo = chess.ChessPiece.PieceType.QUEEN;
                    move1 = new ChessMove(myPosition, temp, promo); //states that pawn is not promoting
                    promo = chess.ChessPiece.PieceType.ROOK;
                    move2 = new ChessMove(myPosition, temp, promo); //states that pawn is not promoting
                    promo = chess.ChessPiece.PieceType.KNIGHT;
                    move3 = new ChessMove(myPosition, temp, promo); //states that pawn is not promoting
                    promo = chess.ChessPiece.PieceType.BISHOP;
                    move4 = new ChessMove(myPosition, temp, promo); //states that pawn is not promoting
                    test = board.getPiece(temp); //gets the piece on the square in front of pawn
                    if (test.getTeamColor() == ChessGame.TeamColor.WHITE) //checks that there's a capturable piece
                    {
                        legalMoves.add(move1); //adds the move going forward to the legal moves list
                        legalMoves.add(move2); //adds the move going forward to the legal moves list
                        legalMoves.add(move3); //adds the move going forward to the legal moves list
                        legalMoves.add(move4); //adds the move going forward to the legal moves list
                    }
                }
            }
            if ( row > 2) {
                //checks if front of pawn is legal

                ChessPosition temp = new ChessPosition(row-1, column); //selects the position in front of pawn
                ChessMove move = new ChessMove(myPosition, temp, promo); //states that pawn is not promoting
                ChessPiece test = board.getPiece(temp); //gets the piece on the square in front of pawn
                if (test.getPieceType() == PieceType.NOTHING) //checks that there's no piece in the way
                    legalMoves.add(move); //adds the move going forward to the legal moves list

                //checks if left front of pawn is legal
                if (column > 1) {
                    temp = new ChessPosition(row - 1, column - 1); //selects the position in front of pawn to the left
                    move = new ChessMove(myPosition, temp, promo); //states that pawn is not promoting
                    test = board.getPiece(temp); //gets the piece on the square in front of pawn
                    if (test.getTeamColor() == ChessGame.TeamColor.WHITE) //checks that there's a capturable piece
                        legalMoves.add(move); //adds the capture move to the legal moves list
                }

                //checks if right front of pawn is legal
                if (column < 8) {
                    temp = new ChessPosition(row - 1, column + 1); //selects the position in front of pawn to the right
                    move = new ChessMove(myPosition, temp, promo); //states that pawn is not promoting
                    test = board.getPiece(temp); //gets the piece on the square in front of pawn
                    if (test.getTeamColor() == ChessGame.TeamColor.WHITE) //checks that there's a capturable piece
                        legalMoves.add(move); //adds the capture move to the legal moves list
                }
            }
            if (row == 7)
            {
                //double move at start
                ChessPosition temp = new ChessPosition(row-2, column); //selects the position two ahead of pawn
                ChessPosition temp2 = new ChessPosition(row-1, column); //checks the spot directly in front of pawn
                ChessMove move = new ChessMove(myPosition, temp, promo); //states that pawn is not promoting
                ChessPiece test = board.getPiece(temp); //gets the piece on the square two steps in front of pawn
                ChessPiece test2 = board.getPiece(temp2); //gets the piece on the square in front of pawn
                if (test.getPieceType() == PieceType.NOTHING && test2.getPieceType() == PieceType.NOTHING) //checks that there's no piece in the way
                    legalMoves.add(move); //adds the double move to the legal moves list

            }
            /*if (row == 4)//en passant possible if pawn is on fourth rank
            {
                ChessMove lastMove = board.getLastMove();
                if (lastMove.getStartPosition().getRow() == 2
                        && lastMove.getEndPosition().getRow() == 4
                        && lastMove.getStartPosition().getPiece().getPieceType() == PieceType.PAWN) //makes sure that the last move was a double pawn move
                {
                    if(myPosition.getColumn() == lastMove.getEndPosition().getColumn()+1) //if the pawn moved to the right of the pawn
                    {
                        ChessPosition temp = new ChessPosition(row - 1, column + 1); //selects the position in front of pawn to the right
                        ChessMove move = new ChessMove(myPosition, temp, promo); //sets up the capture move
                        legalMoves.add(move); //adds the move to the list
                    }
                    if(myPosition.getColumn() == lastMove.getEndPosition().getColumn()-1) //if the pawn moved to the left of the pawn
                    {
                        ChessPosition temp = new ChessPosition(row - 1, column - 1); //selects the position in front of pawn to the left
                        ChessMove move = new ChessMove(myPosition, temp, promo); //sets up the capture move
                        legalMoves.add(move); //adds the move to the list
                    }
                }
            }*/
        }
        return legalMoves;
    }

    //iterates through the eight possible knight moves
    private Collection<ChessMove> knightMoves(ChessPosition myPosition, ChessBoard board)
    {
        int row = myPosition.getRow();
        int column = myPosition.getColumn();
        ChessPiece piece = board.getPiece(myPosition);
        java.util.ArrayList<ChessMove> legalMoves = new java.util.ArrayList<ChessMove>(0);

        ChessMove move = kMove(row, column, row+2, column+1, piece, board); //creates the first knight move
        if (move != null)
            legalMoves.add(move); //adds the move if it's legal

        move = kMove(row, column, row+1, column+2, piece, board); //creates the first knight move
        if (move != null)
            legalMoves.add(move); //adds the move if it's legal

        move = kMove(row, column, row-1, column+2, piece, board); //creates the third knight move
        if (move != null)
            legalMoves.add(move); //adds the move if it's legal

        move = kMove(row, column, row-2, column+1, piece, board); //creates the fourth knight move
        if (move != null)
            legalMoves.add(move); //adds the move if it's legal

        move = kMove(row, column, row-2, column-1, piece, board); //creates the fifth knight move
        if (move != null)
            legalMoves.add(move); //adds the move if it's legal

        move = kMove(row, column, row-1, column-2, piece, board); //creates the sixth knight move
        if (move != null)
            legalMoves.add(move); //adds the move if it's legal

        move = kMove(row, column, row+1, column-2, piece, board); //creates the seventh knight move
        if (move != null)
            legalMoves.add(move); //adds the move if it's legal

        move = kMove(row, column, row+2, column-1, piece, board); //creates the eighth knight move
        if (move != null)
            legalMoves.add(move); //adds the move if it's legal

        return legalMoves;
    }

    //rules for the knight move
    private ChessMove kMove(int startRow, int startCol, int finalRow, int finalCol, ChessPiece piece, ChessBoard board)
    {
        if (finalRow <= 8 && finalCol <= 8 && finalRow >= 1 && finalCol >= 1) //makes sure the move is not out of bounds
        {
            ChessPosition myPosition = new ChessPosition(startRow, startCol);//sets the start position
            ChessPosition temp = new ChessPosition(finalRow, finalCol); //selects the second clockwise position around knight
            ChessMove move = new ChessMove(myPosition, temp, PieceType.NOTHING); //sets up the move
            ChessPiece test = board.getPiece(temp); //gets the piece on the square targeted
            if (test.getPieceType() == PieceType.NOTHING || test.getTeamColor() != piece.getTeamColor() ) //checks that there's no friendly piece in the way
                return move;
            else
                return null;
        }
        else
            return null;
    }

    private Collection<ChessMove> bishopMoves(ChessPosition myPosition, ChessBoard board)
    {
        return dMoves(myPosition, board); //bishops only move diagonally
    }

    private Collection<ChessMove> rookMoves(ChessPosition myPosition, ChessBoard board)
    {
        return cMoves(myPosition, board); //rooks only move cardinally
    }

    private Collection<ChessMove> queenMoves(ChessPosition myPosition, ChessBoard board)
    {
        java.util.ArrayList<ChessMove> legalMoves = new java.util.ArrayList<ChessMove>(0);
        legalMoves.addAll(cMoves(myPosition, board)); //queens move cardinally like rooks
        legalMoves.addAll(dMoves(myPosition, board)); //and diagonally
        return legalMoves;
    }
    private Collection<ChessMove> kingMoves(ChessPosition myPosition, ChessBoard board)
    {
        java.util.ArrayList<ChessMove> legalMoves = new java.util.ArrayList<ChessMove>(0);
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        ChessPiece piece = board.getPiece(myPosition);

        boolean northFine = true;
        boolean eastFine = true;
        boolean southFine = true;
        boolean westFine = true;
        if (myPosition.getRow() == 8) //checks if the square attacked is at the top of the board or not
            northFine = false;
        if (myPosition.getColumn() == 8) //checks if the square attacked is on the right side of the board or not
            eastFine = false;
        if (myPosition.getRow() == 1) //checks if the square attacked is at the bottom of the board or not
            southFine = false;
        if (myPosition.getColumn() == 1) //checks if the square attacked is on the left side of the board or not
            westFine = false;

        ChessPosition checkedPosition;
        if (northFine) {
            checkedPosition = new ChessPosition(row+1, col);
            if (board.getPiece(checkedPosition).getTeamColor() != piece.getTeamColor())
            {
                ChessMove newMove = new ChessMove(myPosition, checkedPosition, PieceType.NOTHING);
                legalMoves.add(newMove); //adds the move from the current square to the checked square as possible
            }
        }
        if (northFine && eastFine) {
            checkedPosition = new ChessPosition(row+1, col+1);
            if (board.getPiece(checkedPosition).getTeamColor() !=piece.getTeamColor())
            {
                ChessMove newMove = new ChessMove(myPosition, checkedPosition, PieceType.NOTHING);
                legalMoves.add(newMove); //adds the move from the current square to the checked square as possible
            }
        }
        if (eastFine) {
            checkedPosition = new ChessPosition(row, col+1);
            if (board.getPiece(checkedPosition).getTeamColor() !=piece.getTeamColor())
            {
                ChessMove newMove = new ChessMove(myPosition, checkedPosition, PieceType.NOTHING);
                legalMoves.add(newMove); //adds the move from the current square to the checked square as possible
            }
        }
        if (eastFine && southFine) {
            checkedPosition = new ChessPosition(row-1, col+1);
            if (board.getPiece(checkedPosition).getTeamColor() !=piece.getTeamColor())
            {
                ChessMove newMove = new ChessMove(myPosition, checkedPosition, PieceType.NOTHING);
                legalMoves.add(newMove); //adds the move from the current square to the checked square as possible
            }
        }
        if (southFine) {
            checkedPosition = new ChessPosition(row-1, col);
            if (board.getPiece(checkedPosition).getTeamColor() !=piece.getTeamColor())
            {
                ChessMove newMove = new ChessMove(myPosition, checkedPosition, PieceType.NOTHING);
                legalMoves.add(newMove); //adds the move from the current square to the checked square as possible
            }
        }
        if (southFine && westFine) {
            checkedPosition = new ChessPosition(row-1, col-1);
            if (board.getPiece(checkedPosition).getTeamColor() !=piece.getTeamColor())
            {
                ChessMove newMove = new ChessMove(myPosition, checkedPosition, PieceType.NOTHING);
                legalMoves.add(newMove); //adds the move from the current square to the checked square as possible
            }
        }
        if (westFine) {
            checkedPosition = new ChessPosition(row, col-1);
            if (board.getPiece(checkedPosition).getTeamColor() !=piece.getTeamColor())
            {
                ChessMove newMove = new ChessMove(myPosition, checkedPosition, PieceType.NOTHING);
                legalMoves.add(newMove); //adds the move from the current square to the checked square as possible
            }
        }
        if (westFine && northFine) {
            checkedPosition = new ChessPosition(row+1, col-1);
            if (board.getPiece(checkedPosition).getTeamColor() !=piece.getTeamColor())
            {
                ChessMove newMove = new ChessMove(myPosition, checkedPosition, PieceType.NOTHING);
                legalMoves.add(newMove); //adds the move from the current square to the checked square as possible
            }
        }
        return legalMoves;
    }

    //diagonal moves (i.e. bishop and queen)
    private Collection<ChessMove> dMoves(ChessPosition myPosition, ChessBoard board)
    {
        int row = myPosition.getRow();
        int column = myPosition.getColumn();
        ChessPiece piece = board.getPiece(myPosition);
        java.util.ArrayList<ChessMove> legalMoves = new java.util.ArrayList<ChessMove>(0);

        boolean legal = true;
        int distance = 0; //distance diagonally from the bishop
        while (legal) //iterates through moves until no longer legal
        {
            distance++;//increments the distance from the bishop
            if (row+distance >= 9 || column+distance >= 9) //makes sure the move isn't out of bounds
            {
                legal = false;
            }
            else {
                ChessPosition temp = new ChessPosition(row + distance, column + distance); //selects the next spot northeast of bishop
                ChessMove move = new ChessMove(myPosition, temp, PieceType.NOTHING); //sets up the move
                ChessPiece test = board.getPiece(temp); //gets the piece on the square targeted
                if (test.getPieceType() == PieceType.NOTHING) //checks that there's no piece in the way
                    legalMoves.add(move);
                else if (test.getTeamColor() != piece.getTeamColor()) {
                    legalMoves.add(move); //lets you capture the piece
                    legal = false; //further moves are illegal
                } else
                    legal = false;
            }
        }

        legal = true;
        distance = 0;
        while (legal) //iterates through moves until no longer legal
        {
            distance++;//increments the distance from the bishop
            if (row-distance <= 0 || column+distance >= 9) //makes sure the move isn't out of bounds
            {
                legal = false;
            }
            else {
                ChessPosition temp = new ChessPosition(row - distance, column + distance); //selects the next spot southeast of bishop
                ChessMove move = new ChessMove(myPosition, temp, PieceType.NOTHING); //sets up the move
                ChessPiece test = board.getPiece(temp); //gets the piece on the square targeted
                if (test.getPieceType() == PieceType.NOTHING) //checks that there's no piece in the way
                    legalMoves.add(move);
                else if (test.getTeamColor() != piece.getTeamColor()) {
                    legalMoves.add(move); //lets you capture the piece
                    legal = false; //further moves are illegal
                } else
                    legal = false;
            }
        }

        legal = true;
        distance = 0;
        while (legal) //iterates through moves until no longer legal
        {
            distance++;//increments the distance from the bishop
            if (row-distance <= 0 || column-distance <= 0) //makes sure the move isn't out of bounds
            {
                legal = false;
            }
            else {
                ChessPosition temp = new ChessPosition(row - distance, column - distance); //selects the next spot southwest of bishop
                ChessMove move = new ChessMove(myPosition, temp, PieceType.NOTHING); //sets up the move
                ChessPiece test = board.getPiece(temp); //gets the piece on the square targeted
                if (test.getPieceType() == PieceType.NOTHING) //checks that there's no piece in the way
                    legalMoves.add(move);
                else if (test.getTeamColor() != piece.getTeamColor()) {
                    legalMoves.add(move); //lets you capture the piece
                    legal = false; //further moves are illegal
                } else
                    legal = false;
            }
        }

        legal = true;
        distance = 0;
        while (legal) //iterates through moves until no longer legal
        {
            distance++;//increments the distance from the bishop
            if (row+distance >= 9 || column-distance <= 0) //makes sure the move isn't out of bounds
            {
                legal = false;
            }
            else {
                ChessPosition temp = new ChessPosition(row + distance, column - distance); //selects the next spot northwest of bishop
                ChessMove move = new ChessMove(myPosition, temp, PieceType.NOTHING); //sets up the move
                ChessPiece test = board.getPiece(temp); //gets the piece on the square targeted
                if (test.getPieceType() == PieceType.NOTHING) //checks that there's no piece in the way
                    legalMoves.add(move);
                else if (test.getTeamColor() != piece.getTeamColor()) {
                    legalMoves.add(move); //lets you capture the piece
                    legal = false; //further moves are illegal
                } else
                    legal = false;
            }
        }

        return legalMoves;

    }

    //cardinal moves (i.e. rook and queen)
    private Collection<ChessMove> cMoves(ChessPosition myPosition, ChessBoard board)
    {
        int row = myPosition.getRow();
        int column = myPosition.getColumn();
        ChessPiece piece = board.getPiece(myPosition);
        java.util.ArrayList<ChessMove> legalMoves = new java.util.ArrayList<ChessMove>(0);

        boolean legal = true;
        int distance = 0; //distance away from rook
        while (legal) //iterates through moves until no longer legal
        {
            distance++;//increments the distance from the rook
            if (row+distance >= 9) //makes sure the move isn't out of bounds
            {
                legal = false;
            }
            else {
                ChessPosition temp = new ChessPosition(row + distance, column); //selects the next spot northeast of bishop
                ChessMove move = new ChessMove(myPosition, temp, PieceType.NOTHING); //sets up the move
                ChessPiece test = board.getPiece(temp); //gets the piece on the square targeted
                if (test.getPieceType() == PieceType.NOTHING) //checks that there's no piece in the way
                    legalMoves.add(move);
                else if (test.getTeamColor() != piece.getTeamColor()) {
                    legalMoves.add(move); //lets you capture the piece
                    legal = false; //further moves are illegal
                } else
                    legal = false;
            }
        }

        legal = true;
        distance = 0; //distance away from rook
        while (legal) //iterates through moves until no longer legal
        {
            distance++;//increments the distance from the rook
            if (column+distance >= 9) //makes sure the move isn't out of bounds
            {
                legal = false;
            }
            else {
                ChessPosition temp = new ChessPosition(row, column+distance); //selects the next spot northeast of bishop
                ChessMove move = new ChessMove(myPosition, temp, PieceType.NOTHING); //sets up the move
                ChessPiece test = board.getPiece(temp); //gets the piece on the square targeted
                if (test.getPieceType() == PieceType.NOTHING) //checks that there's no piece in the way
                    legalMoves.add(move);
                else if (test.getTeamColor() != piece.getTeamColor()) {
                    legalMoves.add(move); //lets you capture the piece
                    legal = false; //further moves are illegal
                } else
                    legal = false;
            }
        }

        legal = true;
        distance = 0; //distance away from rook
        while (legal) //iterates through moves until no longer legal
        {
            distance++;//increments the distance from the rook
            if (row-distance <= 0) //makes sure the move isn't out of bounds
            {
                legal = false;
            }
            else {
                ChessPosition temp = new ChessPosition(row-distance, column); //selects the next spot northeast of bishop
                ChessMove move = new ChessMove(myPosition, temp, PieceType.NOTHING); //sets up the move
                ChessPiece test = board.getPiece(temp); //gets the piece on the square targeted
                if (test.getPieceType() == PieceType.NOTHING) //checks that there's no piece in the way
                    legalMoves.add(move);
                else if (test.getTeamColor() != piece.getTeamColor()) {
                    legalMoves.add(move); //lets you capture the piece
                    legal = false; //further moves are illegal
                } else
                    legal = false;
            }
        }

        legal = true;
        distance = 0; //distance away from rook
        while (legal) //iterates through moves until no longer legal
        {
            distance++;//increments the distance from the rook
            if (column-distance <= 0) //makes sure the move isn't out of bounds
            {
                legal = false;
            }
            else {
                ChessPosition temp = new ChessPosition(row, column-distance); //selects the next spot northeast of bishop
                ChessMove move = new ChessMove(myPosition, temp, PieceType.NOTHING); //sets up the move
                ChessPiece test = board.getPiece(temp); //gets the piece on the square targeted
                if (test.getPieceType() == PieceType.NOTHING) //checks that there's no piece in the way
                    legalMoves.add(move);
                else if (test.getTeamColor() != piece.getTeamColor()) {
                    legalMoves.add(move); //lets you capture the piece
                    legal = false; //further moves are illegal
                } else
                    legal = false;
            }
        }
        return legalMoves;
    }

    public boolean isPinned(ChessPosition friendlyKing, ChessPosition myPosition, ChessBoard board)
    {
        int myRow = myPosition.getRow();
        int myCol = myPosition.getColumn();
        int kingRow = friendlyKing.getRow();
        int kingCol = friendlyKing.getColumn();


        boolean legal = true;
        int distance = 0; //distance away from rook or bishop

        if (myRow - kingRow == 0) //same row, check for pinning rook or queen
        {
            if (myCol > kingCol) //we are to the right of the king
            {
                while (legal) //iterates through moves until no longer legal
                {
                    distance++;//increments the distance from potentially pinned piece
                    if (myCol+distance >= 9) //makes sure the move isn't out of bounds
                    {
                        return false; //piece is not pinned
                    }
                    else {
                        ChessPosition temp = new ChessPosition(myRow, myCol+distance); //selects the next spot right of the pinned? piece
                        ChessPiece test = board.getPiece(temp); //gets the piece on the square targeted
                        if (test.getTeamColor() == friendlyKing.getPiece().getTeamColor()) //it's friendly, no worries
                            return true; //piece is pinned, not allowed to move
                        if (test.getPieceType() == PieceType.ROOK || test.getPieceType() == PieceType.QUEEN) //there's a rook or queen that's pinning if it's an enemy
                        {

                        } else
                            return false; //piece is not pinned (can only be pinned from one direction)
                    }
                }
            }
            else if (myCol < kingCol) //we are to the left of the king
            {
                while (legal) //iterates through moves until no longer legal
                {
                    distance++;//increments the distance from potentially pinned piece
                    if (myCol-distance <= 0) //makes sure the move isn't out of bounds
                    {
                        return false; //piece is not pinned
                    }
                    else {
                        ChessPosition temp = new ChessPosition(myRow, myCol-distance); //selects the next spot left of the pinned? piece
                        ChessPiece test = board.getPiece(temp); //gets the piece on the square targeted
                        if (test.getPieceType() == PieceType.ROOK || test.getPieceType() == PieceType.QUEEN) //there's a rook or queen that's pinning if it's an enemy
                        {
                            if (test.getTeamColor() != friendlyKing.getPiece().getTeamColor()) //oh snap it's an enemy
                                return true; //piece is pinned, not allowed to move
                        } else
                            return false; //piece is not pinned (can only be pinned from one direction)
                    }
                }
            }
        }
        else if (myCol - kingCol == 0) //same column, check for pinning rook or queen
        {
            if (myRow > kingRow) //we are above the king
            {
                while (legal) //iterates through moves until no longer legal
                {
                    distance++;//increments the distance from potentially pinned piece
                    if (myRow+distance >= 9) //makes sure the move isn't out of bounds
                    {
                        return false; //piece is not pinned
                    }
                    else {
                        ChessPosition temp = new ChessPosition(myRow+distance, myCol); //selects the next spot above the pinned? piece
                        ChessPiece test = board.getPiece(temp); //gets the piece on the square targeted
                        if (test.getPieceType() == PieceType.ROOK || test.getPieceType() == PieceType.QUEEN) //there's a rook or queen that's pinning if it's an enemy
                        {
                            if (test.getTeamColor() != friendlyKing.getPiece().getTeamColor()) //oh snap it's an enemy
                                return true; //piece is pinned, not allowed to move
                        } else
                            return false; //piece is not pinned (can only be pinned from one direction)
                    }
                }
            }
            else if (myRow < kingRow) //we are below the king
            {
                while (legal) //iterates through moves until no longer legal
                {
                    distance++;//increments the distance from potentially pinned piece
                    if (myRow-distance <= 0) //makes sure the move isn't out of bounds
                    {
                        return false; //piece is not pinned
                    }
                    else {
                        ChessPosition temp = new ChessPosition(myRow-distance, myCol); //selects the next spot below the pinned? piece
                        ChessPiece test = board.getPiece(temp); //gets the piece on the square targeted
                        if (test.getPieceType() == PieceType.ROOK || test.getPieceType() == PieceType.QUEEN) //there's a rook or queen that's pinning if it's an enemy
                        {
                            if (test.getTeamColor() != friendlyKing.getPiece().getTeamColor()) //oh snap it's an enemy
                                return true; //piece is pinned, not allowed to move
                        } else
                            return false; //piece is not pinned (can only be pinned from one direction)
                    }
                }
            }
        }
        else if (myRow - kingRow == myCol - kingCol) //same diagonal, check for bishop or queen
        {
            if (myRow > kingRow) //we are above and to the right of the king
            {
                while (legal) //iterates through moves until no longer legal
                {
                    distance++;//increments the distance from potentially pinned piece
                    if (myRow+distance >= 9 || myCol+distance >= 9) //makes sure the move isn't out of bounds
                    {
                        return false; //piece is not pinned
                    }
                    else {
                        ChessPosition temp = new ChessPosition(myRow+distance, myCol+distance); //selects the next spot above the pinned? piece
                        ChessPiece test = board.getPiece(temp); //gets the piece on the square targeted
                        if (test.getPieceType() == PieceType.BISHOP || test.getPieceType() == PieceType.QUEEN) //there's a bishop or queen that's pinning if it's an enemy
                        {
                            if (test.getTeamColor() != friendlyKing.getPiece().getTeamColor()) //oh snap it's an enemy
                                return true; //piece is pinned, not allowed to move
                        } else
                            return false; //piece is not pinned (can only be pinned from one direction)
                    }
                }
            }
            else if (myRow < kingRow) //we are below the king
            {
                while (legal) //iterates through moves until no longer legal
                {
                    distance++;//increments the distance from potentially pinned piece
                    if (myRow-distance <= 0 || myCol-distance <= 0) //makes sure the move isn't out of bounds
                    {
                        return false; //piece is not pinned
                    }
                    else {
                        ChessPosition temp = new ChessPosition(myRow-distance, myCol-distance); //selects the next spot below the pinned? piece
                        ChessPiece test = board.getPiece(temp); //gets the piece on the square targeted
                        if (test.getPieceType() == PieceType.BISHOP || test.getPieceType() == PieceType.QUEEN) //there's a bishop or queen that's pinning if it's an enemy
                        {
                            if (test.getTeamColor() != friendlyKing.getPiece().getTeamColor()) //oh snap it's an enemy
                                return true; //piece is pinned, not allowed to move
                        } else
                            return false; //piece is not pinned (can only be pinned from one direction)
                    }
                }
            }
        }
        else if (kingRow - myRow == myCol - kingCol) //same diagonal, check for bishop or queen
        {

        }
        else
            return false;
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

}
