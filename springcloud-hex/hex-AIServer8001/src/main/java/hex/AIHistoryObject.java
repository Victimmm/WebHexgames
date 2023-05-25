package hex;

public class AIHistoryObject {
	int[][] pieces;
    AIHistoryObject(int[][] pieces) {
    	this.pieces = new int[pieces.length][pieces.length];
        for (int i = 0; i < pieces.length; i++) {
            for (int j = 0; j < pieces.length; j++)
                this.pieces[i][j] = pieces[i][j];
        }
    }
}
