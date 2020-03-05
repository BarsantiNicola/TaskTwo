package logic.data;

public class User {
	
	private String username;
	//private String picture;
	private String name;
	private String surname;
	private String favoriteGenre;
	private int age;
	private String lastAccess;
	private String gender;
	
	public User() {}
	
	public String getUsername() {
		
		return username;
	}
	
	public String getName() {
		
		return name;
	}
	
	public String getSurname() {
		
		return surname;
	}
	
	public String getCompleteName() {
		
		String completeName = name + " " + surname;
		
		return completeName;
	}
	
	public String getFavoriteGenre() {
		
		return favoriteGenre;
	}
	
	public int getAge() {
		
		return age;
	}
	
	public String getLastAccess() {
		
		return lastAccess;
	}
	
	public String getGender() {
		
		return gender;
	}
}
