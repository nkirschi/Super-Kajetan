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
        enemies.add(new Knight(new Point(1500, Constants.GROUND_LEVEL)));
        List<Obstacle> obstacles = new List<>();
        privale int levellength;//provisorisch
        levellength = 1000000;//provisorisch
        obstacles.add(new ground(Constants.GROUND_LEVEL, levellength)));
        obstacles.add(new Crate(new Point(3000, Constants.GROUND_LEVEL)));
        return new Level(player, enemies, obstacles, "images/backgrounds/background.png");
    }
}
