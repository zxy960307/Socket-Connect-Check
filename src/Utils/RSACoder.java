package Utils;


import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

import common.Constant;
import constant.PropertiesFilePath;

public class RSACoder {
	//非对称密钥算法
    public static final String KEY_ALGORITHM = "RSA";


    /**
     * 密钥长度，DH算法的默认密钥长度是1024
     * 密钥长度必须是64的倍数，在512到65536位之间
     */
    private static final int KEY_SIZE = 512;
    //公钥
    private static final String PUBLIC_KEY = "RSAPublicKey";

    //私钥
    private static final String PRIVATE_KEY = "RSAPrivateKey";

    /**
     * 初始化密钥对
     *
     * @return Map 甲方密钥的Map
     */
    public static Map<String, Object> initKey() throws Exception {
        //实例化密钥生成器
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        //初始化密钥生成器
        keyPairGenerator.initialize(KEY_SIZE);
        //生成密钥对
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        //甲方公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        //甲方私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        //将密钥存储在map中
        Map<String, Object> keyMap = new HashMap<String, Object>();
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);
        return keyMap;

    }


    /**
     * 私钥加密
     *
     * @param data 待加密数据
     * @param key       密钥
     * @return byte[] 加密数据
     */
    public static byte[] encryptByPrivateKey(byte[] data, byte[] key) throws Exception {

        //取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(key);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        //生成私钥
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
        //数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }

    /**
     * 公钥加密
     *
     * @param data 待加密数据
     * @param key       密钥
     * @return byte[] 加密数据
     */
    public static byte[] encryptByPublicKey(byte[] data, byte[] key) throws Exception {

        //实例化密钥工厂
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        //初始化公钥
        //密钥材料转换
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(key);
        //产生公钥
        PublicKey pubKey = keyFactory.generatePublic(x509KeySpec);

        //数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        return cipher.doFinal(data);
    }

    /**
     * 私钥解密
     *
     * @param data 待解密数据
     * @param key  密钥
     * @return byte[] 解密数据
     */
    public static byte[] decryptByPrivateKey(byte[] data, byte[] key) throws Exception {
        //取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(key);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        //生成私钥
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
        //数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }

    /**
     * 公钥解密
     *
     * @param data 待解密数据
     * @param key  密钥
     * @return byte[] 解密数据
     */
    public static byte[] decryptByPublicKey(byte[] data, byte[] key) throws Exception {

        //实例化密钥工厂
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        //初始化公钥
        //密钥材料转换
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(key);
        //产生公钥
        PublicKey pubKey = keyFactory.generatePublic(x509KeySpec);
        //数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, pubKey);
        return cipher.doFinal(data);
    }

    /**
     * 取得私钥
     *
     * @param keyMap 密钥map
     * @return byte[] 私钥
     */
    public static byte[] getPrivateKey(Map<String, Object> keyMap) {
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        return key.getEncoded();
    }

    /**
     * 取得公钥
     *
     * @param keyMap 密钥map
     * @return byte[] 公钥
     */
    public static byte[] getPublicKey(Map<String, Object> keyMap) throws Exception {
        Key key = (Key) keyMap.get(PUBLIC_KEY);
        return key.getEncoded();
    }

/**
 * 生成一对公钥和私钥，并写入各自文件
 * @param publicKeyFilePath 公钥路径
 * @param privateKeyFilePath 私钥路径
 * @return 正常情况下返回true
 */
    public static boolean getPublicAndPrivateKkeyIntoFile(String publicKeyFilePath,String privateKeyFilePath) {
    	Map map;
    	byte[] publicKeyBytes;
    	byte[] privateKeyBytes;
    	try {
    		map = RSACoder.initKey();
    		publicKeyBytes = RSACoder.getPublicKey(map);
    		privateKeyBytes = RSACoder.getPrivateKey(map);
    	}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
    	String publicKey = Base64.getEncoder().encodeToString(publicKeyBytes);
    	String privateKey = Base64.getEncoder().encodeToString(privateKeyBytes);
    	
    	//写入公钥
    	int publicKeyStart = Integer.parseInt(PropertiesUtils.readValue(publicKeyFilePath, "start"));
    	int publicKeySize = Integer.parseInt(PropertiesUtils.readValue(publicKeyFilePath, "size"));
    	int num = publicKeySize+publicKeyStart;
    	PropertiesUtils.writeProperties(new Integer(num).toString(), publicKey, publicKeyFilePath);
    	
    	//写入私钥
    	PropertiesUtils.writeProperties(new Integer(num).toString(), privateKey, privateKeyFilePath);
    	
    	//公钥size自增
    	int newSize = publicKeySize+1;
    	PropertiesUtils.writeProperties("size", new Integer(newSize).toString(), publicKeyFilePath);
    	return true;
    }
    
    /**
     * 使用对应公钥解密该字符串
     * @param str 加密后的字符串
     * @param num 对应公钥序号
     * @return 正常处理返回解密后的key，否则返回""
     */
    public static String decipheringPrivateStr(String str,int num) {
    	
    	byte[] realStrBytes;
    	byte[] publicKeyBytes = Base64.getDecoder()
    			.decode(PropertiesUtils.readValue(PropertiesFilePath.publicKeyFilePath, new Integer(num).toString()));
    	try {   		
    		realStrBytes =RSACoder.decryptByPublicKey(Base64.getDecoder().decode(str),publicKeyBytes);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}  	
    	return  new String(realStrBytes);
    }
    
    /**
     * 使用对应私钥加密字符串
     * @param str
     * @param num
     * @return 加密正常返回对应字符串，否则返回""
     */
    public static String encryptByPrivateKeyStr(String str,int num) {
    	String privateKeyStr = PropertiesUtils.readValue(PropertiesFilePath.privateKeyFilePath, new Integer(num).toString());
    	byte[] privateKey = Base64.getDecoder().decode(privateKeyStr);
    	byte[] code;
		try {
			code = RSACoder.encryptByPrivateKey(str.getBytes(), privateKey);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
    	return Base64.getEncoder().encodeToString(code);
    }
    
    /**
     * 根据publickey文件中现有数目生成关于公钥序号的随机数
     * @return 随机数
     */
    public static int getPublicKeyRandomSize() {
    	int start = Integer.parseInt(PropertiesUtils.readValue(PropertiesFilePath.publicKeyFilePath, "start"));
    	int end = start+Integer.parseInt(PropertiesUtils.readValue(PropertiesFilePath.publicKeyFilePath, "size"))-1;
    	return start+(int)(Math.random()*(end-start+1));
    }
    
    public static void main(String[] args) throws Exception {
//        //初始化密钥
//        //生成密钥对
//        Map<String, Object> keyMap1 = RSACoder.initKey();
//        //公钥
//        byte[] publicKey1 = RSACoder.getPublicKey(keyMap1);
//
//        //私钥
//        byte[] privateKey1 = RSACoder.getPrivateKey(keyMap1);
//        
//        
//        Map<String, Object> keyMap2 = RSACoder.initKey();
//        //公钥
//        byte[] publicKey2= RSACoder.getPublicKey(keyMap2);
//
//        //私钥
//        byte[] privateKey2 = RSACoder.getPrivateKey(keyMap2);
//        
//        System.out.println("公钥：/n" + Base64.getEncoder().encodeToString(publicKey1));
//        System.out.println("私钥：/n" + Base64.getEncoder().encodeToString(privateKey1));
//        //System.out.println(Base64.getEncoder().encodeToString(Base64.getDecoder().decode(Base64.getEncoder().encodeToString(privateKey)))) ;
//
//        System.out.println("================密钥对构造完毕,甲方将公钥公布给乙方，开始进行加密数据的传输=============");
//        String str = "RSA密码交换算法";
//        System.out.println("/n===========甲方向乙方发送加密数据==============");
//        System.out.println("原文:" + str);
//        //甲方进行数据的加密
//        byte[] code1 = RSACoder.encryptByPrivateKey(str.getBytes(), privateKey1);
//        System.out.println("加密后的数据：" + Base64.getEncoder().encodeToString(code1));
//        //System.out.println("===========乙方使用甲方提供的公钥对数据进行解密==============");
//        //乙方进行数据的解密
//        byte[] decode1 = RSACoder.decryptByPublicKey(code1, publicKey1);
//        System.out.println("乙方解密后的数据：" + new String(decode1) + "/n/n");
//
//        System.out.println("===========反向进行操作，乙方向甲方发送数据==============/n/n");
//
//        str = "乙方向甲方发送数据RSA算法";
//
//        System.out.println("原文:" + str);
//
//        //乙方使用公钥对数据进行加密
//        byte[] code2 = RSACoder.encryptByPublicKey(str.getBytes(), publicKey1);
//        System.out.println("===========乙方使用公钥对数据进行加密==============");
//        System.out.println("加密后的数据：" + Base64.getEncoder().encodeToString(code2));
//
//        System.out.println("=============乙方将数据传送给甲方======================");
//        System.out.println("===========甲方使用私钥对数据进行解密==============");
//
//        //甲方使用私钥对数据进行解密
//        byte[] decode2 = RSACoder.decryptByPrivateKey(code2, privateKey1);
//
//        System.out.println("甲方解密后的数据：" + new String(decode2));
    	
//    	for(int i = 0;i<19;i++) {
//        	System.out.println(RSACoder.getPublicAndPrivateKkeyIntoFile(PropertiesFilePath.publicKeyFilePath,
//        			PropertiesFilePath.privateKeyFilePath));
//    	}

    	
//    	String privateKeyStr = PropertiesUtils.readValue(PropertiesFilePath.privateKeyFilePath, new Integer(10).toString());
//    	System.out.println("文件私钥：/n" + privateKeyStr);
//    	byte[] privateKey = Base64.getDecoder().decode(privateKeyStr);
//    	System.out.println("私钥：/n" + Base64.getEncoder().encodeToString(privateKey));
//    	String str = "RSA密码交换算法";
//    	byte[] code1 = RSACoder.encryptByPrivateKey(str.getBytes(), privateKey);
    	
//    	String str2 = RSACoder.encryptByPublicKeyStr("我喜欢你", 10);
//    	
//    	System.out.println(RSACoder.decipheringPrivateStr(str2,10));   	
    }  
}
