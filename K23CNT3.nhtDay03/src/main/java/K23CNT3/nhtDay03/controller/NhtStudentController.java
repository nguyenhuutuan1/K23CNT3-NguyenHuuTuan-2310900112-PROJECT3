package K23CNT3.nhtDay03.controller;


import K23CNT3.nhtDay03.entity.NhtStudent;
import K23CNT3.nhtDay03.service.NhtServiceStudent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class NhtStudentController {
    @Autowired
    private NhtServiceStudent studentService;
    @GetMapping("/student-list")
    public List<NhtStudent> getAllStudents() {
        return studentService.getStudents();
    }
    @GetMapping("/student/{id}")
    public NhtStudent getAllStudents(@PathVariable String id)
    {
        Long param = Long.parseLong(id);
        return studentService.getStudent(param);
    }
    @PostMapping("/student-add")
    public NhtStudent addStudent(@RequestBody NhtStudent student)
    {
        return studentService.addStudent(student);
    }
    @PutMapping("/student/{id}")
    public NhtStudent updateStudent(@PathVariable String id,
                                 @RequestBody NhtStudent student) {
        Long param = Long.parseLong(id);
        return studentService.updateStudent(param,
                student);
    }
    @DeleteMapping("/student/{id}")
    public boolean deleteStudent(@PathVariable String id) {
        Long param = Long.parseLong(id);
        return studentService.deleteStudent(param);
    }
}
