package br.com.marrs.ischool.view;

import java.io.File;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import br.com.marrs.ischool.MainActivity;
import br.com.marrs.ischool.R;
import br.com.marrs.ischool.entidades.Aluno;
import br.com.marrs.ischool.util.Constantes;
import br.com.marrs.ischool.util.DadosUtil;
import br.com.marrs.ischool.util.ImagemUtils;

public class AlunoActivity extends Activity{

	//private static final int FRAGMENT_ID = 3;
	private ListView listaALuno;
	public  ArrayAdapter<Aluno> adapter;
	private ProgressBar progress;
	private Uri fotoOriginal;
//	private LinearLayout layoutErro;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aluno_layout);
		progress = (ProgressBar) findViewById(R.id.progress_aluno);
		
			
		listaALuno = (ListView)findViewById(R.id.lv_aluno);
			
		adapter = new AlunoAdapter(this);			
			
		listaALuno.setAdapter(adapter);
				
		//MainActivity.instance.loginManager.removeListeners();
			
	}
	
	
	@Override
	public void onResume(){	
		super.onResume();
		handleLoginProcess();	
	}
		
	public void handleLoginProcess(){		
		buildComponents();	
	}
	
	@Override
	public void onPause(){
		super.onPause();
	}
	
	public void buildComponents(){
		
		DadosUtil.usuarioLogado.setAlunos(MainActivity.instance.dbAlunoAdapter.returnAllRowsByIdUsuario(DadosUtil.usuarioLogado.getId()));
		if(DadosUtil.usuarioLogado.getAlunos() != null && !DadosUtil.usuarioLogado.getAlunos().isEmpty()){					
			if(adapter.isEmpty()){
				adapter.addAll(DadosUtil.usuarioLogado.getAlunos());
			}
		
		}else{
			
			//TODO implementar uma mensagem que o pai nao tem filho
		}
	}
	
	private class AlunoAdapter extends ArrayAdapter<Aluno>{
		public AlunoAdapter(Context context) {
			super(context, 0);
		}

		public View getView(int position, View convertView, ViewGroup parent){
			if (convertView == null){
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.aluno_item, parent,false);
			}
			
			TextView tv_titulo = (TextView) convertView.findViewById(R.id.tv_linha1);
			TextView tv_texto = (TextView) convertView.findViewById(R.id.tv_linha2);
			TextView quantNot = (TextView) convertView.findViewById(R.id.tv_quantidade_not);
			ImageView icon = (ImageView)convertView.findViewById(R.id.iv_aluno_icon);

			LinearLayout alunoPainel = (LinearLayout) convertView.findViewById(R.id.aluno_painel);
			
			final Aluno aluno = adapter.getItem(position);	
			
			if(DadosUtil.alunoSelecionado != null && aluno.getId().equals(DadosUtil.alunoSelecionado.getId())){
				convertView.setBackgroundColor(getResources().getColor(R.color.orange_claro));
			}else{
				convertView.setBackgroundColor(Color.WHITE);
			}
			
			if(aluno.getFoto() != null){						
				icon.setImageBitmap(aluno.getFoto());
			}else{
				icon.setImageResource(R.drawable.icon_filho_no_pic);
			}
			
			if(aluno.getQuantidadeMensagensNovas() > 0){
				quantNot.setText(Integer.toString(aluno.getQuantidadeMensagensNovas()));
				quantNot.setVisibility(View.VISIBLE);
			}else{
				quantNot.setVisibility(View.INVISIBLE);
			}
	
			tv_titulo.setText(aluno.getNome());
			
			if(aluno.getMensagemDescricaoMensagem() != null){
				String texto = aluno.getMensagemDescricaoMensagem();
				if(texto.length()>28){
					texto = texto.substring(0,28);
				}
				
				tv_texto.setText(Html.fromHtml(texto));
			}else{
				tv_texto.setText("");
			}				
			
			alunoPainel.setOnClickListener(new View.OnClickListener(){
			    @Override
			    public void onClick(View v){
			    	DadosUtil.alunoSelecionado = aluno;
			    	finish();
			    }
			});
			

			
			icon.setOnClickListener(new View.OnClickListener(){
			    @Override
			    public void onClick(View v){
			    	DadosUtil.alunoSelecionado = aluno;
					
					// custom dialog
					final Dialog dialog = new Dialog(AlunoActivity.this);
					dialog.setContentView(R.layout.dialog_foto_aluno);
					dialog.setTitle("Definir uma foto do perfil");
		 
					LinearLayout painelTirarFoto = (LinearLayout) dialog.findViewById(R.id.ll_item_maquina);

					painelTirarFoto.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							
			                if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			                    Toast.makeText(AlunoActivity.this, "Erro ao acessar diretorio. Não e possivel tirar foto.", Toast.LENGTH_SHORT).show();
			                	return;
			                }
			                
			                File rootPath = new File(Environment.getExternalStorageDirectory()+File.separator+Constantes.DIRETORIO_ROOT+File.separator+Constantes.DIRETORIO_MEDIA,Constantes.DIRETORIO_IMAGES);
			                if(!rootPath.exists()) {
			                    if(!rootPath.mkdirs()){                  	
			                        Toast.makeText(AlunoActivity.this, "Erro ao acessar diretorio. Não e possivel tirar foto.", Toast.LENGTH_SHORT).show();
			                    	return;
			                    }
			                }
						
	                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

	                        fotoOriginal = Uri.fromFile(new File(rootPath, String.valueOf(aluno.getNome()+System.currentTimeMillis())+".jpg"));

	                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fotoOriginal);	                        
	                        startActivityForResult(cameraIntent, Constantes.TAKE_PICTURE_VIDEO);		
	                       
							dialog.dismiss();
						}
					});
					
					LinearLayout painelGaleria = (LinearLayout) dialog.findViewById(R.id.ll_item_galeria);

					painelGaleria.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							
		                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

		                    startActivityForResult(intent, Constantes.SELECT_FILE);
		                    dialog.dismiss();
						}
					});					
		 
					dialog.show();				
			    }
			});
			
			return convertView;
		}
	}
	

	
	public void startProgress(){
		progress.setVisibility(View.VISIBLE);
	}
	
	public void stopProgress(){
		progress.setVisibility(View.GONE);
	}

	
	    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == Activity.RESULT_OK) {

            if (requestCode == Constantes.TAKE_PICTURE_VIDEO) {
            	
            	try{          		
            		Intent cropIntent = new Intent("com.android.camera.action.CROP"); 
            		cropIntent.setDataAndType(fotoOriginal, "image/*");
            		cropIntent.putExtra("crop", "true");
            		cropIntent.putExtra("aspectX", 1);
            		cropIntent.putExtra("aspectY", 1);
            		cropIntent.putExtra("outputX", 300);
            		cropIntent.putExtra("outputY", 300);
            		cropIntent.putExtra("return-data", true);
            		startActivityForResult(cropIntent, Constantes.PIC_CROP);        		
            	}catch(Exception ex){
            		ex.printStackTrace();
            	}
            	

            } else if (requestCode == Constantes.SELECT_FILE) {  
                
                try {
                	Uri selectedImage = data.getData();
            		Intent cropIntent = new Intent("com.android.camera.action.CROP"); 
            		cropIntent.setDataAndType(selectedImage, "image/*");
            		cropIntent.putExtra("crop", "true");
            		cropIntent.putExtra("aspectX", 1);
            		cropIntent.putExtra("aspectY", 1);
            		cropIntent.putExtra("outputX", 300);
            		cropIntent.putExtra("outputY", 300);
            		cropIntent.putExtra("return-data", true);
            		startActivityForResult(cropIntent, Constantes.PIC_CROP);    
				} catch (Exception e1) {
					e1.printStackTrace();
				}

            }else if(requestCode == Constantes.PIC_CROP){
            	Uri imageCropped = data.getData();
            	Bundle extras = data.getExtras();
            	Bitmap thePic = extras.getParcelable("data");
                MainActivity.instance.dbAlunoAdapter.storeAlunoImage(thePic, DadosUtil.alunoSelecionado.getId(),DadosUtil.alunoSelecionado.getPai().getId());           	
                DadosUtil.alunoSelecionado.setFoto(ImagemUtils.circledBitmap(thePic)); 
                new File(DadosUtil.getRealPathFromURI(imageCropped,AlunoActivity.this)).delete();               
                if(fotoOriginal != null){
                    new File(DadosUtil.getRealPathFromURI(fotoOriginal,AlunoActivity.this)).delete();
                }
                
                adapter.notifyDataSetChanged();
            }

        }
    }
    
    


}
