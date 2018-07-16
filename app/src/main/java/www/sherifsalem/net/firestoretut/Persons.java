package www.sherifsalem.net.firestoretut;

public class Persons {

    //note that the key for any field must match withein our main activity in order to update certain key properly
    //same applies to the getter methods if we changed the defaults it will create a new record in our document
    private String name, number, info;

    public Persons(){

    }

    public Persons (String name, String number, String info ){
        this.name = name;
        this.number = number;
        this.info = info;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public String getInfo() {
        return info;
    }

}
