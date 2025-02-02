package com.chess.engine.classic.pieces;

import com.chess.engine.classic.Alliance;
import com.chess.engine.classic.board.Board;
import com.chess.engine.classic.board.BoardUtils;
import com.chess.engine.classic.board.Move;
import com.chess.engine.classic.board.Move.MajorAttackMove;
import com.chess.engine.classic.board.Move.MajorMove;
import com.chess.engine.classic.board.MoveUtils;

import java.util.*;


// هذا الكلاس نهائي ولايمكن توريثه  ويمتد الى كلاس بي

public final class Bishop extends Piece {
    
//هنا اضفنا المتغيرات والثوابت وتعتبر هذه مصفوفة تخزن الإزاحات الممكنة للحركات القانونية على اللوحة
    
    private final static int[] CANDIDATE_MOVE_COORDINATES = {-9, -7, 7, 9};

    
    //
    private final static Map<Integer, MoveUtils.Line[]> PRECOMPUTED_CANDIDATES = computeCandidates();

// هنا الفانكشن الاول للاستدعاء 
    public Bishop(final Alliance alliance,
                  final int piecePosition) {
        super(PieceType.BISHOP, alliance, piecePosition, true);
    }
//هنا الفانكشن الثاني يختص بتحديد ما اذا كانت الحركة الاولى ام الثانية 
    public Bishop(final Alliance alliance,
                  final int piecePosition,
                  final boolean isFirstMove) {
        super(PieceType.BISHOP, alliance, piecePosition, isFirstMove);
    }
    
//طريقة تقوم بحساب وتخزين المرشحين للحركات القانونية
    private static Map<Integer, MoveUtils.Line[]> computeCandidates() {
        Map<Integer, MoveUtils.Line[]> candidates = new HashMap<>();
        for (int position = 0; position < BoardUtils.NUM_TILES; position++) {
            List<MoveUtils.Line> lines = new ArrayList<>();
            for (int offset : CANDIDATE_MOVE_COORDINATES) {
                int destination = position;
                MoveUtils.Line line = new MoveUtils.Line();
                while (BoardUtils.isValidTileCoordinate(destination)) {
                    if (isFirstColumnExclusion(destination, offset) ||
                            isEighthColumnExclusion(destination, offset)) {
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

//تحسب وتعيد الحركات القانونية الممكنة في اللوحة الحالية. تستخدم الحركات المحسوبة مسبقًا وتتحقق من القطع الموجودة على المواضع المحتملة.
//اي انها تحسب وتتحقق من القطع الموجودة مسبقا 
    
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
        return this.pieceAlliance.bishopBonus(this.piecePosition);
    }

    
     //تنشئ وتعيد او ترجع نسخة جديدة من الحجر بعد تنفيذ الحركة
    @Override
    public Bishop movePiece(final Move move) {
        return PieceUtils.INSTANCE.getMovedBishop(move.getMovedPiece().getPieceAllegiance(), move.getDestinationCoordinate());
    }

    
    //تحويل الكائن إلى سلسلة نصية
    
    @Override
    public String toString() {
        return this.pieceType.toString();
    }

    
       //هذه هي الاستثناءات الخاصة بالأعمدة
    
    //ولاكون دقيق اكثر هذا الخوارزمية هيا للتحقق حسب الاعمدة الموضحة مما يمنع بعض الحركات ولهذا استخدمنا بوليان وريتيرن 
    private static boolean isFirstColumnExclusion(final int position,
                                                  final int offset) {
        return (BoardUtils.INSTANCE.FIRST_COLUMN.get(position) &&
                ((offset == -9) || (offset == 7)));
    }

    
    private static boolean isEighthColumnExclusion(final int position,
                                                   final int offset) {
        return BoardUtils.INSTANCE.EIGHTH_COLUMN.get(position) &&
                ((offset == -7) || (offset == 9));
    }

}