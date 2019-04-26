package p3_ayd1;

import java.sql.SQLException;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class Registro extends Utilidad{
	
	public Textbox txtnombre,txtusuario, txtemail, txtcontrasenia;
	public Button btningresar;
	public Label lblnombre, lblusuario, lblemail, lblcontrasenia,lblresultado;
	public Label lblerror;
	public Window wregistro;
	private Integer cod_usuario;
	
	@SuppressWarnings("unchecked")
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);
		limpia_msj();
		
	}
	
	private void limpia_msj(){
		lblnombre.setValue("");
		lblusuario.setValue("");
		lblemail.setValue("");
		lblcontrasenia.setValue("");
		lblresultado.setValue("");
		
	}
	
	public void onClick$btningresar() throws WrongValueException, SQLException{
		System.out.println("ingresa a registro");
	
		limpia_msj();
		int res = verificarCampos(txtnombre.getValue(), txtusuario.getValue(), 
				txtcontrasenia.getValue(), txtemail.getValue());
		if(res==0){
			int consulta =verificaUsuarioExistente(txtusuario.getValue());
			if(consulta==0){
				int agrega =insertarusuario(txtusuario.getValue(), txtnombre.getValue(),
						txtemail.getValue(), txtcontrasenia.getValue());
				if(agrega==0){
					String cod =obtenercodigousuario(txtusuario.getValue());
					insertarcuenta(cod_usuario);
					String mensaje ="El usuario fue agregado exitosamente.";
					mensaje+="El codigo de acceso es: "+cod +" ";
					mensaje+="Presione Salir";
					lblresultado.setValue(mensaje);
				}
				
			}else
			if(consulta==-1){
				lblerror.setValue("Error de conexion a la BD");
			}
			else{
				lblusuario.setValue("El usuario ingresado ya existe");
			}
						
		}
		
	}
	
	private String obtenercodigousuario(String usu) throws SQLException{
		String res="";
		String sql ="SELECT COD_USUARIO  "+
						"FROM USUARIO U "+
						" WHERE U.USUARIO='"+usu+"'";
		Object ret = Utilidad.ejecutaConsulta(sql);
		System.out.println("valor de consulta:"+ret);	
		cod_usuario = new Integer(String.valueOf(ret.toString()));
		return ret.toString();
		
	}
	
	private int insertarcuenta(Integer cod) throws SQLException{
		int res=0;
		String sql ="INSERT INTO CUENTA(SALDO,USUARIO_COD_USUARIO) "+
					"VALUES(0,"+cod.intValue()+")";
		Object ret = Utilidad.ejecutaUpdate(sql);
		System.out.println("valor de consulta:"+ret);
		if(ret.equals("")||!ret.toString().equals("1")){
			res=1;			
		}	
		return res;
		
	}
	
	
	private int insertarusuario(String usu, String nombre, String email,
			String pass) throws SQLException{
		int res=0;
		String sql ="INSERT INTO USUARIO(USUARIO, NOMBRE, EMAIL,CONTRASENIA,ROL) "+
					"VALUES('"+usu+"','"+nombre+"', '"+email+"','"+pass+"',1)";
		Object ret = Utilidad.ejecutaUpdate(sql);
		System.out.println("valor de consulta:"+ret);
		if(ret.equals("")||!ret.toString().equals("1")){
			res=1;			
		}	
		return res;
		
	}
	
	
	
	private int verificaUsuarioExistente(String usu) throws SQLException{
		int res=0;
		String sql ="SELECT COUNT( COD_USUARIO ) "+
						"FROM USUARIO U "+
						" WHERE U.USUARIO='"+usu+"'";
		Object ret = Utilidad.ejecutaConsulta(sql);
		System.out.println("valor de consulta:"+ret);
		if(ret==null){
			res=-1;
		}else
		if(!ret.equals("")&&!ret.toString().equals("0")){
			res=1;			
		}	
		return res;
		
	}
	
	private int verificarCampos(String nombre, String usu, String pass, String email){
		int res=0;
		//se verifican que todos los campos esten llenos de info
		if(nombre==null || nombre.isEmpty()||nombre.toString().trim().equals("")){			
			lblnombre.setValue("Debe ingresar informacion");
			res++;
		}
		if(usu==null || usu.isEmpty()||usu.toString().trim().equals("")){			
			lblusuario.setValue("Debe ingresar informacion");
			res++;
		}else
		if(!usu.matches("[a-zA-Z0-9]+")){
			lblusuario.setValue("Debe ingresar unicamente letras y digitos");
			res++;			
		}
		
		
		if(pass==null || pass.isEmpty()||pass.toString().trim().equals("")){			
			lblcontrasenia.setValue("Debe ingresar informacion");
			res++;
		}
		
		if(email==null || email.isEmpty()||email.toString().trim().equals("")){			
			lblemail.setValue("Debe ingresar informacion");
			res++;
		}
		
		
		return res;
	}

}
