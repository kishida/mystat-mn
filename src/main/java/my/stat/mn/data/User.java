package my.stat.mn.data;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

/**
 *
 * @author naoki
 */
@Data
@Builder
public class User implements Serializable {
    long userId;
    String userName;
    String userHandle;
    String iconUrl;
    LocalDateTime createdAt;
    LocalDateTime modifiedAt;    
}
