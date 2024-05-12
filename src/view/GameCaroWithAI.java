/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Stack;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.plaf.metal.MetalBorders;
import model.Caro_Button;
import model.Player;

/**
 *
 * @author ASUS
 */
public class GameCaroWithAI extends javax.swing.JFrame {

    private static final int ROW = 19;
    private static final int COL = 19;
    private final Caro_Button[][] caro_Buttons = new Caro_Button[COL][ROW];
    private static final int winScore = 999999999;
    private int gameNumber = 0;
    private int userWin = 0;
    private int aIWin = 0;
    private int gameMode;
    public int playerTurn;
    private static final int  allButton = COL*ROW;
    private int countButtonEnable = 0;
    private Stack<Caro_Button> stackOfButtons = new Stack<>();
    private Player currentPlayer;
    /**
     * Creates new form PlayGame
     */
    public GameCaroWithAI(int gameMode, int playerTurn, Player currentPlayer) {
        this.gameMode = gameMode;
        this.currentPlayer = currentPlayer;
        initComponents();
        this.playerTurn = playerTurn;
        this.jPanel_Container_button.setLayout(new GridLayout(19, 19));
        this.addButtonIntoBoard();

        this.getContentPane().setLayout(null);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resource_caro/icon.png")));
        this.jLabel_image_player1.setIcon(new ImageIcon("src/resource_caro/human.png"));
        this.jLabel_image_player2.setIcon(new ImageIcon("src/resource_caro/ai.png"));
        this.jLabel_name_human.setText(currentPlayer.getPlayerUserName());
        this.jTextArea1.setText("\n Luật chơi: Ai có đủ 5 X hoặc O trên cùng 1 hàng thẳng, chéo, ngang trước thì sẽ\n giành chiến thắng\n"
                + "\n Sẽ không tính thắng với hai trường hợp sau:"
                + "\n - Quân thứ 5 của bạn ở hàng cuối hoặc cột cuối"
                + "\n - Bạn đã có đủ 5 quân theo luật nhưng bị chặn hai đầu của quân đối thủ"
                + "\n \n Chúc bạn chơi game vui vẻ!");
        this.newGame();
    }

