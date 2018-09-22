package com.google.android.gcm;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.util.Log;
import br.com.marrs.ischool.Login;
import br.com.marrs.ischool.util.Constantes;

import com.google.android.gms.gcm.GoogleCloudMessaging;

public class GCMManager {
	
	private GoogleCloudMessaging gcm;
	
	private Context context;
	private String regId;
	private Login loginActivity;
	
	public void manageRegisterDeviceGoogle(Login activity){
		
		this.loginActivity = activity;
		loginActivity.startProgress();
		
		this.context = activity.getApplicationContext();
        
        if (isRegistrationNecessary()) {
        	gcm = GoogleCloudMessaging.getInstance(context);
            registerInBackground();
        }else{
        	loginActivity.stopProgress();
        }

	}
	
	private boolean isRegistrationNecessary() {
	    final SharedPreferences prefs = getSharedPreferences();
	    regId = prefs.getString(Constantes.PROPERTY_REG_ID, "");
	    if (regId.isEmpty()) {
	        Log.i("DEBUG", "Registration not found.");
	        return true;
	    }

	    int registeredVersion = prefs.getInt(Constantes.PROPERTY_APP_VERSION, Integer.MIN_VALUE);
	    int currentVersion = getAppVersion(context);
	    if (registeredVersion != currentVersion) {
	        Log.i("DEBUG", "App version changed.");
	        return true;
	    }
	    return false;
	}
	
	private SharedPreferences getSharedPreferences() {
	    return context.getSharedPreferences(Constantes.ISCHOOL_SHARED_PREF, Context.MODE_PRIVATE);
	}
	
	private static int getAppVersion(Context context) {
	    try {
	        PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
	        return packageInfo.versionCode;
	    } catch (NameNotFoundException e) {
	        // fodeu
	        throw new RuntimeException("Could not get package name: " + e);
	    }
	}
	
	
	private void registerInBackground() {
		new AsyncTask<Void, Void, String>() {
	        @Override
	        protected String doInBackground(Void... params) {
	            String msg = "";
	            try {
	                if (gcm == null) {
	                    gcm = GoogleCloudMessaging.getInstance(context);
	                }
	                regId = gcm.register(CommonUtilities.PROJECT_NUMBER);
	                msg = "Device registered, registration ID=" + regId;
	                
	                storeRegistrationId(context, regId);
	            } catch (Exception ex) {
	                msg = "Error :" + ex.getMessage();
	                loginActivity.alertMensagem("Erro ao registrar device", "Erro ao registrar device.. Por favor cheque seu google play services e tente mais tarde.");
	            }
	            return msg;
	        }

	        @Override
	        protected void onPostExecute(String msg) {
	        	regId = msg;
	        	loginActivity.stopProgress();
	        }
	    }.execute(null, null, null);
	}
	

	
	private void storeRegistrationId(Context context, String regId) {
	    final SharedPreferences prefs = getSharedPreferences();
	    int appVersion = getAppVersion(context);
	    Log.i("DEBUG", "Saving regId on app version " + appVersion);
	    SharedPreferences.Editor editor = prefs.edit();
	    editor.putString(Constantes.PROPERTY_REG_ID, regId);
	    editor.putInt(Constantes.PROPERTY_APP_VERSION, appVersion);
	    editor.commit();
	}
	

}
