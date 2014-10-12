package disycs.model;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Token {
	private String tokenString;
	private Date tokenExpireDate;
	private int timeZoneType;
	private String timeZone;
	public static Token token=null;
	public static Token getToken(){
		return Token.token;
	}
	public static void setToken(String tokenString, Date tokenExpireDate, int timeZoneType,String timeZone){
		Token.token = new Token(tokenString,tokenExpireDate,timeZoneType,timeZone);
	}
	private Token(String tokenString, Date tokenExpireDate, int timeZoneType,
			String timeZone) {
		super();
		this.tokenString = tokenString;
		this.tokenExpireDate = tokenExpireDate;
		this.timeZoneType = timeZoneType;
		this.timeZone = timeZone;
	}
	
	public String getTokenString() {
		return tokenString;
	}

	public Date getTokenExpireDate() {
		return tokenExpireDate;
	}

	public int getTimeZoneType() {
		return timeZoneType;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public boolean isExpired(){
		TimeZone tz =TimeZone.getTimeZone(timeZone);
		Calendar Cal = Calendar.getInstance(tz);
		if(Cal.getTime().compareTo(tokenExpireDate)==-1){
			return false;
		}
		return true;
	}
}
