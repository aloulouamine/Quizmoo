package disycs.model.questions;

import android.os.Parcel;
import disycs.model.Option;
import disycs.quizmo.R;
public class QuestionRatingScale extends Question{
	/**
	 * 
	 */
	
	Option[] rowOption,columnOption;

	public QuestionRatingScale(int id, String text, Option[] rowOption,
			Option[] columnOption) {
		super(id, text);
		this.rowOption = rowOption;
		this.columnOption = columnOption;
	}

	public Option[] getRowOption() {
		return rowOption;
	}

	public Option[] getColumnOption() {
		return columnOption;
	}

	@Override
	public
	int getIcon() {
		
		return R.drawable.ic_level;
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
