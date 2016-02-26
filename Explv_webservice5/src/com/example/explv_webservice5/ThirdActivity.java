package com.example.explv_webservice5;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ThirdActivity extends Activity {

	TextView txt;
	private ArrayList<String> mainList;
	private LinearLayout mLinearListView;
	boolean isFirstViewClick = false;
	boolean isSecondViewClick = false;
	boolean isThirdViewClick = false;

	public static String TAG_DATA = "DATA";
	public static String TAG_LABEL = "LABEL";
	static int current_level=1;
	String level1_select_cid=null,level2_select_cid=null,level3_select_cid=null;
	String level1_cur_value=null,level2_cur_value=null,level3_cur_value=null;
	TextView mFirstLevelName,mSecondLevelName,mThirdLevelName,mFourthLevelName;
	AsyncTask<String, Void, String> task1,task2,task3,task4;
	HashMap<Integer,String> level1_labelmap=new HashMap<Integer,String>();
	HashMap<Integer,String> level1_categorymap=new HashMap<Integer,String>();
	HashMap<String,Boolean> level1_checkmap=new HashMap<String,Boolean>();
	
	HashMap<Integer,String> level2_labelmap=new HashMap<Integer,String>();
	HashMap<Integer,String> level2_categorymap=new HashMap<Integer,String>();
	HashMap<String,Boolean> level2_checkmap=new HashMap<String,Boolean>();
	
	HashMap<Integer,String> level3_labelmap=new HashMap<Integer,String>();
	HashMap<Integer,String> level3_categorymap=new HashMap<Integer,String>();
	HashMap<String,Boolean> level3_checkmap=new HashMap<String,Boolean>();
	
	HashMap<Integer,String> level4_labelmap=new HashMap<Integer,String>();
	HashMap<Integer,String> level4_categorymap=new HashMap<Integer,String>();
	
	DBHelper dbbHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_third);
		
