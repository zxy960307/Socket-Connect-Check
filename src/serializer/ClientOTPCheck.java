package serializer;

public class ClientOTPCheck extends Serializer {

	private String OTPPassword;
	
	@Override
	public void read() {
		this.OTPPassword = readString();
		
	}

	@Override
	public void write() {
		writeString(OTPPassword);
		
	}

	public String getOTPPassword() {
		return OTPPassword;
	}

	public void setOTPPassword(String oTPPassword) {
		OTPPassword = oTPPassword;
	}

	
}
