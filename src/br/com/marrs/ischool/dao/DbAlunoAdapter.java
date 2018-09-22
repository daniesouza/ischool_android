package br.com.marrs.ischool.dao;


import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import br.com.marrs.ischool.entidades.Aluno;
import br.com.marrs.ischool.entidades.Cliente;
import br.com.marrs.ischool.entidades.Usuario;
import br.com.marrs.ischool.util.ImagemUtils;

public class DbAlunoAdapter {

	public static final String KEY_ID		 	  	= "id_aluno";
	public static final String KEY_CODIGO_ALUNO  	= "codigo_aluno";	
	public static final String KEY_NOME_ALUNO  		= "nome_aluno";
	public static final String KEY_ATIVO  			= "ativo";
	public static final String KEY_CPF  			= "cpf_aluno";
	public static final String KEY_DATA_CADATRO  	= "data_cadastro";
	public static final String KEY_DATA_NASCIMENTO  = "data_nascimento";	
	public static final String KEY_EMAIL  			= "email";
	public static final String KEY_ENDERECO  		= "endereco";
	public static final String KEY_FALTAS  			= "faltas";
	public static final String KEY_NOTA  			= "notas";
	public static final String KEY_OBSERVACOES  	= "observacoes";	
	public static final String KEY_RG  				= "rg";
	public static final String KEY_TELEFONE  		= "telefone";
	public static final String KEY_ID_CLIENTE	  	= "id_cliente";	
	public static final String KEY_NOME_CLIENTE   	= "nome_cliente";
	public static final String KEY_ID_USUARIO	  	= "id_usuario";	
	public static final String KEY_NOME_USUARIO   	= "nome_usuario";	
	public static final String KEY_FOTO  			= "foto";
	public static final String KEY_MSG_DESCR_EVT	= "msg_descr_evet";
	public static final String KEY_QTDE_EVT_NOVO	= "qtde_evt_novo";
	public static final String KEY_MSG_DESCR_MSG	= "msg_descr_mensagem";
	public static final String KEY_QTDE_MSG_NOVO	= "qtde_mensagem_novo";
	public static final String KEY_FLAG_DELETADO	= "deletado";
	

	
	public static final String DB_NAME  	 = "ischool_userData";
	public static final String DB_TABLE 	 = "ALUNO";
	//private static final int    DB_VERSION = 2;
	
	private static final String DB_CREATE ="CREATE TABLE IF NOT EXISTS ALUNO(" +
											"id_aluno 		 NUMERIC NOT NULL, " +
											"codigo_aluno 	 text(30)," +
											"nome_aluno 	 text(255)," +
											"ativo 	 		 tinyInt(1)," +
											"cpf_aluno 	 	 NUMERIC(11)," +
											"data_cadastro 	 text(20)," +
											"data_nascimento text(20)," +
											"email 	 	 	 text(255)," +
											"endereco 	 	 text(255)," +
											"faltas 	 	 NUMERIC(3)," +			
											"notas 	 		 NUMERIC(3)," +
											"observacoes 	 text(255)," +
											"rg 	 		 text(11)," +											
											"telefone 		 NUMERIC," +
											"id_cliente 	 NUMERIC NOT NULL," +
											"nome_cliente 	 text(255)," +
											"id_usuario 	 NUMERIC NOT NULL," +
											"nome_usuario 	 text(255)," +
											"foto			 BLOB ," +
											"msg_descr_evet  text(255)," +
											"qtde_evt_novo 	 NUMERIC(3)," +
											"msg_descr_mensagem  text(255)," +
											"qtde_mensagem_novo 	 NUMERIC(3)," +											
											"deletado		 tinyInt(1) default 0," +
											"PRIMARY KEY (id_aluno,id_usuario) );";
	
	private SQLiteDatabase mDb;
	private final Context  mCtx;
    private SimpleDateFormat dateFormat;

	
	public DbAlunoAdapter(Context ctx) {
		mCtx = ctx;
		dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS",Locale.US);
	}
	
