package com.chess.engine.board
        
package tvtc.project;


public abstract class Tile {
    
    
    protected final int tileCoordinate;
    
    Tile(int tileCoordinate) {
        
        this.tileCoordinate = tileCoordinate;
        
  
    }
        //هدف هذا الابستراكت هولمعرفة ما إذا كانت البلاطة مشغولة أم لا 

    
    public abstract boolean istileOoccupied(); 
    
    //ارجاع القطعة على البلاطة
    public abstract Piece getPiece ();
    
    //هنا  وضعنا الامبتي عشان البرنامنج يفرق بين الارضية الفارغه والمشغولة
    
     public static final class EmpTytile extends Tile {
    
    
    EmpTytile(int Coordinate) {
    
    super (Coordinate);
}
     
    
     
     
//هنا وضعنا شرط انه يرجع لنا قيمة قولس  لان  البلاطة فارغة 
    
    
    
      @Override 
    
     public boolean isTileOccupied() {
     
         return false ;
     
     }
     
     //هنا يتجاوز امر ارجاع القطعة لان البلاطة فارغة
     @Override 

     public Piece getPiece () {
     
     return null ; 
     
     
   
     }
     
}

     //تعريف قيمة نهائية لتوريث الى تايل
     
    public static final class OccupiedTile extends Tile {
        
        

        //تعريف متغير يخزن القطعة الموجودة على البلاطة.
        private final    Piece pieceOnTile;
        
        //الذي يأخذ إحداثيات البلاطة والقطعة الموجودة على البلاطة ويخزنها.
        
        OccupiedTile(int tileCoordinate, Piece pieceOnTile) { 
            
            super( tileCoordinate);
            this.pieceOnTile = pieceOnTile;
            
         
            
        }
        
        // ارجاع القمية ترو لان اذا كانت البلاطة مشغولة
             @Override
            
             public boolean isTileOccupied(){
                 return true ; 
             }
             
             //تجاوز الدالة  لترجع القطعة المخزنة في البلاطة.
                @Override
            
             public Piece getPiece() { 
                 
                return this.pieceOnTile;
             }
             
             
             }
    
    
        
    }