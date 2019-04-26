package p3_ayd1;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import dto.Credito_dto;
import dto.Cuenta_dto;
import dto.Solicitud_dto;
import dto.Usuario_dto;

@SuppressWarnings("serial")
public class Administrador extends Utilidad {
	
	public Label lbldcuenta, lbldnombre, lbldusuario, lbldcodigo;
	public Window wcuenta;
	public Button btnsalir, btnAprobar, btnReprobar,btnDebitar, btnEliminar,btnCargar;
	public Textbox txttocuenta, txttmonto;
	public Textbox txtpUsuario,txtpNombre, txtpEmail,txtpContrasenia;
	public Label lblpUsuario, lblpNombre, lblpEmail, lblpContrasenia,lblrperfil;
	public Textbox txtmacreditar, txtdacreditar;
	public Label lblemacreditar, lbledacreditar,lblrcredito;
	public Textbox txtmadebitar, txtdadebitar;
	public Label lblemadebitar, lbledadebitar, lblrdebito;
	public Tabpanel tPerfil,tTransferencia,tCredito,tDebito;
	public Label lbletmonto, lbletcuenta,lblrtransferencia,lblecre;
	Cuenta_dto dc;
	Usuario_dto du;
	
	
	private Listbox tablasolicitud,tabladebito,tablausuario;
	public List<Solicitud_dto>  listasolicitud = null;
	public List<Credito_dto>  listacredito = null;
	public List<Usuario_dto>  listausuario = null;
	public Label lbledebitocuenta,lbledmonto,lbledesDebito;
	public Textbox txtdmonto,txtdesDebito;
	public Listitem  liCredito;
	public Button btnEliminUsu;
	public Combobox cmbCre;
	
	Credito_dto cre=null;
	