    private void addButtonIntoBoard() {
        //Setup play button
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                caro_Buttons[i][j] = new Caro_Button(i, j, true);
                this.jPanel_Container_button.add(caro_Buttons[i][j]);
                caro_Buttons[i][j].addMouseListener(new MouseListener() {
                    @Override
                    public void mouseReleased(MouseEvent e) {
                        Caro_Button caro_Buttons_event = (Caro_Button) e.getSource();
                  
                        if (caro_Buttons_event.isEnabled()) {
                            caro_Buttons_event.setState(true);
                            countButtonEnable++;
                            if (getScore(getMatrixBoard(), true, false) >= winScore) {
                                JOptionPane.showMessageDialog(null, "Bạn đã thắng");
                                userWin++;
                                newGame();
                                stackOfButtons.pop().setBorder(new MetalBorders.ButtonBorder());
                                JOptionPane.showMessageDialog(rootPane, "Bạn thắng nên nhường cho máy đi trước nhé", "Ván mới", JOptionPane.INFORMATION_MESSAGE);
                                caro_Buttons[9][9].setState(false);
                                countButtonEnable++;
                                stackOfButtons.push(caro_Buttons[9][9]);
                                return;
                            }
                            checkDraw();
                            aiMove();
                        }

                    }

                    @Override
                    public void mousePressed(MouseEvent e) {
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                    }

                    @Override
                    public void mouseClicked(MouseEvent e) {
                    }
                });
            }
        }
    }

  

   
    private void newGame() {
        gameNumber++;
        countButtonEnable = 0;
        this.jlabel_game_count.setText("Game " + gameNumber);
        this.Jlabel_Score_Human.setText("" + userWin);
        this.jlabel_score_AI.setText("" + aIWin);

        for (int i = 0; i < caro_Buttons.length; i++) {
            for (int j = 0; j < caro_Buttons.length; j++) {
                caro_Buttons[i][j].resetState();
            }
        }
        if (playerTurn == 1 && gameNumber == 1) {
            JOptionPane.showMessageDialog(rootPane, "Máy đi trước", "Ván mới", JOptionPane.INFORMATION_MESSAGE);
            caro_Buttons[9][9].setState(false);
            countButtonEnable++;
            stackOfButtons.push(caro_Buttons[9][9]);
        } else if (playerTurn == 0 && gameNumber == 1) {
            JOptionPane.showMessageDialog(rootPane, "Bạn đi trước", "Ván mới", JOptionPane.INFORMATION_MESSAGE);
        }
    }

     private void aiMove() {
        int nextMoveX = 0, nextMoveY = 0;
        int[] bestMove = calcNextMove(gameMode+1);
        if (bestMove != null) {
            nextMoveX = bestMove[0];
            nextMoveY = bestMove[1];
        }
        caro_Buttons[nextMoveX][nextMoveY].setState(false);
        
        countButtonEnable++;
            if (!stackOfButtons.empty()) {
             stackOfButtons.pop().setBorder(new MetalBorders.ButtonBorder());
        }
        stackOfButtons.push( caro_Buttons[nextMoveX][nextMoveY]);
       
        aiEndGameCheck();
        checkDraw();
    }

    private void aiEndGameCheck() {
        if (getScore(getMatrixBoard(), false, true) >= winScore) {
            JOptionPane.showMessageDialog(null, "Bạn đã thua");
            aIWin++;
            JOptionPane.showMessageDialog(rootPane, "Bạn đã được máy nhường đi trước", "Ván mới", JOptionPane.INFORMATION_MESSAGE);
            newGame();
            stackOfButtons.pop().setBorder(new MetalBorders.ButtonBorder());
        }
    }
    
    private void checkDraw(){
        if (countButtonEnable == allButton) {
            JOptionPane.showMessageDialog(null, "Hòa Rồi Nhé");
            JOptionPane.showMessageDialog(rootPane, "Bạn đã được máy nhường đi trước", "Ván mới", JOptionPane.INFORMATION_MESSAGE);
            newGame();
            stackOfButtons.pop().setBorder(new MetalBorders.ButtonBorder());
        }
    }
    
     public int[] calcNextMove(int depth) {
        int[][] board = getMatrixBoard();
        Object[] bestMove = searchWinningMove(board);
        Object[] badMove = searchLoseMove(board);

        int[] move = new int[2];

        if (badMove[1] != null && badMove[2] != null) {

            move[0] = (Integer) (badMove[1]);
            move[1] = (Integer) (badMove[2]);
        }

        if (bestMove[1] != null && bestMove[2] != null) {

            move[0] = (Integer) (bestMove[1]);
            move[1] = (Integer) (bestMove[2]);
             return move;
        } else {

            bestMove = minimaxSearchAB(depth, board, true, -999999, winScore);
            if (bestMove[1] == null) {
                move = null;
            } else {
                move[0] = (Integer) (bestMove[1]);
                move[1] = (Integer) (bestMove[2]);
            }
        }
        return move;
    }

    public int[][] playNextMove(int[][] board, int[] move, boolean isUserTurn) {
        int i = move[0], j = move[1];
        int[][] newBoard = new int[ROW][COL];
        for (int h = 0; h < ROW; h++) {
            for (int k = 0; k < COL; k++) {
                newBoard[h][k] = board[h][k];
            }
        }
        newBoard[i][j] = isUserTurn ? 2 : 1;
        return newBoard;
    }

    private Object[] searchWinningMove(int[][] matrix) {
        ArrayList<int[]> allPossibleMoves = generateMoves(matrix);
        Object[] winningMove = new Object[3];
        for (int[] move : allPossibleMoves) {
            int[][] dummyBoard = playNextMove(matrix, move, false);
            if (getScore(dummyBoard, false, false) >= winScore) {
                winningMove[1] = move[0];
                winningMove[2] = move[1];
                return winningMove;
            }
        }
        return winningMove;
    }

    private Object[] searchLoseMove(int[][] matrix) {
        ArrayList<int[]> allPossibleMoves = generateMoves(matrix);
        Object[] losingMove = new Object[3];
        for (int[] move : allPossibleMoves) {
            int[][] dummyBoard = playNextMove(matrix, move, true);
            if (getScore(dummyBoard, true, false) >= winScore) {
                losingMove[1] = move[0];
                losingMove[2] = move[1];
                return losingMove;
            }
        }

        return losingMove;
    }


    
   public Object[] minimaxSearchAB(int depth, int[][] board, boolean max, double alpha, double beta) {
       
        ArrayList<int[]> allPossibleMoves = generateMoves(board);
        
       if (depth == 0 || allPossibleMoves.isEmpty()) {
            return new Object[]{evaluateBoardForWhite(board, !max), null, null};
        }

        Object[] bestMove = new Object[3];

        if (max) {
            bestMove[0] = -1.0;
                int i = 0;
            for (int[] move : allPossibleMoves) {
            
                // Chơi thử với move hiện tại
                int[][] dummyBoard = playNextMove(board, move, false);
                Object[] tempMove = minimaxSearchAB(depth - 1, dummyBoard, false, alpha, beta);

                // Cập nhật alpha
                if ((Double) (tempMove[0]) > alpha) {
                    alpha = (Double) (tempMove[0]);
                }
                // Cắt tỉa beta
                if ((Double) (tempMove[0]) >= beta) {
                    return tempMove;
                }
                if ((Double) tempMove[0] > (Double) bestMove[0]) {
                    bestMove = tempMove;
                    bestMove[1] = move[0];
                    bestMove[2] = move[1];
                }
            }

        } else {
            bestMove[0] = 100000000.0;
            bestMove[1] = allPossibleMoves.get(0)[0];
            bestMove[2] = allPossibleMoves.get(0)[1];
            for (int[] move : allPossibleMoves) {
                int[][] dummyBoard = playNextMove(board, move, true);

                Object[] tempMove = minimaxSearchAB(depth - 1, dummyBoard, true, alpha, beta);

                // Cập nhật beta
                if (((Double) tempMove[0]) < beta) {
                    beta = (Double) (tempMove[0]);
                }
                // Cắt tỉa alpha
                if ((Double) (tempMove[0]) <= alpha) {
                    return tempMove;
                }
                if ((Double) tempMove[0] < (Double) bestMove[0]) {
                    bestMove = tempMove;
                    bestMove[1] = move[0];
                    bestMove[2] = move[1];
                }
            }
        }
        return bestMove;
    }
     
      public double evaluateBoardForWhite(int[][] board, boolean userTurn) {

        double xScore = getScore(board, true, userTurn);
        double oScore = getScore(board, false, userTurn);

        if (xScore == 0) {
            xScore = 1.0;
        }

        return oScore / xScore;

    }
     // Tìm tất cả những ô trống có đánh XO liền kề
    public ArrayList<int[]> generateMoves(int[][] boardMatrix) {
        ArrayList<int[]> moveList = new ArrayList<int[]>();

        int boardSize = boardMatrix.length;
   
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (boardMatrix[i][j] > 0) {
                    continue;
                }
                if (i > 0) {
                    if (j > 0) {
                        if (boardMatrix[i - 1][j - 1] > 0 || boardMatrix[i][j - 1] > 0) {
                            int[] move = {i, j};
                            moveList.add(move);
                            continue;
                        }
                    }
                    if (j < boardSize - 1) {
                        if (boardMatrix[i - 1][j + 1] > 0 || boardMatrix[i][j + 1] > 0) {
                            int[] move = {i, j};
                            moveList.add(move);
                            continue;
                        }
                    }
                    if (boardMatrix[i - 1][j] > 0) {
                        int[] move = {i, j};
                        moveList.add(move);
                        continue;
                    }
                }

                if (i < boardSize - 1) {
                    if (j > 0) {
                        if (boardMatrix[i + 1][j - 1] > 0 || boardMatrix[i][j - 1] > 0) {
                            int[] move = {i, j};
                            moveList.add(move);
                            continue;
                        }
                    }
                    if (j < boardSize - 1) {
                        if (boardMatrix[i + 1][j + 1] > 0 || boardMatrix[i][j + 1] > 0) {
                            int[] move = {i, j};
                            moveList.add(move);
                            continue;
                        }
                    }
                    if (boardMatrix[i + 1][j] > 0) {
                        int[] move = {i, j};
                        moveList.add(move);
                        continue;
                    }
                }

            }
        }
        return moveList;

    }

     public int getScore(int[][] boardMatrix, boolean forX, boolean blacksTurn) {

        return evaluateHorizontal(boardMatrix, forX, blacksTurn)
                + evaluateVertical(boardMatrix, forX, blacksTurn)
                + evaluateDiagonal(boardMatrix, forX, blacksTurn);
    }

    public static int evaluateHorizontal(int[][] boardMatrix, boolean forX, boolean playersTurn) {

        int consecutive = 0;
        int blocks = 2;
        int score = 0;

        for (int i = 0; i < boardMatrix.length; i++) {
            for (int j = 0; j < boardMatrix.length; j++) {
                if (boardMatrix[i][j] == (forX ? 2 : 1)) {
                    //2. Đếm...
                    consecutive++;
                } // gặp ô trống
                else if (boardMatrix[i][j] == 0) {
                    if (consecutive > 0) {
                        // Ra: Ô trống ở cuối sau khi đếm. Giảm block rồi bắt đầu tính điểm sau đó reset lại ban đầu
                        blocks--;
                        score += getConsecutiveSetScore(consecutive, blocks, forX == playersTurn);
                        consecutive = 0;
                        blocks = 1;
                    } else {
                        // 1. Vào reset lại blocks = 1 rồi bắt đầu đếm
                        blocks = 1;
                    }
                } //gặp quân địch
                else if (consecutive > 0) {
                    // 2.Ra:  Ô bị chặn sau khi đếm. Tính điểm sau đó reset lại.
                    score += getConsecutiveSetScore(consecutive, blocks, forX == playersTurn);
                    consecutive = 0;
                    blocks = 2;
                } else {
                    //1. Vào: reset lại blocks = 2 rồi bắt đầu đếm
                    blocks = 2;
                }
            }
            
            // 3. Ra: nhưng lúc này đang ở cuối. Nếu liên tục thì vẫn tính cho đến hết dòng
            if (consecutive > 0) {
                score += getConsecutiveSetScore(consecutive, blocks, forX == playersTurn);
            }
            // reset lại để tiếp tục chạy cho dòng tiếp theo
            consecutive = 0;
            blocks = 2;
        }
        return score;
    }
    // hàm tính toán đường dọc tương tự như đường ngang

    public static int evaluateVertical(int[][] boardMatrix, boolean forX, boolean playersTurn) {

        int consecutive = 0;
        int blocks = 2;
        int score = 0;

        for (int j = 0; j < boardMatrix.length; j++) {
            for (int i = 0; i < boardMatrix.length; i++) {
                if (boardMatrix[i][j] == (forX ? 2 : 1)) {
                    consecutive++;
                } else if (boardMatrix[i][j] == 0) {
                    if (consecutive > 0) {
                        blocks--;
                        score += getConsecutiveSetScore(consecutive, blocks, forX == playersTurn);
                        consecutive = 0;
                        blocks = 1;
                    } else {
                        blocks = 1;
                    }
                } else if (consecutive > 0) {
                    score += getConsecutiveSetScore(consecutive, blocks, forX == playersTurn);
                    consecutive = 0;
                    blocks = 2;
                } else {
                    blocks = 2;
                }
            }

            if (consecutive > 0) {
                score += getConsecutiveSetScore(consecutive, blocks, forX == playersTurn);

            }
            consecutive = 0;
            blocks = 2;

        }
        return score;
    }

    // Hàm tính toán 2 đường chéo tương tự như hàng ngang
    public static int evaluateDiagonal(int[][] boardMatrix, boolean forX, boolean playersTurn) {

        int consecutive = 0;
        int blocks = 2;
        int score = 0;
        // Đường chéo /
        for (int k = 0; k <= 2 * (boardMatrix.length - 1); k++) {
            int iStart = Math.max(0, k - boardMatrix.length + 1);
            int iEnd = Math.min(boardMatrix.length - 1, k);
            for (int i = iStart; i <= iEnd; ++i) {
                int j = k - i;

                if (boardMatrix[i][j] == (forX ? 2 : 1)) {
                    consecutive++;
                } else if (boardMatrix[i][j] == 0) {
                    if (consecutive > 0) {
                        blocks--;
                        score += getConsecutiveSetScore(consecutive, blocks, forX == playersTurn);
                        consecutive = 0;
                        blocks = 1;
                    } else {
                        blocks = 1;
                    }
                } else if (consecutive > 0) {
                    score += getConsecutiveSetScore(consecutive, blocks, forX == playersTurn);
                    consecutive = 0;
                    blocks = 2;
                } else {
                    blocks = 2;
                }

            }
            if (consecutive > 0) {
                score += getConsecutiveSetScore(consecutive, blocks, forX == playersTurn);

            }
            consecutive = 0;
            blocks = 2;
        }
        // Đường chéo \
        for (int k = 1 - boardMatrix.length; k < boardMatrix.length; k++) {
            int iStart = Math.max(0, k);
            int iEnd = Math.min(boardMatrix.length + k - 1, boardMatrix.length - 1);
            for (int i = iStart; i <= iEnd; ++i) {
                int j = i - k;

                if (boardMatrix[i][j] == (forX ? 2 : 1)) {
                    consecutive++;
                } else if (boardMatrix[i][j] == 0) {
                    if (consecutive > 0) {
                        blocks--;
                        score += getConsecutiveSetScore(consecutive, blocks, forX == playersTurn);
                        consecutive = 0;
                        blocks = 1;
                    } else {
                        blocks = 1;
                    }
                } else if (consecutive > 0) {
                    score += getConsecutiveSetScore(consecutive, blocks, forX == playersTurn);
                    consecutive = 0;
                    blocks = 2;
                } else {
                    blocks = 2;
                }

            }
            if (consecutive > 0) {
                score += getConsecutiveSetScore(consecutive, blocks, forX == playersTurn);

            }
            consecutive = 0;
            blocks = 2;
        }
        return score;
    }

     public static int getConsecutiveSetScore(int count, int blocks, boolean currentTurn) {
        final int winGuarantee = 100000;
        if (blocks == 2 && count <= 5) {
            return 0;
        }
        switch (count) {
            // Ăn 5 -> Cho điểm cao nhất
            case 5: {
                return winScore;
            }
            case 4: {
                // Đang 4 -> Tuỳ theo lược và bị chặn
                if (currentTurn) {
                    return winGuarantee;
                } else {
                    if (blocks == 0 || blocks == 1) {
                        return winGuarantee/2;
                    } else {
                        return 500;
                    }
                }
            }
            case 3: {
                // Đang 3: Block = 0
                if (blocks == 0) {
                    // Nếu lược của currentTurn thì ăn 3 + 1 = 4 (không bị block) -> 50000 -> Khả năng thắng cao. 
                    // Ngược lại không phải lược của currentTurn thì khả năng bị blocks cao
                    if (currentTurn) {
                        return winGuarantee/3;
                    } else {
                        return 200;
                    }
                } else {
                    // Block == 1 hoặc Blocks == 2
                    if (currentTurn) {
                        return 10;
                    } else {
                        return 5;
                    }
                }
            }
            case 2: {
                // Tương tự với 2
                if (blocks == 0) {
                    if (currentTurn) {
                        return 7;
                    } else {
                        return 5;
                    }
                } else {
                    return 3;
                }
            }
            case 1: {
                return 1;
            }
        }
        return winScore * 2;
    }
     
    public int[][] getMatrixBoard() {
        int[][] matrix = new int[ROW][COL];
        for (int i = 0; i < caro_Buttons.length; i++) {
            for (int j = 0; j < caro_Buttons.length; j++) {
                int value = caro_Buttons[i][j].value;
                matrix[i][j] = value;
            }
        }
        return matrix;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jpanel_container_player = new javax.swing.JPanel();
        jpanel_player1 = new javax.swing.JPanel();
        jLabel_image_player2 = new javax.swing.JLabel();
        jLabel_image_player1 = new javax.swing.JLabel();
        jLabel_name_player2 = new javax.swing.JLabel();
        jLabel_name_human = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jPanel_Container_button = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jbutton_go_back = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jlabel_score = new javax.swing.JLabel();
        Jlabel_Score_Human = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jlabel_score_AI = new javax.swing.JLabel();
        humanScore = new javax.swing.JLabel();
        AIScore = new javax.swing.JLabel();
        jlabel_game_count = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Cờ Caro");
        setIconImage(getToolkit().createImage(".\\NienLuanCoSo\\src\\resource_caro\\IconFrame.png")
        );
        setResizable(false);

        jpanel_container_player.setBackground(new java.awt.Color(255, 255, 255));
        jpanel_container_player.setForeground(new java.awt.Color(255, 255, 255));

        jpanel_player1.setForeground(new java.awt.Color(255, 255, 255));

        jLabel_image_player1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel_image_player1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jLabel_name_player2.setFont(new java.awt.Font("Cambria", 0, 14)); // NOI18N
        jLabel_name_player2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_name_player2.setText("Terminal");

        jLabel_name_human.setFont(new java.awt.Font("Cambria", 0, 14)); // NOI18N
        jLabel_name_human.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_name_human.setText("Ewersdf");

        jLabel1.setFont(new java.awt.Font("Segoe UI Black", 0, 48)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("VS");

        javax.swing.GroupLayout jpanel_player1Layout = new javax.swing.GroupLayout(jpanel_player1);
        jpanel_player1.setLayout(jpanel_player1Layout);
        jpanel_player1Layout.setHorizontalGroup(
            jpanel_player1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpanel_player1Layout.createSequentialGroup()
                .addGroup(jpanel_player1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpanel_player1Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(jLabel_name_human, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jpanel_player1Layout.createSequentialGroup()
                        .addGap(43, 43, 43)
                        .addComponent(jLabel_image_player1, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(54, 54, 54)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 62, Short.MAX_VALUE)
                .addGroup(jpanel_player1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel_image_player2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel_name_player2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jpanel_player1Layout.setVerticalGroup(
            jpanel_player1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpanel_player1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpanel_player1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpanel_player1Layout.createSequentialGroup()
                        .addComponent(jLabel_image_player1, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel_name_human)
                        .addContainerGap(14, Short.MAX_VALUE))
                    .addGroup(jpanel_player1Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jpanel_player1Layout.createSequentialGroup()
                        .addComponent(jLabel_image_player2, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel_name_player2)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        javax.swing.GroupLayout jpanel_container_playerLayout = new javax.swing.GroupLayout(jpanel_container_player);
        jpanel_container_player.setLayout(jpanel_container_playerLayout);
        jpanel_container_playerLayout.setHorizontalGroup(
            jpanel_container_playerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jpanel_player1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jpanel_container_playerLayout.setVerticalGroup(
            jpanel_container_playerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jpanel_player1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel_Container_button.setBackground(new java.awt.Color(204, 204, 204));
        jPanel_Container_button.setBorder(javax.swing.BorderFactory.createCompoundBorder());
        jPanel_Container_button.setForeground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel_Container_buttonLayout = new javax.swing.GroupLayout(jPanel_Container_button);
        jPanel_Container_button.setLayout(jPanel_Container_buttonLayout);
        jPanel_Container_buttonLayout.setHorizontalGroup(
            jPanel_Container_buttonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 656, Short.MAX_VALUE)
        );
        jPanel_Container_buttonLayout.setVerticalGroup(
            jPanel_Container_buttonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jbutton_go_back.setText("Trở Lại");
        jbutton_go_back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbutton_go_backActionPerformed(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(153, 204, 255));
        jPanel2.setForeground(new java.awt.Color(153, 204, 255));

        jlabel_score.setFont(new java.awt.Font("Segoe UI", 3, 18)); // NOI18N
        jlabel_score.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlabel_score.setText("Tỉ Số : ");

        Jlabel_Score_Human.setFont(new java.awt.Font("Source Serif Pro Black", 3, 48)); // NOI18N
        Jlabel_Score_Human.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Jlabel_Score_Human.setText("0");

        jLabel3.setFont(new java.awt.Font("Segoe UI Black", 1, 48)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText(":");

        jlabel_score_AI.setFont(new java.awt.Font("Source Serif Pro Black", 3, 48)); // NOI18N
        jlabel_score_AI.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlabel_score_AI.setText("0");

        humanScore.setText("Người Chơi");

        AIScore.setText("Máy");

        jlabel_game_count.setBackground(new java.awt.Color(255, 51, 102));
        jlabel_game_count.setFont(new java.awt.Font("Segoe UI", 3, 36)); // NOI18N
        jlabel_game_count.setForeground(new java.awt.Color(255, 51, 51));
        jlabel_game_count.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlabel_game_count.setText("Game");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(107, 107, 107)
                .addComponent(humanScore)
                .addGap(118, 118, 118)
                .addComponent(AIScore)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jlabel_score, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Jlabel_Score_Human, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jlabel_score_AI, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(99, 99, 99))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jlabel_game_count, javax.swing.GroupLayout.PREFERRED_SIZE, 430, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jlabel_game_count, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jlabel_score, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Jlabel_Score_Human, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jlabel_score_AI))
                .addGap(8, 8, 8)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(humanScore)
                    .addComponent(AIScore))
                .addContainerGap())
        );

        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("Source Code Pro", 3, 14)); // NOI18N
        jTextArea1.setForeground(new java.awt.Color(102, 102, 255));
        jTextArea1.setLineWrap(true);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 217, Short.MAX_VALUE)
                .addContainerGap())
        );

        jButton1.setText("bàn cờ cũ");
        jButton1.setToolTipText("");
        jButton1.setActionCommand("");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jbutton_go_back, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26))
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jbutton_go_back, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jpanel_container_player, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel_Container_button, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(jpanel_container_player, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jPanel_Container_button, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbutton_go_backActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbutton_go_backActionPerformed
        this.dispose();
        MainMenu newMainMenu = new MainMenu(currentPlayer);
        // TODO add your handling code here:
    }//GEN-LAST:event_jbutton_go_backActionPerformed

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel AIScore;
    public javax.swing.JLabel Jlabel_Score_Human;
    private javax.swing.JLabel humanScore;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel_image_player1;
    private javax.swing.JLabel jLabel_image_player2;
    public javax.swing.JLabel jLabel_name_human;
    private javax.swing.JLabel jLabel_name_player2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    public javax.swing.JPanel jPanel_Container_button;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JButton jbutton_go_back;
    public javax.swing.JLabel jlabel_game_count;
    private javax.swing.JLabel jlabel_score;
    public javax.swing.JLabel jlabel_score_AI;
    private javax.swing.JPanel jpanel_container_player;
    private javax.swing.JPanel jpanel_player1;
    // End of variables declaration//GEN-END:variables
}
