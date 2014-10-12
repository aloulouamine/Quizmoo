package disycs.model;




public class User {
	// Singleton
	private static User user = null;
	public static User getUser(){
		return User.user;
	}
	public static void setUser(String userName, String password){
		if (getUser() == null){
			User.user= new User(userName,password);
		}
	}
	public static void deleteUser(){
		User.user=null;
	}
	private String userName;
	private String password;
	private User(String userName, String password) {
		this.userName = userName;
		String passwordHash = MyCryptoHelper.computePasswordHash(password);
		this.password = passwordHash;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUserName() {
		return userName;
	}
	public String getPassword() {
		return password;
	}
}
	
