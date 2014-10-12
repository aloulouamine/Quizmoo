package disycs.quizmo.navdrawer;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import disycs.model.questions.QuestionMultipleTextBox;
import disycs.model.responses.Response;
import disycs.model.responses.ResponseMultipleTextBox;
import disycs.model.responses.ResponseProvider;
import disycs.quizmo.R;
import disycs.quizmo.design.FontChangeCrawler;

public class QuestionMultipleTextBoxFragment extends Fragment implements ResponseProvider{
	QuestionMultipleTextBox q;
	int index,total;
	EditText singleTextBox;
	ArrayList<EditText> textAreas;
	public void setFont(View rootView)
	{	    
	    FontChangeCrawler fontChanger = new FontChangeCrawler(getActivity().getAssets(), "font/montserratregular.ttf");
	    fontChanger.replaceFonts((ViewGroup) rootView);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		q = (QuestionMultipleTextBox) getArguments().get("QUESTION");
		index = getArguments().getInt("INDEX");
		index--;
		total=getArguments().getInt("TOTAL");
		View rootView=null;
		textAreas = new ArrayList<EditText>();
		rootView= inflater.inflate(R.layout.fragment_question, container, false);
		LinearLayout layout  = (LinearLayout)rootView.findViewById(R.id.question_layout); 
   		QuestionMultipleTextBox myQuestion = (QuestionMultipleTextBox)q;
   		for (int i = 0 ; i<myQuestion.getOptions().length;i++){
   			LinearLayout optionLayout = new LinearLayout(getActivity());
   			optionLayout.setOrientation(LinearLayout.HORIZONTAL);
   			LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
   			optionLayout.setLayoutParams(params);
   			TextView txt = new TextView(getActivity());
   			txt.setText(myQuestion.getOptions()[i].getText()+": ");
   			EditText response = new EditText(getActivity());
   			textAreas.add(response);
   			response.setHint(getResources().getString(R.string.fill_in));
   			response.setLayoutParams(params);
   			optionLayout.addView(txt);
   			optionLayout.addView(response);
   			layout.addView(optionLayout);
   		}
   		LinearLayout outer = (LinearLayout)rootView.findViewById(R.id.question_outer_layout);
        outer.setBackgroundColor(getActivity().getResources().getColor(getArguments().getInt("COLOR")));
		TextView title = (TextView)rootView.findViewById(R.id.txt_ques_title);
        title.setText(index+"/"+total+") "+q.getText());
        setFont(rootView);
        return rootView;
   	}

	@Override
	public Response getResponse() {
		ResponseMultipleTextBox r = new ResponseMultipleTextBox(q);
		for (EditText x : textAreas){
			r.addResponse(x.getText().toString());
		}
		Log.i("Response","Resp of "+q.getText()+"is "+r.getJsonString());
		return r;
	}
}


