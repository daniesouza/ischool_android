package br.com.marrs.ischool.dao;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import br.com.marrs.ischool.entidades.Cliente;
import br.com.marrs.ischool.entidades.Usuario;

public class DbUsuarioAdapter {

	public static final String KEY_ID		 	  	= "id_usuario";
	public static final String KEY_AUTORIDADE  		= "autoridade";	
	public static final String KEY_ID_CLIENTE  		= "id_cliente";
	public static final String KEY_ATIVO  			= "ativo";
	public static final String KEY_CPF  			= "cpf";
	public static final String KEY_DATA_CADATRO  	= "data_cadastro";
	public static final String KEY_DATA_NASCIMENTO  = "data_nascimento";	
	public static final String KEY_EMAIL  			= "email";
	public static final String KEY_ENDERECO  		= "endereco";
	public static final String KEY_NOME  			= "nome";
	public static final String KEY_RG  				= "rg";
	public static final String KEY_TELEFONE  		= "telefone";
	public static final String KEY_LOGIN_USUARIO  	= "login";	
	public static final String KEY_SECURITY_HASH  	= "security_hash_key";	
	public static final String KEY_DATA_VAL_HASH  	= "data_validade_hash";	
	public static final String KEY_DATA_ATUALIZACAO = "data_atualizacao";
	
	public static final String DB_NAME  	 = "ischool_userData";
	public static final String DB_TABLE 	 = "USUARIO";
	//private static final int    DB_VERSION = 2;
	
	private static final String DB_CREATE ="CREATE TABLE IF NOT EXISTS USUARIO(" +
											"id_usuario 	 NUMERIC NOT NULL primary key, " +
											"autoridade 	 text(30)," +
											"id_cliente 	 NUMERIC," +
											"ativo 	 		 tinyInt(1)," +
											"cpf 	 		 NUMERIC(11)," +
											"data_cadastro 	 text(20)," +
											"data_nascimento text(20)," +
											"email 	 	 	 text(255)," +
											"endereco 	 	 text(255)," +
											"nome 	 	 	 text(255)," +			
											"rg 	 		 text(11)," +											
											"telefone 		 NUMERIC," +
											"login			 text(30)," +
											"security_hash_key	text(35)," +
											"data_validade_hash	NUMERIC(15)," +
											"data_atualizacao NUMERIC);";
	
	private SQLiteDatabase mDb;
	private final Context  mCtx;
    private SimpleDateFormat dateFormat;

	
	public DbUsuarioAdapter(Context ctx) {
		mCtx = ctx;
		dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS",Locale.US);
	}
	
	private DbUsuarioAdapter open(){
		try{
			mDb = mCtx.openOrCreateDatabase(DB_NAME, 0, null);
			//mDb.execSQL("DROP TABLE USUARIO");
			mDb.execSQL(DB_CREATE);				
		} catch(SQLException e){
			e.printStackTrace();
			throw new SQLException("Nao Conseguiu Criar o DB");
		}
		
		return this;
	}
	
	private void close(){
		mDb.close();
	}
	
	private void merge(Usuario usuario){
			
		int rows = returnCountById(usuario.getId());
		
		if(rows>0){
			update(usuario);
		}else{
			insert(usuario);
		}	

	}
	