	private DbAlunoAdapter open(){
		try{
			mDb = mCtx.openOrCreateDatabase(DB_NAME, 0, null);
			//mDb.execSQL("DROP TABLE ALUNO");
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
	
	private void merge(Aluno aluno){
		
		int rows = returnCountById(aluno.getId(),aluno.getPai().getId());
		
		if(rows>0){
			update(aluno);
		}else{
			insert(aluno);
		}
	}
	
	private int returnCountById(long idAluno,long idUsuario){
		
		Cursor c = mDb.query(true, DB_TABLE,
					new String[]{KEY_ID}, KEY_ID + "=" +idAluno +" and "+KEY_ID_USUARIO +"="+ idUsuario,
			null, null,null,null, null);
			
			c.moveToFirst();
			
			int result = c.getCount();
			c.close();
			
			return result;	
	}
	
	
	private long insert(Aluno aluno){
			ContentValues contentValues = new ContentValues();
			
			contentValues.put(KEY_ID  			,  aluno.getId());	
			contentValues.put(KEY_CODIGO_ALUNO  ,  aluno.getCodigoAluno());	
			contentValues.put(KEY_NOME_ALUNO	,  aluno.getNome());		
			contentValues.put(KEY_ATIVO	  ,  aluno.getAtivo());		
			contentValues.put(KEY_CPF   ,  aluno.getCpf());		
			contentValues.put(KEY_DATA_CADATRO	  ,  aluno.getDataCadastro()  == null ? null :  dateFormat.format(aluno.getDataCadastro()));		
			contentValues.put(KEY_DATA_NASCIMENTO   , aluno.getDataNascimento() == null ? null :  dateFormat.format(aluno.getDataNascimento()));		
			contentValues.put(KEY_EMAIL  	  ,  aluno.getEmail());		
			contentValues.put(KEY_ENDERECO    ,  aluno.getEndereco());		
			contentValues.put(KEY_FALTAS  ,  aluno.getFaltas());		
			contentValues.put(KEY_NOTA  ,  aluno.getNota());	
			contentValues.put(KEY_OBSERVACOES  	, aluno.getObservacoes());		
			contentValues.put(KEY_RG  		,  aluno.getRg());		
			contentValues.put(KEY_TELEFONE  ,  aluno.getTelefone());
			contentValues.put(KEY_ID_CLIENTE	  ,  aluno.getCliente().getId());		
			contentValues.put(KEY_NOME_CLIENTE   ,  aluno.getCliente().getNome());		
			contentValues.put(KEY_ID_USUARIO	  ,  aluno.getPai().getId());		
			contentValues.put(KEY_NOME_USUARIO   ,  aluno.getPai().getNome());	
			
			return mDb.insert(DB_TABLE, null, contentValues);
	}
	
	public boolean delete(Aluno aluno){
		return mDb.delete(DB_TABLE, KEY_ID+"="+aluno.getId(), null)>0;
	}
	
	public List<Aluno> returnAllRows(){
		List<Aluno> lista = new ArrayList<Aluno>(); 
		Cursor c = null;
		try{
			open();
			c = mDb.query(DB_TABLE, 
					new String[]{
					KEY_ID,
					KEY_CODIGO_ALUNO,  
					KEY_NOME_ALUNO,  
					KEY_ATIVO,  
					KEY_CPF,  
					KEY_DATA_CADATRO,  
					KEY_DATA_NASCIMENTO,  
					KEY_EMAIL,  
					KEY_ENDERECO,  
					KEY_FALTAS,  
					KEY_NOTA,
					KEY_OBSERVACOES,
					KEY_RG,  
					KEY_TELEFONE,
					KEY_ID_CLIENTE,
					KEY_NOME_CLIENTE,
					KEY_ID_USUARIO,
					KEY_NOME_USUARIO,
					KEY_FOTO,
					KEY_MSG_DESCR_EVT,
					KEY_QTDE_EVT_NOVO,
					KEY_MSG_DESCR_MSG,
					KEY_QTDE_MSG_NOVO}, null, null, null, null, KEY_NOME_ALUNO +" desc");
			
	        c.moveToFirst(); 
	        
	        
	        while(!c.isAfterLast()){
	
	        	Aluno aluno = new Aluno();
	        	
	        	aluno.setId(c.getLong(0));
	        	aluno.setCodigoAluno(c.getString(1));      	
	        	aluno.setNome(c.getString(2));       	
	        	aluno.setAtivo(c.getInt(3) == 0 ? false:true);
	        	aluno.setCpf(c.getLong(4));      	
	        	aluno.setDataCadastro(c.getString(5) == null ? null : dateFormat.parse(c.getString(5)));		       	
	        	aluno.setDataNascimento(c.getString(6) == null ? null : dateFormat.parse(c.getString(6)));
	        	aluno.setEmail(c.getString(7));
	        	aluno.setEndereco(c.getString(8));
	        	aluno.setFaltas(c.getInt(9));
	        	aluno.setNota(c.getDouble(10));
	        	aluno.setObservacoes(c.getString(11));
	        	aluno.setRg(c.getString(12));
	        	aluno.setTelefone(c.getString(13));
	        	
	        	aluno.setCliente(new Cliente());
	        	aluno.getCliente().setId(c.getLong(15));
	        	aluno.getCliente().setNome(c.getString(16));
	        	
	        	aluno.setPai(new Usuario());
	        	aluno.getPai().setId(c.getLong(16));
	        	aluno.getPai().setNome(c.getString(17));
	        	
	        	byte[] byteArray = c.getBlob(18);  		        	
	        	if(byteArray != null){
	            	Bitmap bm = BitmapFactory.decodeByteArray(byteArray, 0 ,byteArray.length);
	            	aluno.setFoto(ImagemUtils.circledBitmap(bm));		
	        	}
	
	        	aluno.setMensagemDescricaoEvento(c.getString(19));
	        	aluno.setQuantidadeEventosNovos(c.getInt(20));
	        	
	        	aluno.setMensagemDescricaoMensagem(c.getString(21));
	        	aluno.setQuantidadeMensagensNovas(c.getInt(22));
	        	
	      	
	            lista.add(aluno);  
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
	
	
	public List<Aluno> returnAllRowsByIdUsuario(long idUsuario){
		List<Aluno> lista = new ArrayList<Aluno>(); 
		Cursor c = null;
		try{
			open();
			c = mDb.query(DB_TABLE, 
					new String[]{
					KEY_ID,
					KEY_CODIGO_ALUNO,  
					KEY_NOME_ALUNO,  
					KEY_ATIVO,  
					KEY_CPF,  
					KEY_DATA_CADATRO,  
					KEY_DATA_NASCIMENTO,  
					KEY_EMAIL,  
					KEY_ENDERECO,  
					KEY_FALTAS,  
					KEY_NOTA,
					KEY_OBSERVACOES,
					KEY_RG,  
					KEY_TELEFONE,
					KEY_ID_CLIENTE,
					KEY_NOME_CLIENTE,
					KEY_ID_USUARIO,
					KEY_NOME_USUARIO,
					KEY_FOTO,
					KEY_MSG_DESCR_EVT,
					KEY_QTDE_EVT_NOVO,					
					KEY_MSG_DESCR_MSG,
					KEY_QTDE_MSG_NOVO}, KEY_ID_USUARIO+ "="+idUsuario +" and "+KEY_FLAG_DELETADO+" = 0", null, null, null , KEY_NOME_ALUNO +" asc");
			
	        c.moveToFirst(); 
	        
	        
	        while(!c.isAfterLast()){
	
	        	Aluno aluno = new Aluno();
	        	
	        	aluno.setId(c.getLong(0));
	        	aluno.setCodigoAluno(c.getString(1));      	
	        	aluno.setNome(c.getString(2));       	
	        	aluno.setAtivo(c.getInt(3) == 0 ? false:true);
	        	aluno.setCpf(c.getLong(4));      	
	        	aluno.setDataCadastro(c.getString(5) == null ? null : dateFormat.parse(c.getString(5)));		       	
	        	aluno.setDataNascimento(c.getString(6) == null ? null : dateFormat.parse(c.getString(6)));
	        	aluno.setEmail(c.getString(7));
	        	aluno.setEndereco(c.getString(8));
	        	aluno.setFaltas(c.getInt(9));
	        	aluno.setNota(c.getDouble(10));
	        	aluno.setObservacoes(c.getString(11));
	        	aluno.setRg(c.getString(12));
	        	aluno.setTelefone(c.getString(13));
	        	
	        	aluno.setCliente(new Cliente());
	        	aluno.getCliente().setId(c.getLong(15));
	        	aluno.getCliente().setNome(c.getString(16));
	        	
	        	aluno.setPai(new Usuario());
	        	aluno.getPai().setId(c.getLong(16));
	        	aluno.getPai().setNome(c.getString(17));
	        	
	        	byte[] byteArray = c.getBlob(18);  		        	
	        	if(byteArray != null){
	            	Bitmap bm = BitmapFactory.decodeByteArray(byteArray, 0 ,byteArray.length);
	            	aluno.setFoto(ImagemUtils.circledBitmap(bm));
	        	}
		
	        	aluno.setMensagemDescricaoEvento(c.getString(19));
	        	aluno.setQuantidadeEventosNovos(c.getInt(20));
	        	
	        	aluno.setMensagemDescricaoMensagem(c.getString(21));
	        	aluno.setQuantidadeMensagensNovas(c.getInt(22));
	        	
	      	
	            lista.add(aluno);  
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
	
	public Aluno returnById(long id){
		Aluno aluno = null;
		Cursor c = null;
		try{
			open();
			c = mDb.query(true, DB_TABLE,
					new String[]{
					KEY_ID,
					KEY_CODIGO_ALUNO,  
					KEY_NOME_ALUNO,  
					KEY_ATIVO,  
					KEY_CPF,  
					KEY_DATA_CADATRO,  
					KEY_DATA_NASCIMENTO,  
					KEY_EMAIL,  
					KEY_ENDERECO,  
					KEY_FALTAS,  
					KEY_NOTA,
					KEY_OBSERVACOES,
					KEY_RG,  
					KEY_TELEFONE,
					KEY_ID_CLIENTE,
					KEY_NOME_CLIENTE,
					KEY_ID_USUARIO,
					KEY_NOME_USUARIO,
					KEY_FOTO,
					KEY_MSG_DESCR_EVT,
					KEY_QTDE_EVT_NOVO,					
					KEY_MSG_DESCR_MSG,
					KEY_QTDE_MSG_NOVO}, KEY_ID + "=" +id,
			null, null,null,null, null);
			
			c.moveToFirst();
					
			if(c.getCount() != 0){
				aluno = new Aluno();
	        	
	        	aluno.setId(c.getLong(0));
	        	aluno.setCodigoAluno(c.getString(1));      	
	        	aluno.setNome(c.getString(2));       	
	        	aluno.setAtivo(c.getInt(3) == 0 ? false:true);
	        	aluno.setCpf(c.getLong(4));      	
	        	aluno.setDataCadastro(c.getString(5) == null ? null : dateFormat.parse(c.getString(5)));		       	
	        	aluno.setDataNascimento(c.getString(6) == null ? null : dateFormat.parse(c.getString(6)));
	        	aluno.setEmail(c.getString(7));
	        	aluno.setEndereco(c.getString(8));
	        	aluno.setFaltas(c.getInt(9));
	        	aluno.setNota(c.getDouble(10));
	        	aluno.setObservacoes(c.getString(11));
	        	aluno.setRg(c.getString(12));
	        	aluno.setTelefone(c.getString(13));
	        	
	        	aluno.setCliente(new Cliente());
	        	aluno.getCliente().setId(c.getLong(15));
	        	aluno.getCliente().setNome(c.getString(16));
	        	
	        	aluno.setPai(new Usuario());
	        	aluno.getPai().setId(c.getLong(16));
	        	aluno.getPai().setNome(c.getString(17));
	        	
	        	byte[] byteArray = c.getBlob(18);  		        	
	        	if(byteArray != null){
	            	Bitmap bm = BitmapFactory.decodeByteArray(byteArray, 0 ,byteArray.length);
	            	aluno.setFoto(ImagemUtils.circledBitmap(bm));
	        	}
	        	
	        	aluno.setMensagemDescricaoEvento(c.getString(19));
	        	aluno.setQuantidadeEventosNovos(c.getInt(20));
	        	
	        	aluno.setMensagemDescricaoMensagem(c.getString(21));
	        	aluno.setQuantidadeMensagensNovas(c.getInt(22));
		
	        	
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
		
    	return aluno;
	}
	
	
	private boolean update(Aluno  aluno){
		ContentValues contentValues = new ContentValues();
		
		//contentValues.put(KEY_ID  			,  aluno.getId());	
		contentValues.put(KEY_CODIGO_ALUNO  ,  aluno.getCodigoAluno());	
		contentValues.put(KEY_NOME_ALUNO	,  aluno.getNome());		
		contentValues.put(KEY_ATIVO	  ,  aluno.getAtivo());		
		contentValues.put(KEY_CPF   ,  aluno.getCpf());		
		contentValues.put(KEY_DATA_CADATRO	  ,  aluno.getDataCadastro()  == null ? null :  dateFormat.format(aluno.getDataCadastro()));		
		contentValues.put(KEY_DATA_NASCIMENTO   , aluno.getDataNascimento() == null ? null :  dateFormat.format(aluno.getDataNascimento()));		
		contentValues.put(KEY_EMAIL  	  ,  aluno.getEmail());		
		contentValues.put(KEY_ENDERECO    ,  aluno.getEndereco());		
		contentValues.put(KEY_FALTAS  ,  aluno.getFaltas());		
		contentValues.put(KEY_NOTA  ,  aluno.getNota());	
		contentValues.put(KEY_OBSERVACOES  	, aluno.getObservacoes());		
		contentValues.put(KEY_RG  		,  aluno.getRg());		
		contentValues.put(KEY_TELEFONE  ,  aluno.getTelefone());
		contentValues.put(KEY_ID_CLIENTE	  ,  aluno.getCliente().getId());		
		contentValues.put(KEY_NOME_CLIENTE   ,  aluno.getCliente().getNome());		
		contentValues.put(KEY_ID_USUARIO	  ,  aluno.getPai().getId());		
		contentValues.put(KEY_NOME_USUARIO   ,  aluno.getPai().getNome());	
		
		
		return mDb.update(DB_TABLE, contentValues, KEY_ID + "=" +aluno.getId() +" and "+KEY_ID_USUARIO+ "="+aluno.getPai().getId(), null)>0;
	}
	
	
	public Bitmap getAlunoImage(long idAluno,long idPai){

		Cursor c = null;
		Bitmap bm  = null;
		try{
			open();
			c = mDb.query(true, DB_TABLE,
					new String[]{
					KEY_FOTO}, KEY_ID + "=" +idAluno +" and "+KEY_ID_USUARIO+ "="+idPai,
			null, null,null,null, null);
			
			c.moveToFirst();
					
			if(c.getCount() != 0){
	        	
	        	byte[] byteArray = c.getBlob(0); 
	        	if(byteArray != null){
	            	bm = BitmapFactory.decodeByteArray(byteArray, 0 ,byteArray.length);        	
	        	}
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
		
		return bm;
				
	}
	

	public boolean storeAlunoImage(Bitmap image,long idAluno,long idPai){
		ContentValues contentValues = new ContentValues();
		try{
			open();		
			if(image != null){
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				image.compress(Bitmap.CompressFormat.PNG, 0, bos);
				contentValues.put(KEY_FOTO  , bos.toByteArray());
				return mDb.update(DB_TABLE, contentValues, KEY_ID + "=" +idAluno +" and "+KEY_ID_USUARIO+ "="+idPai, null)>0;
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				close();
			}catch(Exception ex){
				// fodeu
				ex.printStackTrace();
			}
		}
		
		return false;
		
	}
	
	
//	public boolean storeAlunoNewEventos(Aluno aluno){
//		ContentValues contentValues = new ContentValues();
//		try{
//			open();		
//
//			contentValues.put(KEY_MSG_DESCR_EVT  , aluno.getMensagemDescricaoEvento());
//			contentValues.put(KEY_QTDE_EVT_NOVO  , aluno.getQuantidadeEventosNovos());
//			
//			return mDb.update(DB_TABLE, contentValues, KEY_ID + "=" +aluno.getId() +" and "+KEY_ID_USUARIO+ "="+aluno.getPai().getId(), null)>0;
//			
//		}catch(Exception e){
//			e.printStackTrace();
//		}finally{
//			try{
//				close();
//			}catch(Exception ex){
//				// fodeu
//				ex.printStackTrace();
//			}
//		}
//		
//		return false;
//		
//	}
//	
//	public boolean clearAlunosQuantEventos(Aluno aluno){
//		ContentValues contentValues = new ContentValues();
//		try{
//			open();		
//
//			contentValues.put(KEY_QTDE_EVT_NOVO  , 0);
//			
//			return mDb.update(DB_TABLE, contentValues, KEY_ID + "=" +aluno.getId() +" and "+KEY_ID_USUARIO+ "="+aluno.getPai().getId(), null)>0;
//			
//		}catch(Exception e){
//			e.printStackTrace();
//		}finally{
//			try{
//				close();
//			}catch(Exception ex){
//				// fodeu
//				ex.printStackTrace();
//			}
//		}
//		
//		return false;
//		
//	}
	
	
	public boolean storeAlunoNewMensagens(Aluno aluno){
		ContentValues contentValues = new ContentValues();
		try{
			open();		

			contentValues.put(KEY_MSG_DESCR_MSG  , aluno.getMensagemDescricaoMensagem());
			contentValues.put(KEY_QTDE_MSG_NOVO  , aluno.getQuantidadeMensagensNovas());
			
			return mDb.update(DB_TABLE, contentValues, KEY_ID + "=" +aluno.getId() +" and "+KEY_ID_USUARIO+ "="+aluno.getPai().getId(), null)>0;
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				close();
			}catch(Exception ex){
				// fodeu
				ex.printStackTrace();
			}
		}
		
		return false;
		
	}
	
	public boolean clearAlunosQuantMensagens(Aluno aluno){
		ContentValues contentValues = new ContentValues();
		try{
			open();		

			contentValues.put(KEY_QTDE_MSG_NOVO  , 0);
			
			return mDb.update(DB_TABLE, contentValues, KEY_ID + "=" +aluno.getId() +" and "+KEY_ID_USUARIO+ "="+aluno.getPai().getId(), null)>0;
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				close();
			}catch(Exception ex){
				// fodeu
				ex.printStackTrace();
			}
		}
		
		return false;
		
	}
	
	public boolean deactivateAlunosNOTIN(List<Aluno> alunos){
		ContentValues contentValues = new ContentValues();
		try{

			if(!alunos.isEmpty()){
				
				String ids = "";
				
				for(Aluno aluno:alunos){
					ids += aluno.getId()+",";
				}
				
				ids = ids.substring(0, ids.lastIndexOf(","));
				
				contentValues.put(KEY_FLAG_DELETADO  , 1);
				
				return mDb.update(DB_TABLE, contentValues, KEY_ID + " NOT IN (" +ids +") and "+KEY_ID_USUARIO+ "="+alunos.get(0).getPai().getId(), null)>0;
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}		
		return false;
		
	}
	
	public boolean activateAlunosIN(List<Aluno> alunos){
		ContentValues contentValues = new ContentValues();
		try{

			if(!alunos.isEmpty()){
				
				String ids = "";
				
				for(Aluno aluno:alunos){
					ids += aluno.getId()+",";
				}
				
				ids = ids.substring(0, ids.lastIndexOf(","));
				
				contentValues.put(KEY_FLAG_DELETADO  , 0);
				
				return mDb.update(DB_TABLE, contentValues, KEY_ID + " IN (" +ids +") and "+KEY_ID_USUARIO+ "="+alunos.get(0).getPai().getId(), null)>0;
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}		
		return false;
		
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
	
	public void storeAlunosInDataBase(final List<Aluno> alunos){
		
		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void...params) {
								
				try{
					open();
					
					for(Aluno aluno:alunos){
						merge(aluno);
					}
					
					activateAlunosIN(alunos);
					deactivateAlunosNOTIN(alunos);
					
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
	
}
