package projeto1.ufcg.edu.decasa.models;

public class User {
    private String name;
    private String date_of_birth;
    private String gender;
    private String street;
    private String number;
    private String neighborhood;
    private String city;
    private String state;
    private String username;
    private String password;
    private String photo;

    public User(String name, String date_of_birth, String gender, String street, String number,
                String neighborhood, String city, String state, String username, String password,
                String photo) throws Exception{


        if(name == null || name.equals("")){
            throw new Exception("User name is invalid.");
        }
        if(date_of_birth == null || date_of_birth.equals("")){
            throw new Exception("Date of birth is invalid.");
        }
        if(gender == null || password.equals("")){
            throw new Exception("Gender is invalid.");
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
        if(city == null || neighborhood.equals("")){
            throw new Exception("City is invalid.");
        }
        if(state == null || state.equals("")){
            throw new Exception("State is invalid.");
        }
        if(username == null || username.equals("")){
            throw new Exception("Username is invalid.");
        }
        if(password == null || password.equals("")){
            throw new Exception("Password is invalid.");
        }
        if(photo == null || photo.equals("")){
            throw new Exception("Photo is invalid.");
        }

        this.name = name;
        this.date_of_birth = date_of_birth;
        this.gender = gender;
        this.street = street;
        this.number = number;
        this.neighborhood = neighborhood;
        this.city = city;
        this.state = state;
        this.username = username;
        this.password = password;
        this.photo = photo;
    }

    public String getName(){
        return name;
    }
    public String getDateOfBirth(){
        return date_of_birth;
    }
    public String getGender(){
        return gender;
    }
    public String getStreet(){
        return street;
    }
    public String getNumber(){
        return number;
    }
    public String getNeighborhood(){
        return neighborhood;
    }
    public String getCity(){
        return city;
    }
    public String getState(){
        return state;
    }
    public String getUsername(){
        return username;
    }
    public String getPassword(){
        return password;
    }
    public String getPhoto() { return photo; }

    public void setName(String name){
        this.name = name;
    }
    public void setDateOfBirth(String date_of_birth){
        this.date_of_birth = date_of_birth;
    }
    public void setGender(String gender){
        this.gender = gender;
    }
    public void setStreet(String street){
        this.street = street;
    }
    public void setNumber(String number){
        this.number = number;
    }
    public void setNeighborhood(String neighborhood){
        this.neighborhood = neighborhood;
    }
    public void setCity(String city){
        this.city = city;
    }
    public void setState(String state){
        this.state = state;
    }
    public void setPhoto(String photo) { this.photo = photo; }


}

