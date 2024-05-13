/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Stack;
import javax.swing.JOptionPane;
import javax.swing.plaf.metal.MetalBorders;
import model.Caro_Button;
import model.Player;

/**
 *
 * @author ASUS
 */
public class GameCaro1vs1 extends javax.swing.JFrame {

    private static final int ROW = 23;
    private static final int COL = 23;
    private final Caro_Button[][] caro_Buttons = new Caro_Button[COL][ROW];
    private static final int winScore = 999999999;
    private int gameNumber = 0;
    private static final int allButton = COL * ROW;
    private int countButtonEnable = 0;
    private Player currentPlayer;
    private boolean playerTurn = true;
    private int xWinScore = 0;
    private int oWinScore = 0;
    
    /**
     * Creates new form PlayGame
     */
    public GameCaro1vs1(Player player) {
        initComponents();
        this.jPanel_Container_button.setLayout(new GridLayout(23, 23));
        this.addButtonIntoBoard();
        this.currentPlayer = player;
        this.getContentPane().setLayout(null);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resource_caro/icon.png")));
        this.jTextArea1.setText("\n Luật chơi: Ai có đủ 5 X hoặc O trên cùng 1 hàng thẳng, chéo, ngang trước thì sẽ\n giành chiến thắng \n"
                + "\n Sẽ không tính thắng với hai trường hợp sau:"
                + "\n - Quân thứ 5 của bạn ở hàng cuối hoặc cột cuối"
                + "\n - Bạn đã có đủ 5 quân theo luật nhưng bị chặn hai đầu    của quân đối thủ"
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
                            caro_Buttons_event.setState(playerTurn);
                            countButtonEnable++;
                            checkWin();
                            checkDraw();
                            playerTurn = playerTurn == true ? false : true;
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

        for (int i = 0; i < caro_Buttons.length; i++) {
            for (int j = 0; j < caro_Buttons.length; j++) {
                caro_Buttons[i][j].resetState();
            }
        }
    }

    public void checkWin() {
        if (getScore(getMatrixBoard(), playerTurn, !playerTurn) >= winScore) {
            newGame();
            if (playerTurn) {
                 JOptionPane.showMessageDialog(rootPane, "Người chơi X thắng nhường cho người chơi O đi trước nhé", "Ván mới", JOptionPane.INFORMATION_MESSAGE);
                 this.Jlabel_Score_X.setText(++xWinScore+"");
            }else{
                 JOptionPane.showMessageDialog(rootPane, "Người chơi O thắng nhường cho người chơi X đi trước nhé", "Ván mới", JOptionPane.INFORMATION_MESSAGE);
                  this.jlabel_score_O.setText(++oWinScore+"");
            }
            countButtonEnable++;
        }
    }

    private void checkDraw() {
        int i = 1;
        if (countButtonEnable == allButton) {
            JOptionPane.showMessageDialog(null, "Hòa Rồi Nhé");
            this.jLabelDrawScore.setText("Hòa: " + i++);
            newGame();
        }
    }

    // Tìm tất cả những ô trống có đánh XO liền kề
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
                        return winGuarantee / 2;
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
                        return winGuarantee / 3;
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
        jPanel2 = new javax.swing.JPanel();
        jlabel_score = new javax.swing.JLabel();
        Jlabel_Score_X = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jlabel_score_O = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jlabel_game_count = new javax.swing.JLabel();
        jLabelDrawScore = new javax.swing.JLabel();
        jPanel_Container_button = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jbutton_go_back = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Cờ Caro");
        setIconImage(getToolkit().createImage(".\\NienLuanCoSo\\src\\resource_caro\\IconFrame.png")
        );
        setResizable(false);

        jpanel_container_player.setBackground(new java.awt.Color(255, 255, 255));
        jpanel_container_player.setForeground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(153, 204, 255));
        jPanel2.setForeground(new java.awt.Color(153, 204, 255));

        jlabel_score.setFont(new java.awt.Font("Segoe UI", 3, 18)); // NOI18N
        jlabel_score.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlabel_score.setText("Tỉ Số : ");

        Jlabel_Score_X.setFont(new java.awt.Font("Source Serif Pro Black", 3, 48)); // NOI18N
        Jlabel_Score_X.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Jlabel_Score_X.setText("0");

        jLabel3.setFont(new java.awt.Font("Segoe UI Black", 1, 48)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText(":");

        jlabel_score_O.setFont(new java.awt.Font("Source Serif Pro Black", 3, 48)); // NOI18N
        jlabel_score_O.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlabel_score_O.setText("0");

        jLabel7.setText("X");

        jLabel11.setText("O");

        jlabel_game_count.setBackground(new java.awt.Color(255, 51, 102));
        jlabel_game_count.setFont(new java.awt.Font("Segoe UI", 3, 36)); // NOI18N
        jlabel_game_count.setForeground(new java.awt.Color(255, 51, 51));
        jlabel_game_count.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlabel_game_count.setText("Game");

        jLabelDrawScore.setText("Hòa: 0");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jlabel_game_count, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jlabel_score, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Jlabel_Score_X, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(36, 36, 36)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 33, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel7)
                        .addGap(81, 81, 81)
                        .addComponent(jLabelDrawScore, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jlabel_score_O, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(99, 99, 99))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(148, 148, 148))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jlabel_game_count, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jlabel_score, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Jlabel_Score_X, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jlabel_score_O))
                .addGap(8, 8, 8)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel11)
                    .addComponent(jLabelDrawScore))
                .addContainerGap())
        );

        javax.swing.GroupLayout jpanel_container_playerLayout = new javax.swing.GroupLayout(jpanel_container_player);
        jpanel_container_player.setLayout(jpanel_container_playerLayout);
        jpanel_container_playerLayout.setHorizontalGroup(
            jpanel_container_playerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jpanel_container_playerLayout.setVerticalGroup(
            jpanel_container_playerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpanel_container_playerLayout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel_Container_button.setBackground(new java.awt.Color(204, 204, 204));
        jPanel_Container_button.setBorder(javax.swing.BorderFactory.createCompoundBorder());
        jPanel_Container_button.setForeground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel_Container_buttonLayout = new javax.swing.GroupLayout(jPanel_Container_button);
        jPanel_Container_button.setLayout(jPanel_Container_buttonLayout);
        jPanel_Container_buttonLayout.setHorizontalGroup(
            jPanel_Container_buttonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 784, Short.MAX_VALUE)
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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jbutton_go_back, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jbutton_go_back, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("Source Code Pro", 3, 14)); // NOI18N
        jTextArea1.setForeground(new java.awt.Color(102, 102, 255));
        jTextArea1.setLineWrap(true);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 341, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jpanel_container_player, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel_Container_button, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel_Container_button, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(jpanel_container_player, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
    public javax.swing.JLabel Jlabel_Score_X;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabelDrawScore;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    public javax.swing.JPanel jPanel_Container_button;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JButton jbutton_go_back;
    public javax.swing.JLabel jlabel_game_count;
    private javax.swing.JLabel jlabel_score;
    public javax.swing.JLabel jlabel_score_O;
    private javax.swing.JPanel jpanel_container_player;
    // End of variables declaration//GEN-END:variables
}
