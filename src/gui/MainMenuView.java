class MainMenuView extends AbstractView{
  private MainFrame mainframe;
  private static MainMenuView instance;
  
  private MainMenuView (MainFrame mainframe){
    super(mainframe);
  }
  
  public static MainMenuView instance(MainFrame mainframe){
    if(instance==null){
      instance = new MainMenuView(mainframe);
    }
    return instance;
  }
  
  public void update(){
}
}
