### B1: Clone project về máy
``git clone https://github.com/vanhung010/BankingSystemProject.git``
### B2: Tạo nhánh mới
``git checkout -b <Tên thành viên>``

### B3: Tải code lên (Sau khi đã hoàn thanành 1 chức năng)

#### P1: Lưu công việc

```bash 
git add .
git commit -m "mô tả hành động"
```
### P2: Tải code lên nhánh
```bash
git push origin <Tên nhánh>
```
### P3: Tạo pull request
1. Truy cập vào trang GitHub của dự án.

2. Bấm pull request.

3. bấm new pull request
4. Chọn base là main, compare là nhánh đang làm việc
5. Bấm create pull request.
# Lưu ý:
- Mỗi khi bắt đâu làm nhớ pull code từ nhánh main về để cập nhật code mới nhất của nhóm
```bash
git pull origin main
```
