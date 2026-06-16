package model.service;

import model.data.DataCenter;
import model.entity.BankingSystem;

/**
 * Singleton Service để xem cấu hình ngân hàng và lãi suất
 * Chỉ Staff có thể xem chức năng này
 */
public class BankConfigService {
    private static BankConfigService instance;
    private DataCenter dataCenter;

    private BankConfigService() {
        this.dataCenter = DataCenter.getInstance();
    }

    public static BankConfigService getInstance() {
        if (instance == null) {
            instance = new BankConfigService();
        }
        return instance;
    }

    /**
     * Xem tất cả thông tin cấu hình ngân hàng
     */
    public String viewBankConfig() {
        BankingSystem system = dataCenter.getBankingSystem();
        if (system == null) {
            return "❌ Lỗi: Không thể tải cấu hình ngân hàng!";
        }

        return formatBankConfig(system);
    }

    /**
     * Xem lãi suất ngân hàng chi tiết
     */
    public String viewInterestRates() {
        BankingSystem system = dataCenter.getBankingSystem();
        if (system == null) {
            return "❌ Lỗi: Không thể tải cấu hình lãi suất!";
        }

        return formatInterestRates(system);
    }

    /**
     * Xem yêu cầu tối thiểu và cấu hình tài khoản
     */
    public String viewAccountRequirements() {
        BankingSystem system = dataCenter.getBankingSystem();
        if (system == null) {
            return "❌ Lỗi: Không thể tải cấu hình tài khoản!";
        }

        return formatAccountRequirements(system);
    }

    /**
     * Xem thông tin ngày tháng hệ thống
     */
    public String viewSystemDate() {
        BankingSystem system = dataCenter.getBankingSystem();
        if (system == null) {
            return "❌ Lỗi: Không thể tải thông tin ngày hệ thống!";
        }

        StringBuilder result = new StringBuilder();
        result.append("\n============================================================\n");
        result.append("                    NGÀY HỆ THỐNG\n");
        result.append("============================================================\n");
        result.append("📅 Ngày hiện tại : ").append(system.getSystemDate()).append("\n");
        result.append("============================================================\n");
        return result.toString();
    }

    /**
     * Định dạng thông tin cấu hình ngân hàng
     */
    private String formatBankConfig(BankingSystem system) {
        StringBuilder result = new StringBuilder();
        result.append("\n============================================================\n");
        result.append("              CẤU HÌNH NGÂN HÀNG - HKL BANK\n");
        result.append("============================================================\n");

        result.append("🏦 Tên ngân hàng  : ").append(system.getBankName()).append("\n");
        result.append("📅 Ngày hệ thống  : ").append(system.getSystemDate()).append("\n");

        result.append("\n--- LOẠI LÃI SUẤT ---\n");
        result.append("💰 Lãi suất yêu cầu (Nhu cầu) : ")
               .append(String.format("%.2f%%", system.getDemandInterestRate() * 100)).append(" / năm\n");
        result.append("💰 Lãi suất kỳ hạn 1 tháng   : ")
               .append(String.format("%.2f%%", system.getInterestRate1M() * 100)).append(" / năm\n");
        result.append("💰 Lãi suất kỳ hạn 6 tháng   : ")
               .append(String.format("%.2f%%", system.getInterestRate6M() * 100)).append(" / năm\n");
        result.append("💰 Lãi suất kỳ hạn 12 tháng  : ")
               .append(String.format("%.2f%%", system.getInterestRate12M() * 100)).append(" / năm\n");

        result.append("\n--- LOAN ---\n");
        result.append("💳 Lãi suất vay cơ bản       : ")
               .append(String.format("%.2f%%", system.getBaseLoanInterestRate() * 100)).append(" / năm\n");

        result.append("\n--- YÊU CẦU TỐI THIỂU ---\n");
        result.append("💵 Số dư tối thiểu (Thanh toán) : ")
               .append(String.format("%.2f VNĐ", system.getMinCheckingBalance())).append("\n");
        result.append("💵 Tiền gửi tối thiểu (Tiết kiệm) : ")
               .append(String.format("%.2f VNĐ", system.getMinSavingDeposit())).append("\n");

        result.append("============================================================\n");
        return result.toString();
    }

