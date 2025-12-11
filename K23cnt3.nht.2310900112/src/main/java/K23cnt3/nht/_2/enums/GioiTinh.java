package K23cnt3.nht._2.enums;

public enum GioiTinh {
    NAM("Nam"),
    NU("Nữ"),
    KHAC("Khác");

    private final String tenGioiTinh;

    GioiTinh(String tenGioiTinh) {
        this.tenGioiTinh = tenGioiTinh;
    }

    public String getTenGioiTinh() {
        return tenGioiTinh;
    }

    public String getMaGioiTinh() {
        return this.name();
    }

    // Chuyển từ string sang enum
    public static GioiTinh fromString(String text) {
        if (text == null || text.trim().isEmpty()) {
            return NAM;
        }

        String normalizedText = text.trim().toLowerCase();

        for (GioiTinh gioiTinh : GioiTinh.values()) {
            if (gioiTinh.tenGioiTinh.toLowerCase().equals(normalizedText) ||
                    gioiTinh.name().toLowerCase().equals(normalizedText)) {
                return gioiTinh;
            }
        }

        // Kiểm tra các từ đồng nghĩa
        if (normalizedText.equals("male") || normalizedText.equals("m")) {
            return NAM;
        } else if (normalizedText.equals("female") || normalizedText.equals("f")) {
            return NU;
        }

        return KHAC;
    }

    // Lấy tất cả giới tính
    public static GioiTinh[] getAll() {
        return GioiTinh.values();
    }

    // Lấy danh sách giới tính cho select box
    public static String[] getOptions() {
        GioiTinh[] values = values();
        String[] options = new String[values.length];

        for (int i = 0; i < values.length; i++) {
            options[i] = values[i].getTenGioiTinh();
        }

        return options;
    }
}