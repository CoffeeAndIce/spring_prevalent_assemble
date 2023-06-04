import java.security.Provider;
import java.security.Security;

/**
 * @author : CoffeeAndIce
 * @Todo:
 * @Date: 2022/07/24
 */

public class test {
    public static void main(String[] args) {
        for (Provider jceProvider : Security.getProviders()) {
            System.out.println(jceProvider.getInfo());
        }
    }
}
