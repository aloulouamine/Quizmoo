package disycs.model.questions;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import disycs.model.Option;
import disycs.model.questions.Question.QTYPE;

public class QuestionFactory {
	ArrayList<Question>list;
	public ArrayList<Question>buildQuestions(String thumb){
		list = new ArrayList<Question>();
		try {
			Log.i("thumb",thumb);
		JSONObject jObject= new JSONObject(thumb);
		JSONArray jArray = jObject.getJSONArray(("questions"));
		for(int i = 0 ;i<jArray.length();i++){
			JSONObject jQuestion = jArray.getJSONObject(i);
			int id = jQuestion.getInt("id");
			String text = jQuestion.getString("question_text");
			Option[] options,row_options,column_options ;			
			switch (QTYPE.get(jQuestion.getString("type"))){
				case MULTI_CHOICE:
					options=getOption(jQuestion, "options");
					list.add(new QuestionMultipleChoice(id, text, jQuestion.getBoolean("isSingle"), options));
					break;
				case RATING_SCALE:
					row_options=getOption(jQuestion, "row_options");
					column_options=getOption(jQuestion, "column_options");
					list.add(new QuestionRatingScale(id, text, row_options, column_options));
					break;
				case SINGLE_TXT_BOX:
					list.add(new QuestionSingleTextBox(id, text));
					break;
				case RANKING:
					options = getOption(jQuestion, "options");
					list.add(new QuestionRanking(id, text, options));
					break;
				case MULTI_TEXT_BOX:
					options=getOption(jQuestion, "options");
					list.add(new QuestionMultipleTextBox(id, text, options));
					break;
				case PICTORIAL:
					list.add(new QuestionPictorial(id, text, jQuestion.getString("url")));
					break;
				case SELECT_FIELD:
					options=getOption(jQuestion, "options");
					list.add(new QuestionSelectField(id, text, jQuestion.getBoolean("isSingle"), options, jQuestion.getInt("prefered_choices")));
					break;
			default:
				break;			}
		}
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
		return list;
	}
	
	
	private Option[] getOption(JSONObject jQuestion,String optionField) throws JSONException{
		JSONArray jOptions = jQuestion.getJSONArray(optionField);
		Option[] options=new Option[jOptions.length()];
		for (int j=0 ;j<options.length;j++){
			JSONObject jOption = jOptions.getJSONObject(j);
			options[j]=new Option(jOption.getInt("id"),jOption.getString("text"));
		}
		return options;
	}
}
