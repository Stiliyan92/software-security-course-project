package Authentication;

import groovy.json.internal.ReaderCharacterSource;
import org.apache.commons.codec.binary.Hex;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

/**
 * Created by stili on 11/28/2016.
 */
public class AccessController {


    private static AccessController access_controller = null;
    private static String secret_key;
    private DBConnector db;
    private Authenticator authenticator;
    private static final int VALID_PERIOD = 1000 * 60 * 60 * 2; // 2 hours

    private AccessController() {
        db = DBConnector.getInstance();
        this.secret_key = NonceGenerator.getInstance().genNonce();
        authenticator = Authenticator.getInstance();

    }

    public static AccessController getInstance() {
        if (access_controller == null) {
            access_controller = new AccessController();
        }
        return access_controller;
    }

    String makeKey(String Owner, String Grantee, int resource, int operation) {
        String key = null;
        Account own = authenticator.checkifUserExists(Owner);
        Account grantee = authenticator.checkifUserExists(Grantee);
        if (own != null && grantee != null) {
            int ownerID = own.getId();
            int granteeId = grantee.getId();
            if (this.checkGranteePermissions(own, resource)) {
                Capability cap = new Capability(ownerID, granteeId, resource, operation, VALID_PERIOD, secret_key);
                try {
                    key = this.serialize(cap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                addCapability(cap);
            }
        }
        return key;
    }


    // resources id are equal to users id.
    // only owner can grant permissions
    private boolean checkGranteePermissions(Account acc, int resource) {
        if (acc.getId() == resource) {
            return true;
        } else {
            return false;
        }

    }

    //check if capability provides needed permissions
    boolean checkPermission(Account principal, Capability cap, int resourceId, int operationId) {
        Tuple<Integer, Integer> permission = cap.getPermission();
        if (permission.x == resourceId && permission.y == operationId){
            return true;
        }
        else{
            return false;
        }
    }


    //TO DO
    // add capability to db and current active tokens list
    private void addCapability(Capability newOne) {

    }

    // check if user1 and user2 are friends
    boolean checkFriendship(Integer user1, Integer user2) {
        String[] queryFormaters = {user1.toString(), user2.toString()};
        String accountQuery = Consts.ARE_FRIENDS.format(queryFormaters);
        try {
            db.updateDB(accountQuery);
            ResultSet rs = db.getDB(accountQuery);
            if (rs.isBeforeFirst()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    // returns list of Capability objects from the http request
    public List<Capability> getCapabilites(HttpServletRequest request) {
        HttpSession session = request.getSession();
        ArrayList<String> serialized_capabilities = (ArrayList) session.getAttribute("capabilities");
        ArrayList<Capability> capabilities = new ArrayList<Capability>();
        for (String cap : serialized_capabilities) {
            capabilities.add(deserialize(cap));
        }
        return capabilities;
    }


    // return permissions from the list of capabilities "caps"
    public List<Tuple> getPermission(List<Capability> caps, String uname, String resourceId) {
        List<Tuple> permissions = new ArrayList<Tuple>();
        for (Capability cap : caps) {

        }
        return null;
    }


    //serialize the capability "cap" to a String which can be deserialized with this class
    public String serialize(Capability cap) throws Exception {
        String header = Base64.getEncoder().encodeToString(Consts.HEADER.getBytes("utf-8"));
        JSONObject payload = cap.toJSON();
        String data = Base64.getEncoder().encodeToString(payload.toString().getBytes("utf-8"));
        String signature = encodeHMAC(header + "." + data, secret_key);
        signature = Base64.getEncoder().encodeToString(signature.getBytes("utf-8"));
        String jwt = header + "." + data + "." + signature;
        return jwt;
    }

    //create capability object from the serialized String serialized_capability
    public Capability deserialize(String serialized_capability) {
        List<String> partitioned = Arrays.asList(serialized_capability.split("."));
        try {
            if (validate_capability(partitioned)) {
                JSONObject json_capability = null;
                json_capability = new JSONObject(partitioned.get(1));
                int ownerId = json_capability.getInt("iss");
                int granteeId = json_capability.getInt("sub");
                long issuedAt = json_capability.getLong("iat");
                int resourceId = json_capability.getInt("res");
                int operationId = json_capability.getInt("opr");
                Capability cap = new Capability(ownerId, granteeId, resourceId, operationId, VALID_PERIOD, secret_key);
                cap.setIssuedDate(issuedAt);
                return cap;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //validate that capability was issued by this server
    //check if signature differs and if the is the same type of jwt
    private boolean validate_capability(List<String> capability) throws Exception {
        String encoded_hash = encodeHMAC(capability.get(0) + "." + capability.get(1), secret_key);
        String signature = Base64.getEncoder().encodeToString(encoded_hash.getBytes("utf-8"));
        if (!signature.equals(capability.get(2))) {
            System.out.println("Compromised capability: " + capability.get(2));
            return false;
        }
        JSONObject header = new JSONObject(capability.get(0));
        if (header.get("alg").equals("HS256") && header.get("typ").equals("JWT")) {
            return true;
        } else {
            System.out.println("Capability not issued according to server policy, discarding");
            return false;
        }
    }

    //encodes "data" parameter with HMAC SHA256 algorithm  using parameter "key" as key
    public static String encodeHMAC(String key, String data) throws Exception {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
        sha256_HMAC.init(secret_key);
        return Hex.encodeHexString(sha256_HMAC.doFinal(data.getBytes("UTF-8")));
    }
}

