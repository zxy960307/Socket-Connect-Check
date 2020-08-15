package Utils;

public class OTP {

    public static final int INIT_TIME = 0;
    public static final int STEP = 3000;//OTP鏃堕棿姝ラ暱,鍗曚綅姣
    public static final String KEY = "aabbccddeeff";//瀵嗛挜
    
    private String key;
    private Long initTime;
    private int step;
    
    public OTP(String key,Long initTime,int step) {
        this.key=key;
        this.initTime=initTime;
        this.step=step;
    }
    
    public String getOTP() {
        
        Long gap = System.currentTimeMillis()-this.initTime;
        String text = new Long(gap/this.step).toString();
        if(text == "" || text.isEmpty())            
            text = "1";
        
        String result = null;
        try {
            result = PasswordManager.getPassword(this.key, text);
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("TOP生成失败");
        }
        
        return result;    
        } 
}
