package projeto1.ufcg.edu.decasa.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Evaluation implements Parcelable{

    private String professionalValued;
    private String usernameValuer;
    private float evaluationValue;
    private String comment;
    private String date;
    private String photo;
    //private String service;

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
        if(date == null || date.equals("")){
            throw new Exception("Date is invalid.");
        }
        if(photo == null || photo.equals("")){
            throw new Exception("Photo is invalid.");
        }
//        if(service == null || service.equals("")){
//            throw new Exception("Service is invalid.");
//        }

        this.professionalValued = professionalValued;
        this.usernameValuer = usernameValuer;
        this.evaluationValue = evaluationValue;
        this.comment = comment;
        this.date = date;
        this.photo = photo;
        //this.service = service;
    }

    protected Evaluation(Parcel in) {
        professionalValued = in.readString();
        usernameValuer = in.readString();
        evaluationValue = in.readFloat();
        comment = in.readString();
        date = in.readString();
        photo = in.readString();
        //service = in.readString();
    }

    public static final Creator<Evaluation> CREATOR = new Creator<Evaluation>() {
        @Override
        public Evaluation createFromParcel(Parcel in) {
            return new Evaluation(in);
        }

        @Override
        public Evaluation[] newArray(int size) {
            return new Evaluation[size];
        }
    };

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
        if(date == null || date.equals("")){
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

//    public String gerService() {
//        return service;
//    }
//
//    public void serService(String service) throws Exception {
//        if(service == null || service.equals("")){
//            throw new Exception("Service is invalid.");
//        }
//        this.service = service;
//    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(professionalValued);
        parcel.writeString(usernameValuer);
        parcel.writeFloat(evaluationValue);
        parcel.writeString(comment);
        parcel.writeString(date);
        parcel.writeString(photo);
        //parcel.writeString(service);
    }
}
