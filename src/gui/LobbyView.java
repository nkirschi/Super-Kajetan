package gui;

import model.*;
import util.Constants;
import util.Point;
import util.list.List;

import javax.swing.*;
import java.awt.*;

class LobbyView extends AbstractView {
    private static LobbyView instance;

    private LobbyView() {
        super();
        setLayout(new BorderLayout());
        setBackground(Constants.MENU_BACKGROUND_COLOR);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(Constants.MENU_BACKGROUND_COLOR);

        JButton backButton = new JButton("Zurück");
        backButton.setBackground(Constants.BUTTON_COLOR);
        backButton.setFont(Constants.DEFAULT_FONT);
        backButton.addActionListener(a -> MainFrame.getInstance().changeTo(MainMenuView.getInstance()));

        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.PAGE_END);

        //TODO Temporär, entfernen
        JButton temp = new JButton("temp");
        temp.addActionListener(actionEvent -> {
            LevelView levelView = new LevelView(createLevel1());
            MainFrame.getInstance().changeTo(levelView);
            levelView.setFocusable(true);
            levelView.requestFocusInWindow();
        });

        add(temp, BorderLayout.PAGE_START);
    }

    public void update() {

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
        Player player = new Player(new Point(getWidth() / 2, Constants.GROUND_LEVEL));
        List<Enemy> enemies = new List<>();
        List<Obstacle> obstacles = new List<>();
        List<Ground> grounds = new List<>();
        grounds.add(new Ground(600, 1200, 20));
        grounds.add(new Ground(1400, 400, 60));
        return new Level(player, enemies, obstacles, grounds, "images/backgrounds/background.png");
    }
}
