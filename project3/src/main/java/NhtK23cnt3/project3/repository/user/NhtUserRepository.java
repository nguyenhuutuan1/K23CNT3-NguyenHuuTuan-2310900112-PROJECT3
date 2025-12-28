package NhtK23cnt3.project3.repository.user;

import NhtK23cnt3.project3.entity.user.NhtUser;
import NhtK23cnt3.project3.entity.user.NhtUserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NhtUserRepository extends JpaRepository<NhtUser, Long> {

    // Lọc user theo role
    List<NhtUser> findByRole(NhtUserRole role);

    // Dùng cho login
    Optional<NhtUser> findByEmail(String email);

    Optional<NhtUser> findByEmailAndPassword(String email, String password);
}
