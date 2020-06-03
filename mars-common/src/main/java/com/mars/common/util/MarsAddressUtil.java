package com.mars.common.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 * 工具类
 */
public class MarsAddressUtil {

    /**
     * 本机局域网IP
     */
    private static String ip;

    /**
     * 获取本机在局域网的IP
     * @return ip
     * @throws Exception 异常
     */
    public static String getLocalIp() throws Exception {
        if(ip == null){
            ip = MarsAddressUtil.getLocalHostLANAddress().getHostAddress();
        }
        return ip;
    }

    /**
     * 获取端口号
     * @return 端口
     */
    public static int getPort() {
        return MarsConfiguration.getConfig().port();
    }

    /**
     * 获取本服务的IP
     * @return ip
     * @throws UnknownHostException 异常
     */
    public static InetAddress getLocalHostLANAddress() throws UnknownHostException {
        try {
            // 遍历所有的网络接口
            InetAddress candidateAddress = getLocalHost();
            if(candidateAddress != null){
                return candidateAddress;
            }

            // 如果没有发现 non-loopback地址.只能用最次选的方案
            InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
            if (jdkSuppliedAddress == null) {
                throw new UnknownHostException("The JDK InetAddress.getLocalHost() method unexpectedly returned null.");
            }
            return jdkSuppliedAddress;
        } catch (Exception e) {
            UnknownHostException unknownHostException = new UnknownHostException(
                    "Failed to determine LAN address: " + e);
            unknownHostException.initCause(e);
            throw unknownHostException;
        }
    }

    /**
     * 获取本服务的IP
     * @return ip
     * @throws Exception 异常
     */
    private static InetAddress getLocalHost() throws Exception {
        InetAddress candidateAddress = null;

        Enumeration iFaces = NetworkInterface.getNetworkInterfaces();

        while (iFaces.hasMoreElements()) {
            NetworkInterface iFace = (NetworkInterface) iFaces.nextElement();

            Enumeration enumeration= iFace.getInetAddresses();
            // 在所有的接口下再遍历IP
            while (enumeration.hasMoreElements()) {
                InetAddress inetAddress = (InetAddress) enumeration.nextElement();
                // 排除loopback类型地址
                if (!inetAddress.isLoopbackAddress()) {
                    if (inetAddress.isSiteLocalAddress()) {
                        // 如果是site-local地址，就是它了
                        return inetAddress;
                    } else if (candidateAddress == null) {
                        // site-local类型的地址未被发现，先记录候选地址
                        candidateAddress = inetAddress;
                    }
                }
            }
        }
        if (candidateAddress != null) {
            return candidateAddress;
        }
        return null;
    }
}
