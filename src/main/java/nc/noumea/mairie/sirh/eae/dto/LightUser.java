package nc.noumea.mairie.sirh.eae.dto;

public class LightUser {

	private String sAMAccountName;
	private Integer employeeNumber;
	private String mail;
	private String distinguishedName;

	public String getsAMAccountName() {
		return sAMAccountName;
	}

	public void setsAMAccountName(String sAMAccountName) {
		this.sAMAccountName = sAMAccountName;
	}

	public Integer getEmployeeNumber() {
		return employeeNumber;
	}

	public void setEmployeeNumber(Integer employeeNumber) {
		this.employeeNumber = employeeNumber;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getDistinguishedName() {
		return distinguishedName;
	}

	public void setDistinguishedName(String distinguishedName) {
		this.distinguishedName = distinguishedName;
	}
}