	private int returnCountById(long id){
		
		Cursor c = mDb.query(true, DB_TABLE,
					new String[]{KEY_ID}, KEY_ID + "=" +id,
			null, null,null,null, null);
			
			c.moveToFirst();
			
			int result = c.getCount();
			c.close();
			
			return result;	
	}
	
	
	private long insert(Usuario usuario){
			ContentValues contentValues = new ContentValues();
			
			contentValues.put(KEY_ID  			,  usuario.getId());	
			contentValues.put(KEY_AUTORIDADE  ,  usuario.getAutoridade());	
			contentValues.put(KEY_ID_CLIENTE	,  usuario.getCliente().getId());		
			contentValues.put(KEY_ATIVO	  , 	usuario.getAtivo());		
			contentValues.put(KEY_CPF   ,  usuario.getCpf());		
			contentValues.put(KEY_DATA_CADATRO	  ,  usuario.getDtCad()  == null ? null :  dateFormat.format(usuario.getDtCad()));		
			contentValues.put(KEY_DATA_NASCIMENTO   , "");		
			contentValues.put(KEY_EMAIL  	  ,  usuario.getEmail());		
			contentValues.put(KEY_ENDERECO    ,  usuario.getEndereco());		
			contentValues.put(KEY_NOME  ,  usuario.getNome());		
			contentValues.put(KEY_RG  		,  usuario.getRg());		
			contentValues.put(KEY_TELEFONE  ,  usuario.getTelefone());
			contentValues.put(KEY_LOGIN_USUARIO  ,  usuario.getUsuario());
			contentValues.put(KEY_SECURITY_HASH  ,  usuario.getSecurityHashKey());
			contentValues.put(KEY_DATA_VAL_HASH  ,  usuario.getDataValidadeHash());
			contentValues.put(KEY_DATA_ATUALIZACAO  ,  usuario.getDataAtualizacao());

			

			
			return mDb.insert(DB_TABLE, null, contentValues);
	}
	
	public boolean delete(Usuario usuario){
		return mDb.delete(DB_TABLE, KEY_ID+"="+usuario.getId(), null)>0;
	}
	
