package p3_ayd1;

import static org.junit.Assert.*;

import org.junit.Test;

public class AdministradorTest {

	@Test
	public void test() {
		fail("Not yet implemented");
	}
	
	@Test
	public void test_actualizar_solicitud() {
		Administrador admin = new Administrador();
		int res=admin.actualizar_solicitud("aprobado", 11, 1);
		int esperado =1;
		assertEquals(esperado, res);
	}

}
