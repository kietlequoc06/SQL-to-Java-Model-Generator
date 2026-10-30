# SQL to Java Model Generator

Công cụ chuyển đổi tên cột và kiểu dữ liệu từ SQL Server thành Java Model Class.

## 1. Cách chạy

**Yêu cầu:** Đã cài đặt JDK và cấu hình Java trong PATH.

Mở CMD tại thư mục project và chạy:

```cmd
javac -d out src/Mapper.java
java -cp out mapper.Mapper Student
```

Thay `Student` bằng tên class muốn tạo.

## 2. Cách sử dụng

1. Mở SQL Server Management Studio (SSMS).
2. Chọn database và mở **Tables**.
3. Click chuột phải vào bảng cần chuyển đổi, chọn **Design**.
4. Copy danh sách thuộc tính trong bảng (Column Name và Data Type).
5. Dán vào cửa sổ CMD đang chạy chương trình.
6. Nhấn Enter hai lần để kết thúc nhập dữ liệu.

Chương trình sẽ tự động tạo Java Model Class và copy kết quả vào Clipboard.

## 3. Lưu ý

- Mỗi dòng dữ liệu cần có tên cột và kiểu dữ liệu, phân cách bằng Tab.
- Mặc định tên thuộc tính được chuyển sang dạng `camelCase`.
- Có thể sử dụng chế độ giữ nguyên tên cột bằng lệnh:

```cmd
java -cp out utils.Mapper Student keep
```
