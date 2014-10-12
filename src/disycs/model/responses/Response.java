package disycs.model.responses;

import android.os.Parcelable;
import disycs.model.questions.Question;

public abstract class Response implements Parcelable {
	/**
	 * 
	 */
	
	Question q;

	public Response(Question q) {
		this.q = q;
	}
	
	abstract public String getJsonString();
	abstract public boolean isValid();

}