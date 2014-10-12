package disycs.model.responses;

import android.os.Parcel;
import disycs.model.questions.Question;
import disycs.model.questions.QuestionPictorial;

public class ResponsePictorial extends Response {
	QuestionPictorial  q;
	String comment;
	public ResponsePictorial(Question q) {
		super(q);
		// TODO Auto-generated constructor stub
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
	public void setComment(String comment) {
		this.comment=comment;
	}
	@Override
	public String getJsonString() {
		String json = "invalid";
		if(isValid()){
			json="{\"id\":"+q.getId()+",\"comment\":\""+comment+"\"}";
		}
		return json;
	}

	@Override
	public boolean isValid() {
		if(comment.length()>0){
			return true;
		}
		return false;
	}

}
