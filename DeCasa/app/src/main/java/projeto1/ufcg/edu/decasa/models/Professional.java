package projeto1.ufcg.edu.decasa.models;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

public class Professional implements Parcelable {

    private String name;
    private String cpf;
    private String phone;
    private String neighborhood;
    private String street;
    private String number;
    private String city;
    private String state;
    private String site;
    private String socialNetwork;
    private String namePicture;
    private String email;
    private String password;
    private String[] services;
    private Location location;
    private float evaluationsAverage;
    private int numberAssessments;

    public Professional(String name, String cpf, String phone, String street, String number,
                        String neighborhood, String city, String state, String site,
                        String socialNetwork, String email, String password,
                        String[] services, String NamePicture) throws Exception {
        if(name == null || name.equals("")){
            throw new Exception("Professional name is invalid.");
        }
        if(cpf == null || cpf.equals("")){
            throw new Exception("CPF is invalid.");
        }
        if(phone == null || phone.equals("")){
            throw new Exception("Phone is invalid.");
        }
        if(neighborhood == null || neighborhood.equals("")){
            throw new Exception("Neighborhood is invalid.");
        }
        if(street == null || street.equals("")){
            throw new Exception("Street is invalid.");
        }
        if(number == null || number.equals("")){
            throw new Exception("Number is invalid.");
        }
//        if(pictury == null || pictury.equals("")){
//            throw new Exception("Pictury is invalid.");
//        }
        if(email == null || email.equals("")){
            throw new Exception("Email is invalid.");
        }
        if(password == null || password.equals("")){
            throw new Exception("Password is invalid.");
        }
        if(city == null || city.equals("")){
            throw new Exception("City is invalid.");
        }
        if(state == null || state.equals("")){
            throw new Exception("State is invalid.");
        }
        this.name = name;
        this.cpf = cpf;
        this.phone = phone;
        this.neighborhood = neighborhood;
        this.street = street;
        this.number = number;
        this.city = city;
        this.state = state;
        this.site = site;
        this.socialNetwork = socialNetwork;
        this.namePicture = NamePicture;
        this.email = email;
        this.password = password;
        this.services = services;
    }

    public Professional(String name, String cpf, String phone, String street, String number,
                        String neighborhood, String city, String state, String site,
                        String socialNetwork, String email, String[] services) throws Exception {
        if(name == null || name.equals("")){
            throw new Exception("Professional name is invalid.");
        }
        if(cpf == null || cpf.equals("")){
            throw new Exception("CPF is invalid.");
        }
        if(phone == null || phone.equals("")){
            throw new Exception("Phone is invalid.");
        }
        if(neighborhood == null || neighborhood.equals("")){
            throw new Exception("Neighborhood is invalid.");
        }
        if(street == null || street.equals("")){
            throw new Exception("Street is invalid.");
        }
        if(number == null || number.equals("")){
            throw new Exception("Number is invalid.");
        }
//        if(pictury == null || pictury.equals("")){
//            throw new Exception("Pictury is invalid.");
//        }
        if(email == null || email.equals("")){
            throw new Exception("Email is invalid.");
        }
       // if(password == null || password.equals("")){
         //   throw new Exception("Password is invalid.");
       // }
        if(city == null || city.equals("")){
            throw new Exception("City is invalid.");
        }
        if(state == null || state.equals("")){
            throw new Exception("State is invalid.");
        }
        this.name = name;
        this.cpf = cpf;
        this.phone = phone;
        this.neighborhood = neighborhood;
        this.street = street;
        this.number = number;
        this.city = city;
        this.state = state;
        this.site = site;
        this.socialNetwork = socialNetwork;
        //this.pictury = pictury;
        this.email = email;
        //this.password = password;
         this.services = services;
    }

