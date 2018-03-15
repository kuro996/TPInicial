package Grafo;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class GrafoTest {
	
	Grafo g;

	@Before
	public void Generador() 
	{
		g = new Grafo();
		g.agregarCiudad("uno", 1.0, 1.0);
		g.agregarCiudad("dos", 2.0, 2.0);
	}

	@Test
	public void agregarCiudadTest() 
	{
		int a=g.cantCiudades();
		g.agregarCiudad("tres", 2.0, 3.0);
		assertEquals(a+1, g.cantCiudades());
	}
	
	@Test
	public void eliminarCiudadTest()
	{
		g.eliminarCiudad("uno");
		assertFalse(g.existeCiudad("uno"));
	}
	
	@Test
	public void getCiudadTest() 
	{
		assertEquals(g.getCiudad(0), new Ciudad("uno",1.0,1.5));
	}
	
	@Test
	public void test() 
	{
		assertEquals(g.vecinos("uno").size(), g.cantCiudades() - 1);
	}
	
	@Test
	public void solucionGolosaTest()
	{
		g.agregarCiudad("tres", 3.0, 3.0);
		g.editarDistancia("uno", "dos", 2);
		g.editarDistancia("dos", "tres", 4);
		g.editarDistancia("uno", "tres", 3);
		
		ArrayList<Ciudad> esperado=new ArrayList<Ciudad>();
		esperado.add(g.getCiudad(0));
		esperado.add(g.getCiudad(1));
		esperado.add(g.getCiudad(2));
		esperado.add(g.getCiudad(0));
		
		assertEquals(esperado, g.solucionGololsa("uno"));
	}
	
	
	@Test
	public void busquedaLocalTest() 
	{
		g.agregarCiudad("tres", 3.0, 3.0);
		g.editarDistancia("uno", "dos", 2);
		g.editarDistancia("dos", "tres", 4);
		g.editarDistancia("uno", "tres", 3);
		
		g.solucionGololsa("uno");
		for(int i=0;i<10;i++)
		{
			ArrayList<ArrayList<Ciudad>> aux=g.getSoluciones();
			int algo=aux.size();
			g.busquedaLocal();
			if(algo>1)assertTrue(g.calcularDistancia(aux.get(algo-1))<g.calcularDistancia(aux.get(algo-2)));
		}
	}
	
	@Test
	public void existeCiudadTest()
	{
		assertTrue(g.existeCiudad("uno"));
		assertFalse(g.existeCiudad("no existe"));
	}
	

	@Test(expected=IllegalAccessError.class)
	public void getSolucionVacios()
	{
		g.getSoluciones();
	}

}
