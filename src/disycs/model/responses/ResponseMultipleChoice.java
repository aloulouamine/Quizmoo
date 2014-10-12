package disycs.model.responses;

import java.util.ArrayList;

import android.os.Parcel;

import disycs.model.Option;
import disycs.model.questions.Question;
import disycs.model.questions.QuestionMultipleChoice;

public class ResponseMultipleChoice extends Response {

	QuestionMultipleChoice typedQuestion;
	ArrayList<Option> selectedOptions;
	public ResponseMultipleChoice(Question q) {
		super(q);
		typedQuestion =(QuestionMultipleChoice)q; 
		selectedOptions = new ArrayList<Option>();
	}
	public void addSelectedOptions(Option op){
		selectedOptions.add(op);
	}
	public boolean isValid(){
		if(selectedOptions.size()>0)
			return true;
		return false;
	}
	@Override
	public String getJsonString() {
		String s="invalid";
		if(isValid()){
		s = "{\"id\":"+typedQuestion.getId()+",\"checkedIds\":\""+selectedOptions.get(0).getId();
		for(int i =1 ; i<selectedOptions.size();i++){
			s+=","+selectedOptions.get(i).getId();
		}
		s+="\"}";
		}
		return s;
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
