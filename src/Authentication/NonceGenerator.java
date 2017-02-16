package Authentication;

/**
 * Created by stili on 12/1/2016.
 */
import sun.security.krb5.internal.crypto.Nonce;

import java.security.SecureRandom;
import java.math.BigInteger;
import java.util.ArrayList;

public final class NonceGenerator {
        private SecureRandom random;
        private static NonceGenerator generator = null;

    private NonceGenerator() {
        random = new SecureRandom();
            }

    /* Static 'instance' method */
    public static NonceGenerator getInstance( ) {
        if (generator == null){
          generator = new NonceGenerator();
        }
        return generator;
    }
        public String genNonce() {
            return new BigInteger(130, random).toString(32);
        }
    }