//		helper=new DBHelper(getApplicationContext());
		mLinearListView = (LinearLayout) findViewById(R.id.linear_ListView);
		final Context context = ThirdActivity.this;
		
		
		try {
			dbbHelper = new DBHelper(ThirdActivity.this);
			dbbHelper.createDataBase();
			dbbHelper.openDataBase();

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		String parentid1 = "1";
		final String parentid2="1";
		String result = null;
		task1=new DemoAsyTask(context, "1",parentid1).execute();
	}
	public class DemoAsyTask extends AsyncTask<String, Void, String> {

		public ProgressDialog progressDialog;
		JSONObject jsonResponse;
		Context context;
		String level, parentid;
		String uri = "http://stg.incentiveweb.com/webservices/TaicoWSController.cfc?method=fnGetCategories&AuthToken=MTA3NjI6OTExMzQ4MTAwOTg3MjU1OjUxNzNGMkFGLTUwNTYtQTA2My1FNEIyM0E3REZGQjQ0NjI2&clientID=5&Level=";

		public DemoAsyTask(Context context, String level, String parentid) {
			this.context = context;
			this.level = level;
			this.parentid = parentid;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			 progressDialog = new ProgressDialog(context,R.style.MyTheme);
			 progressDialog.setMessage("Loading ...");
			 progressDialog.getWindow().setGravity(Gravity.BOTTOM);
			 progressDialog.show();
			 boolean b=dbbHelper.checkDB("1");
				Toast.makeText(context, "true---"+b, 1).show();
		}

		@Override
		protected String doInBackground(String... params) {
			
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(uri + level + "&parentid="+ parentid);
			String result = null;
			try {
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();
				result = EntityUtils.toString(entity, HTTP.UTF_8).trim();
			} catch (ClientProtocolException e) {
			} catch (IOException e) {
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			
			System.out.println("result" + result);
			if (null != progressDialog && progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
			try {
//				String res1=result;
//				long nn=helper.insert("1", res1);
////				if(nn>0)
//				{
//					System.out.println("record inserted");
//				}
//				else
//				{
//					System.out.println("record not inserted");
//				}
				
				try {
					boolean b=dbbHelper.checkDB("1");
					Toast.makeText(context, "true---"+b, 1).show();
					if(b)
					{
					dbbHelper.updateCategory("1", result);
					Toast.makeText(context, "true", 1).show();
					}
					else
					{
						
						Toast.makeText(context, "false", 1).show();
						dbbHelper.insertCategory("1", result);
					}
					
//					int id=Integer.parseInt(parentid);
//					boolean b2=dbbHelper.checkDB(id);
//					Toast.makeText(context, "true---"+b2, 1).show();
//					dbbHelper.insertCategory("1", result);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				JSONObject	jsonobject = new JSONObject(result);
				JSONArray firstArray = jsonobject.getJSONArray("DATA");
				System.out.println("firstArray size" + firstArray.length());
				if(firstArray.length()!=0)
				{
				for (int i = 0; i < firstArray.length(); i++) {

					View mLinearView = (View) ((LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.row_layout5, null);
					mFirstLevelName = (TextView) mLinearView.findViewById(R.id.textViewName);
					final ImageView img1=(ImageView)mLinearView.findViewById(R.id.img);

					JSONObject firstobj = firstArray.getJSONObject(i);
					String l1 = firstobj.getString("LABEL");
					String cid1 = firstobj.getString("CATEGORYID");
					level1_labelmap.put(i, l1);
					level1_categorymap.put(i,cid1);
					level1_checkmap.put(cid1, false);
					//insert cat ids into sqlite
					mFirstLevelName.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							
							Toast.makeText(context,"category id--"+v.getTag().toString(),Toast.LENGTH_LONG).show();
						}
					});
					img1.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							 level1_select_cid=v.getTag().toString();
//							 String name=((TextView) v).getText().toString();
//							 Toast.makeText(context,"label--"+name,Toast.LENGTH_LONG).show();
							 Toast.makeText(context,"category id--"+level1_select_cid,Toast.LENGTH_LONG).show();
							task2=new DemoAsyTask2(context, "2", level1_select_cid).execute();
							Drawable img=context.getResources().getDrawable(R.drawable.down);
//							 mFirstLevelName.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
//							img1.setBackgroundColor(Color.RED);
//							img1.setImageDrawable(img);
							 mFirstLevelName.setBackgroundColor(Color.RED);
							 mFirstLevelName.setTextColor(Color.WHITE);
							level1_cur_value=level1_select_cid;
							System.out.println("position");
//							System.out.println("label"+name);
							System.out.println("category"+level1_select_cid);
							task1.cancel(true);
						}
					});
					mFirstLevelName.setText(l1);
					mFirstLevelName.setTag(cid1);
					img1.setTag(cid1);
					mLinearListView.addView(mLinearView);
				}
				}
				else
				{
					Toast.makeText(context,"No elements found...Error in the Webservice",Toast.LENGTH_LONG).show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	
	public class DemoAsyTask2 extends AsyncTask<String, Void, String> {

		public ProgressDialog progressDialog;
		JSONObject jsonResponse;
		Context context;
		String level, parentid;
		String uri = "http://stg.incentiveweb.com/webservices/TaicoWSController.cfc?method=fnGetCategories&AuthToken=MTA3NjI6OTExMzQ4MTAwOTg3MjU1OjUxNzNGMkFGLTUwNTYtQTA2My1FNEIyM0E3REZGQjQ0NjI2&clientID=5&Level=";

		public DemoAsyTask2(Context context, String level, String parentid) {
			this.context = context;
			this.level = level;
			this.parentid = parentid;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			 progressDialog = new ProgressDialog(context,R.style.MyTheme);
			 progressDialog.getWindow().setGravity(Gravity.BOTTOM);
			 progressDialog.setMessage("Loading ...");
			 progressDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(uri + level + "&parentid="+ parentid);
			String result = null;
			try {
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();
				result = EntityUtils.toString(entity, HTTP.UTF_8).trim();
			} catch (ClientProtocolException e) {
			} catch (IOException e) {
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			
			System.out.println("result" + result);
			if (null != progressDialog && progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
			try {
				try {
//					int id=Integer.parseInt(parentid);
//					boolean b=dbbHelper.checkDB(id);
//					Toast.makeText(context, "true---"+b, 1).show();
//					dbbHelper.insertCategory(""+parentid, result);
					
					
					boolean b=dbbHelper.checkDB(parentid);
					Toast.makeText(context, "true---"+b, 1).show();
					if(b)
					{
					dbbHelper.updateCategory(""+parentid, result);
					Toast.makeText(context, "true", 1).show();
					}
					else
					{
						
						Toast.makeText(context, "false", 1).show();
						dbbHelper.insertCategory(""+parentid, result);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				JSONObject	jsonobject = new JSONObject(result);
				JSONArray secondArray = jsonobject.getJSONArray("DATA");
				System.out.println("secondArray size" + secondArray.length());
//				mLinearListView.clearDisappearingChildren();
				mLinearListView.removeAllViews();
				for(int i=0;i<level1_labelmap.size();i++)
				{
					View mLinearView = (View) ((LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.row_layout5, null);
					mFirstLevelName = (TextView) mLinearView.findViewById(R.id.textViewName);
					ImageView img1=(ImageView)mLinearView.findViewById(R.id.img);
					mFirstLevelName.setText(level1_labelmap.get(i).toString());
					mFirstLevelName.setTag(level1_categorymap.get(i).toString());
					img1.setTag(level1_categorymap.get(i).toString());
					
					if(level1_categorymap.get(i).equals(level1_cur_value))
					{
						Drawable img=context.getResources().getDrawable(R.drawable.down);
//						 mFirstLevelName.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
						 mFirstLevelName.setBackgroundColor(Color.RED);
						 mFirstLevelName.setTextColor(Color.WHITE);
						 img1.setBackgroundColor(Color.RED);
						 img1.setImageDrawable(img);
					}
					mLinearListView.addView(mLinearView);
					System.out.println("level1_categorymap value"+level1_categorymap.get(i).toString());
					System.out.println("cur value"+level1_cur_value);
					
//					mFirstLevelName.setOnClickListener(new OnClickListener() {
					mFirstLevelName.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							
							Toast.makeText(context,"category id--"+v.getTag().toString(),Toast.LENGTH_LONG).show();
						}
					});
					img1.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
//							level1_checkmap.put(cid1, false);
							
							 level1_select_cid=v.getTag().toString();
//							 String name=((TextView) v).getText().toString();
//							 if(level1_checkmap.equals("false"))
//							 {
							 level1_checkmap.put(level1_select_cid, true);
//							 Toast.makeText(context,"label--"+name,Toast.LENGTH_LONG).show();
							 Toast.makeText(context,"category id--"+level1_select_cid,Toast.LENGTH_LONG).show();
							 Drawable img=context.getResources().getDrawable(R.drawable.down);
//							 mFirstLevelName.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
							new DemoAsyTask2(context, "2", level1_select_cid).execute();
							level1_cur_value=level1_select_cid;
							System.out.println("position");
//							System.out.println("label"+name);
							System.out.println("category"+level1_select_cid);
							task2.cancel(true);
//							 }
//							 else
//							 {
//								 level1_checkmap.put(level1_select_cid, true);
//								 new DemoAsyTask(context, "1", "1");
//							 }
						}
					});
					
					if(level1_categorymap.get(i).equals(level1_cur_value))
					{
						level2_labelmap.clear();
						level2_categorymap.clear();
						if(secondArray.length()!=0)
						{
						for (int j = 0; j < secondArray.length(); j++) {

							View mLinearView2 = (View) ((LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.row_layout5, null);
							mSecondLevelName = (TextView) mLinearView2.findViewById(R.id.textViewName);
							ImageView img2=(ImageView)mLinearView2.findViewById(R.id.img);
							JSONObject secondobj = secondArray.getJSONObject(j);
							String l2 = secondobj.getString("LABEL");
							String cid2 = secondobj.getString("CATEGORYID");
							level2_labelmap.put(j, l2);
							level2_categorymap.put(j,cid2);
							
							LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)mSecondLevelName.getLayoutParams();
						     params.setMargins(40, 0, 0, 0); 
						     mSecondLevelName.setLayoutParams(params);
							
							mSecondLevelName.setText(l2);
							mSecondLevelName.setTag(cid2);
							img2.setTag(cid2);
							mLinearListView.addView(mLinearView2);
//							mSecondLevelName.setOnClickListener(new OnClickListener() {
							mSecondLevelName.setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									
									Toast.makeText(context,"category id--"+v.getTag().toString(),Toast.LENGTH_LONG).show();
								}
							});
							img2.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									 level2_select_cid=v.getTag().toString();
//									 String name=((TextView) v).getText().toString();
//									 Toast.makeText(context,"label--"+name,Toast.LENGTH_LONG).show();
									 Toast.makeText(context,"category id--"+level2_select_cid,Toast.LENGTH_LONG).show();
									task3=new DemoAsyTask3(context, "3", level2_select_cid).execute();
//									level1_cur_value=level1_select_cid;
									level2_cur_value=level2_select_cid;
									System.out.println("position");
//									System.out.println("label"+name);
									System.out.println("category"+level2_select_cid);
									task2.cancel(true);
								}
							});
						}
						}
						else
						{
							Toast.makeText(context,"No Children Available",1).show();
						}
					}
