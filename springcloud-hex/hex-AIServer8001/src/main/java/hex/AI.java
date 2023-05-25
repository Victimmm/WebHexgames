package hex;
import java.awt.*;
import java.util.*;

//AI������Ϊbee����������Ϊqueen bee ������������   https://blog.csdn.net/smnooy/article/details/77510003
public class AI implements Player  {
    private final int RED = 1, BLUE = 2, EMPTY = 0;
    int[][] pieces;  
    public int team;
    private ArrayList<AIHistoryObject> history;//AI��״̬�б� �ڵ��ó���ʱʹ�á�
    private int gridSize;
    private EvaluationNode[][] nodesArray;
	public int moveNumber=1;
    public  Point lastmove=null;
    public Point bestmove=null;
    int Max_depth;
    //�洢�ṹ���洢���͵ȶ��Ǳ���֪����
    public AI(int depth,int gridSize) {
    	//1��2��
    	this.gridSize=gridSize;
    	Max_depth=depth;
        pieces = new int[gridSize + 2][gridSize + 2];//13*13
        for (int i = 1; i < pieces.length - 1; i++) {//ai����������11*11
            for (int j = 1; j < pieces.length - 1; j++) {
                pieces[i][j] = EMPTY;//Ϊ0��ʾ��ǰ���û������
            }
        }
        for (int i = 1; i < pieces.length - 1; i++){//��Ե���������,���ڼ���˫����
            pieces[i][0] = RED ;//����Ϊ��
            pieces[0][i] = BLUE;//����Ϊ��
            pieces[i][pieces.length - 1] = RED;
            pieces[pieces.length - 1][i] = BLUE;
        }
       this.history = new ArrayList<AIHistoryObject>();
    }
	public void setTeam(int team){
		this.team=team;
        if(team==2){
        	this.getPlayerTurn();
        }
    }
    
    public void setDepth(int height){
    	this.Max_depth=height;
    }
    
    public Point setMove( Point hex)
    {
    	lastmove=hex;
    	moveNumber++;
    	bestmove=getPlayerTurn();
		return bestmove;
    }

    public Point getPlayerTurn() {
        AIHistoryObject state = new AIHistoryObject(pieces); 
        history.add(state);//����ʱ�ð������ʷ��¼ɾ��,movenumberҲ�ü�1
        Point lastMove=null;
        
        if (moveNumber > 1) //���ǵ�һ������� 
        	lastMove = new Point(gridSize - 1 - lastmove.y, lastmove.x);
        moveNumber++;
        if (lastMove == null) {//����ǵ�һ����������ӷ���5,5��λ��
            pieces[pieces.length / 2][pieces.length / 2] = team;
           return new Point(5,5);
        }
// 		����Ѿ��������ƶ���Bee�������������м�¼�ƶ��������ƶ���
        else
        {
            pieces[lastMove.x + 1][lastMove.y + 1] = team == 1 ? 2 : 1;//����һ���˷��µ�����ӵ�AI������������
            Point bestMove = getBestMove();//ͨ�����������㷨��������������ȡ��ѵ��ŷ�
            pieces[bestMove.x][bestMove.y] = team;
            int x = bestMove.x - 1;
            int y = bestMove.y - 1;
            return new Point(y, gridSize - 1 - x);
        }
    }

    public void undoCalled() {//����Ĳ���
        if (history.size() > 0) {
            AIHistoryObject previousState = history.get(history.size() - 1);//��ȡ���һ��,Ȼ����ɾ��
            pieces = previousState.pieces;//
            history.remove(history.size() - 1);
        }
    }

