package disycs.model.responses;

import java.util.HashMap;

import android.os.Parcel;
import disycs.model.Option;
import disycs.model.questions.Question;
import disycs.model.questions.QuestionRanking;

public class ResponseRanking extends Response {
	QuestionRanking q;
	HashMap<Option,Integer> ranks;
	public ResponseRanking(Question q) {
		super(q);
		// TODO Auto-generated constructor stub
		this.q=(QuestionRanking)q;
		ranks= new HashMap<Option, Integer>();
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
	
	public void addRank(Option p , int r){
		ranks.put(p, r);
	}

	@Override
	public String getJsonString() {
		String json="invalid";
		if(isValid()){
			json="{\"id\":"+q.getId()+",\"ranks\":[";
			for(int i = 0 ; i<ranks.size();i++){
				if(i>0){
					json+=",";
				}
				json+="{ \"id\":"+q.getOption()[i].getId()+","+
						"\"rank\":"+ranks.get(q.getOption()[i])+"}";
			}
			json+="]}";
		}
		return json;
	}

	@Override
	public boolean isValid() {
		if(ranks.size()==q.getOption().length){
			return true;
		}
		return false;
	}

}