//					mLinearListView.addView(mLinearView);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	public class DemoAsyTask3 extends AsyncTask<String, Void, String> {

		public ProgressDialog progressDialog;
		JSONObject jsonResponse;
		Context context;
		String level, parentid;
		String uri = "http://stg.incentiveweb.com/webservices/TaicoWSController.cfc?method=fnGetCategories&AuthToken=MTA3NjI6OTExMzQ4MTAwOTg3MjU1OjUxNzNGMkFGLTUwNTYtQTA2My1FNEIyM0E3REZGQjQ0NjI2&clientID=5&Level=";

		public DemoAsyTask3(Context context, String level, String parentid) {
			this.context = context;
			this.level = level;
			this.parentid = parentid;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			 progressDialog = new ProgressDialog(context);
			 progressDialog.setMessage("Loading ...");
			 progressDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(uri + level + "&parentid="+ parentid);
			String result = null;
			try {
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();
				result = EntityUtils.toString(entity, HTTP.UTF_8).trim();

			} catch (ClientProtocolException e) {
			} catch (IOException e) {
			}
			return result;
		}
	
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			
			System.out.println("result" + result);
			if (null != progressDialog && progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
			try {
				try {
//					int id=Integer.parseInt(parentid);
//					boolean b=dbbHelper.checkDB(id);
//					Toast.makeText(context, "true---"+b, 1).show();
//					dbbHelper.insertCategory(""+parentid, result);
					
					boolean b=dbbHelper.checkDB(parentid);
					Toast.makeText(context, "true---"+b, 1).show();
					if(b)
					{
					dbbHelper.updateCategory(""+parentid, result);
					Toast.makeText(context, "true", 1).show();
					}
					else
					{
						
						Toast.makeText(context, "false", 1).show();
						dbbHelper.insertCategory(""+parentid, result);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				JSONObject	jsonobject3 = new JSONObject(result);
				JSONArray thirdArray = jsonobject3.getJSONArray("DATA");
				System.out.println("thirdArray size" + thirdArray.length());
				
				mLinearListView.removeAllViews();
				for(int i=0;i<level1_labelmap.size();i++)
				{
					View mLinearView = (View) ((LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.row_layout5, null);
					mFirstLevelName = (TextView) mLinearView.findViewById(R.id.textViewName);
					ImageView img1=(ImageView)mLinearView.findViewById(R.id.img);
					mFirstLevelName.setText(level1_labelmap.get(i).toString());
					mFirstLevelName.setTag(level1_categorymap.get(i).toString());
					img1.setTag(level1_categorymap.get(i).toString());
					if(level1_categorymap.get(i).equals(level1_cur_value))
					{
						Drawable img=context.getResources().getDrawable(R.drawable.down);
//						 mFirstLevelName.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
						 mFirstLevelName.setBackgroundColor(Color.RED);
						 mFirstLevelName.setTextColor(Color.WHITE);
						 img1.setBackgroundColor(Color.RED);
						 img1.setImageDrawable(img);
					}
					mLinearListView.addView(mLinearView);
					System.out.println("level1_categorymap value"+level1_categorymap.get(i).toString());
					System.out.println("cur value"+level1_cur_value);
					
//					mFirstLevelName.setOnClickListener(new OnClickListener() {
					mFirstLevelName.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							
							Toast.makeText(context,"category id--"+v.getTag().toString(),Toast.LENGTH_LONG).show();
						}
					});
					img1.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							 level1_select_cid=v.getTag().toString();
//							 String name=((TextView) v).getText().toString();
//							 Toast.makeText(context,"label--"+name,Toast.LENGTH_LONG).show();
							 Toast.makeText(context,"category id--"+level1_select_cid,Toast.LENGTH_LONG).show();
							new DemoAsyTask2(context, "2", level1_select_cid).execute();
							level1_cur_value=level1_select_cid;
//							System.out.println("label"+name);
							System.out.println("category"+level1_select_cid);
							task3.cancel(true);
						}
					});
					
					if(level1_categorymap.get(i).equals(level1_cur_value))
					{
//						level2_labelmap.clear();
//						level2_categorymap.clear();
						if(level2_labelmap.size()!=0)
						{
						for (int j = 0; j < level2_labelmap.size(); j++) {

							View mLinearView2 = (View) ((LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.row_layout5, null);
							mSecondLevelName = (TextView) mLinearView2.findViewById(R.id.textViewName);
							ImageView img2=(ImageView)mLinearView2.findViewById(R.id.img);
							mSecondLevelName.setText(level2_labelmap.get(j).toString());
							mSecondLevelName.setTag(level2_categorymap.get(j).toString());
							img2.setTag(level2_categorymap.get(j).toString());
							if(level2_categorymap.get(j).equals(level2_cur_value))
							{
								Drawable img=context.getResources().getDrawable(R.drawable.down);
//								mSecondLevelName.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
								mSecondLevelName.setBackgroundColor(Color.GREEN);
								mSecondLevelName.setTextColor(Color.WHITE);
								 img2.setBackgroundColor(Color.GREEN);
								 img2.setImageDrawable(img);
							}
							LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)mSecondLevelName.getLayoutParams();
						     params.setMargins(40, 0, 0, 0); 
						     mSecondLevelName.setLayoutParams(params);
							mLinearListView.addView(mLinearView2);
							
//							mSecondLevelName.setOnClickListener(new OnClickListener() {
							mSecondLevelName.setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									
									Toast.makeText(context,"category id--"+v.getTag().toString(),Toast.LENGTH_LONG).show();
								}
							});
							img2.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									 level2_select_cid=v.getTag().toString();
