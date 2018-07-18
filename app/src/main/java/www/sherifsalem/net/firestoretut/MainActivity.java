package www.sherifsalem.net.firestoretut;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity {

    //TAG  for log msgs
    private static final String TAG = MainActivity.class.getSimpleName();

    //keys for data that will be stored in the firestore database
    private static final String KEY_NAME = "name";
    private static final String KEY_PHONE = "number";
    private static final String KEY_INFO = "info";
    //reference var for the edit text fields
    private EditText nameEditText, numberEditText, infoEditText, priorityEditText;
    private TextView displayTextView;
     private Persons person;

    //initializing an instance of forestore data base
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    //add a collection reference to add multiple records
    private CollectionReference records = db.collection("Persons Database");

    //creating a reference to certain document in the database
    private DocumentReference personRef = db.document("Persons Database/First Person");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //references to the view in this activity
        nameEditText = findViewById(R.id.name_edit_text);
        numberEditText = findViewById(R.id.number);
        infoEditText = findViewById(R.id.info);
        priorityEditText = findViewById(R.id.priority_edit_text);
        displayTextView = findViewById(R.id.display);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //this will load our records once we started the app , passing this to the listener will only make the call when we open
        //our activity and clears and won't update it in the background so we save bandwidth

        records.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
           if (e != null){
               return;
           }else {
               String dataRecords = "";
               for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){

                   Persons persons = documentSnapshot.toObject(Persons.class);
                  //gets the auto generated id for the document
                   persons.setDocumentId(documentSnapshot.getId());


                   String documentId = persons.getDocumentId();
                   String name  = persons.getName();
                   String number = persons.getNumber();
                   String info = persons.getInfo();
                    int priority = persons.getPriority();

                   dataRecords += "ID: "+ documentId +"\nName: "+name + "\nNumber: "+ number +"\nInfo: "
                           + info +"\nPriority: "+ priority+"\n\n";
               }
               displayTextView.setText(dataRecords);
           }

            }
        });


    }

    /**
     * methode to be invoked when the button clicked to save the data to the database
     *
     * @param view
     */
    public void submitRecord(View view) {
        //get the text typed in the edit text fiels and convert it to string
        String name = nameEditText.getText().toString();
        String number = numberEditText.getText().toString();
        String info = infoEditText.getText().toString();

        //we are using this edit text as int so we must check that it is not 0 for we are parsing our edit text to int
        //for the app not to crash
        if (priorityEditText.length() == 0){
            priorityEditText.setText("0");
        }
        int priority = Integer.parseInt(priorityEditText.getText().toString());
        clearEditText();






         person = new Persons(name, number, info, priority);


        //adding the key value pairs to the referenced document
        records.add(person).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(MainActivity.this, "record added successfully", Toast.LENGTH_SHORT).show();
            }

        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "there was an error adding record", Toast.LENGTH_SHORT).show();
                    }
                });

        nameEditText.clearComposingText();
        numberEditText.clearComposingText();
        infoEditText.clearComposingText();

    }

    /**
     * method to load the data that was saved the database
     *
     * @param view
     */
    public void loadPersons(View view) {
        //gets the database records
       records
               //filters the records
               .whereEqualTo("priority",2)
               .orderBy("priority", Query.Direction.DESCENDING)
               //gets the records
               .get()
               .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
           @Override
           public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
            String datarecords = "";

            for (QueryDocumentSnapshot documentSnapshots : queryDocumentSnapshots ){
                Persons persons = documentSnapshots.toObject(Persons.class);
                persons.setDocumentId(documentSnapshots.getId());

                String documentId = persons.getDocumentId();
                String name = persons.getName();
                String number = persons.getNumber();
                String info = persons.getName();
                int priority = persons.getPriority();

                datarecords += "ID: "+ documentId +"\nName: "+name + "\nNumber: "+ number +"\nInfo: "
                        + info +"\nPriority: "+ priority+"\n\n";


            }
               displayTextView.setText(datarecords);
           }
       });
    }



    //delete the name field
    public void deleteName(View view) {

        personRef.update(KEY_NAME, FieldValue.delete());
    }

    /*
    method to clear edit texts after typing
     */
    private void clearEditText (){
        priorityEditText.getText().clear();
        nameEditText.getText().clear();
        numberEditText.getText().clear();
        infoEditText.getText().clear();
    }
}
