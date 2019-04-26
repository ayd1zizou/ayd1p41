package p3_ayd1;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.zkoss.util.media.AMedia;
import org.zkoss.util.media.Media;
import org.zkoss.zhtml.Filedownload;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zss.model.impl.ExcelExporter;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;


import org.apache.poi.ss.usermodel.WorkbookFactory;


import dto.Credito_dto;
import dto.Cuenta_dto;
import dto.Debito_dto;

import dto.Usuario_dto;

@SuppressWarnings("serial")
public class Cuenta extends Utilidad {
	
	public Label lbldcuenta, lbldnombre, lbldusuario, lbldcodigo,lblmonto;
	public Window wcuenta;
	public Button btnsalir;
	public Textbox txttocuenta, txttmonto;
	public Textbox txtpUsuario,txtpNombre, txtpEmail,txtpContrasenia;
	public Label lblpUsuario, lblpNombre, lblpEmail, lblpContrasenia,lblrperfil;
	public Textbox txtmacreditar, txtdacreditar;
	public Label lblemacreditar, lbledacreditar,lblrcredito;
	public Textbox txtmadebitar, txtdadebitar;
	public Label lblemadebitar, lbledadebitar, lblrdebito;
	public Tabpanel tPerfil,tTransferencia,tCredito,tDebito;
	public Label lbletmonto, lbletcuenta,lblrtransferencia;
	Cuenta_dto dc;
	Usuario_dto du;
	public Listbox tablahistorial;
	
	
	public List<Debito_dto>  listaTransferencia = null;
	
	

