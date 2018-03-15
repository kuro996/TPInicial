package Grafo;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class Grafo
{
	// Representamos el grafo por listas de vecinos,una linkedList para contener los datos a mostrar, 
	//un array para la matrizde distancia y otro array para las distintas soluciones al problema del viajero de comercio
	private LinkedList<LinkedList<Integer>> _vecinos;
	private LinkedList<Ciudad> _ciudades;
	private ArrayList<ArrayList<Integer>> _matrix;
	private ArrayList<ArrayList<Ciudad>> _soluciones;
	
	
	// El constructor inicializa todo, menos las soluciones, en vacio
	public Grafo()
	{
		_vecinos = new LinkedList<LinkedList<Integer>>();
		_ciudades= new LinkedList<Ciudad>();
		_matrix = new ArrayList<ArrayList<Integer>>();
	}
	
	//agrega una ciudad y la conecta a todas las existentes, de haber un espacio null en la linkedList lo llena con la nueva ciudad 
	public void agregarCiudad(String ciudad,Double x,Double y)
	{
		if(this.existeCiudad(ciudad)) return;
		if(this._ciudades.contains(null))
		{
			Ciudad ci=new Ciudad(ciudad,x,y);
			int aux=_ciudades.indexOf(null);
			_ciudades.set(aux, ci);	
		}
		else
		{
		Random distancia=new Random();
		Ciudad ci=new Ciudad(ciudad,x,y);
		_vecinos.add(new LinkedList<Integer>());
		_ciudades.add(ci);
		
		for(int i=0;i<_vecinos.size();i++) 
			if(i!=_ciudades.indexOf(ci))this.agregarArista(_ciudades.indexOf(ci), i);
		
		this._matrix.add(new ArrayList<Integer>());
		
		for(int i=0;i<this._matrix.size();i++)
			for (int j = this._matrix.get(i).size(); j < this._matrix.size(); j++)	
				this._matrix.get(i).add(distancia.nextInt(60)+10);
		}
			
	}
	
	// Agregar una arista
	private void agregarArista(int i, int j)
	{
		chequearArista(i, j);
		
		_vecinos.get(i).add(j);
		_vecinos.get(j).add(i);
	}
	
	//se decidio , a la hora de eliminar una ciudad, simplemente poner esta en null en la LinkedList de ciudades ya que todos los metodos ignoran 
	//las poscisiones en donde la linkedList esta en null
	public void eliminarCiudad(String ciudad)
	{
		Ciudad ci=new Ciudad(ciudad,null,null);
		if(_ciudades.contains(ci))
		{
		this._ciudades.set(this._ciudades.indexOf(ci), null);
		}
	}
	
	// Código defensivo: Chequea que los parámetros sean válidos
	private void chequearArista(int i, int j)
	{
		if( i == j )
			throw new IllegalArgumentException("No se pueden agregar loops!");
	}
	
	//modifica las distancia entre dos ciudades en la matriz
	public void editarDistancia(String ciudadUno,String ciudadDos,Integer distancia)
	{
		_matrix.get(_ciudades.indexOf(new Ciudad(ciudadUno,null,null))).set(_ciudades.indexOf(new Ciudad(ciudadDos,null,null)), distancia);
		_matrix.get(_ciudades.indexOf(new Ciudad(ciudadDos,null,null))).set(_ciudades.indexOf(new Ciudad(ciudadUno,null,null)), distancia);
	}
	
	//devuelve un String con el nombre de todas las ciudades y entre corchetes a cuales esta conectado
	public String toString()
	{
		String ret=new String();
		for(int i=0;i<this._vecinos.size();i++)
		{
				if(_ciudades.get(i)!=null)
				{
					ret=ret+_ciudades.get(i).toString()+"[";
					LinkedList<Integer> a=this._vecinos.get(i);
			
					for(int h=0;h<a.size();h++)
					{
						if(this._ciudades.get(a.get(h))!=null)
						{
							ret=ret+this._ciudades.get(a.get(h)).toString()+"("+this._matrix.get(i).get(a.get(h))+") ,";
						}
					}
					ret=ret+"] ";
				}
		}
		return ret;
	}

	//devuelve la cantidad de elementos no null contiene la lista de ciudades
	public int cantCiudades() 
	{
		@SuppressWarnings("unchecked")
		LinkedList<Ciudad> aux=(LinkedList<Ciudad>)_ciudades.clone();
		while(aux.contains(null))
		{
			aux.remove(null);
		}
		return aux.size();
	}

	//devuelve la ciudad segun el indice 
	public Ciudad getCiudad(int i) {
		@SuppressWarnings("unchecked")
		LinkedList<Ciudad> aux=(LinkedList<Ciudad>)_ciudades.clone();
		while(aux.contains(null))
		{
			aux.remove(null);
		}
		return aux.get(i);
	}

	
	//devuele a las ciudades que esta conectada la ciudad ingresada
	public LinkedList<String> vecinos(String ciudad) {
		LinkedList<String> ret=new LinkedList<String>();
		for(Integer i:this._vecinos.get(_ciudades.indexOf(new Ciudad(ciudad,null,null))))
		{
			if(_ciudades.get(i)!=null)ret.add(_ciudades.get(i).toString());
		}
		return ret;
	}
	
	//devuekve una solucion golasa,en este caso es el algoritmo de prim
	public ArrayList<Ciudad> solucionGololsa (String ciuInicio)
	{	
		_soluciones=new ArrayList<ArrayList<Ciudad>>();//se inicializa las soluciones
		
		ArrayList<Ciudad> camino = new ArrayList<Ciudad>();
		camino.add(_ciudades.get(_ciudades.indexOf(new Ciudad(ciuInicio,null,null))));
		int posicionCiudadActiual = _ciudades.indexOf(new Ciudad(ciuInicio,null,null));
		while(camino.size()<this.cantCiudades())
		{
			Ciudad menor = buscarMenor(_matrix.get(posicionCiudadActiual),posicionCiudadActiual, camino); //me da la posicion de la distancia mas corta
			camino.add(menor);
			posicionCiudadActiual = _ciudades.indexOf(menor);
		}
		camino.add(_ciudades.get(_ciudades.indexOf(new Ciudad(ciuInicio,null,null))));
		_soluciones.add(camino);
		return camino;
	}
	
	//retorna el menor nodo al que este conectado la ciudad ingresada (algoritmo de prim)
	private Ciudad buscarMenor(ArrayList<Integer> vecinos,int ciudadActual, ArrayList<Ciudad> visitado) 
	{
		int menor = Integer.MAX_VALUE;
		Ciudad asd=new Ciudad("asd", 0.0, 0.0);
		for (int i = 0; i < vecinos.size(); i++) 
		{
			if(i != ciudadActual && !visitado.contains(_ciudades.get(i)))
			{
				if(vecinos.get(i) < menor)
				{
					
					menor = vecinos.get(i);
					asd=getCiudad(i);
				}
			}
		}
		return asd;
	}
		
	//pasada un array de ciudades este devuelve la suma de lsa distancias entre todas
	public Integer calcularDistancia(ArrayList<Ciudad> camino)
	{
		Integer distancia = 0;
		for (int i = 0; i < camino.size()-1; i++)//-1 es para evitar tradar de acceder a la proxima ciudad de la ultima(no existe)
		{
			distancia += _matrix.get(_ciudades.indexOf(camino.get(i))).get(_ciudades.indexOf(camino.get(i+1)));
		}
		
		return distancia;
	}
	
	//busqueda local con swaping para mejorar el algoritmo goloso
	@SuppressWarnings("unchecked")
	public void busquedaLocal()
	{
		ArrayList<Ciudad> camino=_soluciones.get(_soluciones.size()-1);
		Random random = new Random();
		ArrayList<Ciudad> mejorCaminoActual=(ArrayList<Ciudad>)camino.clone();
		ArrayList<Ciudad> caminoAMejorar = (ArrayList<Ciudad>)camino.clone();
		for (int i = 0; i < 50; i++) {
			int posicionA = random.nextInt(camino.size()-2)+1;
			int posicionB = random.nextInt(camino.size()-2)+1;
			if(posicionA!=posicionB)
			{
				Ciudad ciudadAux = caminoAMejorar.get(posicionA);
				caminoAMejorar.set(posicionA, caminoAMejorar.get(posicionB));
				caminoAMejorar.set(posicionB, ciudadAux);
			}
			
			if(calcularDistancia(camino)>calcularDistancia(caminoAMejorar))
			{
				camino = (ArrayList<Ciudad>) caminoAMejorar.clone();
			}
		}
		if(calcularDistancia(mejorCaminoActual)>calcularDistancia(caminoAMejorar))
		{
			_soluciones.add(caminoAMejorar);
		}
	}
	
	
	//retorna la lista de soluciones en donde mientras mas alto sea el indice en donde se aloja mejor la solucion
	@SuppressWarnings("unchecked")
	public ArrayList<ArrayList<Ciudad>> getSoluciones()
	{
		if(this._soluciones==null)throw new IllegalAccessError();
		ArrayList<ArrayList<Ciudad>> ret=(ArrayList<ArrayList<Ciudad>>)this._soluciones.clone();
		return ret;
	}
	
	//retorna un booleano si existe o no la ciudad
	public boolean existeCiudad(String e) {
		Ciudad aux=new Ciudad(e,null,null);
		return _ciudades.contains(aux);
	}
	
}