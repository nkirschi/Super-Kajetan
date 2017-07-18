package gui;

import logic.Behavior;
import model.*;
import util.Constants;
import util.List;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

class LobbyView extends AbstractView {
    private static LobbyView instance;

    private LobbyView() {
        super();
        setLayout(new BorderLayout());
        setBackground(Constants.MENU_BACKGROUND_COLOR);

        //ButtonPanel, im Prinzip nur der Zurück-Knopf
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setOpaque(false);


        //Level-Buttons-Panel, hardcoded weil LvL auch hardcoded sind
        JPanel levelButtonPanel = new JPanel();
        levelButtonPanel.setLayout(new GridBagLayout());
        levelButtonPanel.setOpaque(false);

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
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        add(scrollPane, BorderLayout.CENTER);


        Font buttonFont = Constants.DEFAULT_FONT.deriveFont(24F);
        //Buttons für einzelne Lvl
        //Level 1
        WoodenButton lvl1 = new WoodenButton("Level 1");
        lvl1.setPreferredSize(Constants.DEFAULT_BUTTON_SIZE);
        lvl1.setFont(buttonFont);
        lvl1.addActionListener(a -> loadLevel(createLevel1()));
        levelButtonPanel.add(lvl1, constraints);

        //Level 2
        WoodenButton lvl2 = new WoodenButton("Level 2");
        lvl2.setPreferredSize(Constants.DEFAULT_BUTTON_SIZE);
        lvl2.setFont(buttonFont);
        lvl2.addActionListener(a -> loadLevel(createLevel2()));
        levelButtonPanel.add(lvl2, constraints);
        
        //Level 3
        WoodenButton lvl3 = new WoodenButton("Level 3");
        lvl3.setPreferredSize(Constants.DEFAULT_BUTTON_SIZE);
        lvl3.setFont(buttonFont);
        lvl3.addActionListener(a -> loadLevel(createLevel3()));
        levelButtonPanel.add(lvl3, constraints);

        //Level 4
        WoodenButton lvl4 = new WoodenButton("ITS JUST AN EXPERIMENT!");
        lvl4.setPreferredSize(Constants.DEFAULT_BUTTON_SIZE);
        lvl4.setFont(buttonFont);
        lvl4.addActionListener(a -> loadLevel(createLevel4()));
        levelButtonPanel.add(lvl4, constraints);

        constraints.insets.set(50, constraints.insets.left, constraints.insets.right, constraints.insets.bottom);
        // Zurück
        WoodenButton backButton = new WoodenButton("Zurück");
        backButton.setPreferredSize(Constants.DEFAULT_BUTTON_SIZE);
        backButton.setFont(buttonFont);
        backButton.addActionListener(a -> MainFrame.getInstance().changeTo(MainMenuView.getInstance()));
        levelButtonPanel.add(backButton, constraints);
    }

    private void loadLevel(Level level) {
        LevelView levelView = new LevelView(level);
        MainFrame.getInstance().changeTo(levelView);
        levelView.setFocusable(true);
        levelView.requestFocusInWindow();
        new Thread(levelView).start();
    }

    public void refresh() {

    }

