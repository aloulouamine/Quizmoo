package disycs.quizmo.navdrawer;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import disycs.model.Questionnaire;
import disycs.model.User;
import disycs.model.questions.Question;
import disycs.model.questions.QuestionFactory;
import disycs.model.questions.QuestionMultipleChoice;
import disycs.model.questions.QuestionMultipleTextBox;
import disycs.model.questions.QuestionPictorial;
import disycs.model.questions.QuestionRatingScale;
import disycs.model.questions.QuestionSelectField;
import disycs.model.questions.QuestionSingleTextBox;
import disycs.model.responses.Response;
import disycs.model.responses.ResponseProvider;
import disycs.quizmo.QuestionnairesActivity;
import disycs.quizmo.R;
import disycs.quizmo.design.Color;
import disycs.quizmo.design.DepthPageTransformer;
import disycs.quizmo.design.FontChangeCrawler;

public class QuestionDetailActivity extends FragmentActivity {
	DrawerLayout drawerLayout ; 
	ListView drawerList;
	ActionBarDrawerToggle actionBarDrawerToggle ;
	// Drawer title
	String drawerTitle;
	Questionnaire q ;
	// App title
	String title;
	ArrayList<Question> navDrawerQuestions;
	NavDrawerListAdapter adapter;
	@Override
	public void setContentView(int view)
	{
	    super.setContentView(view);
	    FontChangeCrawler fontChanger = new FontChangeCrawler(getAssets(), "font/montserratregular.ttf");
	    fontChanger.replaceFonts((ViewGroup)this.findViewById(android.R.id.content));
	}
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_question_detail);
		title = drawerTitle = getTitle().toString();
		// loading slide menu items
		q = (Questionnaire) getIntent().getExtras().get(QuestionnairesActivity.QUESTIONNAIRE_EXTRA);
		QuestionFactory qFactory = new QuestionFactory();
		navDrawerQuestions = qFactory.buildQuestions(q.getThumbnail());
		drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
		drawerList=(ListView)findViewById(R.id.list_slidermenu);
		// Adding questions to navDrawer :
		drawerList.setOnItemClickListener(new SlideMenuClickListener());
		adapter= new NavDrawerListAdapter(getApplicationContext(),navDrawerQuestions);
		drawerList.setAdapter(adapter);
		
		// enabling actionbaricons
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		
		actionBarDrawerToggle = new ActionBarDrawerToggle(this,
				drawerLayout,
				R.drawable.ic_drawer,
				R.string.app_name,
				R.string.app_name
		){
			public void onDrawerClosed(View view){
				getActionBar().setTitle(title);
				invalidateOptionsMenu();
			}
			
			public void onDrawerOpened(View drawerView){
				getActionBar().setTitle(drawerTitle);
				invalidateOptionsMenu();
			}
		};
		drawerLayout.setDrawerListener(actionBarDrawerToggle);
		
	}
	
	ViewPager mViewPager;
	ScreenSlidePagerAdapter pagerAdapter;
	@Override
	protected void onStart(){
		super.onStart();
		

		//Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.quest_pager);
		pagerAdapter= new ScreenSlidePagerAdapter(getSupportFragmentManager());
		mViewPager.setOffscreenPageLimit(navDrawerQuestions.size()+1);
		mViewPager.setAdapter(pagerAdapter);
		mViewPager.setPageTransformer(true, new DepthPageTransformer());
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				// TODO Auto-generated method stub
				if(position>1){
					// Set the drawer list item
					drawerList.setItemChecked(position-1,true);
					
				}
				else{
				drawerList.setItemChecked(0, false);
				}
				
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
			
				
			}
		});
	}
	
	 private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter { 
		 SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();
	        public ScreenSlidePagerAdapter(android.support.v4.app.FragmentManager fm) {
	            super(fm);
	        }
	       
	        @Override
	        public android.support.v4.app.Fragment getItem(int position) {
	            if(position==0){
	            	DescriptionFragment fragment = new DescriptionFragment();
	    			Bundle bundle = new Bundle();
	    			bundle.putSerializable("QUESTIONNAIRE", q);
	    			fragment.setArguments(bundle);
	            	return fragment;
	            }
	            Question quest = navDrawerQuestions.get(position-1);
	            Fragment fragment ;
	            if(quest instanceof QuestionSingleTextBox){
	            	fragment = new QuestionSingleTextBoxFragment();
	            }
	            else if(quest instanceof QuestionSelectField){
	            	fragment = new QuestionSelectFieldFragment();
	            	Log.i("SelectFieldQuestion",quest.getText());
	            }
	            else if(quest instanceof QuestionMultipleTextBox){
	            	fragment = new QuestionMultipleTextBoxFragment();
	            }
	            else if(quest instanceof QuestionPictorial){
	            	fragment = new QuestionPictorialFragment();
	            }
	            else if(quest instanceof QuestionRatingScale){
	            	fragment = new QuestionRatingScaleFragment();
	            }
	            else if (quest instanceof QuestionMultipleChoice){
	            	fragment = new QuestionMultipleChoiceFragment();
	            }
	            else {
	            	fragment = new QuestionRankigFragment();
	            }
				Bundle bundle = new Bundle();
				bundle.putParcelable("QUESTION",navDrawerQuestions.get(position-1));
				bundle.putInt("COLOR", Color.color[q.getCategory().ordinal()]);
				bundle.putInt("INDEX",position+1);
				bundle.putInt("TOTAL",adapter.getCount());
				fragment.setArguments(bundle);
	            return fragment;
	        }

	        @Override
	        public int getCount() {
	            return navDrawerQuestions.size()+1;
	        }
	        
	        @Override
	        public Object instantiateItem(ViewGroup container, int position) {
	            Fragment fragment = (Fragment) super.instantiateItem(container, position);
	            registeredFragments.put(position, fragment);
	            return fragment;
	        }

	        @Override
	        public void destroyItem(ViewGroup container, int position, Object object) {
	            registeredFragments.remove(position);
	            super.destroyItem(container, position, object);
	        }

	        public Fragment getRegisteredFragment(int position) {
	            return registeredFragments.get(position);
	        }
	    }
	public class SlideMenuClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			displayView(position);
			drawerLayout.closeDrawer(drawerList);
		}

	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	 if(actionBarDrawerToggle.onOptionsItemSelected(item)){
		 return true;
	 }
	 switch(item.getItemId()){
	 case R.id.logout:
		logout();
	 case R.id.submit:
		 //TODO
		 ArrayList<Response> responses = new ArrayList<Response>();
		 int i;
		 for (i= 1 ; i< pagerAdapter.getCount();i++){
			 ResponseProvider f = (ResponseProvider)pagerAdapter.getRegisteredFragment(i);
			 Response current = f.getResponse();
			 if(current.isValid()){
				 responses.add(current);
			 }else{
				 mViewPager.setCurrentItem(i, true);
				 Toast.makeText(getApplicationContext(), "Answer this", Toast.LENGTH_SHORT).show();
				 break;
			 }
		 }
		 if(i==pagerAdapter.getCount()){
			 //TODO 
			 
			 this.finish();
		 }
		 
	 default:
		 return super.onOptionsItemSelected(item);
	 }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.questionnaire_menu, menu);
		return true;
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		//boolean drawerOpen = drawerLayout.isDrawerOpen(drawerList);
		return super.onPrepareOptionsMenu(menu);
	}
	private void displayView(int position) {
		mViewPager.setCurrentItem(position+1);
	
	}
	
	@Override
	public void setTitle(CharSequence title){
		this.title = title.toString();
		getActionBar().setTitle(title);
	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState){
		super.onPostCreate(savedInstanceState);
		actionBarDrawerToggle.syncState();
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig){
		super.onConfigurationChanged(newConfig);
		actionBarDrawerToggle.onConfigurationChanged(newConfig);
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
	
}
