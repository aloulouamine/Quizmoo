package disycs.model.questions;


import android.os.Parcel;
import disycs.model.Option;
import disycs.quizmo.R;

public class QuestionMultipleChoice extends Question {
	
	/**
	 * 
	 */
	
	boolean single;
	Option [] option;
	
	public QuestionMultipleChoice(int id, String text, boolean single,
			Option[] option) {
		super(id, text);
		this.single = single;
		this.option = option;
	}

	public boolean isSingle() {
		return single;
	}

	public Option[] getOption() {
		return option;
	}

	@Override
	public int getIcon() {
		if (single)
			return R.drawable.ic_radio;
		return R.drawable.ic_checkbox ;
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
