package Grafo;

public class Ciudad {
	
	private String nombre;
	private Double x,y;
	
	public Ciudad(String n,Double x, Double y)
	{
		if(n==null || n.equals("")) throw new RuntimeException("Ingrese nombre de la ciudad");
		this.nombre=n;
		this.x=x;
		this.y=y;
	}

	@Override
	public String toString() {
		return nombre;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Ciudad other = (Ciudad) obj;
		if (nombre == null) {
			if (other.nombre != null)
				return false;
		} else if (!nombre.equals(other.nombre))
			return false;
		return true;
	}

	public double getX() {
		double ret=this.x;
		return ret;
	}

	public double getY() {
		double ret=this.y;
		return ret;
	}

}
