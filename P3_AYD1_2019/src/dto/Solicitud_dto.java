package dto;

public class Solicitud_dto {
	
	private Integer COD_SOLICITUD_CREDITO;
	private Integer MONTO;
	private String ESTADO;
	private String DESCRIPCION;
	private String FECHA_SOLICITUD;
	private Integer USUARIO_COD_USUARIO;
	private Integer ADMIN_COD_USUARIO1;
	
	public Integer getCOD_SOLICITUD_CREDITO() {
		return COD_SOLICITUD_CREDITO;
	}
	public void setCOD_SOLICITUD_CREDITO(Integer cOD_SOLICITUD_CREDITO) {
		COD_SOLICITUD_CREDITO = cOD_SOLICITUD_CREDITO;
	}
	public Integer getMONTO() {
		return MONTO;
	}
	public void setMONTO(Integer mONTO) {
		MONTO = mONTO;
	}
	public String getESTADO() {
		return ESTADO;
	}
	public void setESTADO(String eSTADO) {
		ESTADO = eSTADO;
	}
	public String getDESCRIPCION() {
		return DESCRIPCION;
	}
	public void setDESCRIPCION(String dESCRIPCION) {
		DESCRIPCION = dESCRIPCION;
	}
	public String getFECHA_SOLICITUD() {
		return FECHA_SOLICITUD;
	}
	public void setFECHA_SOLICITUD(String fECHA_SOLICITUD) {
		FECHA_SOLICITUD = fECHA_SOLICITUD;
	}
	public Integer getUSUARIO_COD_USUARIO() {
		return USUARIO_COD_USUARIO;
	}
	public void setUSUARIO_COD_USUARIO(Integer uSUARIO_COD_USUARIO) {
		USUARIO_COD_USUARIO = uSUARIO_COD_USUARIO;
	}
	public Integer getADMIN_COD_USUARIO1() {
		return ADMIN_COD_USUARIO1;
	}
	public void setADMIN_COD_USUARIO1(Integer aDMIN_COD_USUARIO1) {
		ADMIN_COD_USUARIO1 = aDMIN_COD_USUARIO1;
	}
	
		
	

}
