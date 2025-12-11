package K23cnt3.nht._2.dto;

import lombok.Data;

@Data
public class LoginDTO {
    private String email;
    private String matKhau;
    private boolean rememberMe;

    public LoginDTO() {}

    public LoginDTO(String email, String matKhau) {
        this.email = email;
        this.matKhau = matKhau;
    }
}