    public static LobbyView getInstance() {
        if (instance == null)
            instance = new LobbyView();
        return instance;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        //Praktisch der Teil der für das Hintergrundbild sorgt. Man muss natürlich auch die ganzen Panels auf nicht opaque setzen
        //-> setOpaque(false)
        try {
            g.drawImage(util.ImageUtil.getImage(Constants.MENU_BACKGROUND_2), 0, 0, getWidth(), getHeight(), null);
            g.setColor(new Color(0, 0, 0, 0.7f));
            g.fillRect(0, 0, getWidth(), getHeight());
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * Hardcoded-Level-Methode
     */
    private Level createLevel1() {
        List<Enemy> enemies = new List<>();
        enemies.add(new Knight(2200, 680, Behavior.GUARD, Direction.RIGHT));
        enemies.add(new Knight(3000, 707, Behavior.PATROL, Direction.RIGHT));
        enemies.add(new Skeleton(4100, 590, Behavior.ATTACK, Direction.RIGHT));
        List<Obstacle> obstacles = new List<>();
        obstacles.add(new Barrel(150, 720));
        obstacles.add(new Crate(1750, 640));
        obstacles.add(new Crate(1000, 300));
        obstacles.add(new Barrel(2600, 720));
        obstacles.add(new Crate(4900, 540 + 128));
        obstacles.add(new Crate(5150, 540 + 148));
        obstacles.add(new Crate(5400, 540 + 168));
        obstacles.add(new Crate(5650, 540 + 188));
        List<Ground> grounds = new List<>();
        grounds.add(new Ground(600, 1200, 20, Ground.Type.SOIL));
        grounds.add(new Ground(1400, 400, 60, Ground.Type.SOIL));
        grounds.add(new Ground(1750, 300, 100, Ground.Type.GRASS));
        grounds.add(new Ground(2000, 200, 80, Ground.Type.ROCK));
        grounds.add(new Ground(2200, 200, 60, Ground.Type.GRASS));
        grounds.add(new Ground(2400, 200, 40, Ground.Type.SOIL));
        grounds.add(new Ground(2600, 200, 20, Ground.Type.ROCK));
        grounds.add(new Ground(3100, 800, 33, Ground.Type.ROCK));
        grounds.add(new Ground(3700, 400, 100, Ground.Type.GRASS));
        grounds.add(new Ground(4200, 600, 150, Ground.Type.SOIL));
        grounds.add(new Ground(4600, 200, 200, Ground.Type.ROCK));
        return new Level(enemies, obstacles, grounds, "images/backgrounds/background.png",
                200);
    }

    private Level createLevel2() {
        int rnd = ThreadLocalRandom.current().nextInt(1, 6);
        List<Enemy> enemies = new List<>();
        for (int i = rnd; i>0; i--) {
            if(rnd==1) {
                enemies.add(new Knight(700, 700, Behavior.ATTACK, Direction.LEFT));
            }
            if(rnd==2) {
                enemies.add(new Knight(800, 700, Behavior.ATTACK, Direction.LEFT));
            }
            if(rnd==3) {
                enemies.add(new Knight(900, 700, Behavior.ATTACK, Direction.LEFT));
            }
            if(rnd==4) {
                enemies.add(new Knight(1000, 700, Behavior.ATTACK, Direction.LEFT));
            }
            if(rnd==5) {
                enemies.add(new Knight(1100, 700, Behavior.ATTACK, Direction.LEFT));
            }
        }
        List<Obstacle> obstacles = new List<>();
        List<Ground> grounds = new List<>();
        grounds.add(new Ground(1000, 2000, 20, Ground.Type.SOIL));
        grounds.add(new Ground(1400, 400, 60, Ground.Type.SOIL));
        return new Level(enemies, obstacles, grounds, "images/backgrounds/background3.png",
                10);
    }
    
    private Level createLevel3() {
        List<Enemy> enemies = new List<>();
        /**
        enemies.add(new Knight(1700, 660, Behavior.ATTACK, Direction.LEFT));
        enemies.add(new Knight(1500, 660, Behavior.ATTACK, Direction.LEFT));
        enemies.add(new Skeleton(2800, 620, Behavior.GUARD, Direction.LEFT));
        enemies.add(new Skeleton(4500, 440, Behavior.GUARD, Direction.LEFT));
        enemies.add(new Skeleton(4700, 312, Behavior.GUARD, Direction.LEFT));
        enemies.add(new Knight(6100, 660, Behavior.GUARD, Direction.LEFT));
        */
        List<Obstacle> obstacles = new List<>();
        obstacles.add(new Crate(130, 700));
        obstacles.add(new Barrel(130, 572));
        obstacles.add(new Crate(3100, 620));
        obstacles.add(new Barrel(3450, 700));
        obstacles.add(new Crate(3700, 470));
        obstacles.add(new Barrel(4900, 700));
        obstacles.add(new Crate(4700, 440));
        obstacles.add(new Crate(6250, 660));
        obstacles.add(new Crate(6122, 660));
        obstacles.add(new Crate(6600, 470));
        obstacles.add(new Crate(6950, 590));
        List<Ground> grounds = new List<>();
        grounds.add(new Ground(600, 1400, 40, Ground.Type.GRASS));
        grounds.add(new Ground(1600, 1000, 80, Ground.Type.GRASS));
        grounds.add(new Ground(2400, 600, 100, Ground.Type.SOIL));
        grounds.add(new Ground(2800, 900, 120, Ground.Type.ROCK));
        grounds.add(new Ground(3250, 100, 100, Ground.Type.SOIL));
        grounds.add(new Ground(3350, 50, 80, Ground.Type.SOIL));
        grounds.add(new Ground(3450, 50, 40, Ground.Type.SOIL));
        grounds.add(new Ground(4500, 700, 300, Ground.Type.ROCK));
        grounds.add(new Ground(4150, 150, 260, Ground.Type.SOIL));
        grounds.add(new Ground(4050, 50, 100, Ground.Type.SOIL));
        grounds.add(new Ground(4900, 400, 40, Ground.Type.ROCK));
        grounds.add(new Ground(5200, 200, 60, Ground.Type.SOIL));
        grounds.add(new Ground(5300, 100, 20, Ground.Type.SOIL));
        grounds.add(new Ground(6000, 800, 80, Ground.Type.GRASS));
        grounds.add(new Ground(5600, 100, 40, Ground.Type.SOIL));
        grounds.add(new Ground(6400, 100, 40, Ground.Type.SOIL));
        grounds.add(new Ground(6950, 150, 150, Ground.Type.ROCK));
        grounds.add(new Ground(6875, 50, 50, Ground.Type.ROCK));
        grounds.add(new Ground(7125, 200, 90, Ground.Type.ROCK));
        grounds.add(new Ground(7300, 200, 70, Ground.Type.SOIL));
        grounds.add(new Ground(7700, 600, 50, Ground.Type.GRASS));
        grounds.add(new Ground(8100, 200, 40, Ground.Type.GRASS));
        grounds.add(new Ground(10000, 3400, 10, Ground.Type.ROCK)); // Ebene auf der das Level ergänzt werden kann
        return new Level(enemies, obstacles, grounds, "images/backgrounds/background_extended.png",
                200);
    }

    private Level createLevel4() {
        List<Enemy> enemies = new List<>();
        enemies.add(new Helper(70, 660, Behavior.ATTACK, Direction.LEFT));
        enemies.add(new Helper(280, 620, Behavior.ATTACK, Direction.LEFT));
        List<Obstacle> obstacles = new List<>();
        List<Ground> grounds = new List<>();
        grounds.add(new Ground(600, 1400, 40, Ground.Type.GRASS));
        grounds.add(new Ground(1600, 1000, 700, Ground.Type.GRASS));
        grounds.add(new Ground(2400, 600, 100, Ground.Type.SOIL));
        return new Level(enemies, obstacles, grounds, "images/backgrounds/background_extended.png",
                200);
    }
}
