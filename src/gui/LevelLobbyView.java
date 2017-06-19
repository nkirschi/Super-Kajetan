class LevelLobbyView extends AbstractView{
  private MainFrame mainframe;
  private static LevelLobbyView instance;
  
  private LevelLobbyView (MainFrame mainframe){
    super(mainframe);
  }
  
  public static LevelLobbyView instance(MainFrame mainframe){
    if(instance==null){
      instance = new LevelLobbyView(mainframe);
    }
    return instance;
  }
  
  public void update(){
  }
}
