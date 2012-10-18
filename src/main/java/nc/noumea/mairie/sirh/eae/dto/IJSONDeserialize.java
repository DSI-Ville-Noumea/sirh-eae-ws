package nc.noumea.mairie.sirh.eae.dto;

public interface IJSONDeserialize<T> {
	public T deserializeFromJSON(String json);
}
