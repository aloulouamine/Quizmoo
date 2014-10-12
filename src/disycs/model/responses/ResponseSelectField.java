package disycs.model.responses;

import java.util.ArrayList;

import android.os.Parcel;
import disycs.model.Option;
import disycs.model.questions.Question;
import disycs.model.questions.QuestionSelectField;

public class ResponseSelectField extends Response{
	ArrayList<Option>selected;
	QuestionSelectField q;
	public ResponseSelectField(Question q) {
		super(q);
		this.q=(QuestionSelectField)q;
		selected=new ArrayList<Option>();
		
	}
	public void addSelectedOption(Option p){
		selected.add(p);
	}
	@Override
	public String getJsonString() {
		String json="invalid";
		if(isValid()) {
			json = "{\"id\":"+q.getId()+",\"selectedIds\":\""+selected.get(0).getId();
			for(int i =1 ; i<selected.size();i++){
				json+=","+selected.get(i).getId();
			}
			json+="\"}";
		}
		return json;
	}

	@Override
	public boolean isValid() {
		if(selected.size()>0){
			return true;
		}
		return false;
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	

}
