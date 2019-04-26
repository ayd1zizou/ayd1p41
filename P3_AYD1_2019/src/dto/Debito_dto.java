package dto;

import java.math.BigDecimal;

public class Debito_dto {
	
	private Integer NO_DEBITO;
	private Integer MONTO;
	private String DESCRIPCION;
	private Integer CUENTA_RECEPTORA;
	private String FECHA;
	private Integer CUENTA_NO_CUENTA;
	private Integer CUENTA_USUARIO_COD_USUARIO;
	
	
	public Integer getNO_DEBITO() {
		return NO_DEBITO;
	}
	public void setNO_DEBITO(Integer nO_DEBITO) {
		NO_DEBITO = nO_DEBITO;
	}
	public Integer getMONTO() {
		return MONTO;
	}
	public void setMONTO(Integer mONTO) {
		MONTO = mONTO;
	}
	public String getDESCRIPCION() {
		return DESCRIPCION;
	}
	public void setDESCRIPCION(String dESCRIPCION) {
		DESCRIPCION = dESCRIPCION;
	}
	public Integer getCUENTA_RECEPTORA() {
		return CUENTA_RECEPTORA;
	}
	public void setCUENTA_RECEPTORA(Integer cUENTA_RECEPTORA) {
		CUENTA_RECEPTORA = cUENTA_RECEPTORA;
	}
	public String getFECHA() {
		return FECHA;
	}
	public void setFECHA(String fECHA) {
		FECHA = fECHA;
	}
	public Integer getCUENTA_NO_CUENTA() {
		return CUENTA_NO_CUENTA;
	}
	public void setCUENTA_NO_CUENTA(Integer cUENTA_NO_CUENTA) {
		CUENTA_NO_CUENTA = cUENTA_NO_CUENTA;
	}
	public Integer getCUENTA_USUARIO_COD_USUARIO() {
		return CUENTA_USUARIO_COD_USUARIO;
	}
	public void setCUENTA_USUARIO_COD_USUARIO(Integer cUENTA_USUARIO_COD_USUARIO) {
		CUENTA_USUARIO_COD_USUARIO = cUENTA_USUARIO_COD_USUARIO;
	}
	
		
	
}
