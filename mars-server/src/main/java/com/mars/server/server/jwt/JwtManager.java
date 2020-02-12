package com.mars.server.server.jwt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.mars.core.util.MarsConfiguration;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT管理类
 */
public class JwtManager {
    /**
     * token秘钥
     */
    private final String SECRET = "Mars-java-cloud-config-no1";
    /**
     * token 过期时间: 1天
     */
    private final int calendarField = Calendar.SECOND;
    private int calendarInterval = 86400;

    private static JwtManager jwtManager = new JwtManager();
    private JwtManager(){
        loadCalendarInterval();
    }

    public static JwtManager getJwtManager(){
        return jwtManager;
    }

    /**
     * 加载配置文件中的jwt失效时间
     */
    private void loadCalendarInterval(){
        calendarInterval =  MarsConfiguration.getConfig().jwtTime();
    }

    /**
     * WT生成Token.
     * @param obj
     * @return str
     */
    public String createToken(Object obj) {
        Date iatDate = new Date();
        // expire time
        Calendar nowTime = Calendar.getInstance();
        nowTime.add(calendarField, calendarInterval);
        Date expiresDate = nowTime.getTime();

        // header Map
        Map<String, Object> map = new HashMap<>();
        map.put("alg", "HS256");
        map.put("typ", "JWT");

        JWTCreator.Builder builder = JWT.create().withHeader(map);
        JSONObject json = JSONObject.parseObject(JSON.toJSONString(obj));

        for (String key : json.keySet()) {
            builder.withClaim(key, json.get(key).toString());
        }

        builder.withIssuedAt(iatDate); // sign time
        builder.withExpiresAt(expiresDate); // expire time
        String token = builder.sign(Algorithm.HMAC256(SECRET)); // signature

        return token;
    }

    /**
     * 校验Token
     *
     * @param token
     * @return map
     */
    public boolean verifyToken(String token) {
        Map<String, Claim> claims = decryptToken(token);
        return claims != null;
    }

    /**
     * 解密Token
     *
     * @param token
     * @return map
     */
    private Map<String, Claim> decryptToken(String token) {
        DecodedJWT jwt = null;
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
            jwt = verifier.verify(token);
            return jwt.getClaims();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 根据Token获取存进去的对象
     * @param token
     * @param cls
     * @param <T>
     * @return obj
     */
    public <T> T  getObject(String token,Class<T> cls) {
        JSONObject json = new JSONObject();
        try {
            Map<String, Claim> claims = decryptToken(token);
            if(claims == null || claims.isEmpty()){
                return null;
            }
            for (String key : claims.keySet()) {
                json.put(key, claims.get(key).asString());
            }
            return json.toJavaObject(cls);
        } catch (Exception e) {
            return null;
        }
    }
}
