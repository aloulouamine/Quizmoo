package disycs.model;
import java.io.Serializable;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
public class Questionnaire implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2227106262154165849L;
	public static enum CATEGORY{
		ALL("41"),
		CLIENT_FEEDBACK("42"),
		HUMAIN_SHIT("43"),
		PROSPECTUS("44"),
		SOCIAL_POLITIC("45"),
		OTHER("47");
		private static final Map <String, CATEGORY> lookup
		=new HashMap<String,CATEGORY>();
		static {
			for(CATEGORY s : EnumSet.allOf(CATEGORY.class))
				lookup.put(s.getCode(),s);
		}
		private String value;
		private CATEGORY (String value){
			this.value=value;
		}
		public String getCode() {return value;	}
		public static CATEGORY get(String value){return lookup.get(value);}
	}
	public enum STATE 
	{
	     ONGOING("ongoing"),
	     DRAFT("draft"),
	     CLOSED("closed");

	     private static final Map<String,STATE> lookup 
	          = new HashMap<String,STATE>();

	     static {
	          for(STATE s : EnumSet.allOf(STATE.class))
	               lookup.put(s.getCode(), s);
	     }

	     private String value;

	     private STATE(String value) {
	          this.value = value;
	     }

	     public String getCode() { return value; }

	     public static STATE get(String value) { 
	          return lookup.get(value); 
	     }
	}
	
	
	int id ; 
	String title, description;
	Date dateOfCreation;
	String hash,thumbnail;	
	STATE state;
	CATEGORY category;
	public Questionnaire(int id, String title, String description,
			Date dateOfCreation, String hash , STATE S, CATEGORY C, String thumb) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.dateOfCreation = dateOfCreation;
		this.hash = hash;
		this.state=S;
		this.category=C;
		this.thumbnail=thumb;
	}	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getDateOfCreation() {
		return dateOfCreation;
	}
	public void setDateOfCreation(Date dateOfCreation) {
		this.dateOfCreation = dateOfCreation;
	}
	public String getHash() {
		return hash;
	}
	public void setHash(String hash) {
		this.hash = hash;
	}
	public STATE getState() {
		return state;
	}
	public void setState(STATE state) {
		this.state = state;
	}
	public CATEGORY getCategory() {
		return category;
	}
	public void setCategory(CATEGORY category) {
		this.category = category;
	}
	public String getThumbnail() {
		return thumbnail;
	}
	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}
	
}