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
import disycs.model.questions.QuestionRatingScale;
import disycs.model.responses.Response;
import disycs.model.responses.ResponseProvider;
import disycs.model.responses.ResponseRatingScale;
import disycs.quizmo.R;
import disycs.quizmo.design.FontChangeCrawler;
import disycs.quizmo.design.VerticalTextView;

public class QuestionRatingScaleFragment extends Fragment implements ResponseProvider{
	int index,total;
	EditText singleTextBox;
	QuestionRatingScale q;
	ArrayList<RadioGroup>groups;
	public void setFont(View rootView)
	{	    
	    FontChangeCrawler fontChanger = new FontChangeCrawler(getActivity().getAssets(), "font/montserratregular.ttf");
	    fontChanger.replaceFonts((ViewGroup) rootView);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		q = (QuestionRatingScale) getArguments().get("QUESTION");
		index = getArguments().getInt("INDEX");
		index--;
		total=getArguments().getInt("TOTAL");
		View rootView= inflater.inflate(R.layout.fragment_rating_scale, container, false);
		TableLayout tab = new TableLayout(getActivity());
		tab.setScrollContainer(true);
		groups = new ArrayList<RadioGroup>();
		// Make the view
		LinearLayout titleslayout  = (LinearLayout)rootView.findViewById(R.id.titlesLayout);
		LinearLayout rowslayout = (LinearLayout)rootView.findViewById(R.id.rowstitlesLayout);
		LinearLayout radioLayout = (LinearLayout)rootView.findViewById(R.id.radioLayout);
		float scale = getResources().getDisplayMetrics().density;
		int dpAsPixels = (int) (15*scale + 0.5f);
		for(Option p : q.getRowOption()){
			TextView row = new TextView(getActivity());
			LinearLayout rowLayout = new LinearLayout(getActivity());
			rowLayout.setOrientation(LinearLayout.HORIZONTAL);
			row.setText(p.getText());
			row.setPadding(0, dpAsPixels, 0, 0);
			rowLayout.addView(row);
			
			
			RadioGroup grp = new RadioGroup(getActivity());
			grp.setTag(p);
			groups.add(grp);
			grp.setOrientation(LinearLayout.HORIZONTAL);
			for (Option colop : q.getColumnOption()){
				RadioButton radiobtn = new RadioButton(getActivity());
				radiobtn.setTag(colop);
				grp.addView(radiobtn);
			}
			radioLayout.addView(grp);
			rowslayout.addView(rowLayout);
		}
		for(int i = 0 ; i<q.getColumnOption().length;i++){
			VerticalTextView title = new VerticalTextView(getActivity(), null) ;
			title.setText(q.getColumnOption()[i].getText());
			title.setPadding(0, dpAsPixels,dpAsPixels, 0);
			titleslayout.addView(title);
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
		ResponseRatingScale r = new ResponseRatingScale(q);
		for (RadioGroup grp : groups){
			RadioButton selected = (RadioButton) getView().findViewById(grp.getCheckedRadioButtonId());
			if(selected!=null){
				r.addResponse((Option)grp.getTag(),(Option)selected.getTag());
			}
		}
		Log.i("Response","Resp of "+q.getText()+"is "+r.getJsonString());
		return r;
	}

}
