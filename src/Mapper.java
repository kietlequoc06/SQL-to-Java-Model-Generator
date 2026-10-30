/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mapper;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.util.*;
/**
 * Cách dùng: java Mapper Student -> tên field dạng camelCase (studentId) java
 * Mapper Student keep -> giữ nguyên tên cột (StudentID, Student_ID) Dán các
 * dòng "Tên<tab>Kiểu", kết thúc bằng một dòng trống. Bảng ánh xạ theo Microsoft
 * JDBC Driver for SQL Server ("Using basic data types").
 */
public class Mapper {

    static final Set<String> KEYWORDS = new HashSet<>(Arrays.asList(
            "abstract", "boolean", "break", "byte", "case", "catch", "char", "class", "const",
            "continue", "default", "do", "double", "else", "enum", "extends", "final", "finally",
            "float", "for", "goto", "if", "implements", "import", "instanceof", "int", "interface",
            "long", "native", "new", "package", "private", "protected", "public", "return", "short",
            "static", "strictfp", "super", "switch", "synchronized", "this", "throw", "throws",
            "transient", "try", "void", "volatile", "while"));

    /**
     * SQL Server type -> Java type (đủ theo bảng của Microsoft).
     */
    static String map(String sqlType) {
        String t = sqlType.toLowerCase().replaceAll("\\(.*\\)", "").trim();
        switch (t) {
            // Chuỗi
            case "char":
            case "nchar":
            case "varchar":
            case "nvarchar":
            case "text":
            case "ntext":
            case "xml":
            case "json":
            case "uniqueidentifier":
                return "String";
            // Số nguyên
            case "bigint":
                return "long";
            case "int":
                return "int";
            case "smallint":
                return "short";
            case "tinyint":
                return "short";
            case "bit":
                return "boolean";
            // Số thực
            case "float":
                return "double";
            case "real":
                return "float";
            case "decimal":
            case "numeric":
            case "money":
            case "smallmoney":
                return "BigDecimal";
            // Ngày giờ
            case "date":
                return "Date";            // java.sql.Date
            case "datetime":
            case "datetime2":
            case "smalldatetime":
                return "Timestamp";       // java.sql.Timestamp
            case "time":
                return "Time";            // java.sql.Time
            case "datetimeoffset":
                return "DateTimeOffset";  // microsoft.sql.DateTimeOffset
            // Nhị phân
            case "binary":
            case "varbinary":
            case "image":
            case "timestamp":
            case "rowversion":
            case "geometry":
            case "geography":
            case "udt":
                return "byte[]";
            // Khác
            case "sqlvariant":
            case "sql_variant":
                return "Object";
            case "vector":
                return "Vector";          // microsoft.sql.Vector
            default:
                return "String /*?" + t + "*/";
        }
    }

    /**
     * Dòng import cần cho kiểu Java (null nếu không cần).
     */
    static String importFor(String javaType) {
        switch (javaType) {
            case "BigDecimal":
                return "java.math.BigDecimal";
            case "Date":
                return "java.sql.Date";
            case "Timestamp":
                return "java.sql.Timestamp";
            case "Time":
                return "java.sql.Time";
            case "DateTimeOffset":
                return "microsoft.sql.DateTimeOffset";
            case "Vector":
                return "microsoft.sql.Vector";
            default:
                return null;
        }
    }

    static String cap(String s) {
        return s.isEmpty() ? s : Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }

    /**
     * Chuyển mọi kiểu tên về camelCase: StudentID, Student_ID, student_id,
     * STUDENT_ID, studentId, Student ID, student-id đều thành studentId.
     * URLPath -> urlPath, Student2Name -> student2Name.
     */
    static String camel(String s) {
        String spaced = s.replaceAll("[^A-Za-z0-9]+", " ") // _ - khoảng trắng -> space
                .replaceAll("(?<=[a-z0-9])(?=[A-Z])", " ") // studentId -> student Id
                .replaceAll("(?<=[A-Z])(?=[A-Z][a-z])", " ") // URLPath -> URL Path
                .trim();
        StringBuilder sb = new StringBuilder();
        for (String w : spaced.split(" ")) {
            if (w.isEmpty()) {
                continue;
            }
            w = w.toLowerCase();
            sb.append(sb.length() == 0 ? w : cap(w));
        }
        return safe(sb.toString());
    }

    /**
     * Tránh tên trùng từ khóa Java hoặc bắt đầu bằng số.
     */
    static String safe(String n) {
        if (n.isEmpty()) {
            return "field";
        }
        if (Character.isDigit(n.charAt(0))) {
            n = "_" + n;
        }
        if (KEYWORDS.contains(n)) {
            n = n + "_";
        }
        return n;
    }

    /**
     * Chế độ keep: giữ nguyên tên, chỉ đổi ký tự không hợp lệ (khoảng trắng,
     * dấu -) thành _.
     */
    static String keepName(String s) {
        return safe(s.replaceAll("[^A-Za-z0-9_$]", "_"));
    }

    public static void main(String[] args) {
        String className = args.length > 0 ? args[0] : "Model";
        boolean keep = args.length > 1 && args[1].equalsIgnoreCase("keep");
        List<String> names = new ArrayList<>(), types = new ArrayList<>();

        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if (line.isEmpty()) {
                break;
            }
            // Dán từ SSMS thường ngăn cách bằng tab; nếu không có tab thì tách theo khoảng trắng
            String[] p = line.contains("\t") ? line.split("\\t+") : line.split("\\s+");
            if (p.length < 2) {
                continue;
            }
            names.add(keep ? keepName(p[0].trim()) : camel(p[0].trim()));
            types.add(map(p[1].trim()));
        }

        // Gom import cần thiết
        TreeSet<String> imports = new TreeSet<>();
        for (String t : types) {
            String imp = importFor(t);
            if (imp != null) {
                imports.add(imp);
            }
        }

        StringBuilder sb = new StringBuilder();
        for (String imp : imports) {
            sb.append("import ").append(imp).append(";\n");
        }
        if (!imports.isEmpty()) {
            sb.append("\n");
        }

        sb.append("public class ").append(className).append(" {\n");
        for (int i = 0; i < names.size(); i++) {
            sb.append("    private ").append(types.get(i)).append(" ").append(names.get(i)).append(";\n");
        }

        sb.append("\n    public ").append(className).append("() {\n    }\n\n");

        sb.append("    public ").append(className).append("(");
        for (int i = 0; i < names.size(); i++) {
            sb.append(i > 0 ? ", " : "").append(types.get(i)).append(" ").append(names.get(i));
        }
        sb.append(") {\n");
        for (String n : names) {
            sb.append("        this.").append(n).append(" = ").append(n).append(";\n");
        }
        sb.append("    }\n");

        for (int i = 0; i < names.size(); i++) {
            String n = names.get(i), t = types.get(i);
            sb.append("\n    public ").append(t).append(" get").append(cap(n)).append("() {\n")
                    .append("        return ").append(n).append(";\n    }\n");
            sb.append("\n    public void set").append(cap(n)).append("(").append(t).append(" ").append(n).append(") {\n")
                    .append("        this.").append(n).append(" = ").append(n).append(";\n    }\n");
        }
        sb.append("}\n");

        System.out.println(sb);
        try {
            Toolkit.getDefaultToolkit().getSystemClipboard()
                    .setContents(new StringSelection(sb.toString()), null);
            System.out.println("// Đã copy vào clipboard");
        } catch (Exception ignored) {
        }
    }
}
