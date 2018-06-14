package no;

import java.lang.reflect.*;

public class No<X extends Comparable<X>> {
    protected No<X> esq;
    protected X info;
    protected No<X> dir;

    protected X meuCloneDe(X x) {
        X ret = null;

        try
        {
            Class<?> classe = x.getClass();
            Class<?>[] tipoDoParametroFormal = null; // pq clone tem 0 parametros
            Method metodo = classe.getMethod ("clone", tipoDoParametroFormal);
            Object[] parametroReal = null;// pq clone tem 0 parametros
            ret = ((X)metodo.invoke (x, parametroReal));
        }
        catch (NoSuchMethodException erro)
        {}
        catch (InvocationTargetException erro)
        {}
        catch (IllegalAccessException erro)
        {}

        return ret;
    }
    
    public No<X> getEsq() {
        return this.esq;
    }

    public No<X> getDir() {
        return this.dir;
    }
    
    public X getInfo() {
        return this.info;
    }

    public void setEsq(No<X> e) {
        this.esq = e;
    }

    public void setDir(No<X> d) {
        this.dir = d;
    }
    
    public void setInfo(X x) throws Exception {
    	if (x == null)
    		throw new Exception("Informacao nula");
    	
    	if (x instanceof Cloneable)
    		this.info = this.meuCloneDe(x);
    	else
    		this.info = x;
    }

    public No(No<X> e, X x, No<X> d) throws Exception {
    	if (x == null)
    		throw new Exception("Informacao nula");
    	
    	if (x instanceof Cloneable)
    		this.info = this.meuCloneDe(x);
    	else
    		this.info = x;
    	
        this.esq = e;
        this.dir = d;
    }

    public No(X x) throws Exception {
        this(null, x, null);
    }

    public String toString() {
        String ret = "[";

        ret += this.esq + ", ";
        ret += "(";
        ret += this.info.toString();
        ret += "), ";
        ret += this.dir;

        ret += "]";

        return ret;
    }

    public int hashCode() {
        int ret = 7;

        if (this.esq != null)
            ret = ret * 7 + this.dir.hashCode();

        ret = ret * 7 + this.info.hashCode();

        if (this.dir != null)
            ret = ret * 7 + this.esq.hashCode();

        return ret;
    }

    public boolean equals(Object obj) {
        if (obj == null)
            return false;

        if (obj == this)
            return true;

        if (obj.getClass() != this.getClass())
            return false;

        No<X> no = (No<X>) obj;

        if (this.esq != null) {
            if (!this.esq.equals(no.esq))
                return false;
        } else {
             if (no.esq != null)
                return false;
        }

        if (!this.info.equals(no.info))
        	return false;

        if (this.dir != null) {
            if (!this.dir.equals(no.dir))
                return false;
        } else {
             if (no.dir != null)
                return false;
        }

        return true;
    }

    public No(No<X> m) throws Exception {
    	if (m == null)
    		throw new Exception("No nulo");

    	if (m.dir != null)
    		this.dir = (No<X>)m.dir.clone();
    	else
    		this.dir = null;

    	this.info = m.info;

    	if (m.esq != null)
    		this.esq = (No<X>)m.esq.clone();
    	else
    		this.esq = null;
    }

    public Object clone() {
    	No<X> ret = null;

        try {
            ret = new No<X>(this);
        } 
        catch (Exception e) {}

        return ret;
    }
}
