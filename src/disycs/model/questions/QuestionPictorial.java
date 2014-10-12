package disycs.model.questions;
import android.os.Parcel;
import disycs.quizmo.R;
public class QuestionPictorial extends Question {
	/**
	 * 
	 */
	
	String pictureUrl;

	public QuestionPictorial(int id, String text, String pictureUrl) {
		super(id, text);
		this.pictureUrl = pictureUrl;
	}

	@Override
	public
	int getIcon() {
		
		return R.drawable.ic_picture;
	}

	public String getPictureUrl() {
		return pictureUrl;
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
