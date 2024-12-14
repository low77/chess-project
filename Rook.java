package com.chess.engine.classic.pieces;

import com.chess.engine.classic.Alliance;
import com.chess.engine.classic.board.Board;
import com.chess.engine.classic.board.BoardUtils;
import com.chess.engine.classic.board.Move;
import com.chess.engine.classic.board.Move.MajorAttackMove;
import com.chess.engine.classic.board.Move.MajorMove;
import com.chess.engine.classic.board.MoveUtils;

import java.util.*;


// هنا يعني ان كلاس رووك نهائي ولا يمكن توريثه
public final class Rook extends Piece {

    // تخزين الحركات القانونية
    private final static int[] CANDIDATE_MOVE_COORDINATES = { -8, -1, 1, 8 };

    private final static Map<Integer, MoveUtils.Line[]> PRECOMPUTED_CANDIDATES = computeCandidates();

    
    // تحديد الحركة الاولى 
    public Rook(final Alliance alliance, final int piecePosition) {
        super(PieceType.ROOK, alliance, piecePosition, true);
    }

    public Rook(final Alliance alliance,
                final int piecePosition,
                final boolean isFirstMove) {
        super(PieceType.ROOK, alliance, piecePosition, isFirstMove);
    }

    // حساب وتخزين الحركات القانونية للاحجار على اللوحة
    private static Map<Integer, MoveUtils.Line[]> computeCandidates() {
        final Map<Integer, MoveUtils.Line[]> candidates = new HashMap<>();
        for (int position = 0; position < BoardUtils.NUM_TILES; position++) {
            List<MoveUtils.Line> lines = new ArrayList<>();
            for (int offset : CANDIDATE_MOVE_COORDINATES) {
                int destination = position;
                MoveUtils.Line line = new MoveUtils.Line();
                while (BoardUtils.isValidTileCoordinate(destination)) {
                    if (isColumnExclusion(destination, offset)) {
                        break;
                    }
                    destination += offset;
                    if (BoardUtils.isValidTileCoordinate(destination)) {
                        line.addCoordinate(destination);
                    }
                }
                if (!line.isEmpty()) {
                    lines.add(line);
                }
            }
            if (!lines.isEmpty()) {
                candidates.put(position, lines.toArray(new MoveUtils.Line[0]));
            }
        }
        return Collections.unmodifiableMap(candidates);
    }

// حساب الحركات القانونية
    
    //هنا نستخدم الحركات القانونية للروك المحسوبة مسبقا وتتحقق تسوي تست للاحجار الي موجودة على الاحجار في الاماكن المحتمل اروك يروح لها
    
    
    
    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        final List<Move> legalMoves = new ArrayList<>();
        for (final MoveUtils.Line line : PRECOMPUTED_CANDIDATES.get(this.piecePosition)) {
            for (final int candidateDestinationCoordinate : line.getLineCoordinates()) {
                final Piece pieceAtDestination = board.getPiece(candidateDestinationCoordinate);
                if (pieceAtDestination == null) {
                    legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
                } else {
                    final Alliance pieceAlliance = pieceAtDestination.getPieceAllegiance();
                    if (this.pieceAlliance != pieceAlliance) {
                        legalMoves.add(new MajorAttackMove(board, this, candidateDestinationCoordinate,
                                pieceAtDestination));
                    }
                    break;
                }
            }
        }
        return Collections.unmodifiableList(legalMoves);
    }

    
  
    @Override
    public int locationBonus() {
        return this.pieceAlliance.rookBonus(this.piecePosition);
    }
    
  //تحريك القطعة
    @Override
    public Rook movePiece(final Move move) {
        return PieceUtils.INSTANCE.getMovedRook(move.getMovedPiece().getPieceAllegiance(), move.getDestinationCoordinate());
    }

    
     //تحويل الاوبجكت الى سلسلة نصية
    @Override
    public String toString() {
        return this.pieceType.toString();
    }

     //الاستثناءات 
    
    private static boolean isColumnExclusion(final int position,
                                             final int offset) {
        return (BoardUtils.INSTANCE.FIRST_COLUMN.get(position) && (offset == -1)) ||
                (BoardUtils.INSTANCE.EIGHTH_COLUMN.get(position) && (offset == 1));
    }

}