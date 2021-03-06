import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

public class Game extends JFrame {
    private Button[][] map;
    private JFrame frame;
    private JPanel game;
    private boolean started;
    private int bombCounter;
    private int clickCounter;
    private int clickToWin;

    public Game(Size size) {
        this.frame = new JFrame("Minesweeper");
        frameSizeAdjustment(size);
        menuPanel();
        printingUi();
        frameOtherAdjustments();
        this.started = false;
    }

    public void menuPanel(){
        JButton newGame = new JButton("New Game!");
        newGame.setFocusable(false);
        newGame.setBounds(500,15,100,50);
        newGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                new menu();
            }
        });
        frame.add(newGame);


        JLabel author = new JLabel("Made by Konrad Brylak");

        author.setBounds(1245,820,200,50);
        author.setVisible(true);
        frame.add(author);

    }

    private void frameSizeAdjustment(Size size) {
        if (size == Size.SMALL) {
            this.map = new Button[8][8];
            this.game = new JPanel(new GridLayout(8, 8));
            this.game.setBounds(0,80,1385,755);
            frame.add(game);
            frame.setSize(1400, 900);
        }
        if (size == Size.MEDIUM) {
            this.map = new Button[16][16];
            this.game = new JPanel(new GridLayout(16, 16));
            this.game.setBounds(0,80,1385,755);
            frame.add(game);
            frame.setSize(1400, 900);
        }
        if (size == Size.LARGE) {
            this.map = new Button[32][32];
            this.game = new JPanel(new GridLayout(32, 32));
            this.game.setBounds(0,80,1385,755);
            frame.add(game);
            frame.setSize(1400, 900);
        }
    }

    private void frameOtherAdjustments() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(game);
        frame.setResizable(false);
        frame.setLayout(null);
        frame.setVisible(true);
    }

    private void generatingAndCountingBombs() {
        generatingFirstArea();
        generatingBombs();
        countingBombs();
        this.clickToWin = clickCounter - bombCounter;
    }

    private void generatingBombs() {
        Random r = new Random();
        for (int i = 0; i < map.length*2; i++) {
            int rows = r.nextInt(map.length);
            int columns = r.nextInt(map.length);
            if (map[rows][columns].getValue() != -1 && !map[rows][columns].isFirst()) {
                map[rows][columns].setValue(-1);
                bombCounter++;
            } else {
                i--;
            }
        }
    }

    private void generatingFirstArea() {
        var first = findFirst();

        int startRow = 0;
        int startColumn = 0;
        int endRow = 0;
        int endColumn = 0;

        assert first != null;
        if (canGoUp(first)) startRow = first.getRow() - 1;
        else startRow = first.getRow();

        if (canGoDown(first)) endRow = first.getRow() + 1;
        else endRow = first.getRow();

        if (canGoLeft(first)) startColumn = first.getColumn() - 1;
        else startColumn = first.getColumn();

        if (canGoRight(first)) endColumn = first.getColumn() + 1;
        else endColumn = first.getColumn();


        for (int rows = startRow; rows <= endRow; rows++) {
            for (int columns = startColumn; columns <= endColumn; columns++) {
                map[rows][columns].setFirst(true);
            }
        }
    }

    private void checkingCellsWithValuesAround(Button button) {

        int startRow = 0;
        int startColumn = 0;
        int endRow = 0;
        int endColumn = 0;

        if (canGoUp(button)) startRow = button.getRow() - 1;
        else startRow = button.getRow();

        if (canGoDown(button)) endRow = button.getRow() + 1;
        else endRow = button.getRow();

        if (canGoLeft(button)) startColumn = button.getColumn() - 1;
        else startColumn = button.getColumn();

        if (canGoRight(button)) endColumn = button.getColumn() + 1;
        else endColumn = button.getColumn();


        for (int rows = startRow; rows <= endRow; rows++) {
            for (int columns = startColumn; columns <= endColumn; columns++) {
                if (map[rows][columns].getValue() > 0 && !isSelectedCell(map[rows][columns])) {
                    selectWithValue(map[rows][columns]);
                }
            }
        }
    }

    private Button findFirst(){
        for (int rows = 0; rows < map.length; rows++) {
            for (int columns = 0; columns < map.length; columns++) {
                if (map[rows][columns].isFirst())
                    return map[rows][columns];
            }
        }
        return null;
    }

    private void countingBombs() {
        for (int rows = 0; rows < map.length; rows++) {
            for (int columns = 0; columns < map.length; columns++) {
                if (map[rows][columns].getValue() != -1)
                    map[rows][columns].setValue(countingBombs(rows, columns));
            }
        }
    }

    private int countingBombs(int x, int y) {
        int counter = 0;
        for (int rows = x - 1; rows <= x + 1; rows++) {
            if (rows >= 0 && rows < map.length)
                for (int columns = y - 1; columns <= y + 1; columns++) {
                    if (columns >= 0 && columns < map.length)
                        if (map[rows][columns].getValue() == -1 || map[rows][columns].getValue() == -2)
                            counter++;
                }
        }
        return counter;
    }

    private void printingUi() {
        for (int rows = 0; rows < map.length; rows++) {
            for (int columns = 0; columns < map.length; columns++) {
                clickCounter++;
                var newButton = new Button(rows, columns);
                newButton.addActionListener(e -> {
                    if (!started && !newButton.isFlaged()){
                        newButton.setFirst(true);
                        generatingAndCountingBombs();
                        showField(newButton);
                        started = true;
                    } else {
                        if(!newButton.isFlaged()) {
                            newButton.setSelected(true);
                            showField(newButton);
                        }
                    }
                });
                newButton.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (e.getButton() == 3 && !isSelectedCell(newButton) && !newButton.isFlaged()){
                            newButton.setFlaged(true);
                            newButton.setIcon(new ImageIcon("flag.png"));
                        } else if (e.getButton() == 3 && newButton.isFlaged()){
                            newButton.setFlaged(false);
                            newButton.setIcon(null);
                            newButton.setSelected(false);
                        }
                    }
                });
                game.add(newButton);
                map[rows][columns] = newButton;
            }
        }
    }

    private void showField(Button button) {
        if (button.getValue() == -1) {
            button.setIcon(new ImageIcon("bomb.png"));
            button.setSelected(true);
            endGame();
            JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Game Over");
        } else if (isSelectedCell(button) & button.getValue() == 0) {
            showAllEmptyFields(button);
        } else if (isSelectedCell(button)) {
            selectWithValue(button);
            if (clickToWin == 0) {
                endGame();
                JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "You Win");
            }
        }
    }

    private void endGame() {
        for (Button[] value : map) {
            for (int j = 0; j < map.length; j++) {
                var bomb = value[j];
                if (bomb.getValue() == -1) {
                    if (!bomb.isFlaged()) {
                        bomb.setIcon(new ImageIcon("bomb.png"));
                    } else {
                        bomb.setIcon(new ImageIcon("bombSpotted.png"));
                    }
                }
            }
        }
    }

    private void showAllEmptyFields(Button button) {
        selectEmpty(button);
        checkingCellsWithValuesAround(button);
        if (canGoRight(button)) {
            var nextButton = map[button.getRow()][button.getColumn() + 1];
            if (!isSelectedCell(nextButton) && nextButton.getValue() == 0)
                showAllEmptyFields(nextButton);
        }
        if (canGoLeft(button)) {
            var nextButton = map[button.getRow()][button.getColumn() - 1];
            if (!isSelectedCell(nextButton) && nextButton.getValue() == 0)
                showAllEmptyFields(nextButton);
        }
        if (canGoUp(button)) {
            var nextButton = map[button.getRow() - 1][button.getColumn()];
            if (!isSelectedCell(nextButton) && nextButton.getValue() == 0)
                showAllEmptyFields(nextButton);
        }
        if (canGoDown(button)) {
            var nextButton = map[button.getRow() + 1][button.getColumn()];
            if (!isSelectedCell(nextButton) && nextButton.getValue() == 0)
                showAllEmptyFields(nextButton);
        }
    }

    private void selectWithValue(Button button) {
        if (!button.isRevealed() && !button.isFlaged()) {
            clickToWin--;
            button.setSelected(true);
            button.setText(button.getValue() + "");
            button.setRevealed(true);
        }
    }

    private void selectEmpty(Button button) {
        if (!button.isRevealed() && !button.isFlaged()) {
            clickToWin--;
            button.setSelected(true);
            button.setText("");
            button.setRevealed(true);
        }
    }

    private Boolean canGoUp(Button button) {
        return button.getRow() - 1 >= 0;
    }

    private Boolean canGoDown(Button button) {
        return button.getRow() + 1 < map.length;
    }

    private Boolean canGoRight(Button button) {
        return button.getColumn() + 1 < map.length;
    }

    private Boolean canGoLeft(Button button) {
        return button.getColumn() - 1 >= 0;
    }

    private Boolean isSelectedCell(Button button) {
        return button.isSelected();
    }


}




