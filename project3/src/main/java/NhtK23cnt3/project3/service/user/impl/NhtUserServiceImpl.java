package NhtK23cnt3.project3.service.user.impl;

import NhtK23cnt3.project3.entity.user.NhtUser;
import NhtK23cnt3.project3.entity.user.NhtUserRole;
import NhtK23cnt3.project3.repository.user.NhtUserRepository;
import NhtK23cnt3.project3.service.user.NhtUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NhtUserServiceImpl implements NhtUserService {

    private final NhtUserRepository userRepository;

    @Override
    public NhtUser registerCustomer(String fullName, String email, String password, String phone) {
        // check trùng email
        if (userRepository.findByEmail(email).isPresent()) {
            return null;
        }

        NhtUser u = new NhtUser();
        u.setFullName(fullName);
        u.setEmail(email);
        u.setPassword(password);  // thực tế nên mã hoá
        u.setPhone(phone);
        u.setRole(NhtUserRole.CUSTOMER);
        return userRepository.save(u);
    }

    @Override
    public NhtUser login(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password).orElse(null);
    }

    @Override
    public NhtUser getById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
}