//									 String name=((TextView) v).getText().toString();
//									 Toast.makeText(context,"label--"+name,Toast.LENGTH_LONG).show();
									 Toast.makeText(context,"category id--"+level2_select_cid,Toast.LENGTH_LONG).show();
									new DemoAsyTask3(context, "3", level2_select_cid).execute();
									task3.cancel(true);
//									level1_cur_value=level1_select_cid;
									level2_cur_value=level2_select_cid;
									System.out.println("position");
//									System.out.println("label"+name);
									System.out.println("category"+level2_select_cid);
								}
							});
					
							if(level2_categorymap.get(j).equals(level2_cur_value))
							{
								level3_labelmap.clear();
								level3_categorymap.clear();
								if(thirdArray.length()!=0)
								{
								for (int k = 0; k < thirdArray.length(); k++) {

									View mLinearView3 = (View) ((LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.row_layout5, null);
									mThirdLevelName = (TextView) mLinearView3.findViewById(R.id.textViewName);
									ImageView img3=(ImageView)mLinearView3.findViewById(R.id.img);
									JSONObject thirdobj = thirdArray.getJSONObject(k);
									String l3 = thirdobj.getString("LABEL");
									String cid3 = thirdobj.getString("CATEGORYID");
									level3_labelmap.put(k, l3);
									level3_categorymap.put(k,cid3);
									LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams)mThirdLevelName.getLayoutParams();
								     params2.setMargins(70, 0, 0, 0); 
								     mThirdLevelName.setLayoutParams(params2);
									mThirdLevelName.setText(l3);
									mThirdLevelName.setTag(cid3);
									img3.setTag(cid3);
									mLinearListView.addView(mLinearView3);
									
//									mThirdLevelName.setOnClickListener(new OnClickListener() {
									mThirdLevelName.setOnClickListener(new OnClickListener() {
										
										@Override
										public void onClick(View v) {
											
											Toast.makeText(context,"category id--"+v.getTag().toString(),Toast.LENGTH_LONG).show();
										}
									});
									img3.setOnClickListener(new OnClickListener() {
										@Override
										public void onClick(View v) {
											
											 level3_select_cid=v.getTag().toString();
//											 String name=((TextView) v).getText().toString();
//											 Toast.makeText(context,"label--"+name,Toast.LENGTH_LONG).show();
											 Toast.makeText(context,"category id--"+level3_select_cid,Toast.LENGTH_LONG).show();
											task4=new DemoAsyTask4(context, "4", level3_select_cid).execute();
											level3_cur_value=level3_select_cid;
											System.out.println("position");
//											System.out.println("label"+name);
											System.out.println("category"+level3_select_cid);
											task3.cancel(true);
										}
									});
								}
								}
								else
								{
									Toast.makeText(context,"No Children Available",1).show();
								}
							}
						}
						}
						else
						{
							Toast.makeText(context,"No Children Available",1).show();
						}
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	public class DemoAsyTask4 extends AsyncTask<String, Void, String> {

		public ProgressDialog progressDialog;
		JSONObject jsonResponse;
		Context context;
		String level, parentid;
		String uri = "http://stg.incentiveweb.com/webservices/TaicoWSController.cfc?method=fnGetCategories&AuthToken=MTA3NjI6OTExMzQ4MTAwOTg3MjU1OjUxNzNGMkFGLTUwNTYtQTA2My1FNEIyM0E3REZGQjQ0NjI2&clientID=5&Level=";

		public DemoAsyTask4(Context context, String level, String parentid) {
			this.context = context;
			this.level = level;
			this.parentid = parentid;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			 progressDialog = new ProgressDialog(context);
			 progressDialog.setMessage("Loading ...");
			 progressDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(uri + level + "&parentid="+ parentid);
			String result = null;
			try {
				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();
				result = EntityUtils.toString(entity, HTTP.UTF_8).trim();

			} catch (ClientProtocolException e) {
			} catch (IOException e) {
			}
			return result;
		}
	
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			
			System.out.println("result" + result);
			if (null != progressDialog && progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
			try {
				JSONObject	jsonobject4 = new JSONObject(result);
				JSONArray fourthArray = jsonobject4.getJSONArray("DATA");
				System.out.println("fourthArray size" + fourthArray.length());
				
				mLinearListView.removeAllViews();
				for(int i=0;i<level1_labelmap.size();i++)
				{
					View mLinearView = (View) ((LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.row_layout5, null);
					mFirstLevelName = (TextView) mLinearView.findViewById(R.id.textViewName);
					ImageView img1=(ImageView)mLinearView.findViewById(R.id.img);
					mFirstLevelName.setText(level1_labelmap.get(i).toString());
					mFirstLevelName.setTag(level1_categorymap.get(i).toString());
					img1.setTag(level1_categorymap.get(i).toString());
					if(level1_categorymap.get(i).equals(level1_cur_value))
					{
						Drawable img=context.getResources().getDrawable(R.drawable.down);
//						 mFirstLevelName.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
						 mFirstLevelName.setBackgroundColor(Color.RED);
						 mFirstLevelName.setTextColor(Color.WHITE);
						 img1.setBackgroundColor(Color.RED);
						 img1.setImageDrawable(img);
					}
					mLinearListView.addView(mLinearView);
					System.out.println("level1_categorymap value"+level1_categorymap.get(i).toString());
					System.out.println("cur value"+level1_cur_value);
					
//					mFirstLevelName.setOnClickListener(new OnClickListener() {
					mFirstLevelName.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							
							Toast.makeText(context,"category id--"+v.getTag().toString(),Toast.LENGTH_LONG).show();
						}
					});
					img1.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							 level1_select_cid=v.getTag().toString();
//							 String name=((TextView) v).getText().toString();
//							 Toast.makeText(context,"label--"+name,Toast.LENGTH_LONG).show();
							 Toast.makeText(context,"category id--"+level1_select_cid,Toast.LENGTH_LONG).show();
							new DemoAsyTask2(context, "2", level1_select_cid).execute();
							level1_cur_value=level1_select_cid;
//							System.out.println("label"+name);
							System.out.println("category"+level1_select_cid);
							task3.cancel(true);
						}
					});
					
					if(level1_categorymap.get(i).equals(level1_cur_value))
					{
						if(level2_labelmap.size()!=0)
						{
						for (int j = 0; j < level2_labelmap.size(); j++) {

							View mLinearView2 = (View) ((LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.row_layout5, null);
							mSecondLevelName = (TextView) mLinearView2.findViewById(R.id.textViewName);
							ImageView img2=(ImageView)mLinearView2.findViewById(R.id.img);
							mSecondLevelName.setText(level2_labelmap.get(j).toString());
							mSecondLevelName.setTag(level2_categorymap.get(j).toString());
							img2.setTag(level2_categorymap.get(j).toString());
							if(level2_categorymap.get(j).equals(level2_cur_value))
							{
								Drawable img=context.getResources().getDrawable(R.drawable.down);
//								mSecondLevelName.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
								mSecondLevelName.setBackgroundColor(Color.GREEN);
								mSecondLevelName.setTextColor(Color.WHITE);
								 img2.setBackgroundColor(Color.GREEN);
								 img2.setImageDrawable(img);
							}
							LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)mSecondLevelName.getLayoutParams();
						     params.setMargins(40, 0, 0, 0); 
						     mSecondLevelName.setLayoutParams(params);
							mLinearListView.addView(mLinearView2);
							
//							mSecondLevelName.setOnClickListener(new OnClickListener() {
							mSecondLevelName.setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									
									Toast.makeText(context,"category id--"+v.getTag().toString(),Toast.LENGTH_LONG).show();
								}
							});
							img2.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									 level2_select_cid=v.getTag().toString();
