class HighScoreView extends AbstractView{
  private MainFrame mainframe;
  private static HighScoreView instance;
  
  private HighScoreView (MainFrame mainframe){
    super(mainframe);
  }
  
  public static HighScoreView instance(MainFrame mainframe){
    if(instance==null){
      instance = new HighScoreView(mainframe);
    }
    return instance;
  }
  
  public void update(){
  }
}
