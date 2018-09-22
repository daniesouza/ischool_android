package br.com.marrs.ischool;

import br.com.marrs.ischool.entidades.Usuario;

public interface LoginListener {

	public void onLoginProcessStart();

	public void onLoginProcessSuccess(Usuario usuarioLogado);
	
	public void onLoginProcessFail(String message);
	
	
}
