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
    protected static ShardedJedisPool getShardedJedisPool() throws Exception {
        try{
            if(shardedJedisPool == null){
                loadJedisPoolConfigParams();
                initJedisPoolConfig();
                shardedJedisPool = new ShardedJedisPool(jedisPoolConfig,getJedisShardInfoList());
            }

            return shardedJedisPool;
        } catch (Exception e) {
            logger.error("获取JedisPool对象出错",e);
            throw e;
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
    private static List<JedisShardInfo>  getJedisShardInfoList() throws Exception {

        JSONArray shardInfos = getJedisShardInfos();

        List<JedisShardInfo> list = new ArrayList<>();
        for(int i=0; i<shardInfos.size(); i++){

            JSONObject jsonObject = shardInfos.getJSONObject(i);
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

        Integer mt = config.getInteger("maxTotal");
        if(mt != null){
            maxTotal = mt;
        }

        Integer md = config.getInteger("maxIdle");
        if(md != null){
            maxIdle = md;
        }

        Integer mi = config.getInteger("minIdle");
        if(mi != null){
            minIdle = mi;
        }

        Integer ntper = config.getInteger("numTestsPerEvictionRun");
        if(ntper != null){
            numTestsPerEvictionRun = ntper;
        }

        Integer tberm = config.getInteger("timeBetweenEvictionRunsMillis");
        if(tberm != null){
            timeBetweenEvictionRunsMillis = tberm;
        }

        Integer meitm = config.getInteger("minEvictableIdleTimeMillis");
        if(meitm != null){
            minEvictableIdleTimeMillis = meitm;
        }

        Integer smeitm = config.getInteger("softMinEvictableIdleTimeMillis");
        if(smeitm != null){
            softMinEvictableIdleTimeMillis = smeitm;
        }

        Integer mwm = config.getInteger("maxWaitMillis");
        if(mwm != null){
            maxWaitMillis = mwm;
        }

        Boolean tob = config.getBoolean("testOnBorrow");
        if(tob != null){
            testOnBorrow = tob;
        }

        Boolean twi = config.getBoolean("testWhileIdle");
        if(twi != null){
            testWhileIdle = twi;
        }

        Boolean tor = config.getBoolean("testOnReturn");
        if(tor != null){
            testOnReturn = tor;
        }

        Boolean jeb = config.getBoolean("jmxEnabled");
        if(jeb != null){
            jmxEnabled = jeb;
        }

        String jnp = config.getString("jmxNamePrefix");
        if(jnp != null){
            jmxNamePrefix = jnp;
        }

        Boolean bwe = config.getBoolean("blockWhenExhausted");
        if(bwe != null){
            blockWhenExhausted = bwe;
        }
    }

    /**
     * 从配置文件中获取redis相关配置
     * @return
     */
    private static JSONObject getConfig() {
        return ConfigUtil.getConfig().getJSONObject("redis");
    }

    /**
     * 获取连接信息
     * @return 连接信息
     * @throws Exception 异常
     */
    private static JSONArray getJedisShardInfos() throws Exception {
        JSONArray jedisShardInfos = new JSONArray();

        Object shardInfos = getConfig().get("jedisShardInfos");
        if(shardInfos == null){
            throw new Exception("jedisShardInfos配置不正确");
        }

        if(shardInfos instanceof JSONArray){
            jedisShardInfos = (JSONArray)shardInfos;
        } else {
            jedisShardInfos.add(shardInfos);
        }
        return jedisShardInfos;
    }
}