    private Point getBestMove() {
        //���������ƶ�����Ϊ��Ч�ƶ�����ʹ�ÿ��ܵ�����ƶ�ֵ
        int bestValue = team == RED ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        int bestRow = -1;
        int bestColumn = -1;
        //���������ϵĿ����,���������ж�á�
        for (int i = 1; i < pieces.length - 1; i++) {
            for (int j = 1; j < pieces.length - 1; j++) {
                if (pieces[i][j] != 0)
                    continue;
                //ͨ��չ����Ϸ����������ȡ�Ե�ǰ�ƶ���������
                pieces[i][j] = team;
                int value = expand(1, bestValue, team == RED ? BLUE : RED, nodesArray);//�Ե�ǰ�߷�չ������������ͨ���������������Ž�ĳ���ǰ�߷�������ֵ
                pieces[i][j] = EMPTY;
                // �����һ���ж���ĿǰΪֹ������ж����бȽϣ�����¼��õ��ƶ���
                if (team == RED && value > bestValue) {//��ɫ��������ֵԽ���߷�Խ��
                    bestValue = value;
                    bestRow = i;
                    bestColumn = j;  
                }
                else if (team == BLUE && value < bestValue) {//��ɫ����������ֵԽС���߷�Խ��
                    bestValue = value;
                    bestRow = i;
                    bestColumn = j;
                }
            }
        }
        return new Point(bestRow, bestColumn);
    }

    private int expand(int depth, int previousBest, int currentColour, EvaluationNode[][] nodesArray) {//��������չ��
        // �������������ȣ���ʹ������������������չ����������֧��
        if (depth ==Max_depth) //���������  
            return evaluate();
        int bestValue = currentColour == RED ? Integer.MIN_VALUE : Integer.MAX_VALUE;//��ɫminΪ��������ֵ������maxҲ��
        // ��ȡ���п��ܵ��ƶ���
        Iterator<Move> iterator = getMoves().iterator();
        //ֻ����չ��������ѵĽڵ�Ĳ�������
        for (int i = 0; i < 7 && iterator.hasNext(); i++) {//7Ϊ�������Ŀ��
            //������ ��ȡ��һ�����ƶ�value��������ѡ����ѵ��߷���
            Move nextMove = (Move) iterator.next();
            pieces[nextMove.row][nextMove.column] = currentColour;
            int value = expand(depth + 1, bestValue,currentColour == RED ? BLUE :RED, nodesArray);//�ݹ鹹��������
            pieces[nextMove.row][nextMove.column] = EMPTY;
            // ʹ��Alpha-Beta�����㷨�������һ���ƶ���ĿǰΪֹ������ƶ����бȽϡ�
            if (currentColour == RED && value > bestValue)
                bestValue = value;
            else if (currentColour == BLUE && value < bestValue)
                bestValue = value;
            // �����ǰ���ƶ�ʹ������֧����޼�ֵ�������κβ��з�֧����ֹͣ��չ�������Ե�ǰ��֧�Ľ�����չ��������
            if (currentColour == RED && bestValue > previousBest ||currentColour == BLUE && bestValue < previousBest)
                return bestValue;
        }
        // ����ڴ���ȴ��޷��ƶ�����ֱ�Ӽ���������
        if (bestValue == Integer.MAX_VALUE || bestValue == Integer.MIN_VALUE)
            bestValue = evaluate();
        return bestValue;
    }
    
