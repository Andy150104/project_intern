package swp.internmanagement.internmanagement.payload.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class GetAllUserResponse {
    List<UserInSystemResponse> userList;
    private int pageNo;
    private int pageSize;
    private long totalItems;
    private int totalPages;
}
