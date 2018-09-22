package br.com.marrs.ischool.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import br.com.marrs.ischool.LoginListener;
import br.com.marrs.ischool.MainActivity;
import br.com.marrs.ischool.R;
import br.com.marrs.ischool.entidades.Usuario;
import br.com.marrs.ischool.util.Constantes;
import br.com.marrs.ischool.util.DadosUtil;

public class HomeFragment extends Fragment implements LoginListener
{
	private FrameLayout fl;
	private ProgressBar progress;	
	private static final int FRAGMENT_ID = 2;

	
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		if(fl == null){
			fl = (FrameLayout)inflater.inflate(R.layout.home_fragment, container, false);
			progress = (ProgressBar) fl.findViewById(R.id.progess_home);

		}
		
		MainActivity.instance.loginManager.removeListeners();
		MainActivity.instance.loginManager.addListener(this, HomeFragment.FRAGMENT_ID);
				
        return fl;
    }
	
	@Override
	public void onResume(){	
		super.onResume();	
		handleLoginProcess();
	}
	
	public void handleLoginProcess(){
		
		startProgress();
		
		switch (DadosUtil.statusLogin) {
		
		case Constantes.STATUS_LOGIN_LOGADO:			
			if(DadosUtil.usuarioLogado != null){
				buildComponents();
			}
			
			break;
			
		case Constantes.STATUS_LOGIN_EM_PROCESSO:		
			break;
			
		case Constantes.STATUS_LOGIN_NAO_LOGADO:
			MainActivity.instance.automaticLogin();
			break;
		}
	}
	
	@Override
	public void onPause()
	{
		super.onPause();
	}

	
	public void buildComponents(){
		
		LinearLayout layout = (LinearLayout) fl.findViewById(R.id.layout_home);
		
		layout.setVisibility(View.VISIBLE);	
		
		stopProgress();		
	}

	@Override
	public void onLoginProcessStart() {
		// nao implementar usando f1 pois ainda esta nulo
	}

	@Override
	public void onLoginProcessSuccess(Usuario usuarioLogado) {
		buildComponents();
	}

	@Override
	public void onLoginProcessFail(String message) {
		stopProgress();
		
		final LinearLayout layout = (LinearLayout) fl.findViewById(R.id.layout_home_erro);
		
		layout.setVisibility(View.VISIBLE);					
		layout.setOnClickListener(new View.OnClickListener(){
		    @Override
		    public void onClick(View v){
		    	layout.setVisibility(View.GONE);
		    	startProgress();
				MainActivity.instance.automaticLogin();
		    }
		});

	}
	
	
	public void startProgress()
	{
		progress.setVisibility(View.VISIBLE);
	}
	
	public void stopProgress()
	{
		progress.setVisibility(View.GONE);
	}

}
