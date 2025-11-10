package K23CNT3.nhtDay03.entity;

public class NhtKhoa {
    private String makh;
    private String tenkh;

    public NhtKhoa() {}
    public NhtKhoa(String makh, String tenkh) {
        this.makh = makh;
        this.tenkh = tenkh;
    }

    public String getMakh() { return makh; }
    public void setMakh(String makh) { this.makh = makh; }

    public String getTenkh() { return tenkh; }
    public void setTenkh(String tenkh) { this.tenkh = tenkh; }
}
