package discys.API;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;
import disycs.model.Questionnaire;
import disycs.model.Questionnaire.CATEGORY;
import disycs.model.Questionnaire.STATE;
import disycs.model.Token;
import disycs.model.User;

public class HttpProxy {
	public static void Authentification (String userName , String password){
		
		try {
			ArrayList<NameValuePair> httpBody  = new ArrayList<NameValuePair>();
			httpBody.add(new BasicNameValuePair("grant_type","password"));
			httpBody.add(new BasicNameValuePair("username",userName));
			httpBody.add(new BasicNameValuePair("password",password));
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(ApiURL.AuthAPI);
			httpPost.setEntity(new UrlEncodedFormEntity(httpBody));
			HttpResponse httpResponse = httpClient.execute(httpPost);
			if(httpResponse.getStatusLine().getStatusCode()==200){
				HttpEntity httpEntity = httpResponse.getEntity();
				InputStream is = httpEntity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is,"iso-8859-1"),8);
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line=reader.readLine())!=null){
					sb.append(line+"\n");
				}
				is.close();
				String vracString = sb.toString();
				Log.i("String HTTP", vracString);
				JSONObject jObject = new JSONObject(vracString);		
				JSONObject jUser = jObject.getJSONObject("user");
				JSONObject jToken = jObject.getJSONObject("token");
				JSONObject jExpire = jToken.getJSONObject("Expires");
				User.setUser(jUser.getString("username"), password);
				SimpleDateFormat 	df = new SimpleDateFormat("yyyy-MM-DD hh:mm:ss");
				Token.setToken(jToken.getString("Token"), df.parse(jExpire.getString("date")), jExpire.getInt("timezone_type"), jExpire.getString("timezone"));
				
			}
			else{
				Log.i("Exception", "Authentification Failed from http");
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static ArrayList<Questionnaire> getQuestions (Token token ,STATE state , CATEGORY category, int page){
		ArrayList<Questionnaire> questions = new ArrayList<Questionnaire>();
		try {
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(ApiURL.QuestAPI(state,category,page));
		httpGet.addHeader("x-quizmoo-access-token",Token.getToken().getTokenString());
		HttpResponse httpResponse = httpClient.execute(httpGet);
		if(httpResponse.getStatusLine().getStatusCode()==200){
			HttpEntity httpEntity = httpResponse.getEntity();
			InputStream is = httpEntity.getContent();
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(is,"iso-8859-1"),8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line=reader.readLine())!=null){
				sb.append(line+"\n");
			}
			is.close();
			String vracString = sb.toString();
			Log.i("String HTTP", vracString);
			JSONObject jObject = new JSONObject(vracString);
			JSONArray jArray = jObject.getJSONArray("questionnaires");
			for (int i =0 ; i<jArray.length();i++){
				JSONObject jQuestion = jArray.getJSONObject(i);
				JSONObject jDate = jArray.getJSONObject(i).getJSONObject("dateOfCreation");
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				Date date= format.parse(jDate.getString("date"));
				
				questions.add(new Questionnaire(jQuestion.getInt("id"),
						jQuestion.getString("title"), 
						jQuestion.getString("description"),
						date, 
						jQuestion.getString("hash"),
						state,
						CATEGORY.get(jQuestion.getString("category")),
						getQuestions(jQuestion.getInt("id")
								)));
				}
			}		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return questions;
	}
	
	private static String getQuestions(int id){
		String thumbnail = "";
		try{
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(ApiURL.QuestDetailAPI(id));
			httpGet.addHeader("x-quizmoo-access-token",Token.getToken().getTokenString());
			HttpResponse httpResponse = httpClient.execute(httpGet);
			if(httpResponse.getStatusLine().getStatusCode()==200){
				HttpEntity httpEntity = httpResponse.getEntity();
				InputStream is = httpEntity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is,"iso-8859-1"),8);
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line=reader.readLine())!=null){
					sb.append(line+"\n");
				}
				is.close();
				thumbnail = sb.toString();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return thumbnail ;
		
	}
}