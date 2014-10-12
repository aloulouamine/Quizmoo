package disycs.quizmo.navdrawer;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import disycs.model.questions.Question;
import disycs.quizmo.R;

public class NavDrawerListAdapter extends BaseAdapter {
	Context context ;
	ArrayList<Question> navItems;
	public NavDrawerListAdapter(Context applicationContext,
			ArrayList<Question> navDrawerQuestions) {
		this.context=applicationContext;
		this.navItems=navDrawerQuestions;
	}

	@Override
	public int getCount() {
		return navItems.size();
	}

	@Override
	public Object getItem(int position) {
		return navItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null){
			LayoutInflater mInflater = (LayoutInflater)
					context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView=mInflater.inflate(R.layout.drawer_list_item, null);
		}
		ImageView imgIcon = (ImageView)convertView.findViewById(R.id.icon);
		TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
		imgIcon.setImageDrawable(context.getResources().getDrawable(navItems.get(position).getIcon()));
		txtTitle.setText(navItems.get(position).getText());
		
		return convertView;
	}

}
