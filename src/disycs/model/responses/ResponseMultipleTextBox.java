package disycs.model.responses;

import java.util.ArrayList;

import android.os.Parcel;
import disycs.model.questions.Question;
import disycs.model.questions.QuestionMultipleTextBox;

public class ResponseMultipleTextBox extends Response {
	QuestionMultipleTextBox q;
	ArrayList<String> responses;
	public ResponseMultipleTextBox(Question q) {
		super(q);
		this.q=(QuestionMultipleTextBox)q;
		responses= new ArrayList<String>();
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
	public void addResponse(String s){
		responses.add(s);
	}

	@Override
	public String getJsonString() {
		String json="invalid";
		if(isValid()){
			json="{\"id\":"+q.getId()+",\"answers\":[";
			for(int i = 0 ; i<responses.size();i++){
				if(i>0){
					json+=",";
				}
				json+="{ \"id\":"+q.getOptions()[i].getId()+","+
						"\"text\":"+responses.get(i)+"}";
			}
			json+="]}";
		}
		return json;
	}
	@Override
	public boolean isValid() {
		for (String s : responses){
			if(s.length()<1){
				return false;
			}
		}
		return true;
	}

}
