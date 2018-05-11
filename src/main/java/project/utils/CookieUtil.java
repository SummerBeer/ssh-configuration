package project.utils;

import javax.servlet.http.Cookie;
import java.util.HashMap;
import java.util.Map;

public class CookieUtil {

    public static Map<String, String> getCookieMap(Cookie[] cookies){

        if(cookies != null){
            Map cookieMap = new HashMap<String, String>();

            for(Cookie c : cookies){
                String key = c.getName();
                String value = c.getValue();
                cookieMap.put(key, value);
            }

            return cookieMap;
        }
        else{

            return null;
        }
    }
}
