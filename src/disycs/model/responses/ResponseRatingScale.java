package disycs.model.responses;

import java.util.HashMap;
import java.util.Map;

import android.os.Parcel;
import disycs.model.Option;
import disycs.model.questions.Question;
import disycs.model.questions.QuestionRatingScale;

public class ResponseRatingScale extends Response{
	Map <Option,Option> responses;
	QuestionRatingScale q;
	public ResponseRatingScale(Question q) {
		super(q);
		// TODO Auto-generated constructor stub
		this.q=(QuestionRatingScale) q;
		responses = new HashMap<Option, Option>();
	}
	
	public void addResponse(Option col , Option row){
		responses.put(col, row);
	}
	@Override
	public String getJsonString() {
		// TODO Auto-generated method stub
		String json = "invalid";
		if(isValid()){
			json="{\"id\":"+q.getId()+",\"answers\":[";
			for(int i=0;i<responses.size();i++){
				if(i>0){
					json+=",";
				}
				json+="{ \"col_id\":"+q.getRowOption()[i].getId()+","+
						"\"row_id\":"+responses.get(q.getRowOption()[i]).getId()+"}";
			}
			json+="]}";
		}
		
		return json;
	}

	@Override
	public boolean isValid() {
		if (q.getRowOption().length==responses.size()){
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
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		
	}

}
