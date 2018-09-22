package br.com.marrs.ischool;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import br.com.marrs.ischool.adapter.NavDrawerListAdapter;
import br.com.marrs.ischool.dao.DbAlunoAdapter;
import br.com.marrs.ischool.dao.DbEventoExecutadoAdapter;
import br.com.marrs.ischool.dao.DbMensagemAdapter;
import br.com.marrs.ischool.dao.DbUsuarioAdapter;
import br.com.marrs.ischool.entidades.Aluno;
import br.com.marrs.ischool.entidades.Usuario;
import br.com.marrs.ischool.model.NavDrawerItem;
import br.com.marrs.ischool.util.Constantes;
import br.com.marrs.ischool.util.DadosUtil;
import br.com.marrs.ischool.view.AgendaFragment;
import br.com.marrs.ischool.view.AlunoActivity;
import br.com.marrs.ischool.view.HomeFragment;
import br.com.marrs.ischool.view.LembreteFragment;
import br.com.marrs.ischool.view.mensagens.MensagemFragment;

public class MainActivity extends ActionBarActivity implements LoginListener{
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private static final int FRAGMENT_ID = 1;

	// used to store app title
	private CharSequence mTitle,mDrawerTitle;
	
	
	// slide menu items
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;

	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;	
	private AlertDialog alertDialog;
	public LoginManager loginManager;
	public SharedPreferences prefs;
	boolean automaticLogin;
	public DbAlunoAdapter dbAlunoAdapter;
	public DbUsuarioAdapter dbUsuarioAdapter;
	public DbEventoExecutadoAdapter dbEventoExeAdapter;
	public DbMensagemAdapter dbMensagemAdapter;
	public Fragment fragment = null;
	
	
	public static MainActivity instance;
	

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);		
		instance = this;
		setContentView(R.layout.activity_main);
		loginManager = new LoginManager(this.getApplication());
		loginManager.addListener(this,MainActivity.FRAGMENT_ID);
		prefs = getSharedPreferences(Constantes.ISCHOOL_SHARED_PREF, Context.MODE_PRIVATE);
		dbAlunoAdapter = new DbAlunoAdapter(this);
		dbUsuarioAdapter = new DbUsuarioAdapter(this);
		dbEventoExeAdapter = new DbEventoExecutadoAdapter(this);
		dbMensagemAdapter = new DbMensagemAdapter(this);
		
		int version = Build.VERSION.SDK_INT;
		
		// BLOCO SOMENTE RODA NO ANDROID 5 PARA CIMA
		if(version >= 21){
			
			Window window = this.getWindow();
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			window.setStatusBarColor(this.getResources().getColor(R.color.status_bar_color));
		}
		
		automaticLogin 		= prefs.getBoolean(Constantes.AUTOMATIC_LOGIN, false);
		DadosUtil.deviceID	= prefs.getLong(Constantes.DEVICE_ID, 0);
		

		if(automaticLogin && DadosUtil.statusLogin == Constantes.STATUS_LOGIN_NAO_LOGADO){
			DadosUtil.usuarioLogado = retrieveLoggedUserInformation();
			DadosUtil.statusLogin = Constantes.STATUS_LOGIN_LOGADO;	
		}
		
		if(DadosUtil.usuarioLogado.getAlunos() != null && !DadosUtil.usuarioLogado.getAlunos().isEmpty()){
			DadosUtil.alunoSelecionado = DadosUtil.usuarioLogado.getAlunos().get(0);
		}
		
		buildComponents();
				
		Toast.makeText(this, "CRIOU UM NOVO", 2000).show();
		
		// DESCOMENTAR PARA CRIAR FLOATING ACTION BUTTOn		
