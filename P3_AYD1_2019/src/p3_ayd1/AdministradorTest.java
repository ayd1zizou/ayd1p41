package p3_ayd1;

import static org.junit.Assert.*;

import org.junit.Test;

public class AdministradorTest {

	
	
	@Test
	public void test_actualizar_solicitud() {
		Administrador admin = new Administrador();
		int res=admin.actualizar_solicitud("aprobado", 1, 1);
		int esperado =1;
		assertEquals(esperado, res);
	}
	
	@Test
	public void test_actualizar_credito() {
		Administrador admin = new Administrador();
		int res=admin.actualizar_credito("aprobado", "2019-04-07 01:40:20", 3);
		int esperado =1;
		assertEquals(esperado, res);
	}

}
