class CreditView extends AbstractView{
  private MainFrame mainframe;
  private static CreditView instance;
  
  private CreditView (MainFrame mainframe){
    super(mainframe);
  }
  
  public static CreditView instance(MainFrame mainframe){
    if(instance==null){
      instance = new CreditView(mainframe);
    }
    return instance;
  }
  
  public void update(){
  }
}
