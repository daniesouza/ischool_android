package br.com.marrs.ischool.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.webkit.MimeTypeMap;
import br.com.marrs.ischool.MainActivity;
import br.com.marrs.ischool.entidades.Aluno;
import br.com.marrs.ischool.entidades.Usuario;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

/**
 * @author Daniel Souza de lima e-mail:daniesouza@gmail.com
 * 
 */

public class DadosUtil {
	
	public static Usuario usuarioLogado;
	public static Aluno alunoSelecionado;
	public static Calendar dataSelecionada = Calendar.getInstance();
	public static int statusLogin = Constantes.STATUS_LOGIN_NAO_LOGADO;
	public static Long deviceID;
	public static DecimalFormat df = new DecimalFormat("#.##");
		
	public static boolean isEmpty(Long l) {
		return l == null || l.longValue() == 0;
	}
	
	public static boolean isEmpty(List<?> lista) {
		return lista == null || lista.isEmpty();
	}

	public static boolean isEmpty(Object[] v) {
		return v == null || v.length == 0;
	}
	
	public static boolean isEmpty(byte[] v) {
		return v == null || v.length == 0;
	}
	
	public static boolean isEmpty(Object obj) {
		return obj == null;
	}
	
	public static boolean isEmpty(Double obj) {
		return obj == null || obj.doubleValue() == 0;
	}
	
	public static boolean isEmpty(Integer obj) {
		return obj == null || obj.intValue() == 0;
	}	
	
	public static boolean isEmpty(String str) {
		return str == null || str.isEmpty() || str.trim().equals("");
	}
	
	public static String criptografar(String str) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			return new BigInteger(1, md.digest(str.getBytes())).toString(16);
		} catch (NoSuchAlgorithmException excecao) {
			return null;
		}
	}

	public static long arredondarDecimal(long numero) {
		int fator = 5;
		numero = numero + fator;
		long sobra = (numero % fator);
		long adicionar = fator - sobra;
		return numero + adicionar;
	}
	
	
	public static boolean temConexao(Application application)
    {
		ConnectivityManager conn_man = (ConnectivityManager) application.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] avail_net_info = conn_man.getAllNetworkInfo();
		boolean has_capable_net_available = false;
		
		for(int i = 0; i < avail_net_info.length; ++i) 
		{
			if(avail_net_info[i].isConnected())
			{
				has_capable_net_available = true;
				break;
			}
		}
		return has_capable_net_available;
    }
	
	
    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    public static int checkGooglePlayServices(Activity activity) {
    	   	
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity.getApplicationContext());
        if (resultCode != ConnectionResult.SUCCESS) {
            
        	if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, activity,Constantes.PLAY_SERVICES_RESOLUTION_REQUEST).show();
                return Constantes.GOOGLE_PLAY_ATUALIZAR;
            } else {
            	return Constantes.GOOGLE_PLAY_NAO_SUPORTADO;
            }
        }
               
        return Constantes.GOOGLE_PLAY_SUPORTADO;
    }
    
    

    public static String getRealPathFromURI(Uri contentURI,Context context) {
        String result;
        Cursor cursor = context.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else { 
            cursor.moveToFirst(); 
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA); 
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }
    
    public static String convertHumanReadableByteCount (long size,boolean arredondar){
        long Kb = 1  * 1024;
        long Mb = Kb * 1024;
        long Gb = Mb * 1024;
        long Tb = Gb * 1024;
        long Pb = Tb * 1024;
        long Eb = Pb * 1024;

        if (size <  Kb)                 return df.format(        size     ) + " byte";
        if (size >= Kb && size < Mb)    return df.format(arredondar ? (long) size / Kb : (double) size / Kb  ) + " Kb";
        if (size >= Mb && size < Gb)    return df.format(arredondar ? (long) size / Mb : (double) size / Mb  ) + " Mb";
        if (size >= Gb && size < Tb)    return df.format(arredondar ? (long) size / Gb : (double) size / Gb  ) + " Gb";
        if (size >= Tb && size < Pb)    return df.format(arredondar ? (long) size / Tb : (double) size / Tb  ) + " Tb";
        if (size >= Pb && size < Eb)    return df.format(arredondar ? (long) size / Pb : (double) size / Pb  ) + " Pb";
        if (size >= Eb)                 return df.format(arredondar ? (long) size / Eb : (double) size / Eb  ) + " Eb";

        return "???";
    }

    public static String getExtensionFromUrl(String urlPath){
		MimeTypeMap map = MimeTypeMap.getSingleton();
		String ext = urlPath.substring(urlPath.lastIndexOf(".")+1);			
		String type = map.getMimeTypeFromExtension(ext);
		
		return type;
    }
    
    // ESCANEIA ARQUIVO PARA APARECER NA GALERIA
    public static void scanFile(String path) {

        MediaScannerConnection.scanFile(MainActivity.instance,
                new String[] { path }, null,
                new MediaScannerConnection.OnScanCompletedListener() {

                    public void onScanCompleted(String path, Uri uri) {
                        Log.i("TAG", "Finished scanning " + path);
                    }
                });
    }
    
    /** Check if this device has a camera */
    public static boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

}