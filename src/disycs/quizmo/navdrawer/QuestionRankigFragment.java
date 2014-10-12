package disycs.quizmo.navdrawer;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TextView;
import disycs.model.Option;
import disycs.model.questions.QuestionRanking;
import disycs.model.responses.Response;
import disycs.model.responses.ResponseProvider;
import disycs.model.responses.ResponseRanking;
import disycs.quizmo.R;
import disycs.quizmo.design.FontChangeCrawler;

public class QuestionRankigFragment extends Fragment implements ResponseProvider {
	int index,total;
	EditText singleTextBox;
	QuestionRanking q;
	ArrayList<RadioGroup>groups;
	public void setFont(View rootView)
	{	    
	    FontChangeCrawler fontChanger = new FontChangeCrawler(getActivity().getAssets(), "font/montserratregular.ttf");
	    fontChanger.replaceFonts((ViewGroup) rootView);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		q = (QuestionRanking) getArguments().get("QUESTION");
		index = getArguments().getInt("INDEX");
		index--;
		total=getArguments().getInt("TOTAL");
		View rootView= inflater.inflate(R.layout.fragment_ranking, container, false);
		TableLayout tab = new TableLayout(getActivity());
		tab.setScrollContainer(true);
		groups = new ArrayList<RadioGroup>();
		//TODO Make the view
		LinearLayout titlesLayout= (LinearLayout)rootView.findViewById(R.id.titleslayout);
		LinearLayout radioLayout = (LinearLayout)rootView.findViewById(R.id.radiolayout);
		float scale = getResources().getDisplayMetrics().density;
		int dpAsPixels = (int) (12*scale + 0.5f);
		int radioRank = 0;
		for(Option p : q.getOption()){
			radioRank++;
			TextView row = new TextView(getActivity());
			row.setText(p.getText());
			row.setPadding(0, dpAsPixels, 0, 0);
			titlesLayout.addView(row);
			RadioGroup grp = new RadioGroup(getActivity());
			for(int i = 0 ;i < q.getOption().length;i++){
				RadioButton rb  = new RadioButton(getActivity());
				rb.setText(radioRank+"");
				rb.setTag(q.getOption()[i]);
				grp.addView(rb);
			}
			grp.setTag(radioRank);
			groups.add(grp);
			radioLayout.addView(grp);
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
		ResponseRanking r  = new ResponseRanking(q);
		for(RadioGroup rgp : groups){
			RadioButton selected =(RadioButton)
					getView().findViewById(rgp.getCheckedRadioButtonId());
			if (selected !=null){
				r.addRank((Option)selected.getTag(),(Integer)rgp.getTag());
			}
		}
		Log.i("Response","Resp of "+q.getText()+"is "+r.getJsonString());
		return r;
	}
}