	@SuppressWarnings("unchecked")
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);
		dc = (Cuenta_dto)Sessions.getCurrent().getAttribute("cuenta");
		 du = (Usuario_dto)Sessions.getCurrent().getAttribute("usuario");
		lbldcuenta.setValue(""+dc.getNO_CUENTA().intValue());
		lbldcodigo.setValue(du.getCOD_USUARIO().toString());
		lbldnombre.setValue(du.getNOMBRE().toString());
		lbldusuario.setValue(du.getUSUARIO().toString());	
		lblmonto.setValue(dc.getSALDO().toString());
		
		txtpUsuario.setValue(du.getUSUARIO());
		txtpNombre.setValue(du.getNOMBRE());
		txtpEmail.setValue(du.getEMAIL());
		txtpContrasenia.setValue(du.getCONTRASENIA());
		
		lbledacreditar.setValue("");
		lblemacreditar.setValue("");
		
		lbledadebitar.setValue("");
		lblemadebitar.setValue("");
		
		cargar_lista();

	}
	
	public List<Debito_dto> getListaTransferencia() {
		if(listaTransferencia == null){
			String codcuenta = dc.getNO_CUENTA().toString();			
			listaTransferencia = obtieneListado(codcuenta);						
		}	
		
		return listaTransferencia;
	}

	public void setListaTransferencia(List<Debito_dto> listaTransferencia) {
		this.listaTransferencia = listaTransferencia;
	}
	
	private void cargar_lista(){
		listaTransferencia=null;
		getListaTransferencia();
		AnnotateDataBinder  an =new AnnotateDataBinder(tablahistorial);
		an.loadAll();
		
	}
	
	private List<Debito_dto> obtieneListado(String codcuenta){
		String sql="SELECT NO_DEBITO, MONTO, DESCRIPCION,CUENTA_RECEPTORA,USUARIO.USUARIO USUARIO, FECHA, "+
					" CUENTA_NO_CUENTA,CUENTA_USUARIO_COD_USUARIO "+
					" FROM DEBITO ,USUARIO	 "+
					" WHERE " +
					" CUENTA_USUARIO_COD_USUARIO=COD_USUARIO "+
					" AND  CUENTA_NO_CUENTA= "+codcuenta;
		BeanListHandler<Debito_dto> auxiliar = new BeanListHandler<Debito_dto>(Debito_dto.class);			
		List<Debito_dto> auxiliarLista = new ArrayList<Debito_dto>();
		auxiliarLista  =  Utilidad.ejecutaConsultaList(sql, auxiliar);
		return auxiliarLista;
		
	}
	
	
	
	public void onClick$btnsalir(){
		Executions.sendRedirect("../P3_AYD1_2019/login.zul");
		
	}
	
	
	@SuppressWarnings("unchecked")
	private void obtienecuenta(String cod_usu) throws SQLException{
		String sql =" SELECT NO_CUENTA, SALDO, USUARIO_COD_USUARIO,USUARIO "+
					" FROM CUENTA, USUARIO "+
					" WHERE  "+ 
					" USUARIO_COD_USUARIO = COD_USUARIO "+
					" AND USUARIO_COD_USUARIO= "+cod_usu;
			BeanListHandler<Cuenta_dto> auxiliarBean = new BeanListHandler<Cuenta_dto>(Cuenta_dto.class);
			List<Cuenta_dto> auxiliarLista = new ArrayList<Cuenta_dto>();
			auxiliarLista  =  Utilidad.ejecutaConsultaList(sql, auxiliarBean);				
			System.out.println("tamaño lista: "+auxiliarLista.get(0).getSALDO());
			Sessions.getCurrent().setAttribute("cuenta", auxiliarLista.get(0));
			lblmonto.setValue(auxiliarLista.get(0).getSALDO().toString());
		
	}

	
	private void limpiarTransferencia(){
		lbletmonto.setValue("");
		lbletcuenta.setValue("");
		lblrtransferencia.setValue("");
	}
	
	
	private void limpiarCredito(){
		lbledacreditar.setValue("");
		lblemacreditar.setValue("");
		lblrcredito.setValue("");
		
	}
	
	private void limpiardebito(){
		lbledadebitar.setValue("");
		lblemadebitar.setValue("");
		lblrdebito.setValue("");
		
	}
	
	public void onClick$btntransferir() throws Exception{
		limpiarTransferencia();
		int res = verificarCamposTransferencia(txttocuenta.getValue(), txttmonto.getValue());
		if(res==0){
			int monto_valido = dc.getSALDO().intValue()-Integer.parseInt(txttmonto.getValue());
			if(monto_valido>=0){
			
			/*
			int exis =verificaCuenta(txttocuenta.getValue());
			if(exis==0){
			*/
				//verifica que la encuenta a la que se envia el dinero pertenezca al sistema
			int exis =verificaCuenta(txttocuenta.getValue());				
				
			int actualiza =actualizarcuentadebito(du.getCOD_USUARIO().intValue(),txttmonto.getValue());
			if(actualiza==1){								
				obtienecuenta(du.getCOD_USUARIO().toString());
				
				if(exis==1){
					int inserta = actualizarcuentatransferencia(Integer.valueOf(txttocuenta.getValue()),
						Integer.valueOf(txttmonto.getValue()));	
					//guarda el registro en debido de la cuenta
					int dusu = obtener_usuario_cuenta(txttocuenta.getValue());
					int rde=insertardebito(Integer.valueOf(txttmonto.getValue()), 
							"recepcion transferencia bancaria",null,
							Integer.valueOf(txttocuenta.getValue()), dusu);
				}
				
				if(actualiza==1){
					//guarda debito por transferencia
					int rde=insertardebito(Integer.valueOf(txttmonto.getValue()), 
							"envio transferencia bancaria",Integer.valueOf(txttocuenta.getValue()),
							dc.getNO_CUENTA().intValue(), du.getCOD_USUARIO().intValue());
					
					if(rde==1){
						limpiarTransferencia();
						txttmonto.setValue("");
						txttocuenta.setValue("");
						cargar_lista();
						lblrtransferencia.setValue("Transferencia realizada exitosamente");
					}else{
						lblrtransferencia.setValue("No se ha podido registrar el debito");						
					}
				}						
			}else{
				lblrtransferencia.setValue("No se ha podido realizar la transferencia");
			}
					/*
				}else{
					lblrcredito.setValue("No se ha podido realizar la transferencia");				
				}
				
			}else{
				lblrtransferencia.setValue("La cuenta a transferir no existe");
				
			}*/
			}else{
				lbletmonto.setValue("El monto ingresado excede el saldo de su cuenta");
				
			}
		}	
		
	}
	
	
	public void onClick$btnacreditar() throws Exception{
		limpiarCredito();
		int res = verificarCamposCredito(txtmacreditar.getValue(), txtdacreditar.getValue());
		if(res==0){
			int inserta = insertarcredito(txtmacreditar.getValue(), txtdacreditar.getValue());
			if(inserta==0){
				int inser_sol =insertar_solicitud(Integer.parseInt( txtmacreditar.getValue()), txtdacreditar.getValue(),du.getCOD_USUARIO().intValue());
				if(inser_sol==0){
					txtmacreditar.setValue("");
					txtdacreditar.setValue("");	
					limpiarCredito();
					lblrcredito.setValue("Solicitud enviada exitosamente");
				}else{					
					lblrcredito.setValue("No se ha podido enviar la solicitud de credito");
				}
				/*
				int actualiza =actualizarcuentacredito(du.getCOD_USUARIO().intValue(),txtmacreditar.getValue());
				if(actualiza==0){
						
					
					txtmacreditar.setValue("");
					txtdacreditar.setValue("");					
					obtienecuenta(du.getCOD_USUARIO().toString());
					
					
					limpiarCredito();
					lblrcredito.setValue("Credito realizado exitosamente");
					
				}else{
					lblrcredito.setValue("No se ha podido actualizar la cuenta");
				}
				
				*/
			}else{
				lblrcredito.setValue("No se ha solicitar el credito");				
			}
		}	
		
	}
	
	
	public void onClick$btnadebitar() throws Exception{
		limpiardebito();
		int res = verificarCamposDebito(txtmadebitar.getValue(), txtdadebitar.getValue());
		if(res==0){
			int viejodeb =dc.getSALDO().intValue();
			int nuevo =Integer.valueOf(txtmadebitar.getValue());
			if(nuevo<=viejodeb){
			/*
				int inserta = insertardebito(txtmadebitar.getValue(), txtdadebitar.getValue());
				if(inserta==0){
					int actualiza =actualizarcuentadebito(du.getCOD_USUARIO().intValue(),txtmadebitar.getValue());
					if(actualiza==0){
							
												
						obtienecuenta(du.getCOD_USUARIO().toString());						
						txtdadebitar.setValue("");
						txtmadebitar.setValue("");
						lblrdebito.setValue("Debito realizado exitosamente");
						
					}else{
						lblrdebito.setValue("No se ha podido actualizar la cuenta");
					}
				}else{
					lblrdebito.setValue("No se ha podido realizar el debito");				
				}
				*/
			}else{
				lblrdebito.setValue("No se ha podido realizar el debito, porque la cantidad excede el monto de la cuenta");
			}
		}	
		
	}
	
	private int insertar_solicitud(int monto, String descrip,int cod_usu){
		int res=0;
		String sql ="INSERT INTO SOLICITUD_CREDITO(MONTO, ESTADO, DESCRIPCION," +
				"	 FECHA_SOLICITUD, USUARIO_COD_USUARIO,ADMIN_COD_USUARIO1)"+
					" VALUES("+monto+",'pendiente','"+descrip+"', NOW(),"+
					cod_usu+","+cod_usu+") ";
		
		Object ret = Utilidad.ejecutaUpdate(sql);
		System.out.println("valor de consulta:"+ret);
		if(ret.equals("")||!ret.toString().equals("1")){
			res=1;			
		}	
		return res;
		
		
	}
	
	
	
	private int insertarcredito(String monto, String descrip) throws SQLException{
		int res=0;
		String sql ="INSERT INTO CREDITO(MONTO, DESCRIPCION, FECHA, "+ 
					" RESIDUO, FECHA_RESIDUO, ESTADO, "+
							" CUENTA_NO_CUENTA,CUENTA_USUARIO_COD_USUARIO) "+
					" VALUES("+monto+",'"+descrip+"', NOW(),"+
					monto+",NOW(),'pendiente',"+		
					dc.getNO_CUENTA().intValue()+","+du.getCOD_USUARIO().intValue()+") ";
		
		Object ret = Utilidad.ejecutaUpdate(sql);
		System.out.println("valor de consulta:"+ret);
		if(ret.equals("")||!ret.toString().equals("1")){
			res=0;			
		}	
		return res;
		
	}
	
	
	private int insertardebito(int monto, String descrip,
			Integer cta_rece,int no_cuenta,int cod_usuario ) throws SQLException{
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
	
	private int actualizarcuentacredito(int cod_usu, String monto) throws SQLException{
		int res=0;
		String sql =" UPDATE CUENTA "+
				" SET SALDO=SALDO+"+monto+ 
				" WHERE USUARIO_COD_USUARIO="+cod_usu;
		Object ret = Utilidad.ejecutaUpdate(sql);
		System.out.println("valor de consulta:"+ret);
		if(ret.equals("")||!ret.toString().equals("1")){
			res=1;			
		}	
		return res;
		
	}
	
	
	private int actualizarcuentatransferencia(int cod_cu, int monto) throws SQLException{
		int res=0;
		String sql =" UPDATE CUENTA "+
				" SET SALDO=SALDO+"+monto+ 
				" WHERE NO_CUENTA="+cod_cu;
		Object ret = Utilidad.ejecutaUpdate(sql);
		System.out.println("valor de consulta:"+ret);
		if(ret.equals("")||!ret.toString().equals("1")){
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
	
	private int verificaCuenta(String cod_cuenta) throws SQLException{
		int res=0;
		String sql ="SELECT COUNT( NO_CUENTA ) "+
						"FROM CUENTA "+
						" WHERE NO_CUENTA="+cod_cuenta;
		Object ret = Utilidad.ejecutaConsulta(sql);
		System.out.println("valor de consulta:"+ret);
		if(!ret.equals("")&&!ret.toString().equals("0")){
			res=1;			
		}	
		return res;
		
	}
	
	private int obtener_usuario_cuenta(String cod_cuenta) throws SQLException{
		int res=0;
		String sql ="SELECT USUARIO_COD_USUARIO "+
			" FROM CUENTA "+
			" WHERE NO_CUENTA= "+cod_cuenta;
		Object ret = Utilidad.ejecutaConsulta(sql);
		System.out.println("valor de consulta:"+ret);
		if(!ret.equals("")&&!ret.toString().equals("0")){
			res=Integer.valueOf(ret.toString()) ;			
		}	
		return res;
		
	}
	
	
	private int verificarCamposCredito(String monto, String des){
		int res=0;
		//se verifican que todos los campos esten llenos de info
		if(monto==null || monto.isEmpty()||monto.toString().trim().equals("")){			
			lblemacreditar.setValue("Debe ingresar informacion");
			res++;
		}
		if(des==null || des.isEmpty()||des.toString().trim().equals("")){			
			lbledacreditar.setValue("Debe ingresar informacion");
			res++;
		}
		
		if(!monto.matches("[0-9]+")){
			lblemacreditar.setValue("Debe ingresar solamente digitos");
			res++;
		}
						
		return res;
	}
	
	private int verificarCamposTransferencia(String cuenta , String monto){
		int res=0;
		//se verifican que todos los campos esten llenos de info
		if(cuenta==null || cuenta.isEmpty()||cuenta.toString().trim().equals("")){			
			lbletcuenta.setValue("Debe ingresar informacion");
			res++;
		}
		if(monto==null || monto.isEmpty()||monto.toString().trim().equals("")){			
			lbletmonto.setValue("Debe ingresar informacion");
			res++;
		}
				
		if(!cuenta.matches("[0-9]+")){
			lbletcuenta.setValue("Debe ingresar solamente digitos");
			res++;
		}
		if(!monto.matches("[0-9]+")){
			lbletmonto.setValue("Debe ingresar solamente digitos");
			res++;
		}
		
		if(res!=0){
			return res;
		}
				
		return res;
	}
	
	private int verificarCamposDebito(String monto, String des){
		int res=0;
		//se verifican que todos los campos esten llenos de info
		if(monto==null || monto.isEmpty()||monto.toString().trim().equals("")){			
			lblemadebitar.setValue("Debe ingresar informacion");
			res++;
		}
		if(des==null || des.isEmpty()||des.toString().trim().equals("")){			
			lbledadebitar.setValue("Debe ingresar informacion");
			res++;
		}
		if(res!=0){
			return res;
		}
		if(!monto.matches("[0-9]+")){
			lblemadebitar.setValue("Debe ingresar solamente digitos");
			res++;
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
	
	
	public void onClick$btnexportar() throws Exception{
		System.out.println("ANTES DE ENVIAR A GENERAR EL INFORME");
		generaReporte();
	}
	
	private void generaReporte() throws Exception{		
		try{
			if (listaTransferencia == null || listaTransferencia.size() == 0 
					|| listaTransferencia.isEmpty()){
				Messagebox.show("No se generó el informe ya que la tabla está vacía.");
				return;
			}
			
			SimpleDateFormat formateador = new SimpleDateFormat("dd/MMMM/yyyy_HH:mm:ss");
			Date fecha = new Date();
			
			
			String ruta="/P3_AYD1_2019/reporte/reporte_cuenta.xls";
			String ruta2="/P3_AYD1_2019/reporte_cuenta.xls";
			String ruta3 = Executions.getCurrent().getContextPath();
			
			
			File archivo = new File(ruta);
			File archivo2 = new File(ruta2);
			File archivo3 = new File(ruta3);
			
			System.out.println("RUTA:"+archivo.exists());
			System.out.println("RUTA2:"+archivo2.exists());
			System.out.println("RUTA3:"+ruta3);
			
			{
				
				
				
				HSSFWorkbook libro = new HSSFWorkbook();
				HSSFSheet hoja = libro.createSheet();
				libro.setSheetName(0, "HIstorial");
				
				
				
				HSSFRow row = hoja.createRow(0);
				HSSFFont fontRedBold = libro.createFont();
				HSSFFont fontNormal = libro.createFont();
				fontRedBold.setColor(HSSFFont.COLOR_NORMAL);
				fontRedBold.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				fontNormal.setColor(HSSFFont.COLOR_NORMAL);
				fontNormal.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
				// Create the style
				HSSFCellStyle cellStyleRedBold = libro.createCellStyle();
				HSSFCellStyle cellStyleNormal = libro.createCellStyle();
				cellStyleRedBold.setFont(fontRedBold);
				cellStyleNormal.setFont(fontNormal);
				// encabezados		
				
				HSSFCellStyle estil = libro.createCellStyle();
				HSSFFont font = libro.createFont();
				font.setFontHeightInPoints((short) 15);
				font.setFontName("Bookman Old Style");
				font.setItalic(true);					
				estil.setFont(font);
				cellStyleNormal.setFont(font);
				cellStyleRedBold.setFont(font);
				
				row = hoja.createRow(1);
				HSSFCell celcoddeb = row.createCell((short)1);
				estil.setFillPattern(HSSFCellStyle.ALIGN_CENTER);
				celcoddeb.setCellStyle(estil);
				celcoddeb.setCellType(HSSFCell.CELL_TYPE_STRING);
				celcoddeb.setCellValue("No. Debito");
				
				
				HSSFCell celFecha = row.createCell((short)2);
				estil.setFillPattern(HSSFCellStyle.ALIGN_CENTER);
				celFecha.setCellStyle(estil);
				celFecha.setCellType(HSSFCell.CELL_TYPE_STRING);
				celFecha.setCellValue("Fecha");;
				
				
				
				HSSFCell celUsuario = row.createCell((short)3);
				estil.setFillPattern(HSSFCellStyle.ALIGN_CENTER);
				celUsuario.setCellStyle(estil);
				celUsuario.setCellType(HSSFCell.CELL_TYPE_STRING);
				celUsuario.setCellValue("Usuario");;
				
				HSSFCell celMonto = row.createCell((short)4);
				estil.setFillPattern(HSSFCellStyle.ALIGN_CENTER);
				celMonto.setCellStyle(estil);
				celMonto.setCellType(HSSFCell.CELL_TYPE_STRING);
				celMonto.setCellValue("Monto");;
				
				
				HSSFCell celDescrip = row.createCell((short)5);
				estil.setFillPattern(HSSFCellStyle.ALIGN_CENTER);
				celDescrip.setCellStyle(estil);
				celDescrip.setCellType(HSSFCell.CELL_TYPE_STRING);
				celDescrip.setCellValue("Fecha");;
				
				
				HSSFCell celcuentare = row.createCell((short)6);
				estil.setFillPattern(HSSFCellStyle.ALIGN_CENTER);
				celcuentare.setCellStyle(estil);
				celcuentare.setCellType(HSSFCell.CELL_TYPE_STRING);
				celcuentare.setCellValue("Cuenta Receptora");
				
				
				HSSFCell celCodUsu = row.createCell((short)7);
				estil.setFillPattern(HSSFCellStyle.ALIGN_CENTER);
				celCodUsu.setCellStyle(estil);
				celCodUsu.setCellType(HSSFCell.CELL_TYPE_STRING);
				celCodUsu.setCellValue("Cod. Usuario");
				
				 
				HSSFCell celNumCuenta = row.createCell((short)8);
				estil.setFillPattern(HSSFCellStyle.ALIGN_CENTER);
				celNumCuenta.setCellStyle(estil);
				celNumCuenta.setCellType(HSSFCell.CELL_TYPE_STRING);
				celNumCuenta.setCellValue("No. Cuenta");
				
				
				int a = 0;
				//listado de telefonos
				
				for(int i = 0; i < listaTransferencia.size(); i++){
					if(i == 0){
						a = 2;
					}		
					row = hoja.createRow(a);
					HSSFCell celda1 = row.createCell((short)0);
					celda1.setCellStyle(cellStyleRedBold);
					celda1.setCellType(HSSFCell.CELL_TYPE_STRING);
					celda1.setCellValue(i+1);
					
					Debito_dto db =listaTransferencia.get(i);
					
					
					HSSFCell c1 = row.createCell((short)1);
					c1.setCellStyle(cellStyleNormal);
					c1.setCellType(HSSFCell.CELL_TYPE_STRING);							
					c1.setCellValue(db.getNO_DEBITO().toString());			
					
					HSSFCell c2 = row.createCell((short)2);
					c2.setCellStyle(cellStyleNormal);
					c2.setCellType(HSSFCell.CELL_TYPE_STRING);	
					c2.setCellValue(db.getFECHA().toString());
					
					HSSFCell c21 = row.createCell((short)3);
					c21.setCellStyle(cellStyleNormal);
					c21.setCellType(HSSFCell.CELL_TYPE_STRING);	
					c21.setCellValue(db.getUSUARIO().toString());
					
					HSSFCell c3 = row.createCell((short)4);
					c3.setCellStyle(cellStyleNormal);
					c3.setCellType(HSSFCell.CELL_TYPE_STRING);	
					c3.setCellValue(db.getMONTO().toString());
					
					HSSFCell c4 = row.createCell((short)5);
					c4.setCellStyle(cellStyleNormal);
					c4.setCellType(HSSFCell.CELL_TYPE_STRING);
					c4.setCellValue(db.getDESCRIPCION().toString());
					
					
					HSSFCell c5 = row.createCell((short)6);
					c5.setCellStyle(cellStyleNormal);
					c5.setCellType(HSSFCell.CELL_TYPE_STRING);			
					c5.setCellValue(""+db.getCUENTA_RECEPTORA());
					
					HSSFCell c6 = row.createCell((short)7);
					c6.setCellStyle(cellStyleNormal);
					c6.setCellType(HSSFCell.CELL_TYPE_STRING);			
					c6.setCellValue(db.getCUENTA_USUARIO_COD_USUARIO());
					
					HSSFCell c7 = row.createCell((short)8);
					c7.setCellStyle(cellStyleNormal);
					c7.setCellType(HSSFCell.CELL_TYPE_STRING);			
					c7.setCellValue(db.getCUENTA_NO_CUENTA());
					
					a++;			
				}
				hoja.setDefaultColumnWidth((short)40);					
				
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
		        libro.write(baos);
		        AMedia amedia = new AMedia("historial.xls", "xls",
		                "application/file", baos.toByteArray());
		        Filedownload.save(amedia);
		        baos.close();
				
				
				
				
				
				
				
				/*
				
				int filas = listaTransferencia.size();
				HSSFCellStyle estil = libro.createCellStyle();
				for(int i =0; i< filas; i++){
					//String fila =(String) a.get(i);
					//StringTokenizer st = new StringTokenizer(fila, ",");
					HSSFRow row = hoja.createRow(i);				
					//int j=0;
					//while(st.hasMoreTokens()){
						//String tok = st.nextToken();
						Debito_dto deb = listaTransferencia.get(i);
						//para la cabecera, la primera fila, aplicamos un estilo(negrita y color de fondo azul)
						if(i ==0){
							
							estil.setFillPattern(HSSFCellStyle.ALIGN_CENTER);
							HSSFFont font = libro.createFont();
							font.setFontHeightInPoints((short) 15);
							font.setFontName("Bookman Old Style");
							font.setItalic(true);					
							estil.setFont(font);
							createCell(row, (short) 1, deb.getNO_DEBITO().toString() , estil);
							createCell(row,(short)2, deb.getMONTO().toString(), estil);						
						}else{
							createCell(row, (short) 1, deb.getNO_DEBITO().toString() , estil);
							createCell(row,(short)2, deb.getMONTO().toString(), estil);
												
						}
						//j = j+1;					
					//}				
				}
				
				//asignar automaticamente el tamano de las celdas en funcion del contenido
				hoja.setDefaultColumnWidth((short)25);	
				FileOutputStream fOut = new FileOutputStream("reporte_cuenta");
				// Write the Excel sheet
				libro.write(fOut);
				
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
		        libro.write(baos);
		        AMedia amedia = new AMedia("historial.xls", "xls",
		                "application/file", baos.toByteArray());
		        Filedownload.save(amedia);
		        baos.close();
				
				
				
				*/
				
				
				
				
				
				
				
				/*
				
				
				 Workbook workbook = new HSSFWorkbook();
				    Sheet listSheet = workbook.createSheet("reporte");

				    int rowIndex = 2;
				    for(Debito_dto dt: listaTransferencia ) {
				        Row row = listSheet.createRow(rowIndex++);
				        int cellIndex = 0;
				        row.createCell(cellIndex++).setCellValue(dt.getNO_DEBITO().intValue());
				        row.createCell(cellIndex++).setCellValue(dt.getMONTO().intValue());
				        row.createCell(cellIndex++).setCellValue(dt.getDESCRIPCION());
				        if(dt.getCUENTA_RECEPTORA()!=null){
				        	row.createCell(cellIndex++).setCellValue(dt.getCUENTA_RECEPTORA().intValue());
				        }else{
				        	row.createCell(cellIndex++).setCellValue("");				        	
				        }
				        row.createCell(cellIndex++).setCellValue(dt.getUSUARIO());
				        row.createCell(cellIndex++).setCellValue(dt.getCUENTA_USUARIO_COD_USUARIO().intValue());
				        row.createCell(cellIndex++).setCellValue(dt.getFECHA());
				        row.createCell(cellIndex++).setCellValue(dt.getCUENTA_NO_CUENTA().intValue());
				    }

				    try {
				        ByteArrayOutputStream baos = new ByteArrayOutputStream();
				        workbook.write(baos);
				        AMedia amedia = new AMedia("historial.xls", "xls",
				                "application/file", baos.toByteArray());
				        Filedownload.save(amedia);
				        baos.close();
				    } catch (Exception e) {
				        e.printStackTrace();
				    }
				*/
				
				
				
				
				
				
				
				
			}
		}catch(Exception e){
			Messagebox.show("Ocurrió un error al exportar el informe.");
			e.printStackTrace();
		}
	}	
	
	
	public static void createCell(HSSFRow row, short i, String value, HSSFCellStyle estilo){		
		HSSFCell cell = row.createCell(i);
		value = value+" ";
		cell.setCellValue(value);
		if(estilo != null){
			cell.setCellStyle(estilo);
		}		
	}
	
	
}


