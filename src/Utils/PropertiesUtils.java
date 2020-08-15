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
    * ��ȡ�����ļ�����Ӧ����ֵ
    * @param key
    *            ����
    * @return String
    */
    public  String getKeyValue(String key) {
        return this.props.getProperty(key);
    }

    /**
    * ��������key��ȡ������ֵvalue
    * @param filePath �����ļ�·��
    * @param key ����
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
    * ���£�����룩һ��properties��Ϣ(���������ֵ)
    * ����������Ѿ����ڣ����¸�������ֵ��
    * ��������������ڣ�����һ�Լ�ֵ��
    * @param keyname ����
    * @param keyvalue ��ֵ
    */
    public static void writeProperties(String keyname,String keyvalue,String filePath) {      
    	Properties props = new Properties();
    	
        try {
            // ���� Hashtable �ķ��� put��ʹ�� getProperty �����ṩ�����ԡ�
            // ǿ��Ҫ��Ϊ���Եļ���ֵʹ���ַ���������ֵ�� Hashtable ���� put �Ľ����
        	props.load(new FileInputStream(filePath));
            OutputStream fos = new FileOutputStream(filePath);
            props.setProperty(keyname, keyvalue);
            // ���ʺ�ʹ�� load �������ص� Properties ���еĸ�ʽ��
            // ���� Properties ���е������б�����Ԫ�ضԣ�д�������
            props.store(fos, "Update '" + keyname + "' value");
        } catch (IOException e) {
        	e.printStackTrace();
            System.err.println("�����ļ����´���");
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
            System.err.println("��ȡProperties��С����");
        }finally {
        	return size;
        }
    }
    
    public static void main(String[] args) {
    	//System.out.println( Properties.class.getResource("/").getPath());
    	System.out.println(PropertiesUtils.getPropertiesSize( Properties.class.getResource("/").getPath()+"publickey.properties"));
    }
}
