public class LevelView extends AbstractView {
    private MainFrame mainFrame;
    private boolean running;
    
    public LevelView(Level level) {
        load(level);
    }
    
    public void update() {
        
    }
    
    public void gameLoop() {
        while (running) {
            gameUpdate();
            gameRender();
        }
    }
    
    public void gameUpdate() {
    
    }
    
    public void gameRender() {
    
    }
    
    public void load(Level level) {
        // bla
    }
}
