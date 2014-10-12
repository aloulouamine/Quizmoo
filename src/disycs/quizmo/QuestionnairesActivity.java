package disycs.quizmo;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import discys.API.HttpProxy;
import disycs.model.MySQLiteHelper;
import disycs.model.MySQLiteHelper.SYNC_STATE;
import disycs.model.Questionnaire;
import disycs.model.Questionnaire.CATEGORY;
import disycs.model.Questionnaire.STATE;
import disycs.model.Token;
import disycs.model.User;
import disycs.quizmo.MainActivity.MODE;
import disycs.quizmo.QuestionnaireAdapter.QuestionnaireViewInformations;
import disycs.quizmo.design.FontChangeCrawler;
import disycs.quizmo.navdrawer.QuestionDetailActivity;

public class QuestionnairesActivity extends ActionBarActivity implements
		ActionBar.TabListener {	
	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a {@link FragmentPagerAdapter}
	 * derivative, which will keep every loaded fragment in memory. If this
	 * becomes too memory intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	
	public static String QUESTIONNAIRE_EXTRA="Questionnaire";
	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;
	ViewPager mViewPager;
	@Override
	public void setContentView(int view)
	{
	    super.setContentView(view);
	    FontChangeCrawler fontChanger = new FontChangeCrawler(getAssets(), "font/montserratregular.ttf");
	    fontChanger.replaceFonts((ViewGroup)this.findViewById(android.R.id.content));
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_questionnaires);
		
		// Set up the action bar.
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayHomeAsUpEnabled(false);
		
		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setOffscreenPageLimit(3);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
	}
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if(id==R.id.logout){
			logout();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a PlaceholderFragment (defined as a static inner class
			// below).
			String section = "";
			if(position==0){
				section = STATE.ONGOING.getCode();
			}else if(position ==1 ){
				section = STATE.CLOSED.getCode();
			}
			else {
				section = STATE.DRAFT.getCode();
			}
			return PlaceholderFragment.newInstance(getResources().getStringArray(R.array.state)[position],section);
		}

		@Override
		public int getCount() {
			Resources res = getResources();
			return (res.getStringArray(R.array.state)).length;
			
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			Resources res = getResources();
			String [] titles =res.getStringArray(R.array.state);
			return titles[position].toUpperCase(l);
		}
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment  {
		class GridLoader extends AsyncTask<Questionnaire, Integer, ArrayList<Questionnaire>>{
			QuestionnaireAdapter adapter;
			STATE state;
			CATEGORY category;
			ProgressBar pb;
			public GridLoader( QuestionnaireAdapter adapter , STATE state, CATEGORY category,ProgressBar pb){
				this.adapter= adapter;
				this.state= state;
				this.category = category;
				this.pb= pb;
			}
			
			@Override
			protected void onPreExecute() {
				pb.setVisibility(View.VISIBLE);
			};
			
			@Override
			protected ArrayList<Questionnaire> doInBackground(Questionnaire... params) {
				
				return HttpProxy.getQuestions(Token.getToken(), state,category,1); 
			}
			@Override
			protected void onPostExecute(ArrayList<Questionnaire> result) {
				super.onPostExecute(result);
				
				Log.i("Result",""+result.size());
				adapter.addAll(result);
				adapter.notifyDataSetChanged();
				pb.setVisibility(View.GONE);
			}
		}

		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_TYPE = "section_type";
		private static final String ARG_SECTION_NAME = "section_name";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static PlaceholderFragment newInstance(String sectionType , String sectionName) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putString(ARG_SECTION_TYPE, sectionType);
			args.putString(ARG_SECTION_NAME,sectionName);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}
		QuestionnaireAdapter adapter;
		GridLoader asyncGridLoader;
		ProgressBar pb;
		GridView gridView;
		CATEGORY category;
		STATE state;
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_questionnaires,
					container, false);
			TextView textView = (TextView) rootView
					.findViewById(R.id.section_label);
			gridView = (GridView)rootView
					.findViewById(R.id.GRIDquest);
			pb= (ProgressBar)rootView.
					findViewById(R.id.progressGrid);
			category =CATEGORY.ALL;
			state=STATE.get(getArguments().getString(ARG_SECTION_NAME));
			adapter = new QuestionnaireAdapter(getActivity(),new ArrayList<Questionnaire>());
			//TODO
			if(!(MainActivity.Mode==MODE.ONLINE)){
				asyncGridLoader= new GridLoader(adapter,state,category,pb);
				asyncGridLoader.execute();
			}
			else{
				MySQLiteHelper db = new MySQLiteHelper(getActivity());
				
				try {
					
					ArrayList<Questionnaire>list = db.getAllQuestionnaires(state);
					Toast.makeText(getActivity(),"Offline Mode", Toast.LENGTH_LONG).show();
					adapter.addAll(list);
					adapter.notifyDataSetChanged();
				} catch (ParseException e) {
					Log.i("Parse error",e.toString());
				}
				
			}
			gridView.setAdapter(adapter);
			gridView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> adpt, View view,
						int position, long arg3) {
					Intent intent = new Intent (getActivity(), QuestionDetailActivity.class);
					intent.putExtra(QUESTIONNAIRE_EXTRA,(Questionnaire)adpt.getItemAtPosition(position));
					startActivity(intent);
				}
			});
			
			textView.setText(getArguments().getString(
					ARG_SECTION_TYPE).toUpperCase());
			return rootView;
		}
	}
	
	
	private void logout(){
		AlertDialog.Builder alertDialog  = new AlertDialog.Builder(this);
		alertDialog.setCancelable(false);
		alertDialog.setTitle(R.string.logout);
		alertDialog.setMessage(R.string.logoutMsg);
		alertDialog.setNegativeButton(R.string.alertNegativeAnswer, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				
			}
		});
		alertDialog.setPositiveButton(R.string.alertPositiveAnswer, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				User.deleteUser();
				finish();
			}
		});
		alertDialog.show();
		
	}
	
	
	
	public void showListItemPopup(final View v){
		PopupMenu popupMenu = new PopupMenu(QuestionnairesActivity.this, v);
		popupMenu.getMenuInflater().inflate(R.menu.item_popup, popupMenu.getMenu());
		adaptMenu(popupMenu,v);
		popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				Toast.makeText(QuestionnairesActivity.this,
						item.toString(),
						Toast.LENGTH_LONG).show();
				MySQLiteHelper db = new MySQLiteHelper(QuestionnairesActivity.this);
				PlaceholderFragment phf = getVisibleFragment();
				if(item.getItemId()==R.id.save)
				{					
					db.addQuestionnaire(((QuestionnaireViewInformations)
							v.getTag()).Q);	
					updateAdapter(phf);
				}
				else if(item.getItemId()==R.id.delete){
					db.delQuestionnaire(((QuestionnaireViewInformations)
							v.getTag()).Q);
					updateAdapter(phf);
				}
				
				return true;
			}
		});
	    popupMenu.show();
	}
	private PlaceholderFragment getVisibleFragment(){
		List<Fragment> list =getSupportFragmentManager().getFragments();
		for(Fragment fragment :list){
			if (fragment !=null && fragment.getUserVisibleHint()){
				return (PlaceholderFragment)fragment;
			}
		}
		return null;
	}
	private void updateAdapter(PlaceholderFragment phf){
		if(phf!=null){	phf.adapter.notifyDataSetChanged();	}
	}
	private void adaptMenu(PopupMenu popupMenu, View v){
		//TODO ADAPT THE MENU TO THE VIEW
		QuestionnaireViewInformations QVI = (QuestionnaireViewInformations) v.getTag();
		if(QVI.syncState==SYNC_STATE.DO_NOT_EXIST){
			popupMenu.getMenu().findItem(R.id.delete).setVisible(false);
		}else
		{
			popupMenu.getMenu().findItem(R.id.save).setVisible(false);
		}
		if(getVisibleFragment().getArguments().getString(PlaceholderFragment.ARG_SECTION_NAME)== STATE.ONGOING.getCode()){
		}
	}
}


