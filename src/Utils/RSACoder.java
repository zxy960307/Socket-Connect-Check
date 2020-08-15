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
	//�ǶԳ���Կ�㷨
    public static final String KEY_ALGORITHM = "RSA";


    /**
     * ��Կ���ȣ�DH�㷨��Ĭ����Կ������1024
     * ��Կ���ȱ�����64�ı�������512��65536λ֮��
     */
    private static final int KEY_SIZE = 512;
    //��Կ
    private static final String PUBLIC_KEY = "RSAPublicKey";

    //˽Կ
    private static final String PRIVATE_KEY = "RSAPrivateKey";

    /**
     * ��ʼ����Կ��
     *
     * @return Map �׷���Կ��Map
     */
    public static Map<String, Object> initKey() throws Exception {
        //ʵ������Կ������
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        //��ʼ����Կ������
        keyPairGenerator.initialize(KEY_SIZE);
        //������Կ��
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        //�׷���Կ
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        //�׷�˽Կ
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        //����Կ�洢��map��
        Map<String, Object> keyMap = new HashMap<String, Object>();
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);
        return keyMap;

    }


    /**
     * ˽Կ����
     *
     * @param data ����������
     * @param key       ��Կ
     * @return byte[] ��������
     */
    public static byte[] encryptByPrivateKey(byte[] data, byte[] key) throws Exception {

        //ȡ��˽Կ
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(key);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        //����˽Կ
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
        //���ݼ���
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }

    /**
     * ��Կ����
     *
     * @param data ����������
     * @param key       ��Կ
     * @return byte[] ��������
     */
    public static byte[] encryptByPublicKey(byte[] data, byte[] key) throws Exception {

        //ʵ������Կ����
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        //��ʼ����Կ
        //��Կ����ת��
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(key);
        //������Կ
        PublicKey pubKey = keyFactory.generatePublic(x509KeySpec);

        //���ݼ���
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        return cipher.doFinal(data);
    }

    /**
     * ˽Կ����
     *
     * @param data ����������
     * @param key  ��Կ
     * @return byte[] ��������
     */
    public static byte[] decryptByPrivateKey(byte[] data, byte[] key) throws Exception {
        //ȡ��˽Կ
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(key);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        //����˽Կ
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
        //���ݽ���
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }

    /**
     * ��Կ����
     *
     * @param data ����������
     * @param key  ��Կ
     * @return byte[] ��������
     */
    public static byte[] decryptByPublicKey(byte[] data, byte[] key) throws Exception {

        //ʵ������Կ����
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        //��ʼ����Կ
        //��Կ����ת��
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(key);
        //������Կ
        PublicKey pubKey = keyFactory.generatePublic(x509KeySpec);
        //���ݽ���
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, pubKey);
        return cipher.doFinal(data);
    }

    /**
     * ȡ��˽Կ
     *
     * @param keyMap ��Կmap
     * @return byte[] ˽Կ
     */
    public static byte[] getPrivateKey(Map<String, Object> keyMap) {
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        return key.getEncoded();
    }

    /**
     * ȡ�ù�Կ
     *
     * @param keyMap ��Կmap
     * @return byte[] ��Կ
     */
    public static byte[] getPublicKey(Map<String, Object> keyMap) throws Exception {
        Key key = (Key) keyMap.get(PUBLIC_KEY);
        return key.getEncoded();
    }

