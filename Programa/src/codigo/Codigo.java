package codigo;

public class Codigo {
	protected String cod;
	
	public Codigo() {
		this.cod = "";
	}
	
	public String getCodigo() {
		return this.cod;
	}
	
	public void setCodigo(String codigo) throws Exception {
		if (codigo == null || codigo.trim() == "")
			throw new Exception("Codigo nulo");
		
		this.cod = codigo;
	}
	
	public void tiraUltimo() throws Exception {
		if (this.cod.trim() == "")
			throw new Exception("Codigo vazio");
		
		this.cod = this.cod.substring(0, this.cod.length() - 1);
	}
	
	public void tiraPrim() throws Exception {
		if (this.cod.trim() == "")
			throw new Exception("Codigo vazio");
		
		this.cod = this.cod.substring(1);
    }
	
	public void mais(int x) throws Exception {
		if (x > 1 || x < 0)
			throw new Exception ("Parametro inválido");
		
		this.cod += x;
	}
	
	public int hashCode() {
		int ret = 3;
		ret = ret*7 + this.cod.hashCode();
		return ret;
	}
	
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		
		if (this == obj)
			return true;
		
		if (this.getClass() != obj.getClass())
			return false;
		
		Codigo c = (Codigo)obj;
		
		if (!this.cod.equals(c.cod))
			return false;
		
		return true;
	}
	
	public String toString() {
		return "{" + this.cod + "}";
	}
	
	public Codigo(Codigo m) throws Exception {
		if (m == null)
			throw new Exception("Modelo nulo");
		
		this.cod = m.cod;
	}
	
	public Object clone() {
		Codigo ret = null;
		
		try {
			ret = new Codigo(this);
		}
		catch (Exception e) {}
		
		return ret;
	}
}
