package disycs.model;

public class Option {
	int id;
	String text;
	public Option(int id, String text) {
		super();
		this.id = id;
		this.text = text;
	}
	public int getId() {
		return id;
	}
	public String getText() {
		return text;
	}
	@Override
	public String toString(){
		return text;
	}
}
