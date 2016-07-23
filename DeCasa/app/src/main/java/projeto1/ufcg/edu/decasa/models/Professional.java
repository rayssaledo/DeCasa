package projeto1.ufcg.edu.decasa.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Professional implements Serializable {

    private String name;
    private String cpf;
    private String phone;
    private String neighborhood;
    private String street;
    private String number;
    private String site;
    private String socialNetwork;
    private String pictury;
    private String email;
    private String password;

    public Professional(String name,String cpf, String phone, String neighborhood, String street,
                        String number, String site, String socialNetwork, String pictury,
                        String email, String password)
            throws Exception {
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
        if(pictury == null || pictury.equals("")){
            throw new Exception("Pictury is invalid.");
        }
        if(email == null || email.equals("")){
            throw new Exception("Email is invalid.");
        }
        if(password == null || password.equals("")){
            throw new Exception("Password is invalid.");
        }
        this.name = name;
        this.cpf = cpf;
        this.phone = phone;
        this.neighborhood = neighborhood;
        this.street = street;
        this.number = number;
        this.site = site;
        this.socialNetwork = socialNetwork;
        this.pictury = pictury;
        this.email = email;
        this.password = password;
    }

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

    public String getPictury() {
        return pictury;
    }

    public void setPictury(String pictury) throws Exception {
        if(pictury == null || pictury.equals("")){
            throw new Exception("Pictury is invalid.");
        }
        this.pictury = pictury;
    }

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
}