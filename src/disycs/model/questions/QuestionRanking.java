package disycs.model.questions;

import android.os.Parcel;
import disycs.model.Option;
import disycs.quizmo.R;

public class QuestionRanking extends Question {
	/**
	 * 
	 */
	
	Option[] option;

	public QuestionRanking(int id, String text, Option[] option) {
		super(id, text);
		this.option = option;
	}
	
	

	public Option[] getOption() {
		return option;
	}



	@Override
	public
	int getIcon() {
		
		return R.drawable.ic_rank;
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
