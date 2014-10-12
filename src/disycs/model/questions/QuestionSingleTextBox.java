package disycs.model.questions;
import android.os.Parcel;
import disycs.quizmo.R;
public class QuestionSingleTextBox extends Question{

	/**
	 * 
	 */
	

	public QuestionSingleTextBox(int id, String text) {
		super(id, text);
	}

	@Override
	public
	int getIcon() {
		return R.drawable.ic_txt;
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