	@SuppressWarnings("unchecked")
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);
		dc = (Cuenta_dto)Sessions.getCurrent().getAttribute("cuenta");
		 du = (Usuario_dto)Sessions.getCurrent().getAttribute("usuario");
		lbldcuenta.setValue("0");
		lbldcodigo.setValue(du.getCOD_USUARIO().toString());
		lbldnombre.setValue(du.getNOMBRE().toString());
		lbldusuario.setValue(du.getUSUARIO().toString());	
		
		
		txtpUsuario.setValue(du.getUSUARIO());
		txtpNombre.setValue(du.getNOMBRE());
		txtpEmail.setValue(du.getEMAIL());
		txtpContrasenia.setValue(du.getCONTRASENIA());
		
		
		cargar_lista_solicitud();
		cargar_lista_credito();
		cargar_lista_usuario();

	}
	
	public void onClick$btnsalir(){
		Sessions.getCurrent().setAttribute("cuenta",null);
		Sessions.getCurrent().setAttribute("usuario",null);
		Executions.sendRedirect("../P3_AYD1/login.zul");
		
	}
	
	private void cargar_lista_solicitud(){
		listasolicitud=null;
		getListasolicitud();
		AnnotateDataBinder  an =new AnnotateDataBinder(tablasolicitud);
		an.loadAll();		
	}
	
	public List<Solicitud_dto> getListasolicitud() {
		if(listasolicitud == null){			
			listasolicitud = obtieneListado_solicitud();						
		}	
		return listasolicitud;
	}
	

	public void setListasolicitud(List<Solicitud_dto> listasolicitud) {
		this.listasolicitud = listasolicitud;
	}
	
	private List<Solicitud_dto> obtieneListado_solicitud(){
		String sql="SELECT S.COD_SOLICITUD_CREDITO,S.MONTO,S.ESTADO, S.DESCRIPCION,S.FECHA_SOLICITUD,S.USUARIO_COD_USUARIO,S.ADMIN_COD_USUARIO1 "+
					" FROM solicitud_credito S";
		BeanListHandler<Solicitud_dto> auxiliar = new BeanListHandler<Solicitud_dto>(Solicitud_dto.class);			
		List<Solicitud_dto> auxiliarLista = new ArrayList<Solicitud_dto>();
		auxiliarLista  =  Utilidad.ejecutaConsultaList(sql, auxiliar);
		
		
		return auxiliarLista;		
	}
	
	public void onClick$btnAprobar(){
		System.out.println("ingresar a aprobar");
		if(listasolicitud!=null &&listasolicitud.size()!=0){
			Solicitud_dto sol = listasolicitud.get(tablasolicitud.getSelectedIndex());
			System.out.println("monto:"+sol.getMONTO().intValue());
			if(sol.getESTADO().equals("pendiente")){
				//actualiza tabala solicitud
				int res=actualizar_solicitud("aprobado", sol.getCOD_SOLICITUD_CREDITO().intValue(),
						du.getCOD_USUARIO().intValue());
				cargar_lista_solicitud();
				cargar_lista_credito();
				if(res==1){
					//si se realizo la actualizacion de solicitud
					//actualiza tabla de credito
					int res2=actualizar_credito("aprobado",sol.getFECHA_SOLICITUD(),sol.getUSUARIO_COD_USUARIO().intValue());
					if(res2==1){
						int res3=actualizar_cuenta(sol.getMONTO().intValue(), sol.getUSUARIO_COD_USUARIO().intValue());
						if(res3==1){
							Messagebox.show("solicitud actualizada exitosamente");							
						}else{
							Messagebox.show("No se ha podido actualizar la solicitud");
						}
					}else{
						Messagebox.show("No se ha podido actualizar la solicitud");
					}
					
				}else{
					Messagebox.show("No se ha podido actualizar la solicitud");
				}
			}else{
				Messagebox.show("Solo puede aprobar solicitudes pendientes");
			}
		}else{
			Messagebox.show("No se puede seleccionar porque no hay elementos");
		}
		
	}
	
	public void onClick$btnReprobar(){
		System.out.println("ingresar a reprobar");
		if(listasolicitud!=null &&listasolicitud.size()!=0){
			Solicitud_dto sol = listasolicitud.get(tablasolicitud.getSelectedIndex());
			System.out.println("monto:"+sol.getMONTO().intValue());
			if(sol.getESTADO().equals("pendiente")){
				int res =actualizar_solicitud("reprobado", sol.getCOD_SOLICITUD_CREDITO().intValue(),
						du.getCOD_USUARIO().intValue());
				cargar_lista_solicitud();
				cargar_lista_credito();
				if(res==1){
					//si se realizo la actualizacion de solicitud
					//actualiza tabla de credito
					int res2=actualizar_credito("reprobado",sol.getFECHA_SOLICITUD(),sol.getUSUARIO_COD_USUARIO().intValue());
					if(res2==1){
						Messagebox.show("solicitud actualizada exitosamente");
					}else{
						Messagebox.show("No se ha podido actualizar la solicitud");
					}
					
				}else{
					Messagebox.show("No se ha podido actualizar la solicitud");
				}
			}else{
				Messagebox.show("Solo puede reprobar solicitudes pendientes");
			}
		}else{
			Messagebox.show("No se puede seleccionar porque no hay elementos");
		}
	}
	
	private int actualizar_solicitud(String estado, int cod_sol, int cod_admin){
	
		int res=0;
		String sql ="UPDATE solicitud_credito "+
				" SET estado='"+estado+"' , " +
				" admin_cod_usuario1 ="+cod_admin+
				" WHERE cod_solicitud_credito ="+cod_sol;
		Object ret = Utilidad.ejecutaUpdate(sql);
		System.out.println("valor de consulta:"+ret);
		if(!ret.equals("")&&ret.toString().equals("1")){
			//Messagebox.show("solicitud actualizada exitosamente");
			res=1;
		}else{
			//Messagebox.show("No se ha podido actualiza la solicitud");
		}	
		return res;
	}
	
	private int actualizar_credito(String estado,String feresi,  int cod_usuario){
		
		int res=0;
		String sql ="UPDATE credito "+
				" SET estado= '"+estado+"' , "+
					" FECHA_RESIDUO=NOW() "+	
					" WHERE FECHA = '"+feresi+"' "+
					 	" and CUENTA_USUARIO_COD_USUARIO= "+cod_usuario;
		Object ret = Utilidad.ejecutaUpdate(sql);
		System.out.println("valor de consulta:"+ret);
		if(!ret.equals("")&&ret.toString().equals("1")){
			//Messagebox.show("solicitud actualizada exitosamente");
			res=1;
		}else{
			//Messagebox.show("No se ha podido actualizar la solicitud");
		}	
		return res;
	}
	
	
