package gui;

import model.*;
import util.Constants;
import util.List;
import util.Logger;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

class LobbyView extends AbstractView {
    private static LobbyView instance;

    private LobbyView() {
        super();
        setLayout(new BorderLayout());
        setBackground(Constants.MENU_BACKGROUND_COLOR);

        //ButtonPanel, im Prinzip nur der Zurück-Knopf
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(Constants.MENU_BACKGROUND_COLOR);

        //Zurück-Button
        JButton backButton = new JButton("Zurück");
        backButton.setBackground(Constants.BUTTON_COLOR);
        backButton.setFont(Constants.DEFAULT_FONT);
        backButton.addActionListener(a -> MainFrame.getInstance().changeTo(MainMenuView.getInstance()));
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.PAGE_END);


        //Level-Buttons-Panel, hardcoded weil LvL auch hardcoded sind
        JPanel levelButtonPanel = new JPanel();
        levelButtonPanel.setLayout(new GridBagLayout());
        levelButtonPanel.setBackground(Constants.MENU_BACKGROUND_COLOR);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(5, 0, 5, 0);

        JScrollPane scrollPane = new JScrollPane(levelButtonPanel) {
            @Override
            public void setBorder(Border border) {
                // Nein, Böse! Wieder mal!
            }
        };
        scrollPane.setBackground(Constants.MENU_BACKGROUND_COLOR);
        add(scrollPane, BorderLayout.CENTER);


        Font buttonFont = Constants.DEFAULT_FONT.deriveFont(24F);
        //Buttons für einzelne Lvl
        //Level 1
        JButton lvl1 = new JButton("Level 1");
        lvl1.setBackground(Constants.BUTTON_COLOR);
        lvl1.setPreferredSize(Constants.DEFAULT_BUTTON_SIZE);
        lvl1.setFont(buttonFont);
        lvl1.addActionListener(a -> {
            LevelView levelView = new LevelView(createLevel1());
            MainFrame.getInstance().changeTo(levelView);
            levelView.setFocusable(true);
            levelView.requestFocusInWindow();
            new Thread(levelView).start();
        });
        levelButtonPanel.add(lvl1, constraints);

        //Level 2
        JButton lvl2 = new JButton("Level 2");
        lvl2.setBackground(Constants.BUTTON_COLOR);
        lvl2.setPreferredSize(Constants.DEFAULT_BUTTON_SIZE);
        lvl2.setFont(buttonFont);
        lvl2.addActionListener(a -> {
            LevelView levelView = new LevelView(createLevel2());
            MainFrame.getInstance().changeTo(levelView);
            levelView.setFocusable(true);
            levelView.requestFocusInWindow();
        });
        levelButtonPanel.add(lvl2, constraints);
    }

    public void refresh() {

    }

    public static LobbyView getInstance() {
        if (instance == null)
            instance = new LobbyView();
        return instance;
    }

    /**
     * Hässliche Hardcoded-Level-Methode
     */
    private Level createLevel1() {
        List<Enemy> enemies = new List<>();
        List<Obstacle> obstacles = new List<>();
        obstacles.add(new Crate(1750, 640));
        List<Ground> grounds = new List<>();
        grounds.add(new Ground(600, 1200, 20));
        grounds.add(new Ground(1400, 400, 60));
        grounds.add(new Ground(1750, 300, 100));
        grounds.add(new Ground(2000, 200, 80));
        grounds.add(new Ground(2200, 200, 60));
        grounds.add(new Ground(2400, 200, 40));
        grounds.add(new Ground(2600, 200, 20));
        grounds.add(new Ground(2800, 200, 0));
        Logger.log("Level erstellt", Logger.INFO);
        return new Level(enemies, obstacles, grounds, "images/backgrounds/background.png");
    }

    private Level createLevel2() {
        List<Enemy> enemies = new List<>();
        List<Obstacle> obstacles = new List<>();
        List<Ground> grounds = new List<>();
        grounds.add(new Ground(600, 1200, 20));
        grounds.add(new Ground(1400, 400, 60));
        return new Level(enemies, obstacles, grounds, "images/backgrounds/background2.jpg");
    }
}
