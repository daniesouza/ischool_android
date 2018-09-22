package br.com.marrs.ischool.dao;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import br.com.marrs.ischool.entidades.Aluno;
import br.com.marrs.ischool.entidades.Cliente;
import br.com.marrs.ischool.entidades.Mensagem;
import br.com.marrs.ischool.entidades.Usuario;
import br.com.marrs.ischool.util.Constantes;
import br.com.marrs.ischool.util.DadosUtil;

public class DbMensagemAdapter {

	public static final String KEY_ID		 	  	= "id_device_mensagem";
	public static final String KEY_ID_MSG_WEB	  	= "id_web_mensagem";	
	public static final String KEY_ID_CLIENTE	  	= "id_cliente";	
	public static final String KEY_NOME_CLIENTE   	= "nome_cliente";
	public static final String KEY_ID_USUARIO	  	= "id_usuario";	
	public static final String KEY_NOME_USUARIO   	= "nome_usuario";
	public static final String KEY_ID_ALUNO  	  	= "id_aluno";
	public static final String KEY_NOME_ALUNO    	= "nome_aluno";	
	public static final String KEY_TEXTO_MENSAGEM   = "texto_mensagem";	
	public static final String KEY_DATA_CADASTRO  	= "data_cadastro";
	public static final String KEY_TAMANH_BYTES  	= "tamanho_bytes";
	public static final String KEY_HASH_ARQUIVO  	= "hash_arquivo";
	public static final String KEY_CAMINHO_ARQUIVO 	= "caminho_arquivo";
	public static final String KEY_TIPO_ACAO  		= "tipo_acao";
	public static final String KEY_STATUS_ACAO  	= "status_acao";
	

		
	public static final String DB_NAME  	 = "ischool_userData";
	public static final String DB_TABLE 	 = "MENSAGENS";
	//private static final int    DB_VERSION = 2;
	
	private static final String DB_CREATE ="CREATE TABLE IF NOT EXISTS MENSAGENS(" +
											"id_device_mensagem INTEGER NOT NULL primary key, " +
											"id_web_mensagem 	NUMERIC, " +
											"id_cliente 	 NUMERIC NOT NULL," +
											"nome_cliente 	 text(255)," +
											"id_usuario 	 NUMERIC NOT NULL," +
											"nome_usuario 	 text(255)," +
											"id_aluno 	 	 NUMERIC NOT NULL," +
											"nome_aluno 	 text(255)," +
											"texto_mensagem  text(255)," +
											"data_cadastro 	 text(20)," +
											"tamanho_bytes 	 NUMERIC," +
											"hash_arquivo	 text(35)," +
											"caminho_arquivo text(255)," +
											"tipo_acao 	 	 tinyInt(1)," +
											"status_acao 	 tinyInt(1));";
	
	private SQLiteDatabase mDb;
	private final Context  mCtx;
    private SimpleDateFormat dateFormat;

	
	public DbMensagemAdapter(Context ctx) {
		mCtx = ctx;
		dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS",Locale.US);
	}
	
