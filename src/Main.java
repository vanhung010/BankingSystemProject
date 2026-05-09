import model.data.SystemDao;
import view.LoginView;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        SystemDao systemDao = new SystemDao();

        new LoginView().display();
    }
}