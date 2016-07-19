package projeto1.ufcg.edu.decasa.models;

import java.io.Serializable;
import java.util.List;

public class Professional implements Serializable {

    private String professionalName;
    private List<String> service;

    public Professional(String professionalName, List<String> service) throws Exception {
        if(professionalName == null || professionalName.equals("")){
            throw new Exception("Professional name is invalid.");
        }
        if(service == null){
            throw new Exception("Service is invalid.");
        }

        this.professionalName = professionalName;
        this.service = service;
    }

    public String getProfessionalName() { return professionalName; }

    public List<String> getService() { return service; }


    public void setProfessionalName(String professionalName) throws Exception {
        if(professionalName == null || professionalName.equals("")){
            throw new Exception("Professional name is invalid.");
        }
        this.professionalName = professionalName;
    }

    public void setService(List<String> service) throws Exception {
        if(service == null){
            throw new Exception("Service is invalid.");
        }
        this.service = service;
    }
}