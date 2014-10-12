package disycs.quizmo.navdrawer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import disycs.model.Questionnaire;
import disycs.quizmo.R;
import disycs.quizmo.design.Color;
import disycs.quizmo.design.FontChangeCrawler;

public class DescriptionFragment extends Fragment {
	TextView title;
	TextView description;
	public void setFont(View rootView)
	{	    
	    FontChangeCrawler fontChanger = new FontChangeCrawler(getActivity().getAssets(), "font/montserratregular.ttf");
	    fontChanger.replaceFonts((ViewGroup)rootView);
	}
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_description, container, false);
        title = (TextView)rootView.findViewById(R.id.txt_qest_title);
        description=(TextView)rootView.findViewById(R.id.txt_ques_description);
        
        Questionnaire q = (Questionnaire) getArguments().get("QUESTIONNAIRE");
        LinearLayout outer = (LinearLayout)rootView.findViewById(R.id.question_outer_layout);
        outer.setBackgroundColor(getActivity().getResources().getColor(
        		Color.color[
        		            q.getCategory().ordinal()]));
        title.setText(q.getTitle());
        description.setText(q.getDescription());
        setFont(rootView);
        return rootView;
    }

	
}
