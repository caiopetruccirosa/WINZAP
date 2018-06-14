package meurandomaccessfile;

import java.io.*;
import codigo.*;

public class MeuRandomAccessFile extends RandomAccessFile {
	protected int bits;
	protected int quantos;
	protected int quantosLixo;
	
	public MeuRandomAccessFile(File arq, String s) throws FileNotFoundException {
		super(arq, s);
		this.bits = 0;
		this.quantos = 0;
		this.quantosLixo = 8;
	}
	
	public MeuRandomAccessFile(String arq, String s) throws FileNotFoundException {      
        super(arq, s);
        this.bits = 0;
        this.quantos = 0;
        this.quantosLixo = 8;
    }
	
	public void escreverCodigo(Codigo c) throws Exception {
		if (c == null)
			throw new Exception("Codigo nulo");
		
		String codigo = c.getCodigo();
		for(int i = 0;i < codigo.length(); i++) { 
		    if(!(this.quantos < 8)) {
		        this.write(this.bits);
		        this.bits = 0;
		        this.quantos = 0;
		        this.quantosLixo = 8;
		    }
		    
		    this.bits = this.bits << 1;
	        int bit = Integer.parseInt(codigo.charAt(i) + "");
	        this.bits = this.bits | bit;
	        this.quantos++;
	        this.quantosLixo--;
		}	
	}
	
	public int getQuantidadeLixo() {
		return this.quantosLixo;
	}
	
	public void preencherLixo() {
		this.bits = this.bits << this.quantosLixo;
		try {
			this.write(this.bits);
		}
		catch (Exception e) {}
		this.bits = 0;
		this.quantos = 0;
		this.quantosLixo = 8;
	}
}
