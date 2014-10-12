package disycs.model.questions;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import android.os.Parcelable;

public abstract class Question implements Parcelable {

	int id;
	String text;
	public Question(int id, String text) {
		this.id = id;
		this.text = text;
	}
	
	public static enum QTYPE {
		MULTI_CHOICE("Multiple Choice"),
		RATING_SCALE("Rating Scale"),
		SINGLE_TXT_BOX("Single Text Box"),
		RANKING("Ranking Question"),
		MULTI_TEXT_BOX("Multiple Text Box Question"),
		PICTORIAL("Pictorial Question"),
		SELECT_FIELD("Select Field");
		private static final Map<String,QTYPE>lookup=
				new HashMap<String, QTYPE>();
		static{
			for (QTYPE q: EnumSet.allOf(QTYPE.class))
				lookup.put(q.getCode(), q);
		}
		private String value;
		public String getCode() {
			return value;
		}
		private QTYPE(String value){
			this.value=value;
		}
		public static QTYPE get(String value){return lookup.get(value);}
	}
	public int getId() {
		return id;
	}
	public String getText() {
		return text;
	}
	abstract public int getIcon();
}
