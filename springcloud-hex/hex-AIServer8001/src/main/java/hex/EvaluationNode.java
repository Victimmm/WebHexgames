package hex;

import java.util.HashSet;

public 
class EvaluationNode {
    HashSet<EvaluationNode> redNeighbours;
    HashSet<EvaluationNode> blueNeighbours;
    int row;
    int column;

    EvaluationNode(int row, int column) {
        this.row = row;
        this.column = column;
        redNeighbours = new HashSet<EvaluationNode>();
        blueNeighbours = new HashSet<EvaluationNode>();
    }

    public static void buildEvaluationBoard(int[][] pieces, EvaluationNode[][] nodesArray) {
        // ��ʼ�����е������ڵ㣬���������ǵ��ھ�
        for (int i = 0; i < nodesArray.length; i++)
            for (int j = 0; j < nodesArray.length; j++)
                nodesArray[i][j] = new EvaluationNode(i, j);

        // ����ÿ��EvaluationNode���ھ�
        for (int i = 0; i < nodesArray.length; i++)
            for (int j = 0; j < nodesArray.length; j++) {
                if (pieces[i][j] != 0)
                    continue;
                nodesArray[i][j].redNeighbours = nodesArray[i][j].getNeighbours(1, new HashSet<EvaluationNode>(), nodesArray, pieces);
                nodesArray[i][j].redNeighbours.remove(nodesArray[i][j]);
                nodesArray[i][j].blueNeighbours = nodesArray[i][j].getNeighbours(2, new HashSet<EvaluationNode>(), nodesArray, pieces);
                nodesArray[i][j].blueNeighbours.remove(nodesArray[i][j]);
            }
    }

    private HashSet<EvaluationNode> getNeighbours(int color, HashSet<EvaluationNode> piecesVisited, EvaluationNode[][] nodesArray, int[][] pieces) {
        // ����ѷ��ʵ�ǰƬ�Σ��򷵻ؿ�HashSet
        if (piecesVisited.contains(this))
            return new HashSet<EvaluationNode>();
        HashSet<EvaluationNode> returnValue = new HashSet<EvaluationNode>();
        if (pieces[row][column] == color)
            piecesVisited.add(this);
        //���ǵ�ǰλ�õ������ھ�
        for (int a = -1; a <= 1; a++) {
            for (int b = -1; b <= 1; b++) {
                if (a + b == 0)
                    continue;
                //    O--O--O
                //    | /| /|
                //    |/ |/ |
                //    O--O--O
                //    | /| /|
                //    |/ |/ |
                //    O--O--O
//                �����ǳ����߽���ھ�,��������Щ����
                if (row + a < 0 || row + a == nodesArray.length || column + b < 0 || column + b == nodesArray.length)
                    continue;
                // �����ǰ�ھ�Ϊ�գ�������ӵ��ھ��б��С�
                if (pieces[row + a][column + b] == 0)
                    returnValue.add(nodesArray[row + a][column + b]);
                    // �����ǰ�ھ���һ����ͬ����ɫ�����������ھ���ӵ��ھ��б��С�
                else if (pieces[row + a][column + b] == color) {
                    returnValue.addAll(nodesArray[row + a][column + b].getNeighbours(color, piecesVisited, nodesArray, pieces));
                }
            }
        }
        return returnValue;
    }

    public int hashCode() {//��ϣֵ
        return row * 100 + column;
    }

    public boolean equals(Object other) {
        EvaluationNode otherNode = (EvaluationNode) other;
        return row == otherNode.row && column == otherNode.column;
    }
    
}
