package Utils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class PropertiesUtils {

    private Properties props = new Properties();
    
    public PropertiesUtils(String path) {
    	try {
            props.load(new FileInputStream(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(-1);
        } catch (IOException e) {      
            System.exit(-1);
        }
    }

    /**
    * 读取属性文件中相应键的值
    * @param key
    *            主键
    * @return String
    */
    public  String getKeyValue(String key) {
        return this.props.getProperty(key);
    }

    /**
    * 根据主键key读取主键的值value
    * @param filePath 属性文件路径
    * @param key 键名
    */
    public static String readValue(String filePath, String key) {
        Properties props = new Properties();
        try {
            InputStream in = new BufferedInputStream(new FileInputStream(
                    filePath));
            props.load(in);
            String value = props.getProperty(key);
            return value;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
  
    /**
    * 更新（或插入）一对properties信息(主键及其键值)
    * 如果该主键已经存在，更新该主键的值；
    * 如果该主键不存在，则插件一对键值。
    * @param keyname 键名
    * @param keyvalue 键值
    */
    public static void writeProperties(String keyname,String keyvalue,String filePath) {      
    	Properties props = new Properties();
    	
        try {
            // 调用 Hashtable 的方法 put，使用 getProperty 方法提供并行性。
            // 强制要求为属性的键和值使用字符串。返回值是 Hashtable 调用 put 的结果。
        	props.load(new FileInputStream(filePath));
            OutputStream fos = new FileOutputStream(filePath);
            props.setProperty(keyname, keyvalue);
            // 以适合使用 load 方法加载到 Properties 表中的格式，
            // 将此 Properties 表中的属性列表（键和元素对）写入输出流
            props.store(fos, "Update '" + keyname + "' value");
        } catch (IOException e) {
        	e.printStackTrace();
            System.err.println("属性文件更新错误");
        }
    }
    
    public static int getPropertiesSize(String filePath) {
    	Properties props = new Properties();
    	int size = 0;
    	try {
    		props.load(new FileInputStream(filePath));
    		size = props.size();
    	}catch (IOException e) {
        	e.printStackTrace();
            System.err.println("获取Properties大小错误");
        }finally {
        	return size;
        }
    }
    
    public static void main(String[] args) {
    	//System.out.println( Properties.class.getResource("/").getPath());
    	System.out.println(PropertiesUtils.getPropertiesSize( Properties.class.getResource("/").getPath()+"publickey.properties"));
    }
}
