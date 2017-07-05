package gui;

import util.Constants;
import util.Logger;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

class CreditsView extends AbstractView {
    private static CreditsView instance;
    private final boolean opaque = false; //Hiermit kann man alle Panels/TextFields/... gleichzeitig opaque setzen

    private CreditsView() {
        super();
        setLayout(new BorderLayout());
        setBackground(Constants.MENU_BACKGROUND_COLOR);
        //setOpaque(opaque);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(Constants.MENU_BACKGROUND_COLOR);
        buttonPanel.setOpaque(opaque);

        JButton backButton = new JButton("Zurück");
        backButton.setBackground(Constants.BUTTON_COLOR);
        backButton.setFont(Constants.DEFAULT_FONT);
        backButton.addActionListener(a -> MainFrame.getInstance().changeTo(MainMenuView.getInstance()));

        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.PAGE_END);

        //Panel, dass die Credits beinhaltet, geliebtes GridbagLayout für ... Genau! damit alles Mittig ist. *genervt an Cola schlürf*
        JPanel creditsPanel = new JPanel();
        creditsPanel.setLayout(new GridBagLayout());
        creditsPanel.setBackground(Constants.MENU_BACKGROUND_COLOR);
        //creditsPanel.setOpaque(opaque);
        add(creditsPanel, BorderLayout.CENTER);

        GridBagConstraints headerConstraints = new GridBagConstraints();
        headerConstraints.gridwidth = GridBagConstraints.REMAINDER;
        headerConstraints.fill = GridBagConstraints.HORIZONTAL;
        headerConstraints.insets = new Insets(0, 0, 10, 0);

        //Überschrift
        JLabel header = new JLabel("Credits");
        header.setFont(Constants.DEFAULT_FONT.deriveFont(110F));
        header.setForeground(Constants.FOREGROUND_COLOR);
        creditsPanel.add(header, headerConstraints);

        //headerConstraints werden abgeändert, da sie ab jetzt für die Titel (Abteilungen, Zuständigkeiten??) verwendet werden ...
        headerConstraints.insets = new Insets(15, 0, 5, 0);
        Font headerFont = Constants.DEFAULT_FONT.deriveFont(Font.BOLD, 24F);

        //neue Constraints für die Namen ...
        GridBagConstraints nameConstraints = new GridBagConstraints();
        nameConstraints.gridwidth = GridBagConstraints.REMAINDER;
        nameConstraints.fill = GridBagConstraints.HORIZONTAL;
        nameConstraints.insets = new Insets(0, 0, 0, 0);
        Font nameFont = Constants.DEFAULT_FONT.deriveFont(24F);

        //Jetzt kommt dass eigentliche -> Titel und Namen
        //Namen ...
        JLabel nico = new JLabel("Nikolas Kirschstein");
        nico.setFont(nameFont);
        nico.setForeground(Constants.FOREGROUND_COLOR);

        JLabel maxi = new JLabel("Max Strohmeier");
        maxi.setFont(nameFont);
        maxi.setForeground(Constants.FOREGROUND_COLOR);

        JLabel alex = new JLabel("Alexander Hammerl");
        alex.setFont(nameFont);
        alex.setForeground(Constants.FOREGROUND_COLOR);

        JLabel timm = new JLabel("Tim Mostert");
        timm.setFont(nameFont);
        timm.setForeground(Constants.FOREGROUND_COLOR);

        JLabel fabi = new JLabel("Fabian Weinelt");
        fabi.setFont(nameFont);
        fabi.setForeground(Constants.FOREGROUND_COLOR);

        JLabel bene = new JLabel("Benedikt Mödl");
        bene.setFont(nameFont);
        bene.setForeground(Constants.FOREGROUND_COLOR);

        //Titel ...
        JLabel t1 = new JLabel("GUI - Design und Programmierung");
        t1.setFont(headerFont);
        t1.setForeground(Constants.FOREGROUND_COLOR);

        JLabel t2 = new JLabel("Physik und Spiellogik");
        t2.setFont(headerFont);
        t2.setForeground(Constants.FOREGROUND_COLOR);

        JLabel t3 = new JLabel("Texturen und Model-Programmierung");
        t3.setFont(headerFont);
        t3.setForeground(Constants.FOREGROUND_COLOR);

        //Hinzufügen der ganzen Labels (auf Reihenfolge und Constraints achten!)
        creditsPanel.add(t1, headerConstraints);
        creditsPanel.add(alex, nameConstraints);
        creditsPanel.add(bene, nameConstraints);
        creditsPanel.add(t2, headerConstraints);
        creditsPanel.add(maxi, nameConstraints);
        creditsPanel.add(nico, nameConstraints);
        creditsPanel.add(t3, headerConstraints);
        creditsPanel.add(fabi, nameConstraints);
        creditsPanel.add(timm, nameConstraints);

        Logger.log("Credits geladen", Logger.INFO);
    }

    public void update() {

    }

    static CreditsView getInstance() {
        if (instance == null)
            instance = new CreditsView();
        return instance;
    }

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        //Praktisch der Teil der für das Hintergrundbild sorgt. Man muss natürlich auch die ganzen Panels auf nicht opaque setzen
        //-> setOpaque(false)
        try {
            g.drawImage(util.ImageUtil.getImage("images/menubackground_nobanner.png"), 0, 0, null);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