    //����˫����,����˫���ֱ����ߵ�˫�����ֵ,���ĸ�
    private void calculateAB(int[][] redA, int[][] redB, int[][] blueA, int [][] blueB) {//
        boolean found = true;
        while (found) {
            found = false;
            //���������ϵ�ÿ��λ�ò�����Ƿ���Ը�������
            for (int j = 1; j < redA.length - 1; j++) {
                for (int i = 1; i < redA.length - 1; i++) {
                    if (redA[i][j] != 100000)
                        continue;
                    if (pieces[i][j] != 0)
                        continue;
                  //ͨ�����������ھӲ�����������ֵ��������ھӵĵڶ�����Сֵ������λ�á�
                    int min = 100000;
                    int secondMin = 100000;
                    Iterator<EvaluationNode> iter = nodesArray[i][j].redNeighbours.iterator();//Ѱ���ھӵ�ͬʱ���¸õ��˫�����ֵ
                    while (iter.hasNext()) {
                        EvaluationNode next = (EvaluationNode) iter.next();
                        int number = redA[next.row][next.column];
                        if (number < secondMin) {
                            secondMin = number;
                            if (number < min) {
                                secondMin = min;
                                min = number;
                            }
                        }
                    }
                    if (secondMin < 100) {
                        if (redA[i][j] != secondMin + 1) {
                            found = true;
                            redA[i][j] = secondMin + 1;
                        }
                    }
                }
            }
        }
        // �����ڶ�����ɫ��˫��������
        found = true;
        while (found) {
            found = false;
            for (int j = redB.length - 2; j > 0; j--) {
                for (int i = 1; i < redB.length - 1; i++) {
                    if (redB[i][j] != 100000)
                        continue;
                    if (pieces[i][j] != 0)
                        continue;
                    int min = 100000;
                    int secondMin = 100000;
                    Iterator<EvaluationNode> iter = nodesArray[i][j].redNeighbours.iterator();
                    while (iter.hasNext()) {
                        EvaluationNode next = (EvaluationNode) iter.next();
                        int number = redB[next.row][next.column];
                        if (number < secondMin) {
                            secondMin = number;
                            if (number < min) {
                                secondMin = min;
                                min = number;
                            }
                        }
                    }
                    if (secondMin < 100) {
                        if (redB[i][j] != secondMin + 1) {
                            found = true;
                            redB[i][j] = secondMin + 1;
                        }
                    }
                }
            }
        }
        // ������һ����ɫ��˫��������
        found = true;
        while (found) {
            found = false;
            for (int i = 1; i < blueA.length - 1; i++) {
                for (int j = 1; j < blueA.length - 1; j++) {
                    if (blueA[i][j] != 100000)
                        continue;
                    if (pieces[i][j] != 0)
                        continue;
                    int min = 100000;
                    int secondMin = 100000;
                    Iterator<EvaluationNode> iter = nodesArray[i][j].blueNeighbours.iterator();

                    while (iter.hasNext()) {
                        EvaluationNode next = (EvaluationNode) iter.next();
                        int number = blueA[next.row][next.column];
                        if (number < secondMin) {
                            secondMin = number;
                            if (number < min) {
                                secondMin = min;
                                min = number;
                            }
                        }
                    }
                    if (secondMin < 100) {
                        if (blueA[i][j] != secondMin + 1) {
                            found = true;
                            blueA[i][j] = secondMin + 1;
                        }
                    }
                }
            }
        }
        //�����ڶ�����ɫ��˫��������
        found = true;
        while (found) {
            found = false;
            for (int i = 1; i < blueB.length - 1; i++) {
                for (int j = blueB.length - 2; j > 0; j--) {
                    if (blueB[i][j] != 100000)
                        continue;
                    if (pieces[i][j] != 0)
                        continue;
                    int min = 100000;
                    int secondMin = 100000;
                    Iterator<EvaluationNode> iter = nodesArray[i][j].blueNeighbours.iterator();
                    while (iter.hasNext()) {
                        EvaluationNode next = (EvaluationNode) iter.next();
                        int number = blueB[next.row][next.column];
                        if (number < secondMin) {
                            secondMin = number;
                            if (number < min) {
                                secondMin = min;
                                min = number;
                            }
                        }
                    }
                    if (secondMin < 100) {
                        if (blueB[i][j] != secondMin + 1) 
                        {
                            found = true;
                            blueB[i][j] = secondMin + 1;
                        }
                    }
                }
            }
        }
    }