//		// in Activity Context
//		ImageView icon = new ImageView(this); // Create an icon
//		icon.setImageResource(R.drawable.icon_home);
//
//		final FloatingActionButton actionButton = new FloatingActionButton.Builder(this)
//		                                        .setContentView(icon)
//		                                        .build();
//		
//		actionButton.setVisibility(View.VISIBLE);
//		
//		actionButton.setY(actionButton.getY()-150);
//	//	actionButton.setX(actionButton.getX());
//		SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);
//		
//		// repeat many times:
//		ImageView itemIcon = new ImageView(this);
//		itemIcon.setImageResource(R.drawable.icon_agenda ); 
//		SubActionButton button1 = itemBuilder.setContentView(itemIcon).build();
//		button1.setOnClickListener(new SubActionButton.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				buildDisplay("aluno", null);
//				actionButton.callOnClick();
//				
//			}
//		});
//		
//		// repeat many times:
//		ImageView itemIcon2 = new ImageView(this);
//		itemIcon2.setImageResource(R.drawable.icon_alert ); 
//		SubActionButton button2 = itemBuilder.setContentView(itemIcon2).build();
//		
//		 new FloatingActionMenu.Builder(this)
//        .addSubActionView(button1)
//        .addSubActionView(button2)
//        .attachTo(actionButton)
//        .build();
	
	
	}
	
	
	public void buildComponents(){
		
		
		alertDialog = new AlertDialog.Builder(this).create();		
		String frag = getIntent().getStringExtra("frag") == null ? "home": getIntent().getStringExtra("frag");


		 mTitle = mDrawerTitle = getTitle();

		// load slide menu items
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

		// nav drawer icons from resources
		navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

		navDrawerItems = new ArrayList<NavDrawerItem>();


		navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1),true,"3"));
		//navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1),true,"2"));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(3, -1),true,"3"));
		//navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(5, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(6, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(7, -1)));
		

		// Recycle the typed array
		navMenuIcons.recycle();

		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

		// setting the nav drawer list adapter
		adapter = new NavDrawerListAdapter(getApplicationContext(),	navDrawerItems);
		mDrawerList.setAdapter(adapter);

		// enabling action bar app icon and behaving it as toggle button
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar));

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, //nav menu toggle icon
				R.string.app_name				
		) { 
			public void onDrawerClosed(View view) {
				getSupportActionBar().setTitle(mTitle);
				// calling onPrepareOptionsMenu() to show action bar icons
				supportInvalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				getSupportActionBar().setTitle(mDrawerTitle);
				// calling onPrepareOptionsMenu() to hide action bar icons
				supportInvalidateOptionsMenu();
			}
		};
		
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		
		buildDisplay(frag, getIntent());
	
	}
	
	public void buildDisplay(String frag,Intent intent){
				
		switch (frag) {
		case "home":
			displayView(0,intent);					
			break;
			
		case "aluno":
			displayView(1,intent);					
			break;
			
//		case "dicas":
//			displayView(2,intent);					
//			break;
//						
//		case "historico":
//			displayView(4,intent);					
//			break;	
			
		case "evento":
			displayView(10,intent);					
			break;	
			
		case "mensagem":
			displayView(11,intent);					
			break;			

		default:
			displayView(0,intent);
			break;
		}
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	this.menu = menu;   	
        getMenuInflater().inflate(R.menu.main, menu);    
        getMenuInflater().inflate(R.menu.trocar_aluno, menu);      
        return true;
    }
    
    Menu menu;
 
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action bar actions click
        switch (item.getItemId()) {
        case R.id.ajuda:            	
        	Toast.makeText(this, "CLICOU AJUDA", 2000).show();       	
            return true;
            
        case R.id.sobre:            	
        	Toast.makeText(this, "CLICOU SOBRE", 2000).show();       	
            return true; 
            
        case R.id.trocar_filho:            	
			Intent intent = new Intent(MainActivity.this, AlunoActivity.class);						
			startActivity(intent);
            return true;             
        default:
            return super.onOptionsItemSelected(item);
        }
    }
 
    /***
     * Called when invalidateOptionsMenu() is triggered
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);

        for(int i = 0; i < menu.size(); i++){
        	menu.getItem(i).setVisible(!drawerOpen);
        }

        return super.onPrepareOptionsMenu(menu);
    }
        

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getSupportActionBar().setTitle(mTitle);
	}


	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}
	
	
	public void automaticLogin(){		
		
		String loginDigitado = prefs.getString(Constantes.REMEMBERED_LOGIN, null);
		String senhaCrypt = prefs.getString(Constantes.REMEMBERED_PASS, null);
		String strRegId = prefs.getString(Constantes.PROPERTY_REG_ID, null);
		
		
		if(loginDigitado == null || senhaCrypt == null){
			exibirMensagemNaoLogado(getString(R.string.nao_logado_sistema));			
		}else{
			try {
				loginManager.doLogin(loginDigitado, senhaCrypt,DadosUtil.deviceID,strRegId);
			} catch (Exception e) {
				e.printStackTrace();
				alertMensagem("Erro Inesperado", "Aconteceu uma falha inesperada. Por favor reinicie a aplicação");
			}
		}
		
	}
	
	@Override
	public void onLoginProcessStart() {

	}

	@Override
	public void onLoginProcessSuccess(Usuario usuarioLogado) {

		storeLoggedUserInDataBase(usuarioLogado);		
		storeAlunosInDataBase(usuarioLogado.getAlunos());
		DadosUtil.usuarioLogado = usuarioLogado;
	}

	@Override
	public void onLoginProcessFail(final String message) {
		
		this.runOnUiThread(new Runnable() {
	        public void run() {
	        	
		    	switch (message) {
				case Constantes.USUARIO_SENHA_INCORRETO:
					exibirMensagemNaoLogado(getString(R.string.usuario_senha_incorr));		
					break;    	
				case Constantes.CLIENTE_DESATIVADO:
					exibirMensagemNaoLogado(getString(R.string.cliente_desativado));		
					break;			
				case Constantes.USUARIO_DESATIVADO:
					exibirMensagemNaoLogado(getString(R.string.usuario_desativado));		
					break;
				case Constantes.USUARIO_SEM_ROLES:
					exibirMensagemNaoLogado(getString(R.string.usuario_sem_perfil));		
					break;
				case Constantes.SEM_CONEXAO:
					exibirMensagemSemConexao();	
					break;
				default:
		    		//alertMensagem(getString(R.string.alerta),getString(R.string.sistema_indisponivel));
					break;
				}
	    	
		    }
	     });
				
	}
	
	private void storeLoggedUserInDataBase(Usuario usuarioLogado){		
		dbUsuarioAdapter.storeLoggedUserInDataBase(usuarioLogado);
	}
	
	private void storeAlunosInDataBase(List<Aluno> alunos){
		if(alunos != null && !alunos.isEmpty()){
			dbAlunoAdapter.storeAlunosInDataBase(alunos);
		}
	}
	
	private Usuario retrieveLoggedUserInformation(){
		Usuario usuario = null;
		try{

			Long idUsuario = prefs.getLong(Constantes.ID_LOGGED_USER, 0);

			usuario = dbUsuarioAdapter.returnById(idUsuario);
			usuario.setAlunos(dbAlunoAdapter.returnAllRowsByIdUsuario(idUsuario));
			
		}catch(Exception e){
			//moiou
			e.printStackTrace();
		}
		
		return usuario;
	}
	
	public void exibirMensagemNaoLogado(String mensagem){
		
		alertDialog.setIcon(R.drawable.icon_alert);
		alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ir_tela_login),
			    new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int which) {
			        	logOff();
			        }
		});	
		
		alertMensagem(getString(R.string.alerta), mensagem);
	}
	
	public void exibirMensagemSemConexao(){
		
		alertDialog.setIcon(R.drawable.icon_alert);
		alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok),
			    new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int which) {
			        	dialog.dismiss();
			        }
		});	
		
		alertMensagem(getString(R.string.alerta), getString(R.string.sem_conexao));
	}
	
	private void alertMensagem(String titulo,String mensagem){
		
		alertDialog.setTitle(titulo);
		alertDialog.setMessage(mensagem);
		alertDialog.show();
	}
	
	
	@Override
	public void onResume(){
		super.onResume();	
	}
	
	@Override
	public void onPause(){
		super.onPause();		
	}
	


	/**
	 * Slide menu item click listener
	 * */
	private class SlideMenuClickListener implements	ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
			displayView(position,null);
		}
	}
	
	
	@Override
	public void onBackPressed() {
		FragmentManager fragmentManager = this.getSupportFragmentManager();
		
	    if (fragmentManager.getBackStackEntryCount() > 0){
	        fragmentManager.popBackStackImmediate();
	        fragmentManager.beginTransaction().commit();
	    }else{
	       // super.onBackPressed();
	    	moveTaskToBack(true);
	    }	
	}
	
	
	@Override
	protected void onNewIntent(Intent intent) {
		
		Toast.makeText(this, "USOU A QUE JA EXISTE", 2000).show();
		String frag = intent.getStringExtra("frag") == null ? "home": intent.getStringExtra("frag");
		
		buildDisplay(frag,intent);
		
		super.onNewIntent(intent);
	}


	/**
	 * Diplaying fragment view for selected nav drawer list item
	 * */
	public void displayView(int position,Intent intent) {
		
		FragmentManager fragmentManager = this.getSupportFragmentManager();
	 
//		while(fragmentManager.getBackStackEntryCount() > 0){
//	        fragmentManager.popBackStackImmediate();
//	    }
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		Long idAluno;
		 
		switch (position) {
		case 0:
			fragment = new HomeFragment();			
			transaction.replace(R.id.frame_container, fragment);
			break;
		case 1:			
			fragment = new AgendaFragment();
			transaction.replace(R.id.frame_container, fragment);
			break;
		case 2:
			fragment = new MensagemFragment();
			transaction.replace(R.id.frame_container, fragment);
			break;
		case 5:			
			fragment = null;
			logOff();			
			break;			
		case 10:
			
			idAluno = intent.getExtras().getLong("id_aLuno",0);
			
		    for(Aluno aluno:DadosUtil.usuarioLogado.getAlunos()){
		    	if(aluno.getId().equals(idAluno)){
		    		DadosUtil.alunoSelecionado = aluno;
		    		break;
		    	}
		    }
			
			position = 1;
						

			fragment = new AgendaFragment();
			transaction.replace(R.id.frame_container, fragment);
			
			break;			
			
		case 11:
			
			idAluno = intent.getExtras().getLong("id_aLuno",0);
						
		    for(Aluno aluno:DadosUtil.usuarioLogado.getAlunos()){
		    	if(aluno.getId().equals(idAluno)){
		    		DadosUtil.alunoSelecionado = aluno;
		    		break;
		    	}
		    }
			
			position = 2;
			
			
			fragment = new MensagemFragment();
			transaction.replace(R.id.frame_container, fragment);
			
			break;			
	
		}		
		
		if(intent != null && intent.getExtras() != null){
			fragment.setArguments(intent.getExtras());
		}			
		
		
		transaction.commitAllowingStateLoss();					

		// update selected item and title, then close the drawer
		mDrawerList.setItemChecked(position, true);
		mDrawerList.setSelection(position);
		setTitle(navMenuTitles[position]);
		mDrawerLayout.closeDrawer(mDrawerList);
		 
	}
	
	
	public void logOff(){
		Editor editor = prefs.edit();
		editor.putBoolean(Constantes.AUTOMATIC_LOGIN, false);
		editor.remove(Constantes.ID_LOGGED_USER);
		editor.commit();
		
		startActivity(new Intent(this, Login.class).putExtra("sair", true));
		
		
		finish();
	}
	



}