private int actualizar_cuenta(int monto, int cod_usuario){
		
		int res=0;
		String sql ="UPDATE CUENTA "+
			" SET SALDO=SALDO+"+monto+
				" WHERE USUARIO_COD_USUARIO= "+cod_usuario;
		Object ret = Utilidad.ejecutaUpdate(sql);
		System.out.println("valor de consulta:"+ret);
		if(!ret.equals("")&&ret.toString().equals("1")){
			//Messagebox.show("solicitud actualizada exitosamente");
			res=1;
		}else{
			//Messagebox.show("No se ha podido actualiza la solicitud");
		}	
		return res;
	}
	
	
	private void cargar_lista_credito(){
		listacredito=null;
		getListacredito();
		AnnotateDataBinder  an =new AnnotateDataBinder(tabladebito);
		an.loadAll();		
	}
	
	public List<Credito_dto> getListacredito() {
		if(listacredito == null){			
			listacredito = obtieneListado_credito();						
		}	
		return listacredito;
	}

	public void setListacredito(List<Credito_dto> listacredito) {
		this.listacredito = listacredito;
	}
	
	private List<Credito_dto> obtieneListado_credito(){
		String sql="SELECT S.NO_CREDITO,S.MONTO,S.DESCRIPCION,S.FECHA,S.RESIDUO, " +
				" S.FECHA_RESIDUO,S.ESTADO,S.CUENTA_NO_CUENTA,S.CUENTA_USUARIO_COD_USUARIO "+
					" FROM CREDITO S " +
					" WHERE S.ESTADO='aprobado'";
		BeanListHandler<Credito_dto> auxiliar = new BeanListHandler<Credito_dto>(Credito_dto.class);			
		List<Credito_dto> auxiliarLista = new ArrayList<Credito_dto>();
		auxiliarLista  =  Utilidad.ejecutaConsultaList(sql, auxiliar);
		return auxiliarLista;		
	}
	
	public void onClick$liCredito(){
		Credito_dto cre = listacredito.get(tabladebito.getSelectedIndex());
		System.out.println("se selecciono elemento:"+cre.getDESCRIPCION());
		txttocuenta.setValue(cre.getCUENTA_NO_CUENTA().toString());
		
	}
	
	public void onClick$btnDebitar(){
		limpiarErrDebito();
		cre=listacredito.get(tabladebito.getSelectedIndex());
		Comboitem cmi =cmbCre.getSelectedItem();
		
		if(cmi==null || cmi.equals("")){
			lblecre.setValue("Debe seleccionar una opcion");			
		}else{
			System.out.println("VALOR ITEM::::::::"+cmi.getValue());
		int res =verificarCamposDebitar(txttocuenta.getValue(), 
				txtdmonto.getValue(),txtdesDebito.getValue(), cmi.getValue().toString());
		
		if(res==0){
			if(cre!=null){
				//valida que el monto sea menor al saldo que posee la cuenta
				Cuenta_dto dacu = obtener_cuenta(cre.getCUENTA_NO_CUENTA().intValue());
				if(dacu!=null){
					int resmon =dacu.getSALDO().intValue()- Integer.valueOf(txtdmonto.getValue());
					if(resmon>0){
						int inser_deb =insertardebito(Integer.valueOf(txtdmonto.getValue()),txtdesDebito.getValue(), 
						null,Integer.valueOf(txttocuenta.getValue()), cre.getCUENTA_USUARIO_COD_USUARIO().intValue());
					if(inser_deb==1){
						try {
							int act_cuenta= actualizarcuentadebito(cre.getCUENTA_USUARIO_COD_USUARIO().intValue(),
									txtdmonto.getValue());
							if(act_cuenta==1){
								//actualiza el monto que se redujo en tabla credito
								if(cmi.getValue().equals("si")){
									int act_cre=actualizar_credito_residuo(cre.getFECHA(),
											cre.getCUENTA_USUARIO_COD_USUARIO().intValue(), 
											cre.getCUENTA_NO_CUENTA().intValue(),Integer.valueOf( txtdmonto.getValue()), "aprobado");
									if(act_cre==1){
										Messagebox.show("Se ha realizado el debito exitosamente");
										cargar_lista_credito();
										cargar_lista_solicitud();
										cargar_lista_usuario();
										limpiarCamposDebito();
										limpiarErrDebito();
									}
								}else{
									Messagebox.show("Se ha realizado el debito exitosamente");
									cargar_lista_credito();
									cargar_lista_solicitud();
									cargar_lista_usuario();
									limpiarCamposDebito();
									limpiarErrDebito();
								}
							}
							
						} catch (WrongValueException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
					}else{
						lbledmonto.setValue("El monto a debitar es mayor que el sado de la cuenta, Saldo:"+dacu.getSALDO().intValue());
					}
				}
			}else{
				Messagebox.show("Debe seleccionar un registro de la lista");				
			}
			
		}
	}
		
	}
	
	
	private void cargar_lista_usuario(){
		listausuario=null;
		getListausuario();
		AnnotateDataBinder  an =new AnnotateDataBinder(tablausuario);
		an.loadAll();		
	}
	
	
	private List<Usuario_dto> obtieneListado_usuario(int cod_usu){
		String sql=" SELECT COD_USUARIO, USUARIO, NOMBRE, EMAIL, CONTRASENIA, ROL "+
				" FROM USUARIO "+
				" WHERE COD_USUARIO != "+ cod_usu;
	
		BeanListHandler<Usuario_dto> auxiliar = new BeanListHandler<Usuario_dto>(Usuario_dto.class);			
		List<Usuario_dto> auxiliarLista = new ArrayList<Usuario_dto>();
		auxiliarLista  =  Utilidad.ejecutaConsultaList(sql, auxiliar);
		return auxiliarLista;		
	}
	
	private int verificarCamposDebitar(String cuenta, String monto, String des, String tdebito){
		int res=0;
		//se verifican que todos los campos esten llenos de info
		if(cuenta==null || cuenta.isEmpty()||cuenta.toString().trim().equals("")){			
			lbledebitocuenta.setValue("Debe ingresar informacion");
			res++;
		}
		if(monto==null || monto.isEmpty()||monto.toString().trim().equals("")){			
			lbledmonto.setValue("Debe ingresar informacion");
			res++;
		}
		if(des==null || des.isEmpty()||des.toString().trim().equals("")){			
			lbledesDebito.setValue("Debe ingresar informacion");
			res++;
		}
		
		
		if(!cuenta.matches("[0-9]+")){
			lbledebitocuenta.setValue("Debe ingresar solamente digitos");
			res++;
		}
		
		if(!monto.matches("[0-9]+")){
			lbledmonto.setValue("Debe ingresar solamente digitos");
			res++;
		}
		
		
		return res;
	}
	
	private void limpiarErrDebito(){
		lbledebitocuenta.setValue("");
		lbledmonto.setValue("");
		lbledesDebito.setValue("");
		lblecre.setValue("");
		lblrdebito.setValue("");
		
	}
	
	private void limpiarCamposDebito(){
		txttocuenta.setValue("");
		txtdmonto.setValue("");
		txtdesDebito.setValue("");
		cmbCre.setValue("");
		lblrdebito.setValue("");
		
	}
	


	
	@SuppressWarnings("unused")
	private int insertardebito(int monto, String descrip,
			Integer cta_rece,int no_cuenta,int cod_usuario ) {
		int res=0;
		
		String sql ="INSERT INTO DEBITO(MONTO, DESCRIPCION,CUENTA_RECEPTORA, "+
					" FECHA,CUENTA_NO_CUENTA,CUENTA_USUARIO_COD_USUARIO) "+
						" VALUES("+monto+",' "+descrip+"',"+cta_rece+",NOW(),"+
					no_cuenta+","+cod_usuario+")";
		
		Object ret = Utilidad.ejecutaUpdate(sql);
		System.out.println("valor de consulta:"+ret);
		if(!ret.equals("")&&ret.toString().equals("1")){
			res=1;			
		}	
		return res;
		
	}
	
	private int actualizarcuentadebito(int cod_usu, String monto) throws SQLException{
		int res=0;
		String sql =" UPDATE CUENTA "+
				" SET SALDO=SALDO-"+monto+ 
				" WHERE USUARIO_COD_USUARIO="+cod_usu;
		Object ret = Utilidad.ejecutaUpdate(sql);
		System.out.println("valor de consulta:"+ret);
		if(!ret.equals("")&&ret.toString().equals("1")){
			res=1;			
		}	
		return res;
		
	}
	
	
	
private int actualizar_credito_residuo(String feresi,  int cod_usuario,int cod_cuenta, 
		int residuo, String estado){
		
		int res=0;
		String sql ="UPDATE credito "+
				" SET RESIDUO= RESIDUO - "+residuo+" , "+
					" FECHA_RESIDUO=NOW() "+	
					" WHERE FECHA = '"+feresi+"' "+
					 	" and CUENTA_USUARIO_COD_USUARIO= "+cod_usuario+
					 	" AND CUENTA_NO_CUENTA= "+cod_cuenta+
					 	" AND ESTADO= '"+estado+"'";
		Object ret = Utilidad.ejecutaUpdate(sql);
		System.out.println("valor de consulta:"+ret);
		if(!ret.equals("")&&ret.toString().equals("1")){
			//Messagebox.show("solicitud actualizada exitosamente");
			res=1;
		}else{
			//Messagebox.show("No se ha podido actualizar la solicitud");
		}	
		return res;
	}
	
	
	public void onClick$btnEliminUsu(){
		if(listausuario!=null &&listausuario.size()!=0){
		 Usuario_dto datusu = listausuario.get(tablausuario.getSelectedIndex());
		 	try {
				eliminar_solicitud(datusu.getCOD_USUARIO().intValue());
				eliminar_debito(datusu.getCOD_USUARIO().intValue());
				eliminar_credito(datusu.getCOD_USUARIO().intValue());
				eliminar_cuenta(datusu.getCOD_USUARIO().intValue());
				eliminar_usuario(datusu.getCOD_USUARIO().intValue());
				cargar_lista_credito();
				cargar_lista_solicitud();
				cargar_lista_usuario();
				Messagebox.show("Se ha eliminado el usuario correctamente de todo el si stema");
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			Messagebox.show("No se puede seleccionar porque no hay elementos");
		}
		
	}
	
	private int eliminar_solicitud(int cod_usu) throws SQLException{
		int res=0;
		String sql ="DELETE FROM SOLICITUD_CREDITO "+
					" WHERE USUARIO_COD_USUARIO= "+cod_usu;
		Object ret = Utilidad.ejecutaUpdate(sql);
		System.out.println("valor de consulta:"+ret);
		if(!ret.equals("")&&ret.toString().equals("1")){
			res=1;			
		}	
		return res;
		
	}
	
	private int eliminar_debito(int cod_usu) throws SQLException{
		int res=0;
		String sql ="DELETE FROM DEBITO "+
					" WHERE CUENTA_USUARIO_COD_USUARIO = "+cod_usu;
		Object ret = Utilidad.ejecutaUpdate(sql);
		System.out.println("valor de consulta:"+ret);
		if(!ret.equals("")&&ret.toString().equals("1")){
			res=1;			
		}	
		return res;
		
	}
	
	
	private int eliminar_credito(int cod_usu) throws SQLException{
		int res=0;
		String sql ="DELETE FROM CREDITO "+
					" WHERE CUENTA_USUARIO_COD_USUARIO= "+cod_usu;
		Object ret = Utilidad.ejecutaUpdate(sql);
		System.out.println("valor de consulta:"+ret);
		if(!ret.equals("")&&ret.toString().equals("1")){
			res=1;			
		}	
		return res;
		
	}
	
	private int eliminar_cuenta(int cod_usu) throws SQLException{
		int res=0;
		String sql ="DELETE FROM CUENTA "+
					" WHERE USUARIO_COD_USUARIO= "+cod_usu;
		Object ret = Utilidad.ejecutaUpdate(sql);
		System.out.println("valor de consulta:"+ret);
		if(!ret.equals("")&&ret.toString().equals("1")){
			res=1;			
		}	
		return res;
		
	}

	
	private int eliminar_usuario(int cod_usu) throws SQLException{
		int res=0;
		String sql ="DELETE FROM USUARIO "+
					" WHERE COD_USUARIO= "+cod_usu;
		Object ret = Utilidad.ejecutaUpdate(sql);
		System.out.println("valor de consulta:"+ret);
		if(!ret.equals("")&&ret.toString().equals("1")){
			res=1;			
		}	
		return res;
		
	}

















	
	
	
	
	public void onClick$btnPerfil() throws Exception{
		limpiarPerfil();
		int res =verificarCamposPerfil(txtpNombre.getValue(), txtpUsuario.getValue(),
				txtpContrasenia.getValue(),txtpEmail.getValue());
		if(res==0){
			
				int consul =actualizarperfil(txtpUsuario.getValue(), txtpNombre.getValue(),
						txtpEmail.getValue(),txtpContrasenia.getValue(),du.getCOD_USUARIO().intValue());
				if(consul==0){
					lblrperfil.setValue("Perfil actualizado exitosamente");
					obtieneusuario(du.getUSUARIO());
					doAfterCompose(tPerfil);
					doAfterCompose(wcuenta);
				}else{
					lblrperfil.setValue("No se ha podido actualizar el perfil");
				}
			
			
		}
		
		
	}
	
	
	@SuppressWarnings("unchecked")
	private void obtieneusuario(String usu) throws SQLException{		
		String sql ="SELECT COD_USUARIO, USUARIO, NOMBRE, EMAIL, CONTRASENIA, ROL "+
						"FROM USUARIO U "+
						" WHERE U.USUARIO='"+usu+"' ";
		BeanListHandler<Usuario_dto> auxiliarBean = new BeanListHandler<Usuario_dto>(Usuario_dto.class);
		List<Usuario_dto> auxiliarLista = new ArrayList<Usuario_dto>();
		auxiliarLista  =  Utilidad.ejecutaConsultaList(sql, auxiliarBean);				
		System.out.println("tamaño lista: "+auxiliarLista.get(0).getNOMBRE());
		Sessions.getCurrent().setAttribute("usuario", auxiliarLista.get(0));
		
		
	}
	
	private void limpiarPerfil(){
		lblrperfil.setValue("");
		lblpContrasenia.setValue("");
		lblpEmail.setValue("");
		lblpNombre.setValue("");
		lblpUsuario.setValue("");
		
	}
	
	
	private int actualizarperfil(String usu, String nombre, String email,
			String pass, int cod_usu) throws SQLException{
		int res=0;
		String sql =" UPDATE USUARIO "+
				"SET USUARIO='"+usu+"' , "+ 
				"NOMBRE ='"+nombre+"', "+
				"EMAIL='"+email+"', "+
				"CONTRASENIA='"+pass+"' "+
				" WHERE COD_USUARIO="+cod_usu;
		Object ret = Utilidad.ejecutaUpdate(sql);
		System.out.println("valor de consulta:"+ret);
		if(ret.equals("")||!ret.toString().equals("1")){
			res=1;			
		}	
		return res;
		
	}
		
	
	private int verificarCamposPerfil(String nombre, String usu, String pass, String email){
		int res=0;
		//se verifican que todos los campos esten llenos de info
		if(nombre==null || nombre.isEmpty()||nombre.toString().trim().equals("")){			
			lblpNombre.setValue("Debe ingresar informacion");
			res++;
		}
		if(usu==null || usu.isEmpty()||usu.toString().trim().equals("")){			
			lblpUsuario.setValue("Debe ingresar informacion");
			res++;
		}
		if(pass==null || pass.isEmpty()||pass.toString().trim().equals("")){			
			lblpContrasenia.setValue("Debe ingresar informacion");
			res++;
		}
		
		if(email==null || email.isEmpty()||email.toString().trim().equals("")){			
			lblpEmail.setValue("Debe ingresar informacion");
			res++;
		}
		
		
		return res;
	}

	
	
	private Cuenta_dto obtener_cuenta(int cod_cuenta){
		Cuenta_dto cue = null;
		String sql=" SELECT NO_CUENTA, SALDO, USUARIO_COD_USUARIO "+
					" FROM CUENTA "+
					" WHERE NO_CUENTA = "+cod_cuenta;
		BeanListHandler<Cuenta_dto> auxiliar = new BeanListHandler<Cuenta_dto>(Cuenta_dto.class);			
		List<Cuenta_dto> auxiliarLista = new ArrayList<Cuenta_dto>();
		auxiliarLista  =  Utilidad.ejecutaConsultaList(sql, auxiliar);
		if(auxiliarLista!=null && auxiliarLista.size()>0){
			cue= auxiliarLista.get(0);
		}
		return cue;
		
	}
	
	
	
	
	public List<Usuario_dto> getListausuario() {
		if(listausuario == null){			
			listausuario = obtieneListado_usuario(du.getCOD_USUARIO().intValue());						
		}
		return listausuario;
	}

	public void setListausuario(List<Usuario_dto> listausuario) {
		this.listausuario = listausuario;
	}

	

	
	
	

}