    protected Professional(Parcel in) {
        name = in.readString();
        cpf = in.readString();
        phone = in.readString();
        street = in.readString();
        number = in.readString();
        neighborhood = in.readString();
        city = in.readString();
        state = in.readString();
        site = in.readString();
        socialNetwork = in.readString();
        email = in.readString();
        password = in.readString();
        namePicture = in.readString();
        services = in.createStringArray();
        location = in.readParcelable(Location.class.getClassLoader());
        evaluationsAverage = in.readFloat();
        numberAssessments = in.readInt();
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

    public String getName() { return name; }

    public void setName(String name) throws Exception {
        if(name == null || name.equals("")){
            throw new Exception("Professional name is invalid.");
        }
        this.name = name;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) throws Exception {
        if(cpf == null || cpf.equals("")){
            throw new Exception("CPF is invalid.");
        }
        this.cpf = cpf;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) throws Exception {
        if(phone == null || phone.equals("")){
            throw new Exception("Phone is invalid.");
        }
        this.phone = phone;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) throws Exception {
        if(neighborhood == null || neighborhood.equals("")){
            throw new Exception("Neighborhood is invalid.");
        }
        this.neighborhood = neighborhood;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) throws Exception {
        if(street == null || street.equals("")){
            throw new Exception("Street is invalid.");
        }
        this.street = street;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) throws Exception {
        if(number == null || number.equals("")){
            throw new Exception("Number is invalid.");
        }
        this.number = number;
    }

    public String getCity() { return city; }

    public void setCity(String city) throws Exception {
        if(city == null || city.equals("")){
            throw new Exception("City is invalid.");
        }
        this.city = city;
    }

    public String getState() { return state; }

    public void setState(String state) throws Exception {
        if(state == null || state.equals("")){
            throw new Exception("State is invalid.");
        }
        this.state = state;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) throws Exception {
        if(site == null || site.equals("")){
            throw new Exception("Site is invalid.");
        }
        this.site = site;
    }

    public String getSocialNetwork() {
        return socialNetwork;
    }

    public void setSocialNetwork(String socialNetwork) throws Exception {
        if(socialNetwork == null || socialNetwork.equals("")){
            throw new Exception("Social network is invalid.");
        }
        this.socialNetwork = socialNetwork;
    }

//    public String getPictury() {
//        return pictury;
//    }
//
//    public void setPictury(String pictury) throws Exception {
//        if(pictury == null || pictury.equals("")){
//            throw new Exception("Pictury is invalid.");
//        }
//        this.pictury = pictury;
//    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) throws Exception {
        if(email == null || email.equals("")){
            throw new Exception("Email is invalid.");
        }
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) throws Exception {
        if(password == null || password.equals("")){
            throw new Exception("Password is invalid.");
        }
        this.password = password;
    }

    public String[] getServices() {
        return services;
    }

    public void setServices(String[] services) {
        this.services = services;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public float getEvaluationsAverage() {
        return evaluationsAverage;
    }

    public void setEvaluationsAverage(float evaluationsAverage) {
        this.evaluationsAverage = evaluationsAverage;
    }

    public int getNumberAssessments() {
        return numberAssessments;
    }

    public void setNumberAssessments(int numberAssessments) {
        this.numberAssessments = numberAssessments;
    }

    public String getNamePicture() {
        return this.namePicture;
    }

    public void setNamePicture(String namePicture) {
        this.namePicture = namePicture;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(cpf);
        parcel.writeString(phone);
        parcel.writeString(street);
        parcel.writeString(number);
        parcel.writeString(neighborhood);
        parcel.writeString(city);
        parcel.writeString(state);
        parcel.writeString(site);
        parcel.writeString(socialNetwork);
        parcel.writeString(email);
        parcel.writeString(password);
        parcel.writeString(namePicture);
        parcel.writeStringArray(services);
        parcel.writeParcelable(location, i);
        parcel.writeFloat(evaluationsAverage);
        parcel.writeInt(numberAssessments);
    }

    @Override
    public String toString() {
        return "Professional{" +
                "name='" + name + '\'' +
                ", cpf='" + cpf + '\'' +
                ", phone='" + phone + '\'' +
                ", neighborhood='" + neighborhood + '\'' +
                ", street='" + street + '\'' +
                ", number='" + number + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", site='" + site + '\'' +
                ", socialNetwork='" + socialNetwork + '\'' +
                ", namePicture='" + namePicture + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", services=" + Arrays.toString(services) +
                ", location=" + location +
                ", evaluationsAverage=" + evaluationsAverage +
                ", numberAssessments=" + numberAssessments +
                '}';
    }
}