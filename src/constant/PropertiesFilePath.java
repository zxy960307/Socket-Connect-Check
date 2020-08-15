package constant;

import java.util.Properties;

public interface PropertiesFilePath {
	public static final String publicKeyFilePath = Properties.class.getResource("/").getPath()+"publickey.properties";
	public static final String serversFilePath = Properties.class.getResource("/").getPath()+"servers.properties";
	public static final String privateKeyFilePath = Properties.class.getResource("/").getPath()+"privateKey.properties";
	public static final String clientsFilePath = Properties.class.getResource("/").getPath()+"client.properties";
}
