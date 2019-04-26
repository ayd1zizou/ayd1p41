package dto;

import java.math.BigDecimal;

public class Credito_dto {
	
	private Integer NO_CREDITO;
	private Integer MONTO;
	private String DESCRIPCION;
	private String FECHA;
	private Integer RESIDUO;
	private String FECHA_RESIDUO;
	private String ESTADO;
	private Integer CUENTA_NO_CUENTA;
	private Integer CUENTA_USUARIO_COD_USUARIO;
	
	
	
	
	public Integer getNO_CREDITO() {
		return NO_CREDITO;
	}
	public void setNO_CREDITO(Integer nO_CREDITO) {
		NO_CREDITO = nO_CREDITO;
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
	public Integer getRESIDUO() {
		return RESIDUO;
	}
	public void setRESIDUO(Integer rESIDUO) {
		RESIDUO = rESIDUO;
	}
	public String getFECHA_RESIDUO() {
		return FECHA_RESIDUO;
	}
	public void setFECHA_RESIDUO(String fECHA_RESIDUO) {
		FECHA_RESIDUO = fECHA_RESIDUO;
	}
	public String getESTADO() {
		return ESTADO;
	}
	public void setESTADO(String eSTADO) {
		ESTADO = eSTADO;
	}
	
	
}
