
package tvtc.project;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import java.util.List;
import java.util.Collection;


public abstract class Piece {

    protected final int piecePosition;
     protected final Alliance pieceAlliance;
     Piece(final int piecePosition,final Alliance pieceAlliance){
         this.pieceAlliance = pieceAlliance;
         this.piecePosition = piecePosition;
         
     }
     public int getPiecePosition(){
     return this.piecePosition;
     }
     public Alliance getPieceAlliance(){
     
     return this.pieceAlliance;
     }
             
             
             
     public abstract Collection<Move> calculatelegalMoves (final Board board);
     
   public enum PieceType {
       
    PAWN("P"),
    KNIGHT("N"),
    BISHOP("B"),
    ROOK("R"),
    QUEEN("Q"),
    KING("K");

    private String pieceName;

    PieceType(final String pieceName) {
        this.pieceName = pieceName;
    }

    @Override
    public String toString() {
        return this.pieceName;
    }
}

}