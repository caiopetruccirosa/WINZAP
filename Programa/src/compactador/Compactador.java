package compactador;

import java.io.*;

import codigo.*;
import info.*;
import meurandomaccessfile.*;
import no.*;

public class Compactador {
	protected File arquivo;
	protected No<Info> raiz;
	
	protected int qtd;
	protected int[] caracteres;
	protected No<Info>[] nos;
	
	protected Codigo[] codigos;
	protected Codigo codigo;
	
	public Compactador(File arq) throws Exception {
		if (arq == null)
			throw new FileNotFoundException("Arquivo nulo");
		
		this.arquivo = arq;
		this.raiz = null;
		
		this.qtd = 0;
		this.caracteres = new int[256];
		this.nos = new No[256];
		
		this.codigos = new Codigo[256];
		this.codigo = null;
	}
	
	public void compactar() {
		try {
			MeuRandomAccessFile leitor = new MeuRandomAccessFile(this.arquivo, "r");
			for (int i = 0; i < leitor.length(); i++)
				this.caracteres[leitor.read()]++;
			leitor.close();
			
			for (int i = 0; i < this.caracteres.length; i++) {
				if (this.caracteres[i] > 0) {
					this.nos[i] = new No<Info>(new Info(i, this.caracteres[i]));
					this.qtd++;
				}
			}
			
			this.ordenar(this.nos);
			for (int i = this.nos.length-1; i > 0; i--) {
				if (this.nos[i] != null) {
					No<Info> esq = this.nos[i];
					No<Info> dir = this.nos[i-1];
					
					int freq = esq.getInfo().getFrequencia() + dir.getInfo().getFrequencia();
					No<Info> no = new No<Info>(esq, new Info(null, freq), dir);
					
					this.nos[i] = null;
					this.nos[i-1] = no;
					
					this.ordenar(this.nos);
				}
			}
			
			this.raiz = nos[0];
			this.codigo = new Codigo();
			this.transformarEmCodigo(this.raiz);
			this.escreverArquivoCompactado();
		} 
		catch (Exception e) {}
	}
	
	public void descompactar() throws Exception {
		if (!this.getFileExtension(this.arquivo).equals("zap"))
			throw new Exception("Arquivo inválido para descompactação!");
		
		try {
			MeuRandomAccessFile leitor = new MeuRandomAccessFile(this.arquivo, "r");
			
			int tamanhoExtensao = leitor.read();
			String extensao = "";
			for (int i = 0; i < tamanhoExtensao; i++)
				extensao += (char)leitor.read();

			int qtdLixo = leitor.read();
			this.qtd = leitor.readInt();
			
			for (int i = 0; i < this.qtd; i++) {
				int codigo = leitor.read();
				int frequencia = leitor.readInt();
				
				this.caracteres[codigo] = frequencia;
			}
			
			for (int i = 0; i < this.caracteres.length; i++) {
				if (this.caracteres[i] > 0)
					this.nos[i] = new No<Info>(new Info(i, this.caracteres[i]));
			}
			
			this.ordenar(this.nos);
			for (int i = this.nos.length-1; i > 0; i--) {
				if (this.nos[i] != null) {
					No<Info> esq = this.nos[i];
					No<Info> dir = this.nos[i-1];
					
					int freq = esq.getInfo().getFrequencia() + dir.getInfo().getFrequencia();
					No<Info> no = new No<Info>(esq, new Info(null, freq), dir);
					
					this.nos[i] = null;
					this.nos[i-1] = no;
					
					this.ordenar(this.nos);
				}
			}
			
			this.raiz = this.nos[0];
			this.codigo = new Codigo();
			this.transformarEmCodigo(this.raiz);
			this.escreverArquivoDescompactado(leitor, extensao, qtdLixo);
			leitor.close();
		} 
		catch (Exception e) {}
	}
	
	/////////////////////////////////////////////////////////////
	
	protected void transformarEmCodigo(No<Info> no) {
		if (no != null) {
			Integer codigoAtual = no.getInfo().getCodigo();
	        
			if (codigoAtual != null)
				this.codigos[codigoAtual] = (Codigo)this.codigo.clone();
	        else {
	            try {
	            	this.codigo.mais(0);
	                transformarEmCodigo(no.getEsq());
	                this.codigo.tiraUltimo();
	                this.codigo.mais(1);
	                transformarEmCodigo(no.getDir());
	                this.codigo.tiraUltimo();
	            }
	            catch(Exception e) {
	            	e.printStackTrace();
	            }
	        }
	    }
    }
	
