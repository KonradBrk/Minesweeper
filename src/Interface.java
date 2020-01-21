import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class Interface extends JFrame{
    private Size size;
    private Button buttons;
    private Button[][] map;
    private JFrame frame;
    private JPanel layout;
    private int bombCounter;
    private int clickCounter;
    private int clickToWin;

    public Interface(Size size) {
        this.frame = new JFrame("Minesweeper");
        frameSizeAdjustment(size);
        printingUi();
        frameOtherAdjustments();
        generatingBombs();
        countingBombs();
        this.clickToWin = clickCounter - bombCounter;
}

    private void frameSizeAdjustment(Size size) {
        if (size == Size.SMALL) {
            this.map = new Button[8][8];
            this.layout = new JPanel(new GridLayout(8,8));
            frame.setSize(200,200);
        }
        if (size == Size.MEDIUM) {
            this.map = new Button[16][16];
            this.layout = new JPanel(new GridLayout(16,16));
            frame.setSize(400,400);
        }
        if (size == Size.LARGE) {
            this.map = new Button[32][32];
            this.layout = new JPanel(new GridLayout(32,32));
            frame.setSize(800,800);
        }
    }

    private void frameOtherAdjustments(){
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(layout);
        frame.setVisible(true);
    }

    private void generatingBombs(){
        Random r = new Random();
        for (int i = 0; i < map.length; i++) {
            int rows = r.nextInt(map.length);
            int columns = r.nextInt(map.length);
            if(map[rows][columns].getValue() != -1) {
                map[rows][columns].setValue(-1);
                bombCounter++;
            } else {
                i--;
            }
        }
    }

    private void countingBombs(){
        for (int rows = 0; rows < map.length; rows++) {
            for (int columns = 0; columns < map.length; columns++) {
                if (map[rows][columns].getValue() != -1)
                    map[rows][columns].setValue(countingBombs(rows,columns));
            }
        }
    }

    private int countingBombs(int x, int y){
        int counter = 0;
        for (int rows = x-1; rows <= x+1; rows++) {
            if (rows >= 0 && rows < map.length)
            for (int columns = y-1; columns <=y+1; columns++) {
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
                var newButton = new Button(rows,columns);
                ActionListener action = new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        newButton.setSelected(true);
                        showField(newButton);
                    }
                };
                newButton.addActionListener(action);
                layout.add(newButton);
                map[rows][columns] = newButton;
            }
        }
    }

    private void showField(Button button){
        if (button.getValue()==-1){
            button.setText("B");
            button.setSelected(true);
            JOptionPane.showMessageDialog(JOptionPane.getRootFrame(),"Game Over");
        } else if (isSelectedCell(button) & button.getValue()==0){
            showAllEmptyFields(button);
        } else if (isSelectedCell(button)) {
            selectWithValue(button);
            if (clickToWin == 0) JOptionPane.showMessageDialog(JOptionPane.getRootFrame(),"You Win");
        }
    }

    public void cheat(){
        for (Button[] value : map) {
            for (int j = 0; j < map.length; j++) {
                var bomb = value[j];
                if (bomb.getValue() == -1) {
                    bomb.setText("B");
                }
            }
        }
    }

    private void showAllEmptyFields(Button button) {
            selectEmpty(button);
            if (canGoRight(button)) {
                var nextButton = map[button.getRow()][button.getColumn() + 1];
                if (!isSelectedCell(nextButton) && nextButton.getValue() == 0)
                    showAllEmptyFields(nextButton);
                else if (!isSelectedCell(nextButton) && nextButton.getValue() > 0) {
                    selectWithValue(nextButton);
                    if (canGoUp(nextButton)) {
                        var nextButtonUp = map[nextButton.getRow() - 1][nextButton.getColumn()];
                        if (nextButtonUp.getValue() > 0 && !isSelectedCell(nextButtonUp))
                            selectWithValue(nextButtonUp);
                    }
                    if (canGoDown(nextButton)) {
                        var nextButtonDown = map[nextButton.getRow() + 1][nextButton.getColumn()];
                        if (nextButtonDown.getValue() > 0 && !isSelectedCell(nextButtonDown))
                            selectWithValue(nextButtonDown);
                    }
                }
            }
            if (canGoLeft(button)) {
                var nextButton = map[button.getRow()][button.getColumn() - 1];
                if (!isSelectedCell(nextButton) && nextButton.getValue() == 0)
                    showAllEmptyFields(nextButton);
                else if (!isSelectedCell(nextButton) && nextButton.getValue() > 0) {
                    selectWithValue(nextButton);
                    if (canGoUp(nextButton)) {
                        var nextButtonUp = map[nextButton.getRow() - 1][nextButton.getColumn()];
                        if (nextButtonUp.getValue() > 0 && !isSelectedCell(nextButtonUp))
                            selectWithValue(nextButtonUp);
                    }
                    if (canGoDown(nextButton)) {
                        var nextButtonDown = map[nextButton.getRow() + 1][nextButton.getColumn()];
                        if (nextButtonDown.getValue() > 0 && !isSelectedCell(nextButtonDown))
                            selectWithValue(nextButtonDown);
                    }
                }
            }
            if (canGoUp(button)) {
                var nextButton = map[button.getRow() - 1][button.getColumn()];
                if (!isSelectedCell(nextButton) && nextButton.getValue() == 0)
                    showAllEmptyFields(nextButton);
                else if (!isSelectedCell(nextButton) && nextButton.getValue() > 0) {
                    selectWithValue(nextButton);
                    if(canGoLeft(nextButton)) {
                        var nextButtonLeft = map[nextButton.getRow()][nextButton.getColumn() - 1];
                        if(nextButtonLeft.getValue() > 0 && !isSelectedCell(nextButtonLeft))
                            selectWithValue(nextButtonLeft);
                    }
                    if(canGoRight(nextButton)) {
                        var nextButtonRight = map[nextButton.getRow()][nextButton.getColumn() + 1];
                        if (nextButtonRight.getValue() > 0 && !isSelectedCell(nextButtonRight))
                            selectWithValue(nextButtonRight);
                    }
                }
            }
            if (canGoDown(button)) {
                var nextButton = map[button.getRow() + 1][button.getColumn()];
                if (!isSelectedCell(nextButton) && nextButton.getValue() == 0)
                    showAllEmptyFields(nextButton);
                else if (!isSelectedCell(nextButton) && nextButton.getValue() > 0) {
                    selectWithValue(nextButton);
                    if(canGoLeft(nextButton)) {
                        var nextButtonLeft = map[nextButton.getRow()][nextButton.getColumn() - 1];
                        if(nextButtonLeft.getValue() > 0 && !isSelectedCell(nextButtonLeft))
                            selectWithValue(nextButtonLeft);
                    }
                    if(canGoRight(nextButton)) {
                        var nextButtonRight = map[nextButton.getRow()][nextButton.getColumn() + 1];
                        if (nextButtonRight.getValue() > 0 && !isSelectedCell(nextButtonRight))
                            selectWithValue(nextButtonRight);
                    }
                }
            }
    }

    private void selectWithValue(Button nextButton) {
        clickToWin--;
        nextButton.setSelected(true);
        nextButton.setText(nextButton.getValue() + "");
    }

    private void selectEmpty(Button button) {
        clickToWin--;
        button.setSelected(true);
        button.setText("");
    }

    private Boolean canGoUp(Button button){
        return button.getRow() - 1 >= 0;
    }
    private Boolean canGoDown(Button button){
        return button.getRow() + 1 < map.length;
    }
    private Boolean canGoRight(Button button){
        return button.getColumn() + 1 < map.length;
    }
    private Boolean canGoLeft(Button button){
        return button.getColumn() - 1 >= 0;
    }

    private Boolean isSelectedCell(Button button){
        return button.isSelected();
    }



}




