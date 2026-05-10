## Luồng hoạt động
**Người dùng (View) $\rightarrow$ Controller $\rightarrow$ Service $\rightarrow$ DAO** 
## Kiến trúc
1. Tầng Thực thể (Entity)
**Vai trò:** Là tầng cơ bản nhất, định nghĩa cấu trúc dữ liệu của các đối tượng trong hệ thống.

2. Tầng Truy cập Dữ liệu (DAO - Data Access Object)
   **Vai trò:** Chịu trách nhiệm tương tác trực tiếp với nơi lưu trữ dữ liệu (DataCenter)

    **Chức năng:**
   - Chứa các hàm tuương tác với cơ sở dữ liệu (thêm, xóa, cập nhật, lấy dữ liệu,...)

3. Tầng Dịch vụ (Service)
   **Vai trò:** Là trung tâm xử lý logic nghiệp vụ  của ứng dụng.

    **Chức năng:**

   - Chứa các hàm xử lí logic
   - Gọi tầng DAO để lấy dữ liệu, từ dữ liệu đó dùng để xử lí

4. Tầng Điều khiển (Controller)
   **Vai trò:** Làm cầu nối giữa giao diện người dùng (View) và tầng dịch vụ (Service).

    **Chức năng:**

    - Tiếp nhận các yêu cầu và dữ liệu đầu vào từ View.

    - Gọi các phương thức tương ứng trong tầng Service để thực thi yêu cầu.
    - Quyết định view sẽ hiển thị cái gì

5. Tầng Giao diện (View)
   Vai trò: Tương tác trực tiếp với người dùng qua giao diện dòng lệnh (Console).
