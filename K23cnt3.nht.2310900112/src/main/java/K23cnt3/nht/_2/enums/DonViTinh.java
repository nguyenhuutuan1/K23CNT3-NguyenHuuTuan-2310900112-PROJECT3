package K23cnt3.nht._2.enums;

public enum DonViTinh {
    CAI("Cái"),
    CHAI("Chai"),
    LON("Lon"),
    HOP("Hộp"),
    GOI("Gói"),
    TUI("Túi"),
    BAO("Bao"),
    THUNG("Thùng"),
    KG("Kg"),
    GAM("Gam"),
    LIT("Lít"),
    ML("Ml");

    private final String tenDonVi;

    DonViTinh(String tenDonVi) {
        this.tenDonVi = tenDonVi;
    }

    public String getTenDonVi() {
        return tenDonVi;
    }

    public String getMaDonVi() {
        return this.name();
    }

    public static DonViTinh fromString(String text) {
        if (text == null || text.trim().isEmpty()) {
            return CAI;
        }

        String normalizedText = text.trim().toUpperCase();

        for (DonViTinh donVi : DonViTinh.values()) {
            if (donVi.tenDonVi.equalsIgnoreCase(text) ||
                    donVi.name().equalsIgnoreCase(normalizedText)) {
                return donVi;
            }
        }

        return CAI;
    }

    public static String[] getAllNames() {
        DonViTinh[] values = values();
        String[] names = new String[values.length];

        for (int i = 0; i < values.length; i++) {
            names[i] = values[i].getTenDonVi();
        }

        return names;
    }
}