	public List<Usuario> returnAllRows(){
		List<Usuario> lista = new ArrayList<Usuario>(); 
		Cursor c = null;
		try{
			open();
		
			c = mDb.query(DB_TABLE, 
				new String[]{
				KEY_ID,
				KEY_AUTORIDADE,  		
				KEY_ID_CLIENTE , 		
				KEY_ATIVO,  		
				KEY_CPF , 			
				KEY_DATA_CADATRO,  
				KEY_DATA_NASCIMENTO,
				KEY_EMAIL,  			
				KEY_ENDERECO , 		
				KEY_NOME , 			
				KEY_RG,  				
				KEY_TELEFONE , 		
				KEY_LOGIN_USUARIO,
				KEY_SECURITY_HASH,
				KEY_DATA_VAL_HASH,
				KEY_DATA_ATUALIZACAO}, null, null, null, null, null);
		
        c.moveToFirst(); 
        
        
        while(!c.isAfterLast()){

        	Usuario usuario = new Usuario();
        	
        	usuario.setId(c.getLong(0));
        	usuario.setAutoridade(c.getString(1));   
        	usuario.setCliente(new Cliente());    
        	usuario.getCliente().setId(c.getLong(2));       	
        	usuario.setAtivo(c.getInt(3) == 0 ? false:true);
        	usuario.setCpf(c.getLong(4));      	
        	usuario.setDtCad(c.getString(5) == null ? null : dateFormat.parse(c.getString(5)));		       	
        	//usuario.setDataNascimento(c.getString(6) == null ? null : dateFormat.parse(c.getString(6)));
        	usuario.setEmail(c.getString(7));
        	usuario.setEndereco(c.getString(8));
        	usuario.setNome(c.getString(9));
        	usuario.setRg(c.getString(10));
        	usuario.setTelefone(c.getString(11));
        	usuario.setUsuario(c.getString(12));
        	usuario.setSecurityHashKey(c.getString(13));
        	usuario.setDataValidadeHash(c.getLong(14));
        	usuario.setDataAtualizacao(c.getLong(15));
      	
            lista.add(usuario);  
            c.moveToNext();  
        }  
        
        
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			try{
				if(c != null){
					c.close();
				}
				close();
			}catch(Exception ex){
				// fodeu
				ex.printStackTrace();
			}

		}
        
        return lista; 
	}
	
	
	public Usuario returnById(long id){
		Usuario usuario = null;
		Cursor c = null;
		try{
			open();
			
			c = mDb.query(true, DB_TABLE,
					new String[]{
					KEY_ID,
					KEY_AUTORIDADE,  		
					KEY_ID_CLIENTE , 		
					KEY_ATIVO,  		
					KEY_CPF , 			
					KEY_DATA_CADATRO,  
					KEY_DATA_NASCIMENTO,
					KEY_EMAIL,  			
					KEY_ENDERECO , 		
					KEY_NOME , 			
					KEY_RG,  				
					KEY_TELEFONE , 		
					KEY_LOGIN_USUARIO,
					KEY_SECURITY_HASH,
					KEY_DATA_VAL_HASH,
					KEY_DATA_ATUALIZACAO}, KEY_ID + "=" +id,
			null, null,null,null, null);
			
			c.moveToFirst();
					
			if(c.getCount() != 0)
			{
				usuario = new Usuario();
	        	
	        	usuario.setId(c.getLong(0));
	        	usuario.setAutoridade(c.getString(1));   
	        	usuario.setCliente(new Cliente());    
	        	usuario.getCliente().setId(c.getLong(2));       	
	        	usuario.setAtivo(c.getInt(3) == 0 ? false:true);
	        	usuario.setCpf(c.getLong(4));      	
	        	usuario.setDtCad(c.getString(5) == null ? null : dateFormat.parse(c.getString(5)));		       	
	        	//usuario.setDataNascimento(c.getString(6) == null ? null : dateFormat.parse(c.getString(6)));
	        	usuario.setEmail(c.getString(7));
	        	usuario.setEndereco(c.getString(8));
	        	usuario.setNome(c.getString(9));
	        	usuario.setRg(c.getString(10));
	        	usuario.setTelefone(c.getString(11));
	        	usuario.setUsuario(c.getString(12));
	        	usuario.setSecurityHashKey(c.getString(13));
	        	usuario.setDataValidadeHash(c.getLong(14));
	        	usuario.setDataAtualizacao(c.getLong(15));
	        	
			}
				    	
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			try{
				if(c != null){
					c.close();
				}
				close();
			}catch(Exception ex){
				// fodeu
				ex.printStackTrace();
			}

		}

		return usuario;
	}
	
	
	private boolean update(Usuario  usuario){
		ContentValues contentValues = new ContentValues();
		
		contentValues.put(KEY_ID  			,  usuario.getId());	
		contentValues.put(KEY_AUTORIDADE  ,  usuario.getAutoridade());	
		contentValues.put(KEY_ID_CLIENTE	,  usuario.getCliente().getId());		
		contentValues.put(KEY_ATIVO	  , 	usuario.getAtivo());		
		contentValues.put(KEY_CPF   ,  usuario.getCpf());		
		contentValues.put(KEY_DATA_CADATRO	  ,  usuario.getDtCad()  == null ? null :  dateFormat.format(usuario.getDtCad()));		
		contentValues.put(KEY_DATA_NASCIMENTO   , "");		
		contentValues.put(KEY_EMAIL  	  ,  usuario.getEmail());		
		contentValues.put(KEY_ENDERECO    ,  usuario.getEndereco());		
		contentValues.put(KEY_NOME  ,  usuario.getNome());		
		contentValues.put(KEY_RG  		,  usuario.getRg());		
		contentValues.put(KEY_TELEFONE  ,  usuario.getTelefone());
		contentValues.put(KEY_LOGIN_USUARIO  ,  usuario.getUsuario());
		contentValues.put(KEY_SECURITY_HASH  ,  usuario.getSecurityHashKey());
		contentValues.put(KEY_DATA_VAL_HASH  ,  usuario.getDataValidadeHash());
		contentValues.put(KEY_DATA_ATUALIZACAO  ,  usuario.getDataAtualizacao());
		
		
		return mDb.update(DB_TABLE, contentValues, KEY_ID + "=" +usuario.getId(), null)>0;
	}
	
	
	public void deleteAllData(){
		try{
			open();
			mDb.execSQL("delete from "+DB_TABLE);
		}catch(Exception e){
			
		}finally{
			try{
				close();
			}catch(Exception ex){
				// fodeu
				ex.printStackTrace();
			}
		}	
	}
	
	
	public void storeLoggedUserInDataBase(Usuario usuarioLogado){
		
		new AsyncTask<Usuario, Void, Void>() {

			@Override
			protected Void doInBackground(Usuario... usuarioLogado) {
				
				try{
					open();			
					merge(usuarioLogado[0]);				
				}catch(Exception ex){
					ex.printStackTrace();
				}finally{
					try{
						close();
					}catch(Exception ex){
						// fodeu
						ex.printStackTrace();
					}
				}
				return null;
			}
			
			
		}.execute(usuarioLogado);
	}
	
}
