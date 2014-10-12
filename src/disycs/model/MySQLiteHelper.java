package disycs.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import disycs.model.Questionnaire.CATEGORY;
import disycs.model.Questionnaire.STATE;

public class MySQLiteHelper extends SQLiteOpenHelper {
	private final static int dataBaseVersion = 1;
	private final static String dataBaseName="QuizmooDB";
	// TABLE USER
	private final String USER_TABLE_NAME = "User";
	private final String KEY_USER_NAME = "userName";
	private final String KEY_PASSWORD= "password";
	//TABLE QUESTIONNAIRE
	private final String QUESTIONNAIRE_TABLE_NAME = "Questionnaire";
	private final String KEY_ID_QUESTIONNAIRE="idQuest";
	private final String KEY_TITLE_QUESTIONNAIRE ="titQuest";
	private final String KEY_DESC_QUESTIONNAIRE = "descQuest";
	private final String KEY_DAT_CR_QUESTIONNAIRE="datCrQuest";
	private final String KEY_HASH_QUESTIONNAIRE = "hashQuest";
	private final String KEY_STATE_QUESTIONNAIRE="stateQuest";
	private final String KEY_CAT_QUESTIONNAIRE="categQuest";
	private final String KEY_QUESTIONS="Questions";
	private final String USER_COLUMNS[] = {KEY_USER_NAME,KEY_PASSWORD};
	
	
	public static enum SYNC_STATE{
		EXIST,EXIST_AND_SYNC,DO_NOT_EXIST
	}
	public MySQLiteHelper(Context context) {
		super(context, dataBaseName, null, dataBaseVersion);
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_USER_TABLE = " CREATE TABLE User ( " +
				"userName TEXT PRIMARY KEY NOT NULL,"+
				"password TEXT NOT NULL)";
		db.execSQL(CREATE_USER_TABLE);
		String CREATE_QUESTIONNAIRE_TABLE="CREATE TABLE Questionnaire ("+
				"idQuest	INT		PRIMARY KEY NOT NULL,"+
				"titQuest	TEXT 	NOT NULL,"+
				"descQuest	TEXT	,"+
				"datCrQuest TEXT	,"+
				"hashQuest	TEXT 	NOT NULL,"+
				"stateQuest TEXT	,"+
				"categQuest TEXT	," +
				KEY_QUESTIONS+" TEXT ,"+
				KEY_USER_NAME+" TEXT NOT NULL,"+
				"FOREIGN KEY ("+KEY_USER_NAME+") REFERENCES "+ USER_TABLE_NAME+"("+KEY_USER_NAME+"));";
		db.execSQL(CREATE_QUESTIONNAIRE_TABLE);
	}
	public void addUser(User user){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_USER_NAME,user.getUserName());
		values.put(KEY_PASSWORD, user.getPassword());
		db.insert(USER_TABLE_NAME, null, values);
		db.close();
	}
	public boolean checkUser(String userName){
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(USER_TABLE_NAME,
				USER_COLUMNS,
				KEY_USER_NAME+"= ?",
				new String[] {userName},
				null,null,null,null);
		if ((cursor!=null)&&(cursor.getCount()>0)) {
			db.close();
			return true;
		}
		db.close();
		return false;
	}
	public boolean loginOfflineUserSession (String userName , String password){
		SQLiteDatabase db = this.getReadableDatabase();
		String passwordHash = MyCryptoHelper.computePasswordHash(password);
		String queryString = "SELECT * " +
				"FROM "+USER_TABLE_NAME +
				" WHERE " +
				KEY_USER_NAME +"='"+userName +
				"' AND "+ KEY_PASSWORD+"='"+passwordHash+"'";
		Cursor cursor = db.rawQuery(queryString,null);
		if ((cursor != null)&&(cursor.getCount()>0)) {
			User.setUser(userName, passwordHash);
			db.close();
			return true;
		}
		User.deleteUser();
		db.close();
		return false;		
	}
	private ContentValues questionnaireContVal(Questionnaire Q){
	SimpleDateFormat 	df = new SimpleDateFormat("yyyy-MM-DD hh:mm:ss");
	ContentValues contentValues = new ContentValues();
	contentValues.put(KEY_ID_QUESTIONNAIRE, Q.getId());
	contentValues.put(KEY_TITLE_QUESTIONNAIRE, Q.getTitle());
	contentValues.put(KEY_DESC_QUESTIONNAIRE,Q.getDescription());
	contentValues.put(KEY_DAT_CR_QUESTIONNAIRE,df.format(Q.getDateOfCreation()));
	contentValues.put(KEY_HASH_QUESTIONNAIRE, Q.getHash());
	contentValues.put(KEY_STATE_QUESTIONNAIRE, Q.getState().getCode());
	contentValues.put(KEY_CAT_QUESTIONNAIRE, Q.getCategory().getCode());
	contentValues.put(KEY_USER_NAME, User.getUser().getUserName());
	contentValues.put(KEY_QUESTIONS,Q.getThumbnail());
	Log.i("date format",df.format(Q.getDateOfCreation()));
	return contentValues;
}
	public void addQuestionnaire(Questionnaire Q){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues contentValues = questionnaireContVal(Q);
		db.insert(QUESTIONNAIRE_TABLE_NAME, null, contentValues);
		db.close();
	}
	private Cursor getQuestionnaie(Questionnaire Q){
		SQLiteDatabase db = this.getReadableDatabase();
		String queryString ="SELECT * " +
				"FROM "+ QUESTIONNAIRE_TABLE_NAME+
				" WHERE " +
				KEY_USER_NAME+" = '"+User.getUser().getUserName()+
				"' AND " +
				KEY_ID_QUESTIONNAIRE+" = '"+Q.getId()+"'";
		Cursor cursor = db.rawQuery(queryString, null);
		return cursor;
	}
	public SYNC_STATE getQuestionnaireState(Questionnaire Q){
		Cursor cursor= getQuestionnaie(Q);
		if ((cursor != null)&&(cursor.getCount()>0)) {
			cursor.moveToNext();
			if(cursor.getString(4).equals(Q.getHash())){
				return SYNC_STATE.EXIST_AND_SYNC;
			}else{
				return SYNC_STATE.EXIST;
			}
		}else {
			return SYNC_STATE.DO_NOT_EXIST;
		}
		
	}
	public boolean updateQuestionnaire(Questionnaire Q){
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = getQuestionnaie(Q);
		if ((cursor != null)&&(cursor.getCount()>0)) {
			ContentValues contentValues = questionnaireContVal(Q);
			db.update(QUESTIONNAIRE_TABLE_NAME, contentValues, KEY_ID_QUESTIONNAIRE+"=?", new String[]{cursor.getString(0)});
			db.close();
			return true;
		}
		return false;
	}
	public int getResponseCount(Questionnaire questionnaire) {
		// TODO Auto-generated method stub
		return 0;
	}
	public void delQuestionnaire(Questionnaire q) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(QUESTIONNAIRE_TABLE_NAME, KEY_ID_QUESTIONNAIRE+" = ?", new String[]{q.id+""});
		db.close();
	}
	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}
	public ArrayList<Questionnaire> getAllQuestionnaires(STATE state) throws ParseException {
		ArrayList<Questionnaire> list = new ArrayList<Questionnaire>();
		SQLiteDatabase db = this.getReadableDatabase();
		String queryString ="SELECT * " +
				"FROM "+ QUESTIONNAIRE_TABLE_NAME+
				" Where "+KEY_STATE_QUESTIONNAIRE+" = '"+state.getCode()+"'";
		Cursor cursor = db.rawQuery(queryString, null);
		while (cursor.moveToNext()){
			Log.i("Cursor offline ","offline next");
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-DD hh:mm:ss");
			Questionnaire q = new Questionnaire(cursor.getInt(0), 
					cursor.getString(1), 
					cursor.getString(2), 
					df.parse(cursor.getString(3)), 
					cursor.getString(4),
					STATE.get(cursor.getString(5)),
					CATEGORY.get(cursor.getString(6)),
					cursor.getString(7));
			list.add(q);
		}
		return list;
		
		
	}
	
}
