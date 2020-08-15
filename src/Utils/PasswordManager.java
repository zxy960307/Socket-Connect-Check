package Utils;
import java.math.BigInteger;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class PasswordManager {

    private static final String ALGORITHM = "HmacSHA1";//鍔犲瘑绠楁硶
    private static final String ENCODING = "UTF-8";//缂栫爜鏍煎紡
    private static final int PASSWORDLENGTH = 9;//瀵嗙爜闀垮害
    
    
    public static String getPassword(String key,String text) throws Exception {
        //閿欒鍙傛暟澶勭悊
        if(key.length() == 0 || key.isEmpty())
            return null;
        else if (text.length()==0||text.isEmpty())
            return null;

        //鐢熸垚鍔犲瘑涓�
        byte data[] =key.getBytes(ENCODING);
        SecretKey secretKey = new SecretKeySpec(data,ALGORITHM);
        Mac mac = Mac.getInstance(ALGORITHM);
        mac.init(secretKey);
        byte[] textBytes = text.getBytes(ENCODING);
        byte[] result = mac.doFinal(textBytes);//鍔犲瘑锛岃幏寰�20闀垮害鐨勬暟缁�
        String resultString = byte2hex(result);
        
        
        int offSet = decodeHEX(resultString.substring(0,1));//鏍规嵁棣栧瓧鑺傞珮浣嶅喅瀹氬亸绉婚噺
        return resultString.substring(offSet,offSet+PasswordManager.PASSWORDLENGTH);
        
    } 
    
    //16杩涘埗杞负10杩涘埗
    public static int decodeHEX(String hexs){
        BigInteger bigint=new BigInteger(hexs, 16);
        int numb=bigint.intValue();
            return numb;
        }


    //灏�2杩涘埗鏁扮粍杞负16杩涘埗涓嬪瓧绗︿覆
    public static String byte2hex(byte[] b)
    {
        StringBuilder hs = new StringBuilder();
        String stmp;
        for (int n = 0; b!=null && n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1)
                hs.append('0');
            hs.append(stmp);
        }
        return hs.toString().toUpperCase();
    }
    
    public static String getRandomString(int length){
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<length;i++){
          int number=random.nextInt(62);
          sb.append(str.charAt(number));
        }
        return sb.toString();
    }

}
