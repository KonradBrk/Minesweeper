import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class menu {
    private JFrame menu;
    private JButton sizeSmall;
    private JButton sizeMedium;
    private JButton sizeLarge;
    private JLabel title;


    public menu() {
        menu = new JFrame();
        sizeSmall = new JButton();
        sizeMedium = new JButton();
        sizeLarge = new JButton();
        title = new JLabel("Select your level!",SwingConstants.CENTER);
        printMenu();
    }


    public void printMenu(){
        menu.setBounds(500,200,400,400);
        menu.setLayout(null);
        title.setBounds(145,10,100,45);
        buttonAdjustmentSmall(sizeSmall);
        buttonAdjustmentMedium(sizeMedium);
        buttonAdjustmentLarge(sizeLarge);
        menu.add(title);
        menu.add(sizeSmall);
        menu.add(sizeMedium);
        menu.add(sizeLarge);
        menu.setVisible(true);
        menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    public void buttonAdjustmentSmall(JButton sizeSmall){
        sizeSmall.setBounds(120,70,150,45);
        sizeSmall.setText("Beginner");
        sizeSmall.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menu.setVisible(false);
                new Game(Size.SMALL);
            }
        });
    }

    public void buttonAdjustmentMedium(JButton sizeMedium){
        sizeMedium.setBounds(120,140,150,45);
        sizeMedium.setText("Advanced");
        sizeMedium.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menu.setVisible(false);
                new Game(Size.MEDIUM);
            }
        });
    }

    public void buttonAdjustmentLarge(JButton sizeLarge){
        sizeLarge.setBounds(120,210,150,45);
        sizeLarge.setText("Expert");
        sizeLarge.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menu.setVisible(false);
                new Game(Size.LARGE);
            }
        });
    }

}
