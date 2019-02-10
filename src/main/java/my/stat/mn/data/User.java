package my.stat.mn.data;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

/**
 *
 * @author naoki
 */
@Data
@Builder
public class User {
    long userId;
    String userName;
    String userHandle;
    String iconUrl;
    LocalDateTime createdAt;
    LocalDateTime modifiedAt;    
}
