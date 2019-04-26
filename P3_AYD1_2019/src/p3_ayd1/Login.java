package p3_ayd1;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;

import dto.Cuenta_dto;
import dto.Usuario_dto;;


@SuppressWarnings({ "serial" })
public class Login extends Utilidad{
	public Button btningresar;
	public Textbox txtusuario, txtcontrasenia, txtcodigo;
	public Label lblusuario, lblcontrasenia, lblcodigo, lblError;
	public Window wlogin;
	
	@SuppressWarnings("unchecked")
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);
		limpia_msj();

	}
	
	private void limpia_msj(){
		lblcodigo.setValue("");
		lblcontrasenia.setValue("");
		lblusuario.setValue("");
		lblError.setValue("");		
		
	}
	
	
	public void onClick$btningresar() throws WrongValueException, SQLException{
		limpia_msj();
		System.out.println("ingresa a metodo de boton");
		System.out.println("codigo:"+txtcodigo.getValue());
		System.out.println("usuario:"+txtusuario.getValue());
		System.out.println("contraseña:"+txtcontrasenia.getValue());
		int res=verificarCampos(txtcodigo.getValue(), txtusuario.getValue(),txtcontrasenia.getValue());
		
		if(res==0){
			//btningresar.setHref("../P3_AYD1/cuenta.zul");
			int consulta =verificaUsuario(txtcodigo.getValue(), txtusuario.getValue(),
					txtcontrasenia.getValue());
			if(consulta==1){
				
				Usuario_dto usu =obtieneusuario(txtusuario.getValue());
				if(usu.getROL().intValue()==1){
					Executions.sendRedirect("../P3_AYD1/cuenta.zul");
				}else{
					Sessions.getCurrent().setAttribute("cuenta", null);
					Executions.sendRedirect("../P3_AYD1/administrador.zul");					
				}
			}else
			if(consulta==-1){
				lblError.setValue("Error al realizar conexion a BD");
			}
			else{
				lblcontrasenia.setValue("Los valores ingresados son incorrectos");				
			}
		}
		
		//consulta_ejemplo();
		
	}
	
	
	private void obtienecuenta(Integer cod) throws SQLException{		
		String sql ="SELECT NO_CUENTA, SALDO, USUARIO_COD_USUARIO "+
						"FROM CUENTA "+
						" WHERE USUARIO_COD_USUARIO="+cod.intValue();
		BeanListHandler<Cuenta_dto> auxiliarBean = new BeanListHandler<Cuenta_dto>(Cuenta_dto.class);
		List<Cuenta_dto> auxiliarLista = new ArrayList<Cuenta_dto>();
		auxiliarLista  =  Utilidad.ejecutaConsultaList(sql, auxiliarBean);				
		System.out.println("tamaño lista: "+auxiliarLista.size());
		if(auxiliarLista.size()>0){
			Sessions.getCurrent().setAttribute("cuenta", auxiliarLista.get(0));
		}else{
			Sessions.getCurrent().setAttribute("cuenta", null);			
		}
	}
	
	@SuppressWarnings({ "unchecked", "unused" })
	private Usuario_dto obtieneusuario(String usu) throws SQLException{		
		String sql ="SELECT COD_USUARIO, USUARIO, NOMBRE, EMAIL, CONTRASENIA, ROL "+
						"FROM USUARIO U "+
						" WHERE U.USUARIO='"+usu+"' ";
		BeanListHandler<Usuario_dto> auxiliarBean = new BeanListHandler<Usuario_dto>(Usuario_dto.class);
		List<Usuario_dto> auxiliarLista = new ArrayList<Usuario_dto>();		
		auxiliarLista  =  Utilidad.ejecutaConsultaList(sql, auxiliarBean);
		Usuario_dto dat_usu = auxiliarLista.get(0);
		System.out.println("tamaño lista: "+auxiliarLista.get(0).getNOMBRE());
		//manda sesion de usuario
		Sessions.getCurrent().setAttribute("usuario", auxiliarLista.get(0));
		//manda sesion de cuenta de usuario
		obtienecuenta(auxiliarLista.get(0).getCOD_USUARIO());
		return dat_usu;
	}
	
	
	
	private int verificaUsuario(String cod, String usu, String pass) throws SQLException{
		int res=0;
		String sql ="SELECT COUNT( COD_USUARIO ) "+
						"FROM USUARIO U "+
						" WHERE U.USUARIO='"+usu+"' "+
						" AND U.CONTRASENIA='"+pass+"' "+
						" AND U.COD_USUARIO="+cod;
		Object ret = Utilidad.ejecutaConsulta(sql);
		System.out.println("valor de consulta:"+ret);
		if(ret==null||ret.equals("")){
			res=-1;
		}else
		if(ret.toString().equals("0")){
			res=0;			
		}else{
			res=1;
		}
		return res;
		
	}
	
	private int verificarCampos(String cod, String usu, String pass){
		int res=0;
		//se verifican que todos los campos esten llenos de info
		if(cod==null || cod.isEmpty()||cod.toString().trim().equals("")){			
			lblcodigo.setValue("Debe ingresar informacion");
			res++;
		}
		
		if(usu==null || usu.isEmpty()||usu.toString().trim().equals("")){			
			lblusuario.setValue("Debe ingresar informacion");
			res++;
			
		}else
		if(!usu.matches("[a-zA-Z0-9]+")){
			lblusuario.setValue("Solo puede ingresar numeros y letras");
			res++;
		}
		
		if(pass==null || pass.isEmpty()||pass.toString().trim().equals("")){			
			lblcontrasenia.setValue("Debe ingresar informacion");
			res++;
		}
		if(!cod.matches("[0-9]+")){			
			lblcodigo.setValue("Solo puede ingresar numeros");
			res++;
		}
		
		if(res!=0){
			return res;
		}
		
		return res;
	}
	
	
	private void consulta_ejemplo(){

		String query ="select now() fecha";		
		Object fecha = ejecutaConsulta( query);
		if(fecha!=null){
			System.out.println("FECHA:: "+fecha);
		}else{
			System.out.println("NO SE PUEDE OBTENER FECHA DEL SISTEMA:: ");
		}
	}
	
	
	
	
	
}
