package applet.idp;

import applet.sp.SPCredentials;
import net.shibboleth.utilities.java.support.resolver.CriteriaSet;
import net.shibboleth.utilities.java.support.resolver.Criterion;
import net.shibboleth.utilities.java.support.resolver.ResolverException;
import org.opensaml.core.criterion.EntityIdCriterion;
import org.opensaml.security.credential.Credential;
import org.opensaml.security.credential.impl.KeyStoreCredentialResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.security.KeyStore;
import java.util.HashMap;
import java.util.Map;

/**
 * @Classname IdpCredentials
 * @Description TODO idp 获取证书内容讯息
 * @Date 2022/7/25 12:57
 * @Created by CoffeeAndIce
 */
public class IdpCredentials {
    private static Logger logger = LoggerFactory.getLogger(SPCredentials.class);
    private static final String KEY_STORE_PASSWORD = "ringo";
    private static final String KEY_STORE_ENTRY_PASSWORD = "ringo";
    private static final String KEY_STORE_PATH = "/coffeeandice.jks";
    private static final String KEY_ENTRY_ID = "coffeeandice";

    private static final Credential credential;

    static {
        try {
            KeyStore keystore = readKeystoreFromFile(KEY_STORE_PATH, KEY_STORE_PASSWORD);
            Map<String, String> passwordMap = new HashMap<String, String>();
            passwordMap.put(KEY_ENTRY_ID, KEY_STORE_ENTRY_PASSWORD);
            KeyStoreCredentialResolver resolver = new KeyStoreCredentialResolver(keystore, passwordMap);
            Criterion criterion = new EntityIdCriterion(KEY_ENTRY_ID);
            CriteriaSet criteriaSet = new CriteriaSet();
            criteriaSet.add(criterion);

            credential = resolver.resolveSingle(criteriaSet);

        } catch (ResolverException e) {
            throw new RuntimeException("Something went wrong reading credentials", e);
        }
    }

    private static KeyStore readKeystoreFromFile(String pathToKeyStore, String keyStorePassword) {
        try {
            KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
            InputStream inputStream = SPCredentials.class.getResourceAsStream(pathToKeyStore);
            keystore.load(inputStream, keyStorePassword.toCharArray());
            inputStream.close();
            return keystore;
        } catch (Exception e) {
            throw new RuntimeException("Something went wrong reading keystore", e);
        }
    }

    public static Credential getCredential() {
        logger.info("相应的凭证值：{}", credential);
        return credential;
    }
}