//									 String name=((TextView) v).getText().toString();
//									 Toast.makeText(context,"label--"+name,Toast.LENGTH_LONG).show();
									 Toast.makeText(context,"category id--"+level2_select_cid,Toast.LENGTH_LONG).show();
									new DemoAsyTask3(context, "3", level2_select_cid).execute();
									task3.cancel(true);
//									level1_cur_value=level1_select_cid;
									level2_cur_value=level2_select_cid;
//									System.out.println("label"+name);
									System.out.println("category"+level2_select_cid);
								}
							});
					
							if(level2_categorymap.get(j).equals(level2_cur_value))
							{
//								level3_labelmap.clear();
//								level3_categorymap.clear();
								if(level3_categorymap.size()!=0)
								{
								for (int k = 0; k < level3_labelmap.size(); k++) {

									View mLinearView3 = (View) ((LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.row_layout5, null);
									mThirdLevelName = (TextView) mLinearView3.findViewById(R.id.textViewName);
									ImageView img3=(ImageView)mLinearView3.findViewById(R.id.img);
									String l3 = level3_labelmap.get(k).toString();
									String cid3 = level3_categorymap.get(k).toString();
//									level4_labelmap.put(k, l3);
//									level4_categorymap.put(k,cid3);
									mThirdLevelName.setText(l3);
									mThirdLevelName.setTag(cid3);
									img3.setTag(cid3);
									if(level3_categorymap.get(k).equals(level3_cur_value))
									{
										Drawable img=context.getResources().getDrawable(R.drawable.down);
//										mThirdLevelName.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
										mThirdLevelName.setBackgroundColor(Color.YELLOW);
										mThirdLevelName.setTextColor(Color.WHITE);
										 img3.setBackgroundColor(Color.YELLOW);
										 img3.setImageDrawable(img);
									}
									LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams)mThirdLevelName.getLayoutParams();
								    params2.setMargins(70, 0, 0, 0); 
								    mThirdLevelName.setLayoutParams(params2);
								    mLinearListView.addView(mLinearView3);
									
//								    mThirdLevelName.setOnClickListener(new OnClickListener() {
								    mThirdLevelName.setOnClickListener(new OnClickListener() {
										
										@Override
										public void onClick(View v) {
											
											Toast.makeText(context,"category id--"+v.getTag().toString(),Toast.LENGTH_LONG).show();
										}
									});
								    img3.setOnClickListener(new OnClickListener() {
										
										@Override
										public void onClick(View v) {
											level3_select_cid=v.getTag().toString();
//											 String name=((TextView) v).getText().toString();
//											 Toast.makeText(context,"label--"+name,Toast.LENGTH_LONG).show();
											 Toast.makeText(context,"category id--"+level3_select_cid,Toast.LENGTH_LONG).show();
											new DemoAsyTask4(context, "4", level3_select_cid).execute();
											level3_cur_value=level3_select_cid;
//											System.out.println("label"+name);
											System.out.println("category"+level3_select_cid);
											task4.cancel(true);
										}
									});
									
									if(level3_categorymap.get(k).equals(level3_cur_value))
									{
										if(fourthArray.length()!=0)
										{
											Toast.makeText(context,"Have children",1).show();
//											for(int p=0;p<fourthArray.length();p++)
//											{
//												View mLinearView4= (View) ((LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.row_fourth, null);
//												mFourthLevelName = (TextView) mLinearView4.findViewById(R.id.textAvailable);
//												final ImageView mImageArrowFourth = (ImageView) mLinearView4.findViewById(R.id.imageFourthArrow);
//												final LinearLayout mLinearScrollFifth = (LinearLayout) mLinearView4.findViewById(R.id.linear_scroll_fifth);
//												mLinearScrollFifth.setVisibility(View.GONE);
//												mImageArrowFourth.setBackgroundResource(R.drawable.right);
//												
//												JSONObject fourthobj = fourthArray.getJSONObject(k);
//												String l4 = fourthobj.getString("LABEL");
//												String cid4 = fourthobj.getString("CATEGORYID");
//												level4_labelmap.put(k, l4);
//												level4_categorymap.put(k,cid4);
//												
//												LinearLayout.LayoutParams params3 = (LinearLayout.LayoutParams)mFourthLevelName.getLayoutParams();
//											     params3.setMargins(100, 0, 0, 0); 
//											     mFourthLevelName.setLayoutParams(params3);
//											     
//												mFourthLevelName.setText(l4);
//												mFourthLevelName.setTag(cid4);
//												mLinearListView.addView(mLinearView4);
////												mLinearScrollFourth.setVisibility(View.VISIBLE);
////												mLinearScrollFourth.addView(mLinearView4);
//											}
										}
										else
										{
											Toast.makeText(context,"No Children Available",1).show();
										}
									}
								}
								}
								else
								{
									Toast.makeText(context,"No Children Available",1).show();
								}
							}
						}
						}
						else
						{
							Toast.makeText(context,"No Children Available",1).show();//level2
						}
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
}
