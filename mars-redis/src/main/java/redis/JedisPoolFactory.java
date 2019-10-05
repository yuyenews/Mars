package redis;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mars.core.util.ConfigUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;

import java.util.ArrayList;
import java.util.List;

/**
 * jedisPool工厂，用于获取JedisPool对象
 */
public class JedisPoolFactory {

    private static  Logger logger = LoggerFactory.getLogger(JedisPoolFactory.class);// 日志

    private static int maxTotal = 2048;
    private static int maxIdle = 200;
    private static int minIdle = 2;
    private static int numTestsPerEvictionRun = 2048;
    private static int timeBetweenEvictionRunsMillis = 30000;
    private static int minEvictableIdleTimeMillis = -1;
    private static int softMinEvictableIdleTimeMillis = 10000;
    private static int maxWaitMillis = 10000;
    private static boolean testOnBorrow = true;
    private static boolean testWhileIdle = true;
    private static boolean testOnReturn = true;
    private static boolean jmxEnabled = true;
    private static String jmxNamePrefix = null;
    private static boolean blockWhenExhausted = true;

    private static JedisPoolConfig jedisPoolConfig;

    private static ShardedJedisPool shardedJedisPool;

    /**
     * 获取ShardedJedisPool对象
     * @return
     */
    public static ShardedJedisPool getShardedJedisPool() throws Exception {
        try{
            if(shardedJedisPool == null){
                loadJedisPoolConfigParams();
                initJedisPoolConfig();
                shardedJedisPool = new ShardedJedisPool(jedisPoolConfig,getJedisShardInfoList());
            }

            return shardedJedisPool;
        } catch (Exception e) {
            logger.error("获取JedisPool对象出错",e);
            throw  new Exception(e);
        }

    }

    /**
     * 初始化JedisPoolConfig
     */
    private static void initJedisPoolConfig(){
        jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(maxTotal);
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMinIdle(minIdle);
        jedisPoolConfig.setNumTestsPerEvictionRun(numTestsPerEvictionRun);
        jedisPoolConfig.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        jedisPoolConfig.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        jedisPoolConfig.setSoftMinEvictableIdleTimeMillis(softMinEvictableIdleTimeMillis);
        jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
        jedisPoolConfig.setTestOnBorrow(testOnBorrow);
        jedisPoolConfig.setTestWhileIdle(testWhileIdle);
        jedisPoolConfig.setTestOnReturn(testOnReturn);
        jedisPoolConfig.setJmxEnabled(jmxEnabled);
        if(jmxNamePrefix != null){
            jedisPoolConfig.setJmxNamePrefix(jmxNamePrefix);
        }
        jedisPoolConfig.setBlockWhenExhausted(blockWhenExhausted);
    }

    /**
     * 获取JedisShardInfoList
     * @return
     */
    private static List<JedisShardInfo>  getJedisShardInfoList(){
        JSONObject config = getConfig();
        JSONArray jedisShardInfos = config.getJSONArray("jedisShardInfos");

        List<JedisShardInfo> list = new ArrayList<>();
        for(int i=0; i<jedisShardInfos.size(); i++){

            JSONObject jsonObject = jedisShardInfos.getJSONObject(i);
            String host = jsonObject.getString("host");
            Integer port = jsonObject.getInteger("port");
            String password = jsonObject.getString("password");
            String name = jsonObject.getString("name");
            Object connectionTimeout = jsonObject.get("connectionTimeout");
            Object soTimeout = jsonObject.get("soTimeout");

            JedisShardInfo jedisShardInfo = new JedisShardInfo(host,port,name);
            jedisShardInfo.setPassword(password);
            if(connectionTimeout != null){
                jedisShardInfo.setConnectionTimeout(Integer.parseInt(connectionTimeout.toString()));
            }
            if(soTimeout != null){
                jedisShardInfo.setSoTimeout(Integer.parseInt(soTimeout.toString()));
            }

            list.add(jedisShardInfo);

        }
        return list;
    }

    /**
     * 从配置文件加载JedisPoolConfig的参数
     */
    private static void loadJedisPoolConfigParams(){
        JSONObject config = getConfig();

        Object mt = config.get("maxTotal");
        if(mt != null){
            maxTotal = Integer.parseInt(mt.toString());
        }

        Object md = config.get("maxIdle");
        if(md != null){
            maxIdle = Integer.parseInt(md.toString());
        }

        Object mi = config.get("minIdle");
        if(mi != null){
            minIdle = Integer.parseInt(mi.toString());
        }

        Object ntper = config.get("numTestsPerEvictionRun");
        if(ntper != null){
            numTestsPerEvictionRun = Integer.parseInt(ntper.toString());
        }

        Object tberm = config.get("timeBetweenEvictionRunsMillis");
        if(tberm != null){
            timeBetweenEvictionRunsMillis = Integer.parseInt(tberm.toString());
        }

        Object meitm = config.get("minEvictableIdleTimeMillis");
        if(meitm != null){
            minEvictableIdleTimeMillis = Integer.parseInt(meitm.toString());
        }

        Object smeitm = config.get("softMinEvictableIdleTimeMillis");
        if(smeitm != null){
            softMinEvictableIdleTimeMillis = Integer.parseInt(smeitm.toString());
        }

        Object mwm = config.get("maxWaitMillis");
        if(mwm != null){
            maxWaitMillis = Integer.parseInt(mwm.toString());
        }

        Object tob = config.get("testOnBorrow");
        if(tob != null){
            testOnBorrow = Boolean.parseBoolean(tob.toString());
        }

        Object twi = config.get("testWhileIdle");
        if(twi != null){
            testWhileIdle = Boolean.parseBoolean(twi.toString());
        }

        Object tor = config.get("testOnReturn");
        if(tor != null){
            testOnReturn = Boolean.parseBoolean(tor.toString());
        }

        Object jeb = config.get("jmxEnabled");
        if(jeb != null){
            jmxEnabled = Boolean.parseBoolean(jeb.toString());
        }

        Object jnp = config.get("jmxNamePrefix");
        if(jnp != null){
            jmxNamePrefix = jnp.toString();
        }

        Object bwe = config.get("blockWhenExhausted");
        if(bwe != null){
            blockWhenExhausted = Boolean.parseBoolean(bwe.toString());
        }
    }

    /**
     * 从配置文件中获取redis相关配置
     * @return
     */
    private static JSONObject getConfig(){
        JSONObject config = ConfigUtil.getConfig();
        Object obj = config.get("redis");
        if(obj != null){
            return (JSONObject)obj;
        }
        return null;
    }
}