/**
 * ����һ�Թ�Կ��˽Կ����д������ļ�
 * @param publicKeyFilePath ��Կ·��
 * @param privateKeyFilePath ˽Կ·��
 * @return ��������·���true
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
    	
    	//д�빫Կ
    	int publicKeyStart = Integer.parseInt(PropertiesUtils.readValue(publicKeyFilePath, "start"));
    	int publicKeySize = Integer.parseInt(PropertiesUtils.readValue(publicKeyFilePath, "size"));
    	int num = publicKeySize+publicKeyStart;
    	PropertiesUtils.writeProperties(new Integer(num).toString(), publicKey, publicKeyFilePath);
    	
    	//д��˽Կ
    	PropertiesUtils.writeProperties(new Integer(num).toString(), privateKey, privateKeyFilePath);
    	
    	//��Կsize����
    	int newSize = publicKeySize+1;
    	PropertiesUtils.writeProperties("size", new Integer(newSize).toString(), publicKeyFilePath);
    	return true;
    }
    
    /**
     * ʹ�ö�Ӧ��Կ���ܸ��ַ���
     * @param str ���ܺ���ַ���
     * @param num ��Ӧ��Կ���
     * @return ���������ؽ��ܺ��key�����򷵻�""
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
     * ʹ�ö�Ӧ˽Կ�����ַ���
     * @param str
     * @param num
     * @return �����������ض�Ӧ�ַ��������򷵻�""
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
     * ����publickey�ļ���������Ŀ���ɹ��ڹ�Կ��ŵ������
     * @return �����
     */
    public static int getPublicKeyRandomSize() {
    	int start = Integer.parseInt(PropertiesUtils.readValue(PropertiesFilePath.publicKeyFilePath, "start"));
    	int end = start+Integer.parseInt(PropertiesUtils.readValue(PropertiesFilePath.publicKeyFilePath, "size"))-1;
    	return start+(int)(Math.random()*(end-start+1));
    }
    
    public static void main(String[] args) throws Exception {
//        //��ʼ����Կ
//        //������Կ��
//        Map<String, Object> keyMap1 = RSACoder.initKey();
//        //��Կ
//        byte[] publicKey1 = RSACoder.getPublicKey(keyMap1);
//
//        //˽Կ
//        byte[] privateKey1 = RSACoder.getPrivateKey(keyMap1);
//        
//        
//        Map<String, Object> keyMap2 = RSACoder.initKey();
//        //��Կ
//        byte[] publicKey2= RSACoder.getPublicKey(keyMap2);
//
//        //˽Կ
//        byte[] privateKey2 = RSACoder.getPrivateKey(keyMap2);
//        
//        System.out.println("��Կ��/n" + Base64.getEncoder().encodeToString(publicKey1));
//        System.out.println("˽Կ��/n" + Base64.getEncoder().encodeToString(privateKey1));
//        //System.out.println(Base64.getEncoder().encodeToString(Base64.getDecoder().decode(Base64.getEncoder().encodeToString(privateKey)))) ;
//
//        System.out.println("================��Կ�Թ������,�׷�����Կ�������ҷ�����ʼ���м������ݵĴ���=============");
//        String str = "RSA���뽻���㷨";
//        System.out.println("/n===========�׷����ҷ����ͼ�������==============");
//        System.out.println("ԭ��:" + str);
//        //�׷��������ݵļ���
//        byte[] code1 = RSACoder.encryptByPrivateKey(str.getBytes(), privateKey1);
//        System.out.println("���ܺ�����ݣ�" + Base64.getEncoder().encodeToString(code1));
//        //System.out.println("===========�ҷ�ʹ�ü׷��ṩ�Ĺ�Կ�����ݽ��н���==============");
//        //�ҷ��������ݵĽ���
//        byte[] decode1 = RSACoder.decryptByPublicKey(code1, publicKey1);
//        System.out.println("�ҷ����ܺ�����ݣ�" + new String(decode1) + "/n/n");
//
//        System.out.println("===========������в������ҷ���׷���������==============/n/n");
//
//        str = "�ҷ���׷���������RSA�㷨";
//
//        System.out.println("ԭ��:" + str);
//
//        //�ҷ�ʹ�ù�Կ�����ݽ��м���
//        byte[] code2 = RSACoder.encryptByPublicKey(str.getBytes(), publicKey1);
//        System.out.println("===========�ҷ�ʹ�ù�Կ�����ݽ��м���==============");
//        System.out.println("���ܺ�����ݣ�" + Base64.getEncoder().encodeToString(code2));
//
//        System.out.println("=============�ҷ������ݴ��͸��׷�======================");
//        System.out.println("===========�׷�ʹ��˽Կ�����ݽ��н���==============");
//
//        //�׷�ʹ��˽Կ�����ݽ��н���
//        byte[] decode2 = RSACoder.decryptByPrivateKey(code2, privateKey1);
//
//        System.out.println("�׷����ܺ�����ݣ�" + new String(decode2));
    	
//    	for(int i = 0;i<19;i++) {
//        	System.out.println(RSACoder.getPublicAndPrivateKkeyIntoFile(PropertiesFilePath.publicKeyFilePath,
//        			PropertiesFilePath.privateKeyFilePath));
//    	}

    	
//    	String privateKeyStr = PropertiesUtils.readValue(PropertiesFilePath.privateKeyFilePath, new Integer(10).toString());
//    	System.out.println("�ļ�˽Կ��/n" + privateKeyStr);
//    	byte[] privateKey = Base64.getDecoder().decode(privateKeyStr);
//    	System.out.println("˽Կ��/n" + Base64.getEncoder().encodeToString(privateKey));
//    	String str = "RSA���뽻���㷨";
//    	byte[] code1 = RSACoder.encryptByPrivateKey(str.getBytes(), privateKey);
    	
//    	String str2 = RSACoder.encryptByPublicKeyStr("��ϲ����", 10);
//    	
//    	System.out.println(RSACoder.decipheringPrivateStr(str2,10));   	
    }  
}
