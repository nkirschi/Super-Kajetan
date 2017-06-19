class SettingView extends AbstractView{
  private MainFrame mainframe;
  private static SettingView instance;
  
  private SettingView (MainFrame mainframe){
    super(mainframe);
  }
  
  public static SettingView instance(MainFrame mainframe){
    if(instance==null){
      instance = new SettingView(mainframe);
    }
    return instance;
  }
  
  public void update(){
}
}
