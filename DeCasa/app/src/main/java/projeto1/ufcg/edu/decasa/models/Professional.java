package projeto1.ufcg.edu.decasa.models;

import java.io.Serializable;

public class Professional implements Serializable {

    private String professionalName;
    private String service;

    public Professional(String professionalName, String service) throws Exception {
        if(professionalName == null || professionalName.equals("")){
            throw new Exception("Professional name is invalid.");
        }
        if(service == null || service.equals("")){
            throw new Exception("Service is invalid.");
        }

        this.professionalName = professionalName;
        this.service = service;
    }

    public String getProfessionalName() { return professionalName; }

    public String getService() { return service; }


    public void setProfessionalName(String professionalName) throws Exception {
        if(professionalName == null || professionalName.equals("")){
            throw new Exception("Professional name is invalid.");
        }
        this.professionalName = professionalName;
    }

    public void setService(String service) throws Exception {
        if(service == null || service.equals("")){
            throw new Exception("Service is invalid.");
        }
        this.service = service;
    }
}
