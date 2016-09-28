package nc.noumea.mairie.sirh.eae.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PersistenceUnit;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "EAE_CAMPAGNE_TASK")
@PersistenceUnit(unitName = "eaePersistenceUnit")
@NamedQueries({
		 @NamedQuery(name = "EaeCampagneTask.getEaeCampagneTask", query = "select e from EaeCampagneTask e where e.idEaeCampagneTask = :idEaeCampagneTask") ,
		 @NamedQuery(name = "EaeCampagneTask.getEaeCampagneTaskByIdCampagne", query = "select e from EaeCampagneTask e where e.eaeCampagne.idCampagneEae = :idEaeCampagne order by e.dateCalculEae desc") 
})
public class EaeCampagneTask {

	@Id
	@Column(name = "ID_CAMPAGNE_TASK")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idEaeCampagneTask;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "ID_CAMPAGNE_EAE", referencedColumnName = "ID_CAMPAGNE_EAE")
	private EaeCampagne eaeCampagne;

	@NotNull
	@Column(name = "ANNEE")
	private int annee;

	@NotNull
	@Column(name = "ID_AGENT")
	private Integer idAgent;

	@Column(name = "DATE_CALCUL_EAE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateCalculEae;

	@Column(name = "TASK_STATUS")
	private String taskStatus;

	public int getIdEaeCampagneTask() {
		return idEaeCampagneTask;
	}

	public void setIdEaeCampagneTask(int idEaeCampagneTask) {
		this.idEaeCampagneTask = idEaeCampagneTask;
	}

	public EaeCampagne getEaeCampagne() {
		return eaeCampagne;
	}

	public void setEaeCampagne(EaeCampagne eaeCampagne) {
		this.eaeCampagne = eaeCampagne;
	}

	public int getAnnee() {
		return annee;
	}

	public void setAnnee(int annee) {
		this.annee = annee;
	}

	public Integer getIdAgent() {
		return idAgent;
	}

	public void setIdAgent(Integer idAgent) {
		this.idAgent = idAgent;
	}

	public Date getDateCalculEae() {
		return dateCalculEae;
	}

	public void setDateCalculEae(Date dateCalculEae) {
		this.dateCalculEae = dateCalculEae;
	}

	public String getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}

}
