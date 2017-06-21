package gui;

import java.awt.*;

public class GUIConstants {
    public static final Color menuBackgroundColor = new Color(131, 131, 131);
    public static final Color buttonColor = new Color(197, 197, 197);
    public static Dimension defaultButtonSize = new Dimension(300,100);
    //TODO Default Font, Textgröße, ...

    public static void setDefaultButtonSize(Dimension dimension){
        defaultButtonSize = dimension;
    }
}
