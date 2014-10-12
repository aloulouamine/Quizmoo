package disycs.quizmo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import discys.API.HttpProxy;
import disycs.model.MySQLiteHelper;
import disycs.model.User;
import disycs.quizmo.design.FontChangeCrawler;

public class MainActivity extends Activity {
	public static enum MODE{ONLINE,OFFLINE}
	public static MODE Mode;
	Animation slideUp;
	ProgressBar LoginProgressBar;
	EditText TxtLogin,TxtPwd;
	Button BtnLogin;
	TextView LabelFeedback,title;
	LinearLayout LoginForm;
	CheckBox syncChbx;
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
		setContentView(R.layout.activity_main);
		
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		slideUp = AnimationUtils.loadAnimation(this, R.anim.slideup);
		
		TxtLogin = (EditText)findViewById(R.id.TxtUserName);
		
		TxtPwd = (EditText)findViewById(R.id.TxtPassword);
		
		LoginProgressBar = (ProgressBar) findViewById(R.id.LoadingBar);
		LoginProgressBar.setVisibility(View.GONE);
		BtnLogin = (Button)findViewById(R.id.BtnLogin);
		
		LabelFeedback = (TextView) findViewById(R.id.LabelFeedback);
		syncChbx = (CheckBox)findViewById(R.id.checkBoxSync);
		title = (TextView) findViewById(R.id.txtapptitle);
		LoginForm =(LinearLayout)findViewById(R.id.layout_log_form);
		BtnLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				LoginProgressBar.setVisibility(View.VISIBLE);
				LoginForm.setAnimation(slideUp);
				@SuppressWarnings("rawtypes")
				class Login extends AsyncTask<String, Enum, Boolean>{
					private boolean isNetworkAvailable() {
					    ConnectivityManager connectivityManager 
					          = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
					    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
					    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
					}
					@Override
					protected void onPreExecute() {
						super.onPreExecute();
						LabelFeedback.setVisibility(View.VISIBLE);
						LabelFeedback.setText(R.string.tryConnection);
					}

					@Override
					protected Boolean doInBackground(String... params) {
						if(isNetworkAvailable()){
							// Try the online mode 
							publishProgress(MODE.ONLINE);
							HttpProxy.Authentification(params[0], params[1]);
							
						}else{
							// Try the offline mode 
							publishProgress(MODE.OFFLINE);
							MySQLiteHelper db = new MySQLiteHelper(getApplicationContext());
							db.loginOfflineUserSession(params[0], params[1]);
							
						}
						return null;
					}
					@Override
					protected void onProgressUpdate(Enum... values) {
						switch ((MODE)values[0]){
						case ONLINE : 
							LabelFeedback.setText(R.string.loggingin);
							Mode=MODE.ONLINE;
						break;
						case OFFLINE : 
							LabelFeedback.setText(R.string.offlineLogin);
							Mode=MODE.OFFLINE;
						break;
						}
					}
					@Override
					protected void onPostExecute(Boolean result) {
						LoginProgressBar.setVisibility(View.GONE);
						SharedPreferences settings = getSharedPreferences("Quizmoo", 0);
						SharedPreferences.Editor editor = settings.edit();
						if(User.getUser()==null){
							LabelFeedback.setText(R.string.onlineFailure);
						}
						else{
							if (Mode==MODE.ONLINE ){
								if(syncChbx.isChecked()){
									MySQLiteHelper db = new MySQLiteHelper(getApplicationContext());
									if(db.checkUser(User.getUser().getUserName())==false){
										db.addUser(User.getUser());
									}
									editor.putBoolean("SYNC", true);
									editor.commit();
								}else{
									editor.putBoolean("SYNC", false);
									editor.commit();
								}
							}
							
							Intent intent = new Intent(getApplicationContext(),QuestionnairesActivity.class);
							startActivity(intent);
							finish();
						}
					}
				}
				Login asyncLog = new Login();
				asyncLog.execute(TxtLogin.getText().toString(),
						TxtPwd.getText().toString());
			}			
		});
		
	}
	
}