    /**
     * Định dạng thông tin lãi suất chi tiết
     */
    private String formatInterestRates(BankingSystem system) {
        StringBuilder result = new StringBuilder();
        result.append("\n============================================================\n");
        result.append("              BẢNG LÃI SUẤT - HKL BANK\n");
        result.append("============================================================\n");

        result.append("\n📊 LOẠI TÀI KHOẢN TIẾT KIỆM:\n");
        result.append("┌─────────────────┬──────────────────┐\n");
        result.append("│ KỲ HẠN GỬI      │ LÃI SUẤT (% NĂM) │\n");
        result.append("├─────────────────┼──────────────────┤\n");
        result.append(String.format("│ 1 tháng         │ %15.2f%% │\n", system.getInterestRate1M() * 100));
        result.append(String.format("│ 6 tháng         │ %15.2f%% │\n", system.getInterestRate6M() * 100));
        result.append(String.format("│ 12 tháng        │ %15.2f%% │\n", system.getInterestRate12M() * 100));
        result.append("└─────────────────┴──────────────────┘\n");

        result.append("\n📊 LOẠI TÀI KHOẢN THANH TOÁN:\n");
        result.append("┌──────────────────────────────────────┐\n");
        result.append("│ LÃI SUẤT YÊU CẦU (DEMAND ACCOUNT)   │\n");
        result.append("├──────────────────────────────────────┤\n");
        result.append(String.format("│ %-36.2f%% │\n", system.getDemandInterestRate() * 100));
        result.append("└──────────────────────────────────────┘\n");

        result.append("\n📊 LOẠI TÀI KHOẢN VAY:\n");
        result.append("┌──────────────────────────────────────┐\n");
        result.append("│ LÃI SUẤT VAY CƠ BẢN (BASE LOAN)     │\n");
        result.append("├──────────────────────────────────────┤\n");
        result.append(String.format("│ %-36.2f%% │\n", system.getBaseLoanInterestRate() * 100));
        result.append("└──────────────────────────────────────┘\n");

        result.append("\n💡 GHI CHÚ:\n");
        result.append("   • Tất toán sớm sổ tiết kiệm chỉ nhận 50% tiền lãi\n");
        result.append("   • Tất toán đúng hạn nhận 100% tiền lãi\n");

        result.append("============================================================\n");
        return result.toString();
    }

    /**
     * Định dạng yêu cầu tối thiểu cho tài khoản
     */
    private String formatAccountRequirements(BankingSystem system) {
        StringBuilder result = new StringBuilder();
        result.append("\n============================================================\n");
        result.append("          YÊU CẦU TỐI THIỂU - HKL BANK\n");
        result.append("============================================================\n");

        result.append("\n💳 TÀI KHOẢN THANH TOÁN (CHECKING):\n");
        result.append("   • Số dư tối thiểu  : ")
               .append(String.format("%.2f VNĐ\n", system.getMinCheckingBalance()));
        result.append("   • Điều kiện mở tài khoản : Bất kỳ khách hàng nào\n");

        result.append("\n💳 TÀI KHOẢN TIẾT KIỆM (SAVING):\n");
        result.append("   • Tiền gửi tối thiểu : ")
               .append(String.format("%.2f VNĐ\n", system.getMinSavingDeposit()));
        result.append("   • Kỳ hạn gửi         : 1 tháng, 6 tháng, 12 tháng\n");
        result.append("   • Lãi suất           : Xem bảng lãi suất\n");
        result.append("   • Điều kiện          : Cần mở tài khoản thanh toán trước\n");

        result.append("\n💳 TÀI KHOẢN VAY (LOAN):\n");
        result.append("   • Số tiền vay        : Tùy vào khả năng tài chính\n");
        result.append("   • Kỳ hạn vay         : 1 tháng, 6 tháng, 12 tháng\n");
        result.append("   • Lãi suất           : Xem bảng lãi suất\n");
        result.append("   • Điều kiện          : Khách hàng phải có tài khoản hoạt động\n");

        result.append("\n============================================================\n");
        return result.toString();
    }
}

