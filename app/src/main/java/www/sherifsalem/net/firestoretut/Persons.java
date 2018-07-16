package www.sherifsalem.net.firestoretut;

import com.google.firebase.firestore.Exclude;

public class Persons {

    //note that the key for any field must match withein our main activity in order to update certain key properly
    //same applies to the getter methods if we changed the defaults it will create a new record in our document
    private String name, number, info, documentId;

    public Persons(){

    }

    public Persons (String name, String number, String info ){
        this.name = name;
        this.number = number;
        this.info = info;
    }

    @Exclude
    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
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
