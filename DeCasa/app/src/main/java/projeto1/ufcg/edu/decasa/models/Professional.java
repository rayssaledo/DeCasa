package projeto1.ufcg.edu.decasa.models;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

public class Professional implements Parcelable {

    private String name;
    private String businessName;
    private String cpf;
    private String phone1;
    private String phone2;
    private String phone3;
    private String phone4;
    private String street;
    private String number;
    private String neighborhood;
    private String city;
    private String state;
    private String site;
    private String socialNetwork1;
    private String socialNetwork2;
    private String service;
    private String description;
    private String email;
    private String picture;
    private String plan;
    private float avg;
    private int numberAssessments;
    private Location location;

    public Professional (String name, String businessName, String cpf, String phone1, String phone2,
                         String phone3, String phone4, String street, String number,
                         String neighborhood, String city, String state, String site,
                         String socialNetwork1, String socialNetwork2, String service,
                         String description, String email, float avg, String picture, String plan)
            throws Exception {

        if(name == null || name.equals("")){
            throw new Exception("Professional name is invalid.");
        }
        if(cpf == null || cpf.equals("")){
            throw new Exception("CPF is invalid.");
        }
        if(phone1 == null || phone1.equals("")){
            throw new Exception("Phone1  is invalid.");
        }
        if(street == null || street.equals("")){
            throw new Exception("Street is invalid.");
        }
        if(number == null || number.equals("")){
            throw new Exception("Number is invalid.");
        }
        if(neighborhood == null || neighborhood.equals("")){
            throw new Exception("Neighborhood is invalid.");
        }
        if(city == null || city.equals("")){
            throw new Exception("City is invalid.");
        }
        if(state == null || state.equals("")){
            throw new Exception("State is invalid.");
        }
        if(service == null || service.equals("")){
            throw new Exception("Service is invalid.");
        }
        if(email == null || email.equals("")){
            throw new Exception("Email is invalid.");
        }
        if(plan == null || plan.equals("")){
            throw new Exception("Plan is invalid.");
        }

        this.name = name;
        this.businessName = businessName;
        this.cpf = cpf;
        this.phone1 = phone1;
        this.phone2 = phone2;
        this.phone3 = phone3;
        this.phone4 = phone4;
        this.street = street;
        this.number = number;
        this.neighborhood = neighborhood;
        this.city = city;
        this.state = state;
        this.site = site;
        this.socialNetwork1 = socialNetwork1;
        this.socialNetwork2 = socialNetwork2;
        this.service = service;
        this.description = description;
        this.email = email;
        this.avg = avg;
        this.picture = picture;
        this.plan = plan;

    }

    protected Professional(Parcel in) {
        name = in.readString();
        businessName = in.readString();
        cpf = in.readString();
        phone1 = in.readString();
        phone2 = in.readString();
        phone3 = in.readString();
        phone4 = in.readString();
        street = in.readString();
        number = in.readString();
        neighborhood = in.readString();
        city = in.readString();
        state = in.readString();
        site = in.readString();
        socialNetwork1 = in.readString();
        socialNetwork2 = in.readString();
        service = in.readString();
        description = in.readString();
        email = in.readString();
        picture = in.readString();
        plan = in.readString();
        avg = in.readFloat();
        numberAssessments = in.readInt();
        location = in.readParcelable(Location.class.getClassLoader());
    }

    public static final Creator<Professional> CREATOR = new Creator<Professional>() {
        @Override
        public Professional createFromParcel(Parcel in) {
            return new Professional(in);
        }

        @Override
        public Professional[] newArray(int size) {
            return new Professional[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public String getPhone3() {
        return phone3;
    }

    public void setPhone3(String phone3) {
        this.phone3 = phone3;
    }

    public String getPhone4() {
        return phone4;
    }

    public void setPhone4(String phone4) {
        this.phone4 = phone4;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getSocialNetwork1() {
        return socialNetwork1;
    }

    public void setSocialNetwork1(String socialNetwork1) {
        this.socialNetwork1 = socialNetwork1;
    }

    public String getSocialNetwork2() {
        return socialNetwork2;
    }

    public void setSocialNetwork2(String socialNetwork2) {
        this.socialNetwork2 = socialNetwork2;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public float getAvg() {
        return avg;
    }

    public void setAvg(float avg) {
        this.avg = avg;
    }

    public int getNumberAssessments() {
        return numberAssessments;
    }

    public void setNumberAssessments(int numberAssessments) {
        this.numberAssessments = numberAssessments;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(businessName);
        parcel.writeString(cpf);
        parcel.writeString(phone1);
        parcel.writeString(phone2);
        parcel.writeString(phone3);
        parcel.writeString(phone4);
        parcel.writeString(street);
        parcel.writeString(number);
        parcel.writeString(neighborhood);
        parcel.writeString(city);
        parcel.writeString(state);
        parcel.writeString(site);
        parcel.writeString(socialNetwork1);
        parcel.writeString(socialNetwork2);
        parcel.writeString(service);
        parcel.writeString(description);
        parcel.writeString(email);
        parcel.writeString(picture);
        parcel.writeString(plan);
        parcel.writeFloat(avg);
        parcel.writeInt(numberAssessments);
        parcel.writeParcelable(location, i);
    }

}