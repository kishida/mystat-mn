package my.stat.mn.data;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;

/**
 *
 * @author naoki
 */
@Data
@Builder
public class Status {
    
    ObjectId oid;
    String userHandle;
    String text;
    LocalDateTime createdAt;
    
}
