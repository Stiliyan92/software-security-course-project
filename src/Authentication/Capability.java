package Authentication;


import com.intellij.vcs.log.Hash;
import org.apache.commons.codec.binary.Hex;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kohsuke.rngom.parse.host.Base;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * Created by stili on 11/29/2016.
 */

public class Capability {


    public Capability(JSONObject json_capability) {
        try {
            this.ownerId = json_capability.getInt("iss");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject toJSON() {
        JSONObject payload = new JSONObject();
        try {
            payload.put("iss", this.ownerId);
            payload.put("sub", this.granteeId);
            payload.put("iat", this.issuedAt);
            payload.put("exp", this.issuedAt.getTime() + this.valid_period);
            payload.put("res", this.permission.x);
            payload.put("opr", this.permission.y);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return payload;
    }
    public int getOwnerId() {
        return ownerId;
    }

    public int getGranteeId() {
        return granteeId;
    }

    public Tuple<Integer, Integer> getPermission() {
        return permission;
    }

    public Date getissuedAt() {
        return issuedAt;
    }
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private int ownerId;
    private int granteeId;
    private Tuple<Integer, Integer> permission;
    private Date issuedAt;
    //nonce = "number used once" or "number once."
    private String nonce;
    private int valid_period;

    public Capability(int ownerId, int granteeId, int resourceId, int operationId, int valid_period, String nonce) {
        this.ownerId = ownerId;
        this.granteeId = granteeId;
        this.permission = new Tuple<Integer, Integer>(resourceId, operationId);
        this.issuedAt = new Date();
        this.valid_period = valid_period;
       // this.nonce = NonceGenerator.getInstance().genNonce();
        this.nonce = nonce;
    }


    public void setIssuedDate(long issuedAt){
       this.issuedAt = new Date(issuedAt);
    }

    public boolean isExpired(){
        Date now = new Date();
        if(now.getTime() - this.issuedAt.getTime() > this.valid_period){
            return true;
        }
        else{
            return false;
        }
    }


}