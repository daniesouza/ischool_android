package br.com.marrs.ischool;

import java.util.ArrayList;

import android.app.Application;
import android.util.SparseArray;
import br.com.marrs.ischool.entidades.Aluno;
import br.com.marrs.ischool.entidades.Usuario;
import br.com.marrs.ischool.util.Constantes;
import br.com.marrs.ischool.util.Crypto;
import br.com.marrs.ischool.util.DadosUtil;
import br.com.marrs.ischool.webservice.AlunoCrypt;
import br.com.marrs.ischool.webservice.IschoolWebServiceImplService;
import br.com.marrs.ischool.webservice.UsuarioCrypt;
import br.com.marrs.ischool.webservice.VectorByte;

public class LoginManager{
	
	private Usuario usuario;
	private SparseArray<LoginListener> loginListeners;
	private Application application;
	

	public LoginManager(Application application){	
		this.application = application;	
		loginListeners =  new SparseArray<LoginListener>(10);
	}
	
	public void addListener(LoginListener loginListener,Integer key) {
		loginListeners.put(key,loginListener);		
	}
	
	public void removeListeners(){
		for(int i=2;i<=10;i++){
			loginListeners.remove(i);
		}
	}
	
	public final void doLogin(String login,String pass,Long deviceRegId,String strDeviceReg) throws Exception{
		
		if(!DadosUtil.temConexao(application)){
			
			int key = 0;
			for(int i = 0; i < loginListeners.size(); i++) {
			   key = loginListeners.keyAt(i);
			   LoginListener loginListener = loginListeners.get(key);
			   loginListener.onLoginProcessFail(Constantes.SEM_CONEXAO);
			}
			
			return;
		}
						
		final VectorByte vectorLogin = new VectorByte(Crypto.encrypt(login.getBytes("UTF-8")));
		final VectorByte vectorPass  = new VectorByte(Crypto.encrypt(new String(Crypto.gerarSalt()+Crypto.encriptSenhaMD5(Constantes.FIXSALT+pass+Constantes.FIXSALT)).getBytes("UTF-8")));		
		final VectorByte deviceRegIdVec = new VectorByte(Crypto.encrypt(deviceRegId.toString().getBytes("UTF-8")));
		final VectorByte strDeviceRegVec = new VectorByte(Crypto.encrypt(strDeviceReg.getBytes("UTF-8"))); 
		final VectorByte tipoDevice = new VectorByte(Crypto.encrypt(Constantes.DEVICE_ANDROID.toString().getBytes("UTF-8")));
		
	    Runnable runnable = new Runnable() {
	        @Override
	        public void run() { 
	        	
				DadosUtil.statusLogin = Constantes.STATUS_LOGIN_EM_PROCESSO;
				int key = 0;
				for(int i = 0; i < loginListeners.size(); i++) {
				   key = loginListeners.keyAt(i);
				   LoginListener loginListener = loginListeners.get(key);
				   loginListener.onLoginProcessStart();
				}					
				
    			try{
      				
    				UsuarioCrypt usuarioCrypt = IschoolWebServiceImplService.getInstance().loginDevice(vectorLogin, vectorPass,deviceRegIdVec,strDeviceRegVec,tipoDevice);
    				
    				usuario  = usuarioCrypt.decriptarUsuario();
    				usuario.setCliente(usuarioCrypt.cliente.decriptarCliente());
    				
    				if(usuarioCrypt.alunos != null && !usuarioCrypt.alunos.isEmpty()){
    					usuario.setAlunos(new ArrayList<Aluno>());
    					
    					for(AlunoCrypt alunoCrypt:usuarioCrypt.alunos){
    						Aluno aluno = alunoCrypt.decriptarAluno();
    						aluno.setCliente(usuario.getCliente());
    						aluno.setPai(usuario);
    						usuario.getAlunos().add(aluno);
    					}
    				}
    					    					    								    				
    				
					DadosUtil.statusLogin = Constantes.STATUS_LOGIN_LOGADO;
					int key1 = 0;
					for(int i = 0; i < loginListeners.size(); i++) {
					   key1 = loginListeners.keyAt(i);
					   LoginListener loginListener = loginListeners.get(key1);
					   loginListener.onLoginProcessSuccess(usuario);
					}						

					
    			}
    			catch(Exception ex){
    				ex.printStackTrace();				
    				String msg = ex.getMessage() == null ? "" : ex.getMessage();
					DadosUtil.statusLogin = Constantes.STATUS_LOGIN_NAO_LOGADO;
					int key2 = 0;
					for(int i = 0; i < loginListeners.size(); i++) {
					   key2 = loginListeners.keyAt(i);
					   LoginListener loginListener = loginListeners.get(key2);
					   loginListener.onLoginProcessFail(msg);
					}
    			}

			}
	        
	    };
	    
	    Thread loginThread = new Thread(runnable);
	    loginThread.start();	
		
	}
	

}
