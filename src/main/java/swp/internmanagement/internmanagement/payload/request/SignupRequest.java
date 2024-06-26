package swp.internmanagement.internmanagement.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequest {
    private int jobApplicationId;
    private String fullName;
    private int companyId;
    private String email;
    private String role;
}
