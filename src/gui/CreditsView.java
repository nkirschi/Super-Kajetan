package gui;

import util.Constants;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

class CreditsView extends AbstractView {
    private static CreditsView instance;

    private CreditsView() {
        super();
        setLayout(new BorderLayout());
        setBackground(Constants.MENU_BACKGROUND_COLOR);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setOpaque(false);

        WoodenButton backButton = new WoodenButton("Zurück");
        backButton.setFont(Constants.DEFAULT_FONT);
        backButton.setPreferredSize(Constants.DEFAULT_BUTTON_SIZE_2);
        backButton.addActionListener(a -> MainFrame.getInstance().changeTo(MainMenuView.getInstance()));

        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.PAGE_END);

        //Panel, dass die Credits beinhaltet, geliebtes GridbagLayout für ... Genau! damit alles Mittig ist. *genervt an Cola schlürf*
        JPanel creditsPanel = new JPanel();
        creditsPanel.setLayout(new GridBagLayout());
        creditsPanel.setOpaque(false);
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

        //neue Constraints für die Namen ...
        GridBagConstraints nameConstraints = new GridBagConstraints();
        nameConstraints.gridwidth = GridBagConstraints.REMAINDER;
        nameConstraints.fill = GridBagConstraints.CENTER;
        nameConstraints.insets = new Insets(5, 0, 5, 0);
        Font nameFont = Constants.DEFAULT_FONT.deriveFont(24F);

        //Jetzt kommt dass eigentliche -> Namen
        //Namen ...
        JLabel alex = new JLabel("Alexander Hammerl");
        alex.setFont(nameFont);
        alex.setForeground(Constants.FOREGROUND_COLOR);

        JLabel bene = new JLabel("Benedikt Mödl");
        bene.setFont(nameFont);
        bene.setForeground(Constants.FOREGROUND_COLOR);

        JLabel maxi = new JLabel("Max Strohmeier");
        maxi.setFont(nameFont);
        maxi.setForeground(Constants.FOREGROUND_COLOR);

        JLabel niko = new JLabel("Nikolas Kirschstein");
        niko.setFont(nameFont);
        niko.setForeground(Constants.FOREGROUND_COLOR);

        JLabel fabi = new JLabel("Fabian Weinelt");
        fabi.setFont(nameFont);
        fabi.setForeground(Constants.FOREGROUND_COLOR);

        JLabel timm = new JLabel("Tim Mostert");
        timm.setFont(nameFont);
        timm.setForeground(Constants.FOREGROUND_COLOR);

        //Hinzufügen der ganzen Labels (auf Reihenfolge und Constraints achten!)
        creditsPanel.add(alex, nameConstraints);
        creditsPanel.add(bene, nameConstraints);
        creditsPanel.add(maxi, nameConstraints);
        creditsPanel.add(niko, nameConstraints);
        creditsPanel.add(fabi, nameConstraints);
        creditsPanel.add(timm, nameConstraints);
    }

    public void refresh() {

    }

    static CreditsView getInstance() {
        if (instance == null)
            instance = new CreditsView();
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
}
