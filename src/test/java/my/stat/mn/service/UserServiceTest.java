/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.stat.mn.service;

import io.minio.MinioClient;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author naoki
 */
public class UserServiceTest {
    
    public UserServiceTest() {
    }

    @Test
    @Ignore
    public void iconUrl() throws Exception{
        MinioClient client = new MinioClient(
                "http://localhost:9000", "mystat", "naokimystat");
        UserService service = new UserService();
        service.minio = client;
        System.out.println(service.iconUrl("ab"));
    }
    
}
