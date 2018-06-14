package info;

public class Info implements Comparable<Info> {
	protected Integer codigo;
	protected Integer frequencia;
	
	public Info(Integer c, Integer f) throws Exception {
		if (c != null)
			if (c < 0 || c > 255)
				throw new Exception("Codigo invalido");
		
		if (f < 0 || f == null)
			throw new Exception("Frequencia invalida");
		
		this.codigo = c;
		this.frequencia = f;
	}
	
	public Integer getCodigo() {
		return this.codigo;
	}
	
	public int getFrequencia() {
		return this.frequencia;
	}
	
	public void setCodigo(Integer c) throws Exception {
		if (c != null)
			if (c < 0 || c > 255)
				throw new Exception("Codigo invalido");
		
		this.codigo = c;
	}
	
	public void setFrequencia(Integer f) throws Exception {
		if (f < 0 || f == null)
			throw new Exception("Frequencia invalida");
		
		this.frequencia = f;
	}
	
	public String toString() {
		String ret = "{";
		ret += this.codigo + ",";
		ret += this.frequencia;
		ret += "}";
		
		return ret;
	}
	
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		
		if (obj == this)
			return true;
		
		if (obj.getClass() != this.getClass())
			return false;
		
		Info i = (Info) obj;
		
		if (this.codigo != i.codigo)
			return false;
		
		if (this.frequencia != i.frequencia)
			return false;
		
		return true;
	}
	
	public int hashCode() {
		int ret = 7;
		
		ret = ret*3 + new Integer(this.codigo).hashCode();
		ret = ret*3 + new Integer(this.frequencia).hashCode();
		
		return ret;
	}
	
	public Info(Info m) throws Exception {
		if (m == null)
			throw new Exception("Modelo nulo");
		
		this.codigo = m.codigo;
		this.frequencia = m.frequencia;
	}
	
	public Object clone() {
		Info ret = null;
		
		try {
			ret = new Info(this);
		}
		catch(Exception e) {}
		
		return ret;
	}

	public int compareTo(Info info) {
		if (info == null)
			return -1;
		
		if (this.frequencia < info.frequencia)
			return -1;
		
		if (this.frequencia > info.frequencia)
			return 1;
		
		return 0;
			
	}
}
