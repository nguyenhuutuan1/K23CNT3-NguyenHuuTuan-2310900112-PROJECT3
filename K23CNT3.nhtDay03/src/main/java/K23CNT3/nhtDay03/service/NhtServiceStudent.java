package K23CNT3.nhtDay03.service;

import K23CNT3.nhtDay03.entity.NhtStudent;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
@Service
public class NhtServiceStudent {
    List<NhtStudent> students
            = new ArrayList<NhtStudent>();
    public NhtServiceStudent() {
        students.addAll(Arrays.asList( new NhtStudent(1L,"NguyenHuuTuan",20,"Nam","HaNoi","0328942958","huutuansans@gmail.com"),
                                        new NhtStudent(2L,"Devmaster",25,"Non","Số 25VNP","0978611889","contact@devmaster.edu.vn"),
                                        new NhtStudent(3L,"Devmaster3",22,"Non","Số 25VNP","0978611889","chungtrinhj@gmail.com")
));
    }
    // Lấy toàn bộ danh sách sinh viên
    public List<NhtStudent> getStudents() {
        return students;
    }
    // Lấy sinh viên theo id
    public NhtStudent getStudent(Long id) {
        return students.stream()
                .filter(student -> student
                        .getId().equals(id))
                .findFirst().orElse(null);
    }
    // Thêm mới một sinh viên
    public NhtStudent addStudent(NhtStudent student) {
        students.add(student);
        return student;
    }
    // Cập nhật thông tin sinh viên
    public NhtStudent updateStudent(Long id, NhtStudent student)
    {
        NhtStudent check = getStudent(id);
        if (check == null) {
            return null;
        }
        students.forEach(item -> {
            if (item.getId()==id) {
                item.setName(student.getName());
                item.setAddress(student.getAddress());
                item.setEmail(student.getEmail());
                item.setPhone(student.getPhone());
                item.setAge(student.getAge());
                item.setGender(student.getGender());
            }
        });
        return student;
    }
    // Xóa thông tin sinh viên
    public boolean deleteStudent(Long id){
        NhtStudent check = getStudent(id);
        return students.remove(check);
    }

}
