package disycs.model.questions;


import android.os.Parcel;
import disycs.model.Option;
import disycs.quizmo.R;

public class QuestionSelectField extends Question {
	/**
	 * 
	 */
	
	boolean isSingle;
	Option  []	 options;
	int type;
	

	public QuestionSelectField(int id, String text, boolean isSingle,
			Option[] options, int type) {
		super(id, text);
		this.isSingle = isSingle;
		this.options = options;
		this.type = type;
	}


	@Override
	public int getIcon() {
		return R.drawable.ic_dropdown;
	}


	public Option[] getOptions() {
		return options;
	}


	public void setOptions(Option[] options) {
		this.options = options;
	}


	public int getType() {
		return type;
	}


	public void setType(int type) {
		this.type = type;
	}
	public float getInf(){
		return Float.parseFloat(options[0].getText());
	}
	public float getSup(){
		return Float.parseFloat(options[options.length-1].getText());
	}
	public float getIntervall(){
		return getSup()-getInf();
	}
	public float getStep(){
		return Float.parseFloat(options[1].getText())-getInf();
	}


	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		
	}
	
}
