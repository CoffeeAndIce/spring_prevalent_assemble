package coffeeandice;

import com.coffeeandice.config.AspectConfig;
import com.coffeeandice.target.TargetService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author : CoffeeAndIce
 * @Todo:
 */

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = AspectConfig.class)
public class AopTest {

    @Autowired
    private TargetService targetService;

    @Test
    public void test() {
        targetService.testAdvice("fuck");
        targetService.testExceptAdvice("except ready");

    }
}
