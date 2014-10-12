package discys.API;

import disycs.model.Questionnaire.CATEGORY;
import disycs.model.Questionnaire.STATE;

public class ApiURL {

	static String BaseURL = "http://www.disycs.com/quizmoo-staging/web";
	public static String AuthAPI=BaseURL+"/apiauth";
	
	public static String QuestAPI(STATE state, CATEGORY category, int page){
			return BaseURL+"/api/questionnaire/"+
					state.getCode()+"/"+
					page+"/"+
					category.getCode();
	}
	
	public static String QuestDetailAPI(int id){
		return BaseURL+"/api/questionnaire/details/"+id;
	}
}
