package projeto1.ufcg.edu.decasa.models;

import java.util.Date;

public class Evaluation {

    private String professionalValued;
    private String usernameValuer;
    private float evaluationValue;
    private String comment;
    private String date;
    private String photo;

    public Evaluation(String professionalValued, String usernameValuer, float evaluationValue,
                      String comment, String date, String photo) throws Exception {

        if(professionalValued == null || professionalValued.equals("")){
            throw new Exception("Professional valued is invalid.");
        }
        if(usernameValuer == null || usernameValuer.equals("")){
            throw new Exception("Username valuer is invalid.");
        }
        if(evaluationValue <= 0){
            throw new Exception("Evaluation value is invalid.");
        }
        if(date == null || professionalValued.equals("")){
            throw new Exception("Date is invalid.");
        }
        if(photo == null || photo.equals("")){
            throw new Exception("Photo is invalid.");
        }

        this.professionalValued = professionalValued;
        this.usernameValuer = usernameValuer;
        this.evaluationValue = evaluationValue;
        this.comment = comment;
        this.date = date;
        this.photo = photo;
    }

    public String getUsernameValuer() {
        return usernameValuer;
    }

    public void setUsernameValuer(String usernameValuer) throws Exception {
        if(usernameValuer == null || usernameValuer.equals("")){
            throw new Exception("Username valuer is invalid.");
        }
        this.usernameValuer = usernameValuer;
    }

    public String getProfessionalValued() {
        return professionalValued;
    }

    public void setProfessionalValued(String professionalValued) throws Exception {
        if(professionalValued == null || professionalValued.equals("")){
            throw new Exception("Professional valued is invalid.");
        }
        this.professionalValued = professionalValued;
    }

    public float getEvaluationValue() {
        return evaluationValue;
    }

    public void setEvaluationValue(float evaluationValue) throws Exception {
        if(evaluationValue <= 0){
            throw new Exception("Evaluation value is invalid.");
        }
        this.evaluationValue = evaluationValue;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) throws Exception {
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) throws Exception {
        if(date == null || professionalValued.equals("")){
            throw new Exception("Date is invalid.");
        }
        this.date = date;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) throws Exception {
        if(photo == null || photo.equals("")){
            throw new Exception("Photo is invalid.");
        }
        this.photo = photo;
    }
}
