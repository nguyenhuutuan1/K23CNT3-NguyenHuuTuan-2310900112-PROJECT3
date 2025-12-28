package NhtK23cnt3.project3.repository.order;

import NhtK23cnt3.project3.entity.order.NhtOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NhtOrderRepository extends JpaRepository<NhtOrder, Long> {
    Optional<NhtOrder> findByIdAndPhone(Long id, String phone);
    List<NhtOrder> findByPhone(String phone);
    List<NhtOrder> findByUser_IdOrderByCreatedAtDesc(Long userId);
}