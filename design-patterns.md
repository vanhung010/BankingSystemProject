# Design Patterns — Banking System

---

## 1. Singleton

**Mục đích:** Đảm bảo chỉ có duy nhất 1 instance của `BankSystem` tồn tại trong suốt vòng đời ứng dụng.

**Áp dụng vào:** `BankSystem`

**Cách hoạt động:**
- Constructor là `private` — không thể tạo object từ bên ngoài
- Truy cập duy nhất qua `BankSystem.getInstance()`
- Lần đầu gọi → tạo instance, các lần sau → trả về instance cũ

```java
public class BankSystem {
    private static BankSystem instance;

    private BankSystem() {}

    public static BankSystem getInstance() {
        if (instance == null) {
            instance = new BankSystem();
        }
        return instance;
    }
}
```

**Lý do dùng:** `BankSystem` lưu các thông số toàn cục như lãi suất, ngày hệ thống — nếu tồn tại nhiều instance sẽ gây mâu thuẫn dữ liệu.

---

## 2. Factory Method

**Mục đích:** Tách biệt logic khởi tạo từng loại tài khoản ra khỏi code gọi — mỗi loại tài khoản có tham số khởi tạo khác nhau.

**Áp dụng vào:** `AccountFactory`

**Cách hoạt động:**
- 3 method riêng biệt, mỗi method tạo đúng 1 loại tài khoản với đúng tham số cần thiết
- Caller không cần biết bên trong khởi tạo như thế nào

```java
public class AccountFactory {
    public static CheckingAccount createChecking(Customer owner, double minBalance) { ... }

    public static SavingsAccount createSavings(Customer owner, int termMonths, double balance) { ... }

    public static LoanAccount createLoan(Customer owner, double principal, double rate, int term) { ... }
}
```

**Lý do dùng:** `LoanAccount` cần `principal`, `rate`, `term` — không thể gộp chung 1 method với `CheckingAccount` chỉ cần `minBalance`. Tách riêng đảm bảo đúng tham số, không phải truyền `null`.

---

## 3. Strategy

**Mục đích:** Cho phép thay đổi thuật toán tính lãi suất linh hoạt mà không sửa code của `Account`.

**Áp dụng vào:** `InterestStrategy` — tính lãi cho `SavingsAccount` và `LoanAccount`

**Cách hoạt động:**
- Interface `InterestStrategy` định nghĩa method `calculate(principal, rate, time)`
- Mỗi loại tài khoản dùng một strategy khác nhau, gán vào lúc khởi tạo
- `Account` chỉ gọi `interestStrategy.calculate(...)` — không biết bên trong tính thế nào

```java
public interface InterestStrategy {
    double calculate(double principal, double rate, int time);
}

// Lãi kép cho tiết kiệm có kỳ hạn
public class TermInterestStrategy implements InterestStrategy { ... }

// Lãi không kỳ hạn
public class DemandInterestStrategy implements InterestStrategy { ... }

// Không tính lãi (CheckingAccount)
public class NoInterestStrategy implements InterestStrategy { ... }
```

**Lý do dùng:** Công thức tính lãi khác nhau giữa tiết kiệm có kỳ hạn, không kỳ hạn và tài khoản vay — Strategy giúp hoán đổi công thức mà không cần `if-else` trong `Account`.

---

## 4. Observer

**Mục đích:** Khi một sự kiện xảy ra, tự động thông báo đến tất cả các thành phần liên quan mà không tạo sự phụ thuộc trực tiếp.

**Áp dụng vào 2 tính năng:**

### 4a. Tăng 1 tháng

| Vai trò | Class |
|---|---|
| Subject | `SystemService` |
| Observer interface | `MonthlyEventObserver` |
| Concrete Observer | `SavingExpiryObserver`, `LoanMonthlyObserver`, `InterestObserver` |

Khi `updateDateSystemPlus1Month()` chạy xong → `notifyObservers()` → 3 Observer tự động xử lý phần của mình theo đúng thứ tự.

```
SystemService.updateDateSystemPlus1Month()
    └── notifyObservers()
            ├── SavingExpiryObserver  → kiểm tra & gia hạn sổ tiết kiệm đáo hạn
            ├── LoanMonthlyObserver   → khóa tài khoản, reset chu kỳ trả nợ
            └── InterestObserver      → cộng lãi vay hàng tháng
```

### 4b. Thay đổi trạng thái tài khoản

| Vai trò | Class |
|---|---|
| Subject | `Account` |
| Observer interface | `AccountStatusObserver` |
| Concrete Observer | `AccountStatusLogger` |

Khi `account.changeState(newStatus)` được gọi → thay đổi trạng thái → `notifyObservers()` → `AccountStatusLogger` in thông báo tương ứng.

**Lý do dùng:** Thay vì `StaffController` phải tự gọi 9 method thủ công, Controller chỉ cần 1 dòng. Thêm nghiệp vụ mới → tạo thêm Observer, không sửa code cũ.

---

## 5. MVC (Model - View - Controller)

**Mục đích:** Tách biệt 3 tầng trách nhiệm — dữ liệu, giao diện, xử lý logic — để dễ bảo trì và phân công.

**Áp dụng vào:** Toàn bộ cấu trúc project

| Tầng | Package | Trách nhiệm |
|---|---|---|
| **Model** | `entity`, `service`, `dao`, `pattern` | Dữ liệu, nghiệp vụ, truy xuất DB |
| **View** | `view` | Hiển thị menu, nhận input từ người dùng |
| **Controller** | `controller` | Nhận yêu cầu từ View, gọi Service, trả kết quả về View |

**Luồng hoạt động:**

```
View  ──(input)──▶  Controller  ──(gọi)──▶  Service / DAO
                        │                        │
                        │◀────(kết quả)──────────┘
                        │
                    ──(hiển thị)──▶  View
```

**Quy tắc quan trọng:**
- `View` không gọi trực tiếp `Service` hay `DAO`
- `Controller` không chứa logic nghiệp vụ — chỉ điều phối
- `Model` không biết `View` tồn tại

**Lý do dùng:** 5 thành viên phân công theo tầng — người làm View không ảnh hưởng người làm Service, dễ test từng phần độc lập.

---

## Tổng kết

| Pattern | Class chính | Giải quyết vấn đề gì |
|---|---|---|
| Singleton | `BankSystem` | Tránh nhiều instance gây mâu thuẫn dữ liệu |
| Factory Method | `AccountFactory` | Mỗi loại tài khoản có tham số khởi tạo khác nhau |
| Strategy | `InterestStrategy` | Công thức tính lãi khác nhau theo loại tài khoản |
| Observer | `SystemService`, `Account` | Tự động kích hoạt nghiệp vụ khi sự kiện xảy ra |
| MVC | Toàn bộ project | Tách biệt UI, logic, dữ liệu — dễ phân công nhóm |
