# SQL-to-Java-Model-Generator

Công cụ nhỏ giúp tự động chuyển đổi thông tin cột từ SQL Server thành Java Model Class, hỗ trợ tạo nhanh các thuộc tính, constructor, getter, setter và import cần thiết.

## 1. Giới thiệu

Khi phát triển Java Web Application, việc tạo Java Model từ cấu trúc bảng SQL Server thường phải thực hiện thủ công.

Công cụ này giúp đơn giản hóa quá trình đó bằng cách:
- Chuyển đổi kiểu dữ liệu SQL Server sang kiểu dữ liệu Java.
- Tự động tạo các thuộc tính (`Fields`).
- Tạo constructor không tham số và constructor đầy đủ tham số.
- Tạo getter và setter.
- Tự động thêm các câu lệnh import cần thiết.
- Sao chép kết quả vào clipboard để sử dụng ngay.

## 2. Công nghệ sử dụng

- Java
- SQL Server
- Java AWT (Clipboard)

## 3. Yêu cầu

- JDK đã được cài đặt.
- Có thể sử dụng CMD hoặc IDE để chạy chương trình.

## 4. Hướng dẫn sử dụng

### Bước 1: Biên dịch chương trình

Mở CMD tại thư mục gốc của project và chạy:

```cmd
javac -d out src/Mapper.java
```

### Bước 2: Chạy chương trình

Cú pháp:

```cmd
java -cp out utils.Mapper [ClassName] [keep]
```

Trong đó:
- `ClassName`: Tên Java Model được tạo. Mặc định là `Model`.
- `keep`: Tùy chọn giữ nguyên tên cột thay vì chuyển sang camelCase.

Ví dụ:

```cmd
java -cp out utils.Mapper Student
```

Hoặc:

```cmd
java -cp out utils.Mapper Student keep
```

### Bước 3: Nhập dữ liệu

Copy tên cột và kiểu dữ liệu từ SQL Server, sau đó paste vào chương trình.

Định dạng đầu vào:

```text
ColumnName    DataType
```

Hai giá trị phải được phân cách bằng Tab. Mỗi cột nằm trên một dòng riêng biệt.

Ví dụ:

```text
StudentID    int
FullName     nvarchar(100)
Email        varchar(100)
DateOfBirth  date
GPA          decimal(4,2)
IsActive     bit
```

Sau khi paste dữ liệu, nhấn Enter thêm một lần để kết thúc nhập liệu.

### Bước 4: Lấy kết quả

Chương trình sẽ tự động:
- Sinh Java Model Class.
- Hiển thị source code trên CMD.
- Copy toàn bộ kết quả vào clipboard.

Sau đó chỉ cần paste vào file Java Model tương ứng.

## 5. Quy tắc chuyển đổi tên cột

Chế độ mặc định sử dụng camelCase.

| Tên cột SQL Server | Tên thuộc tính Java |
|---|---|
| StudentID | studentId |
| Student_ID | studentId |
| student_id | studentId |
| URLPath | urlPath |
| Student2Name | student2Name |

Nếu sử dụng chế độ `keep`, chương trình giữ nguyên tên cột trong phạm vi các ký tự Java cho phép.

Ví dụ:

| Tên cột SQL Server | Tên thuộc tính Java |
|---|---|
| StudentID | StudentID |
| Student_ID | Student_ID |

## 6. Bảng ánh xạ kiểu dữ liệu

| SQL Server | Java |
|---|---|
| char, nchar, varchar, nvarchar | String |
| text, ntext, xml, json | String |
| uniqueidentifier | String |
| bigint | long |
| int | int |
| smallint, tinyint | short |
| bit | boolean |
| float | double |
| real | float |
| decimal, numeric | BigDecimal |
| money, smallmoney | BigDecimal |
| date | java.sql.Date |
| datetime, datetime2, smalldatetime | java.sql.Timestamp |
| time | java.sql.Time |
| datetimeoffset | microsoft.sql.DateTimeOffset |
| binary, varbinary, image | byte[] |
| timestamp, rowversion | byte[] |
| geometry, geography, udt | byte[] |
| sql_variant | Object |
| vector | microsoft.sql.Vector |

Các kiểu dữ liệu chưa được hỗ trợ sẽ mặc định chuyển thành `String` và được đánh dấu bằng comment để dễ nhận biết.

## 7. Lưu ý

- Dữ liệu đầu vào cần có tên cột và kiểu dữ liệu tương ứng.
- Công cụ hiện hỗ trợ định dạng hai cột phân cách bằng Tab hoặc khoảng trắng.
- Tên class được truyền vào cần tuân thủ quy tắc đặt tên của Java.
- `DateTimeOffset` và `Vector` cần phiên bản Microsoft JDBC Driver tương thích.
- Các trường số được sử dụng kiểu primitive theo thiết kế hiện tại.

## 8. Định hướng phát triển

Một số tính năng có thể được bổ sung trong tương lai:
- Hỗ trợ thêm hệ quản trị cơ sở dữ liệu.
- Cải thiện khả năng xử lý tên cột đặc biệt.
- Bổ sung thêm các tùy chọn tạo Java Model.

## 9. License

Dự án được phát hành theo giấy phép MIT License.
