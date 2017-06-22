package gui;

class CreditsView extends AbstractView {
    private static CreditsView instance;

    private CreditsView() {
        super();
    }

    public void update() {

    }

    static CreditsView getInstance() {
        if (instance == null)
            instance = new CreditsView();
        return instance;
    }
}
