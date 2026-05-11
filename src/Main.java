import model.data.SystemDao;
import view.LoginView;
import util.ReadFile;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        // Khởi chạy việc nạp dữ liệu từ file txt vào System DataCenter
        ReadFile.loadDataToDataCenter();

        SystemDao systemDao = new SystemDao();

        new LoginView().display();
    }
}