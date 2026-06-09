import model.data.SystemDao;
import view.LoginView;
import util.ReadFile;


public class Main {
    public static void main(String[] args) {

        ReadFile.loadDataToDataCenter();

        SystemDao systemDao = new SystemDao();

        new LoginView().display();
    }
}