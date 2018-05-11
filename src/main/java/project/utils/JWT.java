package project.utils;

import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class JWT {

    private static Logger logger = LoggerFactory.getLogger(JWT.class);

    private static final String SECRET = "shdsioafgs9aanwr214*@%$jksa&%";
    private static final String EXP = "exp";
    private static final String PAYLOAD = "payload";

    /**
     * sign
     *
     * @param login
     * @param maxAge
     * @return string ; signed token
     */
    public static<T> String sign(T login, long maxAge){
        try{
            final JWTSigner signer = new JWTSigner(SECRET);
            final Map<String, Object> claims = new HashMap<String, Object>();
            ObjectMapper mapper = new ObjectMapper();
            String jsonString = mapper.writeValueAsString(login);

            claims.put(PAYLOAD, jsonString);
            claims.put(EXP, System.currentTimeMillis() + maxAge);

            return signer.sign(claims);
        }
        catch(Exception e){
            logger.error("JWT sign exception");
            return null;
        }
    }

    /**
     * unsign
     *
     * @param jwt
     * @param classT
     * @return object ; unsigned token
     */
    public static<T> T unsign(String jwt, Class<T> classT){
        final JWTVerifier verifier = new JWTVerifier(SECRET);
        try{
            final Map<String, Object> claims = verifier.verify(jwt);
            if(claims.containsKey(EXP) && claims.containsKey(PAYLOAD)){
                long exp = (long)claims.get(EXP);
                long currentTime = System.currentTimeMillis();
                if(exp > currentTime){
                    String json = (String)claims.get(PAYLOAD);
                    ObjectMapper mapper = new ObjectMapper();
                    return mapper.readValue(json, classT);
                }
                else{
                    logger.warn("unsign: token expired");
                    return  null;
                }
            }
            else{
                logger.warn("unsign: invalid sign");
                return null;
            }
        }
        catch(Exception e){
            logger.warn("unsign exception");
            return null;
        }
    }
}
