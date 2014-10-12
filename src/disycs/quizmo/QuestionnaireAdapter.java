package disycs.quizmo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import disycs.model.MySQLiteHelper;
import disycs.model.MySQLiteHelper.SYNC_STATE;
import disycs.model.Questionnaire;
import disycs.quizmo.design.Color;
import disycs.quizmo.design.FontChangeCrawler;

public class QuestionnaireAdapter extends ArrayAdapter<Questionnaire>{
	private final List<Questionnaire> list;
	private final Activity context;
	
	private final boolean sync;
	public QuestionnaireAdapter(Activity context, 
			List<Questionnaire> list) {
		super(context,R.layout.questionnaire_item, list);
		this.context=context;
		this.list= list;
		SharedPreferences settings = context.getSharedPreferences("Quizmoo", 0);
		this.sync=settings.getBoolean("SYNC", true);		
	}

	static class ViewHolder{
		protected TextView title,description, date,label;
		protected RelativeLayout label_layout;		
		protected ImageView show_more,attach;
	}
	@Override 
	public View getView(int position , View convertView , ViewGroup parent){
		View view = null;
		if (convertView == null){
			LayoutInflater inflator = context.getLayoutInflater();
			view = inflator.inflate(R.layout.questionnaire_item, null);
			FontChangeCrawler fontChanger = new FontChangeCrawler(context.getAssets(), "font/montserratregular.ttf");
			fontChanger.replaceFonts((ViewGroup)view);
			final ViewHolder viewHolder = new ViewHolder();
			viewHolder.title = (TextView)view.findViewById(R.id.txtapptitle);
			viewHolder.description= (TextView)view.findViewById(R.id.txt_descriptioin_quest_item);
			viewHolder.date = (TextView)view.findViewById(R.id.txt_date_quest_item);
			viewHolder.label=(TextView)view.findViewById(R.id.txt_item_label);
			viewHolder.label_layout=(RelativeLayout)view.findViewById(R.id.label_layout);
			viewHolder.show_more=(ImageView)view.findViewById(R.id.img_showmore_listitem);
			viewHolder.attach= (ImageView)view.findViewById(R.id.imgattach);
			view.setTag(viewHolder);
		}else{
				view=convertView;
			}
		ViewHolder holder= (ViewHolder)view.getTag();
		updateViewHolder(holder,sync,position);	
		return view;
	}
	private void updateViewHolder(ViewHolder holder,  boolean sync, int position){
		holder.title.setText(list.get(position).getTitle());
		holder.description.setText(list.get(position).getDescription());
		DateFormat df = new SimpleDateFormat("EEE, MMM d, ''yy");
		holder.date.setText(df.format(list.get(position).getDateOfCreation()));
		holder.label_layout.setBackgroundColor(context.getResources().getColor(Color.color
				[list.get(position).getCategory().ordinal()]));
		holder.label.setText(context.getResources().getStringArray(R.array.category)
				[list.get(position).getCategory().ordinal()]);
		holder.attach.setVisibility(View.GONE);
		if(sync){
			
			MySQLiteHelper db =new  MySQLiteHelper(context);
			SYNC_STATE syncState = db.getQuestionnaireState(list.get(position));
			int responses = db.getResponseCount(list.get(position));
			if(syncState==SYNC_STATE.EXIST_AND_SYNC){
				holder.attach.setVisibility(View.VISIBLE);
			}
			else if(syncState==SYNC_STATE.EXIST){
				db.updateQuestionnaire(list.get(position));
			}
			
			holder.show_more.setTag(
					new QuestionnaireViewInformations(list.get(position),
					syncState,
					responses));		
		}else{
			holder.show_more.setVisibility(View.GONE);
		}
	}
	class QuestionnaireViewInformations{
		Questionnaire Q ;
		SYNC_STATE syncState;
		int responses;
		public QuestionnaireViewInformations(Questionnaire q,
				SYNC_STATE syncState, int responses) {
			Q = q;
			this.syncState = syncState;
			this.responses = responses;
		}
		public Questionnaire getQ() {
			return Q;
		}
		public void setQ(Questionnaire q) {
			Q = q;
		}
		public SYNC_STATE getSyncState() {
			return syncState;
		}
		public void setSyncState(SYNC_STATE syncState) {
			this.syncState = syncState;
		}
		public int getResponses() {
			return responses;
		}
		public void setResponses(int responses) {
			this.responses = responses;
		}
		
	}
}