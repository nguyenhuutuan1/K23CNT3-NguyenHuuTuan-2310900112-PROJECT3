package K23CNT3.nhtDay03.service;

import K23CNT3.nhtDay03.entity.NhtKhoa;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NhtKhoaService {
    private List<NhtKhoa> khoaList = new ArrayList<>();

    public NhtKhoaService() {
        khoaList.add(new NhtKhoa("KH01", "Công nghệ thông tin"));
        khoaList.add(new NhtKhoa("KH02", "Kinh tế"));
        khoaList.add(new NhtKhoa("KH03", "Điện tử viễn thông"));
        khoaList.add(new NhtKhoa("KH04", "Xây dựng"));
        khoaList.add(new NhtKhoa        ("KH05", "Quản trị kinh doanh"));
    }

    public List<NhtKhoa> getAll() {
        return khoaList;
    }

    public NhtKhoa getByMakh(String makh) {
        return khoaList.stream().filter(k -> k.getMakh().equals(makh)).findFirst().orElse(null);
    }

    public NhtKhoa add(NhtKhoa k) {
        khoaList.add(k);
        return k; // trả về đối tượng vừa thêm
    }

    public NhtKhoa update(String makh, NhtKhoa k) {
        NhtKhoa existing = getByMakh(makh);
        if(existing != null) {
            existing.setTenkh(k.getTenkh());
            return existing; // trả về đối tượng vừa cập nhật
        }
        return null; // nếu không tìm thấy
    }

    public boolean delete(String makh) {
        return khoaList.removeIf(k -> k.getMakh().equals(makh)); // trả về true nếu xóa thành công
    }
}
