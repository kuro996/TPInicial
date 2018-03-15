package Grafo;

import static org.junit.Assert.*;

import java.awt.Point;

import org.junit.Before;
import org.junit.Test;

public class CiudadTest {
	Ciudad uno;
	Ciudad dos;
	Ciudad tres;
	Ciudad cuatro;
	Ciudad cinco;
	Ciudad seis;
	
	@Before
	public void GenerarCiudades()
	{
		uno=new Ciudad("uno",1.0,1.0);
		dos=new Ciudad("dos",2.0,2.0);
		tres=new Ciudad("tres",3.0,3.0);
		cuatro=new Ciudad("cuatro",4.0,4.0);
		cinco=new Ciudad("cinco",5.0,5.0);
		seis=new Ciudad("seis",6.0,6.0);
	}

	@Test
	public void testToString() 
	{
		assertEquals("uno", uno.toString());
		assertEquals("dos", dos.toString());
		assertEquals("tres", tres.toString());
		assertEquals("cuatro", cuatro.toString());
		assertEquals("cinco", cinco.toString());
		assertEquals("seis", seis.toString());
	}

	@Test
	public void testGetPoint() 
	{
		Point Puno=new Point(1, 1);
		Point Pdos=new Point(2, 2);
		Point Ptres=new Point(3, 3);
		Point Pcuatro=new Point(4, 4);
		Point Pcinco=new Point(5, 5);
		Point Pseis=new Point(6, 6);
		
		assertEquals(Puno, new Point(((int)uno.getX()),((int)uno.getY())));
		assertEquals(Pdos, new Point(((int)dos.getX()),((int)dos.getY())));
		assertEquals(Ptres, new Point(((int)tres.getX()),((int)tres.getY())));
		assertEquals(Pcuatro, new Point(((int)cuatro.getX()),((int)cuatro.getY())));
		assertEquals(Pcinco, new Point(((int)cinco.getX()),((int)cinco.getY())));
		assertEquals(Pseis, new Point(((int)seis.getX()),((int)seis.getY())));
	}
	
	
}
