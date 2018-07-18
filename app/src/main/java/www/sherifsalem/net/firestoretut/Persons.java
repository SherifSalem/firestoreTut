package www.sherifsalem.net.firestoretut;

import com.google.firebase.firestore.Exclude;

public class Persons {

    //note that the key for any field must match withein our main activity in order to update certain key properly
    //same applies to the getter methods if we changed the defaults it will create a new record in our document
    private String name, number, info, documentId;
    private int priority;

    public Persons(){

    }

    public Persons (String name, String number, String info, int priority ){
        this.name = name;
        this.number = number;
        this.info = info;
        this.priority = priority;
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

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
