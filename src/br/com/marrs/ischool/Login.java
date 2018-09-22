package br.com.marrs.ischool;

//import org.kobjects.base64.Base64;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import br.com.marrs.ischool.dao.DbAlunoAdapter;
import br.com.marrs.ischool.dao.DbUsuarioAdapter;
import br.com.marrs.ischool.entidades.Aluno;
import br.com.marrs.ischool.entidades.Usuario;
import br.com.marrs.ischool.util.Constantes;
import br.com.marrs.ischool.util.Crypto;
import br.com.marrs.ischool.util.DadosUtil;

import com.google.android.gcm.GCMManager;

public class Login extends Activity implements LoginListener
{	
	private FrameLayout progress;
	private AlertDialog alertDialog;	
	private SharedPreferences prefs;
	private Context context;
	private boolean automaticLogin;
	private LoginManager loginManager;
	private static final int FRAGMENT_ID = 0;
	private String loginDigitado;
	private String senhaCrypt;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);

		context = getApplicationContext();		
		loginManager = new LoginManager(this.getApplication());
	
		prefs = getSharedPreferences();
		
		automaticLogin = prefs.getBoolean(Constantes.AUTOMATIC_LOGIN, false);
		
		if(automaticLogin){						
			Intent intent = new Intent(Login.this, MainActivity.class);						
			startActivity(intent);
			finish();
		}else{
			loginManager.addListener(this,Login.FRAGMENT_ID);
			setContentView(R.layout.login);	
			buildLoginComponents();
			
			EditText loginEdit =  (EditText) findViewById(R.id.et_login);
			EditText senhaEdit =  (EditText) findViewById(R.id.et_senha);
			
			loginEdit.setText("pai");
			senhaEdit.setText("12345678");
		}

	}
	
	@Override
	protected void onResume() {
		super.onResume();		
		manageDeviceGoogle();
	}
	
	private void manageDeviceGoogle(){
		
		GCMManager gcmManager = new GCMManager();
		gcmManager.manageRegisterDeviceGoogle(this);			
		
	}
	
	private void buildLoginComponents(){	
		
		progress = (FrameLayout) findViewById(R.id.progress_login);
		
		alertDialog = new AlertDialog.Builder(this).create();		
		alertDialog.setIcon(R.drawable.icon_alert);
		alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
			    new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int which) {
			            dialog.dismiss();
			        }
		});
		
		int result = DadosUtil.checkGooglePlayServices(this);
		
		switch (result) {
		case Constantes.GOOGLE_PLAY_NAO_SUPORTADO:
			
    		alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
    			    new DialogInterface.OnClickListener() {
    			        public void onClick(DialogInterface dialog, int which) {
    			        	 finish();
    			        }
    			    });
    		
    		alertMensagem(getString(R.string.alerta), getString(R.string.device_nao_suportado));	
			
			break;
			
		case Constantes.GOOGLE_PLAY_ATUALIZAR:
			
			alertMensagem(getString(R.string.alerta), getString(R.string.google_play_atualizar));	
			break;

		default:
			break;
		}
	}
	
	
	public void login(View view){
			
		EditText loginEdit =  (EditText) findViewById(R.id.et_login);
		EditText senhaEdit =  (EditText) findViewById(R.id.et_senha);
		
		loginDigitado = loginEdit.getText().toString();
		senhaCrypt = senhaEdit.getText().toString();
		senhaCrypt = Crypto.encriptSenhaMD5(senhaCrypt);
			
		String strRegId = prefs.getString(Constantes.PROPERTY_REG_ID, null);
		Long   regId	= prefs.getLong(Constantes.DEVICE_ID, 0);
		
		try {
			loginManager.doLogin(loginDigitado, senhaCrypt,regId,strRegId);
		} catch (Exception e) {
			e.printStackTrace();
			alertMensagem("Erro Inesperado", "Aconteceu uma falha inesperada. Por favor reinicie a aplicação");
		}

	}
	
	@Override
	public void onLoginProcessStart() {		
		this.runOnUiThread(new Runnable() {
	        public void run() {  
	    		startProgress();		
	        }
		});
	}

	@Override
	public void onLoginProcessSuccess(Usuario usuarioLogado) {
		
		boolean gravarLoginPass = ((CheckBox) findViewById(R.id.cb_gravar)).isChecked();
				
		if(gravarLoginPass){
			storeLoginPass(getApplicationContext(), loginDigitado, senhaCrypt,gravarLoginPass);
		}else{						
			removeStoredLoginPass(context);
		}			
		
		storeDeviceId(getApplicationContext(),usuarioLogado.getDeviceId());
		storeLoggedUser(getApplicationContext(),usuarioLogado.getId());
		storeLoggedUserInDataBase(usuarioLogado);		
		storeAlunosInDataBase(usuarioLogado.getAlunos());	
		
		
		DadosUtil.usuarioLogado = usuarioLogado;
		
		
		startActivity(new Intent(Login.this, MainActivity.class));
		
		this.runOnUiThread(new Runnable() {
	        public void run() {  
	    		stopProgress();
	    		finish();
	        }
		});

		
	}

	@Override
	public void onLoginProcessFail(final String message) {
		
		this.runOnUiThread(new Runnable() {
	        public void run() {    
                       
            	switch (message) {
        		case Constantes.USUARIO_SENHA_INCORRETO:
            		alertMensagem(getString(R.string.alerta),getString(R.string.usuario_senha_incorr));        		
        			break;
        		case Constantes.CLIENTE_DESATIVADO:
        			alertMensagem(getString(R.string.alerta),getString(R.string.cliente_desativado));
        			break;			
        		case Constantes.USUARIO_DESATIVADO:
        			alertMensagem(getString(R.string.alerta),getString(R.string.usuario_desativado));
        			break;
        		case Constantes.USUARIO_SEM_ROLES:
            		alertMensagem(getString(R.string.alerta),getString(R.string.usuario_sem_perfil));
        			break;
        		case Constantes.SEM_CONEXAO:
        			alertMensagem(getString(R.string.alerta), getString(R.string.sem_conexao));
        			break;
        		default:
            		alertMensagem(getString(R.string.alerta),getString(R.string.sistema_indisponivel));
        			break;
        		}
        		
        		stopProgress();
            }
        
		});

		
	}
	
	private void storeLoggedUserInDataBase(Usuario usuarioLogado){
		DbUsuarioAdapter dbUsuarioAdapter = new DbUsuarioAdapter(Login.this);
		dbUsuarioAdapter.storeLoggedUserInDataBase(usuarioLogado);
	}
	
	private void storeAlunosInDataBase(List<Aluno> alunos){
		
		DbAlunoAdapter dbAlunoAdapter = new DbAlunoAdapter(Login.this);
		
		if(alunos != null && !alunos.isEmpty()){
			dbAlunoAdapter.storeAlunosInDataBase(alunos);
		}
	}
	

	public void alertMensagem(String titulo,String mensagem){
		
		alertDialog.setTitle(titulo);
		alertDialog.setMessage(mensagem);
		alertDialog.show();
	}

	private void storeDeviceId(Context context,Long deviceId){
		
		final SharedPreferences prefs = getSharedPreferences();
		SharedPreferences.Editor editor = prefs.edit();
	    editor.putLong(Constantes.DEVICE_ID, deviceId);
	    editor.commit();

	}
	private void storeLoginPass(Context context, String login,String passCrypt,boolean loginAutomatico) {
	    final SharedPreferences prefs = getSharedPreferences();

	    SharedPreferences.Editor editor = prefs.edit();
	    editor.putString(Constantes.REMEMBERED_LOGIN, login);
	    editor.putString(Constantes.REMEMBERED_PASS, passCrypt);
	    editor.putBoolean(Constantes.AUTOMATIC_LOGIN, loginAutomatico);
	    
	    editor.commit();
	}
	
	private void storeLoggedUser(Context context, Long idUsuario) {
	    final SharedPreferences prefs = getSharedPreferences();

	    SharedPreferences.Editor editor = prefs.edit();
	    editor.putLong(Constantes.ID_LOGGED_USER, idUsuario);
	    
	    editor.commit();
	}
	
	private void removeStoredLoginPass(Context context) {
	    final SharedPreferences prefs = getSharedPreferences();

	    SharedPreferences.Editor editor = prefs.edit();
	    editor.remove(Constantes.REMEMBERED_LOGIN);
	    editor.remove(Constantes.REMEMBERED_PASS);
	    editor.remove(Constantes.AUTOMATIC_LOGIN);
	    
	    editor.commit();
	}
	
	private SharedPreferences getSharedPreferences() {
	    return getSharedPreferences(Constantes.ISCHOOL_SHARED_PREF, Context.MODE_PRIVATE);
	}
	


	@Override
	public void onPause(){
		super.onPause();
	}
	
	public void startProgress(){
		progress.setVisibility(View.VISIBLE);
	}
	
	public void stopProgress(){
		progress.setVisibility(View.GONE);
	}

}
