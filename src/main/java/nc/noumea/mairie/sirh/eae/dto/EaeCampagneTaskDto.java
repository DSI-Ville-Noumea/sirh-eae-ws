package nc.noumea.mairie.sirh.eae.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import nc.noumea.mairie.sirh.eae.domain.EaeCampagneTask;
import nc.noumea.mairie.sirh.tools.transformer.JsonDateDeserializer;
import nc.noumea.mairie.sirh.tools.transformer.JsonDateSerializer;

public class EaeCampagneTaskDto implements Serializable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -3756552262600025321L;

	private Integer				idEaeCampagneTask;
	private Integer				idEaeCampagne;
	private Integer				annee;
	private Integer				idAgent;
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date				dateCalculEae;
	private String				taskStatus;

	public EaeCampagneTaskDto() {

	}

	public EaeCampagneTaskDto(EaeCampagneTask task) {
		this();
		this.idEaeCampagneTask = task.getIdEaeCampagneTask();
		this.idEaeCampagne = task.getEaeCampagne().getIdCampagneEae();
		this.annee = task.getAnnee();
		this.idAgent = task.getIdAgent();
		this.dateCalculEae = task.getDateCalculEae();
		this.taskStatus = task.getTaskStatus();
	}

	public Integer getIdEaeCampagneTask() {
		return idEaeCampagneTask;
	}

	public void setIdEaeCampagneTask(Integer idEaeCampagneTask) {
		this.idEaeCampagneTask = idEaeCampagneTask;
	}

	public Integer getIdEaeCampagne() {
		return idEaeCampagne;
	}

	public void setIdEaeCampagne(Integer idEaeCampagne) {
		this.idEaeCampagne = idEaeCampagne;
	}

	public Integer getAnnee() {
		return annee;
	}

	public void setAnnee(Integer annee) {
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
