package disycs.quizmo.navdrawer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;
import disycs.model.Option;
import disycs.model.questions.QuestionSelectField;
import disycs.model.responses.Response;
import disycs.model.responses.ResponseProvider;
import disycs.model.responses.ResponseSelectField;
import disycs.model.responses.ResponseSingleTextBox;
import disycs.quizmo.R;
import disycs.quizmo.design.FontChangeCrawler;

public class QuestionSelectFieldFragment extends Fragment implements ResponseProvider{
	QuestionSelectField myQuestion;
	int index,total;
	EditText singleTextBox;
	ResponseSingleTextBox response ;
	TextView minTxt, maxTxt,valTxt;
	SeekBar seekBarNum;
	Spinner mySpinner;
	ArrayAdapter<Option> adapter;
	public void setFont(View rootView)
	{	    
	    FontChangeCrawler fontChanger = new FontChangeCrawler(getActivity().getAssets(), "font/montserratregular.ttf");
	    fontChanger.replaceFonts((ViewGroup) rootView);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		myQuestion = (QuestionSelectField) getArguments().get("QUESTION");
		index = getArguments().getInt("INDEX");
		index--;
		total=getArguments().getInt("TOTAL");
		View rootView=null;
		if(myQuestion.getType()==0){
			rootView= inflater.inflate(R.layout.fragment_selectfield_num, container, false);
			minTxt=(TextView)rootView.findViewById(R.id.txt_min_val);
			maxTxt=(TextView)rootView.findViewById(R.id.txt_max_val);
			valTxt=(TextView)rootView.findViewById(R.id.txt_val);
			seekBarNum=(SeekBar)rootView.findViewById(R.id.seekBar_num);
			minTxt.setText(myQuestion.getInf()+"");
			maxTxt.setText(myQuestion.getSup()+"");
			valTxt.setText(myQuestion.getInf()+"");
			seekBarNum.setMax((int)myQuestion.getIntervall());
			seekBarNum.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
				
				
				
				@Override
				public void onProgressChanged(SeekBar seekBar, int progress,
						boolean fromUser) {
					valTxt.setText((progress+myQuestion.getInf())+"");
					
				}

				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {
				
					
				}

				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {
			
					
				}
			});
		}else
		{
			rootView= inflater.inflate(R.layout.fragment_selectfield_spinner, container, false);
			mySpinner=(Spinner)rootView.findViewById(R.id.spinner_answer);
			adapter = new ArrayAdapter<Option>(getActivity(),android.R.layout.simple_spinner_item) ;
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			for (Option p : myQuestion.getOptions()){
				adapter.add(p);
			}
			mySpinner.setAdapter(adapter);
		}
		LinearLayout outer = (LinearLayout)rootView.findViewById(R.id.question_outer_layout);
        outer.setBackgroundColor(getActivity().getResources().getColor(getArguments().getInt("COLOR")));
		TextView title = (TextView)rootView.findViewById(R.id.txt_ques_title);
        title.setText(index+"/"+total+") "+myQuestion.getText());
        setFont(rootView);
		return rootView;
	}
	@Override
	public Response getResponse() {
		ResponseSelectField response = new ResponseSelectField(myQuestion);
		if (myQuestion.getType()==0){
			Option selected = myQuestion.getOptions()[0];
			Log.i("seekbar",seekBarNum.getProgress()+"");
			for (Option p : myQuestion.getOptions()){
				// TODO
				if (p.getText().equals(valTxt.getText().toString())){
					selected=p;
					Log.i("option val", p.getText());
					Log.i("option id",p.getId()+"");
				}
			}
			response.addSelectedOption(selected);			
		}else{
			response.addSelectedOption((Option)mySpinner.getSelectedItem());
		}
		Log.i("Response","Resp of "+myQuestion.getText()+"is "+response.getJsonString());
		return response;
	}
}
