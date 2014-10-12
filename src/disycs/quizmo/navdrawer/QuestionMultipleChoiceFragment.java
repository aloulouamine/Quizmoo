package disycs.quizmo.navdrawer;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import disycs.model.Option;
import disycs.model.questions.QuestionMultipleChoice;
import disycs.model.responses.Response;
import disycs.model.responses.ResponseMultipleChoice;
import disycs.model.responses.ResponseProvider;
import disycs.quizmo.R;
import disycs.quizmo.design.FontChangeCrawler;


public class QuestionMultipleChoiceFragment extends Fragment  implements ResponseProvider {
	QuestionMultipleChoice q;
	int index,total;
	Response response;
	
	public void setFont(View rootView)
	{	    
	    FontChangeCrawler fontChanger = new FontChangeCrawler(getActivity().getAssets(), "font/montserratregular.ttf");
	    fontChanger.replaceFonts((ViewGroup) rootView);
	}
	// Single TextBox
	RadioGroup rg;
	// multipleCheckBox
	ArrayList<CheckBox> multipleCheckBox;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		q = (QuestionMultipleChoice) getArguments().get("QUESTION");
		index = getArguments().getInt("INDEX");
		index--;
		total=getArguments().getInt("TOTAL");
		View rootView=null;
		
			rootView= inflater.inflate(R.layout.fragment_question, container, false);
	        createView(q,rootView);  
	        Log.i("Layout Normal","Fragment Question");
		LinearLayout outer = (LinearLayout)rootView.findViewById(R.id.question_outer_layout);
        outer.setBackgroundColor(getActivity().getResources().getColor(getArguments().getInt("COLOR")));
		TextView title = (TextView)rootView.findViewById(R.id.txt_ques_title);
        title.setText(index+"/"+total+") "+q.getText());
        setFont(rootView);
        return rootView;
    }
	void createView(QuestionMultipleChoice q, View rootView){
    	LinearLayout layout  = (LinearLayout)rootView.findViewById(R.id.question_layout); 
   		if(q.isSingle()){
   			rg = new RadioGroup(getActivity())  ;
   			LinearLayout optionLayout = new LinearLayout(getActivity());
       		optionLayout.setOrientation(LinearLayout.HORIZONTAL);
   			for(int i = 0 ; i<q.getOption().length;i++){
   				
       			RadioButton rb = new RadioButton(getActivity());
       			rb.setText(q.getOption()[i].getText());
       			rb.setTag(q.getOption()[i]);
       			rg.addView(rb);
       			
   			}
   			optionLayout.addView(rg);
   			layout.addView(optionLayout);
   		}else{
   			multipleCheckBox = new ArrayList<CheckBox>();
   			for(int i = 0 ; i<q.getOption().length;i++){
   				LinearLayout optionLayout = new LinearLayout(getActivity());
       			optionLayout.setOrientation(LinearLayout.HORIZONTAL);
       			CheckBox cb = new CheckBox(getActivity());
       			cb.setText(q.getOption()[i].getText());
       			cb.setTag(q.getOption()[i]);
       			multipleCheckBox.add(cb);
       			optionLayout.addView(cb);
       			layout.addView(optionLayout);
   			}
   		}
   	}
    	
	@Override
	public void onStart(){
		super.onStart();
		
	}
	
	@Override
	public Response getResponse() {
		ResponseMultipleChoice r = new ResponseMultipleChoice(q);
		if(q.isSingle()){
			RadioButton selected = (RadioButton)getView().findViewById(rg.getCheckedRadioButtonId());
			if (selected!=null){
				r.addSelectedOptions((Option) selected.getTag());
			}
		}else{
			for(CheckBox cb : multipleCheckBox){
				if (cb.isChecked()){
					r.addSelectedOptions((Option)cb.getTag());
				}
			}
		}
		Log.i("Response","Resp of "+q.getText()+"is "+r.getJsonString());
		return r;
	}

}
