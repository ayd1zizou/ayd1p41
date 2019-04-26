package dto;

import java.math.BigDecimal;

public class Usuario_dto {
	private Integer COD_USUARIO;
	private String USUARIO;
	private String NOMBRE;
	private String EMAIL;
	private String CONTRASENIA;
	private Integer ROL;
	
	public Integer getCOD_USUARIO() {
		return COD_USUARIO;
	}
	public void setCOD_USUARIO(Integer cOD_USUARIO) {
		COD_USUARIO = cOD_USUARIO;
	}
	public String getUSUARIO() {
		return USUARIO;
	}
	public void setUSUARIO(String uSUARIO) {
		USUARIO = uSUARIO;
	}
	public String getNOMBRE() {
		return NOMBRE;
	}
	public void setNOMBRE(String nOMBRE) {
		NOMBRE = nOMBRE;
	}
	public String getEMAIL() {
		return EMAIL;
	}
	public void setEMAIL(String eMAIL) {
		EMAIL = eMAIL;
	}
	public String getCONTRASENIA() {
		return CONTRASENIA;
	}
	public void setCONTRASENIA(String cONTRASENIA) {
		CONTRASENIA = cONTRASENIA;
	}
	public Integer getROL() {
		return ROL;
	}
	public void setROL(Integer rOL) {
		ROL = rOL;
	}
	
	
}