    private ArrayList<Move> getMoves() {//���㡢�ҳ���ǰ�����һ�����п��ܵ����
        // Ϊ��ǰλ�ù�����������
        nodesArray = new EvaluationNode[pieces.length][pieces.length];
        EvaluationNode.buildEvaluationBoard(pieces, nodesArray);
        // �����ĸ�˫�������С�
        int[][] redA = new int[pieces.length][pieces.length];//length=11
        int[][] redB = new int[pieces.length][pieces.length];
        int[][] blueA = new int[pieces.length][pieces.length];
        int[][] blueB = new int[pieces.length][pieces.length];
        for (int i = 0; i < pieces.length; i++) {
            for (int j = 0; j < pieces.length; j++) {
                redA[i][j] = redB[i][j] = blueA[i][j] = blueB[i][j] = 100000;
            }
        }
        
        // ���ĸ��ǽ�����Ϊ0�������ﹹ��˫�������顣
        redA[0][0] = redA[redA.length - 1][0] = redB[0][redB.length - 1] = redB[redB.length - 1][redB.length - 1] = 0;
        blueA[0][0] = blueA[0][blueA.length - 1] = blueB[blueB.length - 1][0] = blueB[blueB.length - 1][blueB.length - 1] = 0;

        calculateAB(redA, redB, blueA, blueB);
        ArrayList<Move> moves = new ArrayList<Move>();
 
        for (int i = 1; i < pieces.length - 1; i++) {
            for (int j = 1; j < pieces.length - 1; j++) {
                if (pieces[i][j] != 0)//��ǰ���������������
                    continue;
                
//                ApplicationContext context=new ClassPathXmlApplicationContext("applicationContext.xml");
//                Move move=(Move)context.getBean("move");
//                move.setRow(j);
//                move.setColumn(j);
//                move.setValue(redA[i][j] + redB[i][j] + blueA[i][j] + blueB[i][j]);
//                moves.add(move);
                
                moves.add(new Move(i, j, redA[i][j] + redB[i][j] + blueA[i][j] + blueB[i][j]));
            }
        }
        // ��˳�����ѵ�������һ�����ŷ������������йؼ���Ϊmove�����value
        Collections.sort(moves);
        return moves;
    }

    private int evaluate() {//��������
        nodesArray = new EvaluationNode[pieces.length][pieces.length];
        EvaluationNode.buildEvaluationBoard(pieces, nodesArray);

        //�����ĸ�˫�������С�
        int[][] redA = new int[pieces.length][pieces.length];
        int[][] redB = new int[pieces.length][pieces.length];
        int[][] blueA = new int[pieces.length][pieces.length];
        int[][] blueB = new int[pieces.length][pieces.length];
        for (int i = 0; i < pieces.length; i++) {
            for (int j = 0; j < pieces.length; j++) {
                redA[i][j] = redB[i][j] = blueA[i][j] = blueB[i][j] = 100000;
            }
        }
        
        // ��������ĸ�������Ϊ0�������ﹹ����
        redA[0][0] = redA[redA.length - 1][0] = redB[0][redB.length - 1] = redB[redB.length - 1][redB.length - 1] = 0;
        blueA[0][0] = blueA[0][blueA.length - 1] = blueB[blueB.length - 1][0] = blueB[blueB.length - 1][blueB.length - 1] = 0;

        calculateAB(redA, redB, blueA, blueB);

        //����Ǳ���������ԡ�   �����ض���ɫ�İ��Ǳ������С���������ɫ��Ӧ�İ���ܺ��Ϸ�����˫����ֵ��
        //�����ض���ɫ���ƶ������ڶ�Ӧ�ڸ���ɫ�İ���ܺ��ϳ�����С˫����ֵ�Ĵ�����
        int redPotential = 100000;
        int bluePotential = 100000;
        int redMobility = 0;
        int blueMobility = 0;
        for (int i = 1; i < redA.length - 1; i++) {
            for (int j = 1; j < redA.length - 1; j++) {
                if (pieces[i][j] == 0) {
                    int temp = redA[i][j] + redB[i][j];
                    if (temp < redPotential) {
                        redPotential = temp;
                        redMobility = 1;
                    }
                    else if (temp == redPotential)
                        redMobility++;
                    temp = blueA[i][j] + blueB[i][j];//˫������Ǳ��ֵ
                    if (temp < bluePotential) {//Ѱ����СǱ��ֵ
                        bluePotential = temp;
                        blueMobility = 1;
                    } else if (temp == bluePotential)
                        blueMobility++;
                }
            }
        }
        return
                100 * (bluePotential - redPotential) - (blueMobility - redMobility);
    }
}