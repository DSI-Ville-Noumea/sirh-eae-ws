// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package nc.noumea.mairie.sirh.domain;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import nc.noumea.mairie.sirh.domain.Agent;

privileged aspect Agent_Roo_Json {
    
    public String Agent.toJson() {
        return new JSONSerializer().exclude("*.class").serialize(this);
    }
    
    public static Agent Agent.fromJsonToAgent(String json) {
        return new JSONDeserializer<Agent>().use(null, Agent.class).deserialize(json);
    }
    
    public static String Agent.toJsonArray(Collection<Agent> collection) {
        return new JSONSerializer().exclude("*.class").serialize(collection);
    }
    
    public static Collection<Agent> Agent.fromJsonArrayToAgents(String json) {
        return new JSONDeserializer<List<Agent>>().use(null, ArrayList.class).use("values", Agent.class).deserialize(json);
    }
    
}
