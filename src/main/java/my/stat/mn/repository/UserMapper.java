package my.stat.mn.repository;

import java.util.List;
import java.util.Optional;
import my.stat.mn.data.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

/**
 *
 * @author naoki
 */
public interface UserMapper {
    @Select("select * from USERS")
    List<User> findAll();

    @Select("select * from USERS where USER_ID=#{id}")
    Optional<User> findById(long id);    
    
    @Select("select * from USERS where USER_HANDLE=#{handle}")
    Optional<User> findByHandle(String handle);
    
    @Insert("insert into USERS(user_name, user_handle) values (#{userName}, #{userHandle})")
    @Options(useGeneratedKeys = true)
    void insert(User u);
}
