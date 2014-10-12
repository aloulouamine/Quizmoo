package disycs.quizmo.navdrawer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import disycs.model.questions.QuestionSingleTextBox;
import disycs.model.responses.Response;
import disycs.model.responses.ResponseProvider;
import disycs.model.responses.ResponseSingleTextBox;
import disycs.quizmo.R;
import disycs.quizmo.design.FontChangeCrawler;

public class QuestionSingleTextBoxFragment extends Fragment  implements ResponseProvider{
	
	QuestionSingleTextBox q;
	int index,total;
	EditText singleTextBox;
	ResponseSingleTextBox response ;
	public void setFont(View rootView)
	{	    
	    FontChangeCrawler fontChanger = new FontChangeCrawler(getActivity().getAssets(), "font/montserratregular.ttf");
	    fontChanger.replaceFonts((ViewGroup) rootView);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		q = (QuestionSingleTextBox) getArguments().get("QUESTION");
		index = getArguments().getInt("INDEX");
		index--;
		total=getArguments().getInt("TOTAL");
		View rootView=null;
		rootView= inflater.inflate(R.layout.fragment_single_txtbox, container, false);
		singleTextBox=(EditText)rootView.findViewById(R.id.txt_single_answer);
		LinearLayout outer = (LinearLayout)rootView.findViewById(R.id.question_outer_layout);
        outer.setBackgroundColor(getActivity().getResources().getColor(getArguments().getInt("COLOR")));
		TextView title = (TextView)rootView.findViewById(R.id.txt_ques_title);
        title.setText(index+"/"+total+") "+q.getText());
        setFont(rootView);
		return rootView;
	}

	@Override
	public Response getResponse() {
		ResponseSingleTextBox r = new ResponseSingleTextBox(q,singleTextBox.getText().toString());
		Log.i("Response","Resp of "+q.getText()+"is "+r.getJsonString());
		return r;
	}
	
	
	
}