	protected void escreverArquivoDescompactado(MeuRandomAccessFile leitor, String extensao, int qtdLixo) {
		try {
			File novoArquivo = this.gerarNovoArquivo(this.getDir(this.arquivo), extensao);
			
			MeuRandomAccessFile escritor = new MeuRandomAccessFile(novoArquivo, "rw");
			
			String codCaracter = "";
			String codByte = "";
			
			No<Info> no = this.raiz;
			
			for (long i = leitor.getFilePointer(); i < leitor.length(); i++) {
				int valorByte = leitor.read();
				
				codByte = "";
				for (int b = 0; b < 8; b++) {
					int bit = this.getBit(valorByte, b);
					codByte = bit + codByte;
				}
				
				if (i < leitor.length()-1) {
					for (int j = 0; j < codByte.length(); j++) {
						if (no.getEsq() == null && no.getDir() == null) {
							for (int k = 0; k < this.codigos.length; k++) {
								if (this.codigos[k] != null) {
									if (this.codigos[k].getCodigo().equals(codCaracter)) {
										char c = (char)k;
										escritor.write(c);
										
										codCaracter = "";
										no = this.raiz;
										
										break;
									}
								}
							}
						}
						
						char bit = codByte.charAt(j);
						codCaracter += bit;
						if (bit == '1')
							no = no.getDir();
						else
							no = no.getEsq();
					}
				} else {
					for (int j = 0; j < codByte.length()-qtdLixo; j++) {
						if (no.getEsq() == null && no.getDir() == null) {
							for (int k = 0; k < this.codigos.length; k++) {
								if (this.codigos[k] != null) {
									if (this.codigos[k].getCodigo().equals(codCaracter)) {
										char c = (char)k;
										escritor.write(c);
										
										codCaracter = "";
										no = this.raiz;
										
										break;
									}
								}
							}
						}
						
						char bit = codByte.charAt(j);
						codCaracter += bit;
						if (bit == '1')
							no = no.getDir();
						else
							no = no.getEsq();
					}
					
					if (!codCaracter.equals("")) {
						for (int k = 0; k < this.codigos.length; k++) {
							if (this.codigos[k] != null) {
								if (this.codigos[k].getCodigo().equals(codCaracter)) {
									char c = (char)k;
									escritor.write(c);
									
									codCaracter = "";
									no = this.raiz;
									
									break;
								}
							}
						}	
					}
				}
			}

			escritor.close();
		}
		catch(Exception e) {}
	}
	
	///////////////////////////////////////////////////////////
	
	protected void escreverArquivoCompactado() {
		try {
			String extensao = this.getFileExtension(this.arquivo);
			File novoArquivo = this.gerarNovoArquivo(this.getDir(this.arquivo), "zap");
			
			MeuRandomAccessFile escritor = new MeuRandomAccessFile(novoArquivo, "rw");
			MeuRandomAccessFile leitor = new MeuRandomAccessFile(this.arquivo, "r");
			
			escritor.write(extensao.length());
			for (int i = 0; i < extensao.length(); i++)
				escritor.write((int)extensao.charAt(i));
			
			long posLixo = escritor.getFilePointer();
			escritor.write(0);
			escritor.writeInt(this.qtd);
			
			for (int i = 0; i < this.caracteres.length; i++) {
				if (this.caracteres[i] > 0) {
					escritor.write(i);
					escritor.writeInt(this.caracteres[i]);
				}
			}
			
			for (int i = 0; i < leitor.length(); i++)
				escritor.escreverCodigo(this.codigos[leitor.read()]);
			
			int qtdLixo = escritor.getQuantidadeLixo();
			escritor.preencherLixo();
			
			escritor.seek(posLixo);
			escritor.write(qtdLixo);
			
			escritor.close();
			leitor.close();
		}
		catch(Exception e) {}
	}
	
	protected File gerarNovoArquivo(String path, String extensao) {
		File arq = new File(path + "." + extensao);
		
		int n = 1;
		while (arq.exists()) {
			arq = new File(path + "(" + n + ")." + extensao);
			n++;
		}

		return arq;
	}
	
	protected String getDir(File arq) {
		String dir = null;
		try {
			dir = arq.getCanonicalPath();
			return dir.substring(0, dir.lastIndexOf('.'));
		}
		catch (Exception e) {
			return dir;
		}
	}
	
	protected int getBit(int n, int k) {
		return (n >> k) & 1;
	}
	
	protected String getFileExtension(File arq) {
        try {
        	String filename = arq.getName();
            return filename.substring(filename.lastIndexOf(".")+1);
        } 
        catch (Exception e) { return ""; }
    }
	
	protected void ordenar(No<Info>[] arr) throws Exception {
		if (arr == null)
			throw new Exception("Vetor nulo");
		
		int maior;
		No<Info> aux;
		for (int i = 0; i < arr.length-1; i++) {
			maior = i;
			
			for (int j = (i+1); j < arr.length; j++) {
				if (arr[j] != null) {
					if (arr[maior] == null)
						maior = j;
					else if (arr[j].getInfo().compareTo(arr[maior].getInfo()) > 0)
						maior = j;
				}
			}
				
			if (i != maior) {
				aux = arr[i];
				arr[i] = arr[maior];
				arr[maior] = aux;
			}
		}
	}
}
