package my.stat.mn.data;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

/**
 *
 * @author naoki
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Status {
    ObjectId id;
    String userHandle;
    String text;
    LocalDateTime createdAt;
    
}
