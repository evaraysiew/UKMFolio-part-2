import java.util.*;

public class User {
	private String username;
	private String password;
	private String role;
	
	private static ArrayList<User> users = new ArrayList<>();
	
	public User(String u, String p, String r) {
		username = u.toLowerCase();
		password = p;
		role = r;
		
		users.add(this);
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public String getRole() {
		return role;
	}
	
	 public static ArrayList<User> getUsers() {
	        return users;
	 }
	 
	 public static User getUserByUsername(String username) {
	        for (User user : users) {
	            if (user.getUsername().equalsIgnoreCase(username)) {
	                return user;
	            }
	        }
	        return null; // Return null if the user is not found
	    }
}
