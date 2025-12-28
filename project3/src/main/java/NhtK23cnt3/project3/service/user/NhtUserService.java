package NhtK23cnt3.project3.service.user;

import NhtK23cnt3.project3.entity.user.NhtUser;

public interface NhtUserService {

    NhtUser registerCustomer(String fullName, String email, String password, String phone);

    NhtUser login(String email, String password);

    NhtUser getById(Long id);
}
