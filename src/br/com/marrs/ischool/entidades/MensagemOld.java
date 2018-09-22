package br.com.marrs.ischool.entidades;

public class MensagemOld 
{
	public int id_sistema, icon,gravity,color;
	public String data, mensagem;
	
	public MensagemOld(int id_sistema, String data, int icon,int gravity,int color, String mensagem)
	{
		this.id_sistema = id_sistema;
		this.data = data;
		this.icon = icon;
		this.mensagem = mensagem;
		this.gravity = gravity;
		this.color = color;
	}
}
