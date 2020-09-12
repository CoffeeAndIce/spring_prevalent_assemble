import com.coffeeandice.entity.User;
import com.coffeeandice.services.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;


/**
* author : CoffeeAndIce
*/
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(value = "classpath:springApplication.xml")
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @Test
    public void getUser() {
        List<User> user = userService.getUser();

        System.out.println(user.get(0).getName());

        System.out.println(user.get(0).getNo());

    }
}


