package K23CNT3.nhtDay03.controller;

import K23CNT3.nhtDay03.entity.NhtKhoa;
import K23CNT3.nhtDay03.service.NhtKhoaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/khoa")
public class NhtKhoaController {
    private final NhtKhoaService khoaService;

    public NhtKhoaController(NhtKhoaService khoaService) {
        this.khoaService = khoaService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<NhtKhoa>> getAll() {
        return ResponseEntity.ok(khoaService.getAll());
    }

    @GetMapping("/{makh}")
    public ResponseEntity<NhtKhoa> getByMakh(@PathVariable String makh) {
        NhtKhoa k = khoaService.getByMakh(makh);
        return k != null ? ResponseEntity.ok(k) : ResponseEntity.notFound().build();
    }

    @PostMapping("/add")
    public ResponseEntity<NhtKhoa> add(@RequestBody NhtKhoa k) {
        return ResponseEntity.ok(khoaService.add(k));
    }

    @PutMapping("/update/{makh}")
    public ResponseEntity<NhtKhoa> update(@PathVariable String makh, @RequestBody NhtKhoa k) {
        NhtKhoa updated = khoaService.update(makh, k);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/delete/{makh}")
    public ResponseEntity<Void> delete(@PathVariable String makh) {
        boolean deleted = khoaService.delete(makh);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
