package gui;

import util.Constants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

class CreditsView extends AbstractView {
    private static CreditsView instance;

    private CreditsView() {
        super();
        setLayout(new BorderLayout());
        setBackground(Constants.MENU_BACKGROUND_COLOR);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(Constants.MENU_BACKGROUND_COLOR);

        JButton backButton = new JButton("Zurück");
        backButton.setBackground(Constants.BUTTON_COLOR);
        backButton.setFont(Constants.DEFAULT_FONT);
        //backButton.setPreferredSize(GUIConstants.defaultButtonSize);
        backButton.addActionListener(a -> MainFrame.getInstance().changeTo(MainMenuView.getInstance()));

        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.PAGE_END);

        //Überschrift
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(Constants.MENU_BACKGROUND_COLOR);
        headerPanel.setBorder(new EmptyBorder(30, 0, 0, 0));
        JLabel header = new JLabel("Credits");
        header.setFont(Constants.DEFAULT_FONT.deriveFont(110F));
        headerPanel.add(header);
        add(headerPanel, BorderLayout.PAGE_START);

        //Credits
        JPanel p1 = new JPanel();
        JPanel p2 = new JPanel();
        JPanel p3 = new JPanel();
        JPanel p4 = new JPanel();
        JPanel p5 = new JPanel();
        JPanel p6 = new JPanel();


        p1.setBackground(Constants.MENU_BACKGROUND_COLOR);
        p2.setBackground(Constants.MENU_BACKGROUND_COLOR);
        p3.setBackground(Constants.MENU_BACKGROUND_COLOR);
        p4.setBackground(Constants.MENU_BACKGROUND_COLOR);
        p5.setBackground(Constants.MENU_BACKGROUND_COLOR);
        p6.setBackground(Constants.MENU_BACKGROUND_COLOR);
        p6.setBackground(Constants.MENU_BACKGROUND_COLOR);


        JLabel l1 = new JLabel("Nikolas Kirschstein");
        JLabel l2 = new JLabel("Max Strohmeier");
        JLabel l3 = new JLabel("Benedikt Mödl");
        JLabel l4 = new JLabel("Alexander Hammerl");
        JLabel l5 = new JLabel("Fabian Weinelt");
        JLabel l6 = new JLabel("Tim Mostert");

        p1.add(l1);
        p2.add(l2);
        p3.add(l3);
        p4.add(l4);
        p5.add(l5);
        p6.add(l6);


        l1.setFont(Constants.DEFAULT_FONT.deriveFont(24F));
        l2.setFont(Constants.DEFAULT_FONT.deriveFont(24F));
        l3.setFont(Constants.DEFAULT_FONT.deriveFont(24F));
        l4.setFont(Constants.DEFAULT_FONT.deriveFont(24F));
        l5.setFont(Constants.DEFAULT_FONT.deriveFont(24F));
        l6.setFont(Constants.DEFAULT_FONT.deriveFont(24F));


        JPanel creditsPanel = new JPanel();
        creditsPanel.setBackground(Constants.MENU_BACKGROUND_COLOR);
        creditsPanel.setLayout(new BoxLayout(creditsPanel, BoxLayout.Y_AXIS));
        creditsPanel.add(Box.createRigidArea(new Dimension(100, 30)));
        creditsPanel.add(p1);
        creditsPanel.add(p2);
        creditsPanel.add(p3);
        creditsPanel.add(p4);
        creditsPanel.add(p5);
        creditsPanel.add(p6);
        creditsPanel.add(Box.createRigidArea(new Dimension(100, 300)));


        add(creditsPanel, BorderLayout.CENTER);


        Logger.log("Credits geladen", Logger.INFO);


    }

    public void update() {

    }

    static CreditsView getInstance() {
        if (instance == null)
            instance = new CreditsView();
        return instance;
    }


}