	private DbMensagemAdapter open(){
		try{
			mDb = mCtx.openOrCreateDatabase(DB_NAME, 0, null);
	//		mDb.execSQL("DROP TABLE MENSAGENS");
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
	
	private void merge(Mensagem mensagem){
		
		int rows = returnCountById(mensagem.getId());
		
		if(rows>0){
			update(mensagem);
		}else{
			insert(mensagem);
		}
	}
	
	private int returnCountById(Long id){
		
		if(id == null || id == 0l){
			return 0;
		}
		
		Cursor c = mDb.query(true, DB_TABLE,
					new String[]{KEY_ID}, KEY_ID + "=" +id,
			null, null,null,null, null);
			
			c.moveToFirst();
			
			int result = c.getCount();
			c.close();
			
			return result;	
	}
	
	
	private long insert(Mensagem mensagem){
			ContentValues contentValues = new ContentValues();
					
			mensagem.setId(Long.valueOf(DadosUtil.deviceID.toString()+""+System.currentTimeMillis()));
			
			contentValues.put(KEY_ID  			,  mensagem.getId());
			contentValues.put(KEY_ID_MSG_WEB  	,  mensagem.getIdWebMensagem());	
			contentValues.put(KEY_ID_CLIENTE	,  mensagem.getCliente().getId());		
			contentValues.put(KEY_NOME_CLIENTE   ,  mensagem.getCliente().getNome());		
			contentValues.put(KEY_ID_USUARIO	  ,  mensagem.getUsuario().getId());		
			contentValues.put(KEY_NOME_USUARIO   ,  mensagem.getUsuario().getNome());		
			contentValues.put(KEY_ID_ALUNO  	,  mensagem.getAluno().getId());		
			contentValues.put(KEY_NOME_ALUNO	,  mensagem.getAluno().getNome());	
			contentValues.put(KEY_TEXTO_MENSAGEM	,  mensagem.getMensagem());	
			contentValues.put(KEY_DATA_CADASTRO  ,  mensagem.getDataCadastro() == null ? null : dateFormat.format(mensagem.getDataCadastro()));		
			contentValues.put(KEY_TAMANH_BYTES	,  mensagem.getTamanhoBytes());	
			contentValues.put(KEY_HASH_ARQUIVO	,  mensagem.getHashArquivo());
			contentValues.put(KEY_CAMINHO_ARQUIVO	,  mensagem.getCaminhoArquivo()); 
			contentValues.put(KEY_TIPO_ACAO	,  mensagem.getTipoAcao());
			contentValues.put(KEY_STATUS_ACAO	,  mensagem.getStatusAcao());
			

			return mDb.insert(DB_TABLE, null, contentValues);
	}
	
	
	private boolean update(Mensagem  mensagem){
		ContentValues contentValues = new ContentValues();
		
		contentValues.put(KEY_ID  			,  mensagem.getId());	
		contentValues.put(KEY_ID_MSG_WEB  	,  mensagem.getIdWebMensagem());
		contentValues.put(KEY_ID_CLIENTE	  ,  mensagem.getCliente().getId());		
		contentValues.put(KEY_NOME_CLIENTE   ,  mensagem.getCliente().getNome());		
		contentValues.put(KEY_ID_USUARIO	  ,  mensagem.getUsuario().getId());		
		contentValues.put(KEY_NOME_USUARIO   ,  mensagem.getUsuario().getNome());		
		contentValues.put(KEY_ID_ALUNO  	,  mensagem.getAluno().getId());		
		contentValues.put(KEY_NOME_ALUNO	,  mensagem.getAluno().getNome());	
		contentValues.put(KEY_TEXTO_MENSAGEM	,  mensagem.getMensagem());	
		contentValues.put(KEY_DATA_CADASTRO  ,  mensagem.getDataCadastro() == null ? null : dateFormat.format(mensagem.getDataCadastro()));		
		contentValues.put(KEY_TAMANH_BYTES	,  mensagem.getTamanhoBytes());	
		contentValues.put(KEY_HASH_ARQUIVO	,  mensagem.getHashArquivo());	
		contentValues.put(KEY_CAMINHO_ARQUIVO	,  mensagem.getCaminhoArquivo()); 
		contentValues.put(KEY_TIPO_ACAO	,  mensagem.getTipoAcao());
		contentValues.put(KEY_STATUS_ACAO	,  mensagem.getStatusAcao());
		
		return mDb.update(DB_TABLE, contentValues, KEY_ID + "=" +mensagem.getId(), null)>0;
	}
	
	public boolean delete(Mensagem Mensagem){
		return mDb.delete(DB_TABLE, KEY_ID+"="+Mensagem.getId(), null)>0;
	}
	
	public List<Mensagem> returnAllRows(){
		List<Mensagem> lista = new ArrayList<Mensagem>(); 
		Cursor c = null;
		try{
		open();

		c = mDb.query(DB_TABLE, 
				new String[]{
				KEY_ID,
				KEY_ID_MSG_WEB,
				KEY_ID_CLIENTE,  
				KEY_NOME_CLIENTE,  
				KEY_ID_USUARIO,  
				KEY_NOME_USUARIO,  
				KEY_ID_ALUNO,
				KEY_NOME_ALUNO,
				KEY_TEXTO_MENSAGEM,
				KEY_DATA_CADASTRO,
				KEY_TAMANH_BYTES,
				KEY_HASH_ARQUIVO,
				KEY_CAMINHO_ARQUIVO,
				KEY_TIPO_ACAO,
				KEY_STATUS_ACAO}, null, null, null, null, KEY_DATA_CADASTRO +" desc");
		
        c.moveToFirst(); 
        
        
        while(!c.isAfterLast()){

        	Mensagem mensagem = new Mensagem();
        	
        	mensagem.setId(c.getLong(0));
        	mensagem.setIdWebMensagem(c.getLong(1));
        	
        	mensagem.setCliente(new Cliente());
        	mensagem.getCliente().setId(c.getLong(2));
        	mensagem.getCliente().setNome(c.getString(3));
        	
        	mensagem.setUsuario(new Usuario());
        	mensagem.getUsuario().setId(c.getLong(4));
        	mensagem.getUsuario().setNome(c.getString(5));

        	
        	mensagem.setAluno(new Aluno());
        	mensagem.getAluno().setId(c.getLong(6));
        	mensagem.getAluno().setNome(c.getString(7));
        	
        	mensagem.setMensagem(c.getString(8));
        	mensagem.setDataCadastro(c.getString(9) == null ? null : dateFormat.parse(c.getString(9)));		
        	mensagem.setTamanhoBytes(c.getLong(10));
        	mensagem.setHashArquivo(c.getString(11));
        	mensagem.setCaminhoArquivo(c.getString(12));
        	mensagem.setTipoAcao(c.getInt(13));
        	mensagem.setStatusAcao(c.getInt(14));
		
        	
            lista.add(mensagem);  
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
	
	
	public Mensagem returnById(long id){
		Mensagem Mensagem = null;
		
		Cursor c = null;
		try{
		 open();
		 c = mDb.query(true, DB_TABLE,
				new String[]{
					KEY_ID,
					KEY_ID_MSG_WEB,
					KEY_ID_CLIENTE,  
					KEY_NOME_CLIENTE,  
					KEY_ID_USUARIO,  
					KEY_NOME_USUARIO,  
					KEY_ID_ALUNO,
					KEY_NOME_ALUNO,
					KEY_TEXTO_MENSAGEM,
					KEY_DATA_CADASTRO,
					KEY_TAMANH_BYTES,
					KEY_HASH_ARQUIVO,
					KEY_CAMINHO_ARQUIVO,
					KEY_TIPO_ACAO,
					KEY_STATUS_ACAO}, KEY_ID + "=" +id,
		null, null,null,null, null);
		
		c.moveToFirst();
				
		if(c.getCount() != 0){
        	Mensagem mensagem = new Mensagem();
        	
        	mensagem.setId(c.getLong(0));
        	mensagem.setIdWebMensagem(c.getLong(1));
        	
        	mensagem.setCliente(new Cliente());
        	mensagem.getCliente().setId(c.getLong(2));
        	mensagem.getCliente().setNome(c.getString(3));
        	
        	mensagem.setUsuario(new Usuario());
        	mensagem.getUsuario().setId(c.getLong(4));
        	mensagem.getUsuario().setNome(c.getString(5));

        	
        	mensagem.setAluno(new Aluno());
        	mensagem.getAluno().setId(c.getLong(6));
        	mensagem.getAluno().setNome(c.getString(7));
        	
        	mensagem.setMensagem(c.getString(8));
        	mensagem.setDataCadastro(c.getString(9) == null ? null : dateFormat.parse(c.getString(9)));		
        	mensagem.setTamanhoBytes(c.getLong(10));
        	mensagem.setHashArquivo(c.getString(11));
        	mensagem.setCaminhoArquivo(c.getString(12));
        	mensagem.setTipoAcao(c.getInt(13));
        	mensagem.setStatusAcao(c.getInt(14));

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
		
    	return Mensagem;
	}
	
	public List<Long> getIdsSincronizar(long idAluno){
		List<Long> lista = new ArrayList<Long>(); 
		
		Calendar dataInicio = Calendar.getInstance();
		
		dataInicio.set(Calendar.HOUR_OF_DAY, 0);
		dataInicio.set(Calendar.MINUTE, 0);
		dataInicio.set(Calendar.SECOND, 0);
		dataInicio.set(Calendar.MILLISECOND, 0);
					
		Calendar dataFim = Calendar.getInstance();
		dataFim.set(Calendar.HOUR_OF_DAY, 23);
		dataFim.set(Calendar.MINUTE, 59);
		dataFim.set(Calendar.SECOND, 59);
		dataFim.set(Calendar.MILLISECOND, 999);
		
		Cursor c = null;
		try{
		 open();
		 c = mDb.query(DB_TABLE, 
				new String[]{
					KEY_ID_MSG_WEB},					
					KEY_DATA_CADASTRO+" >= Datetime('"+dateFormat.format(dataInicio.getTime())+"') and "+KEY_DATA_CADASTRO+ " <= Datetime('"+dateFormat.format(dataFim.getTime())+"') and "+
					KEY_ID_ALUNO+"="+idAluno,
				null, null,null,null,null);
		
        c.moveToFirst(); 
        
        
        while(!c.isAfterLast()){
            lista.add(c.getLong(0));  
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
	
	public List<Mensagem> returnByoffSet(long idAluno,long offset){
		List<Mensagem> lista = new ArrayList<Mensagem>(); 
		
		Calendar dataInicio = Calendar.getInstance();
		
		dataInicio.set(Calendar.HOUR_OF_DAY, 0);
		dataInicio.set(Calendar.MINUTE, 0);
		dataInicio.set(Calendar.SECOND, 0);
		dataInicio.set(Calendar.MILLISECOND, 0);
					
		Calendar dataFim = Calendar.getInstance();
		dataFim.set(Calendar.HOUR_OF_DAY, 23);
		dataFim.set(Calendar.MINUTE, 59);
		dataFim.set(Calendar.SECOND, 59);
		dataFim.set(Calendar.MILLISECOND, 999);
		
		Cursor c = null;
		try{
		 open();
		 c = mDb.query(DB_TABLE, 
				new String[]{
					KEY_ID,
					KEY_ID_MSG_WEB,
					KEY_ID_CLIENTE,  
					KEY_NOME_CLIENTE,  
					KEY_ID_USUARIO,  
					KEY_NOME_USUARIO,  
					KEY_ID_ALUNO,
					KEY_NOME_ALUNO,
					KEY_TEXTO_MENSAGEM,
					KEY_DATA_CADASTRO,
					KEY_TAMANH_BYTES,
					KEY_HASH_ARQUIVO,
					KEY_CAMINHO_ARQUIVO,
					KEY_TIPO_ACAO,
					KEY_STATUS_ACAO},
					
					//KEY_DATA_CADASTRO+" >= Datetime('"+dateFormat.format(dataInicio.getTime())+"') and "+KEY_DATA_CADASTRO+ " <= Datetime('"+dateFormat.format(dataFim.getTime())+"') and "+
					KEY_ID_ALUNO+"="+idAluno+ (offset == 0 ? "" : " and "+KEY_ID+" < "+offset)+" order by "+KEY_ID+" desc LIMIT "+Constantes.ITENS_PAGINACAO,
				null, null,null,null,null);
		
        c.moveToFirst(); 
        
        
        while(!c.isAfterLast()){

        	Mensagem mensagem = new Mensagem();
        	
       // 	System.out.println(c.getLong(0)+ " - " +c.getString(8));
        	
        	mensagem.setId(c.getLong(0));
        	mensagem.setIdWebMensagem(c.getLong(1));
        	
        	mensagem.setCliente(new Cliente());
        	mensagem.getCliente().setId(c.getLong(2));
        	mensagem.getCliente().setNome(c.getString(3));
        	
        	mensagem.setUsuario(new Usuario());
        	mensagem.getUsuario().setId(c.getLong(4));
        	mensagem.getUsuario().setNome(c.getString(5));

        	
        	mensagem.setAluno(new Aluno());
        	mensagem.getAluno().setId(c.getLong(6));
        	mensagem.getAluno().setNome(c.getString(7));
        	
        	mensagem.setMensagem(c.getString(8));
        	mensagem.setDataCadastro(c.getString(9) == null ? null : dateFormat.parse(c.getString(9)));		
        	mensagem.setTamanhoBytes(c.getLong(10));
        	mensagem.setHashArquivo(c.getString(11));
        	mensagem.setCaminhoArquivo(c.getString(12));
        	mensagem.setTipoAcao(c.getInt(13));
        	mensagem.setStatusAcao(c.getInt(14));
        	
            lista.add(mensagem);  
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
	
	public synchronized void storeMensagensInDataBase(final List<Mensagem> mensagens){
		
		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void...params) {
								
				try{
					open();
					
					for(Mensagem mensagem:mensagens){
						merge(mensagem);						
					}				
					
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
			
			
		}.execute();
	}
	
	
	public synchronized void storeMensagemInDataBase(final Mensagem mensagem){
		
		try{
			open();
			
			merge(mensagem);		
						
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
	}
	
}
