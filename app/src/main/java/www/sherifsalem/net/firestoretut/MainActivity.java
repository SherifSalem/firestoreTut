package www.sherifsalem.net.firestoretut;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity {

    //TAG  for log msgs
    private static final String TAG = MainActivity.class.getSimpleName();

    //keys for data that will be stored in the firestore database
    private static final String KEY_NAME = "name";
    private static final String KEY_PHONE = "number";
    private static final String KEY_INFO = "info";
    //reference var for the edit text fields
    private EditText nameEditText, numberEditText, infoEditText;
    private TextView displayTextView;
     private Persons person;

    //initializing an instance of forestore data base
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

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

        displayTextView = findViewById(R.id.display);
    }

    @Override
    protected void onStart() {
        super.onStart();


        //snapshot listener to update the data when the activity starts
        personRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                //if there is an exception will show a toast then abort this method
                if (e != null) {
                    Toast.makeText(MainActivity.this, "Error loading data", Toast.LENGTH_SHORT).show();
                    return;
                }
                //if the the document exists will fetch the data and display it in the display textview
                if (documentSnapshot.exists()) {

//                    String name = documentSnapshot.getString(KEY_NAME);
//                    String number = documentSnapshot.getString(KEY_PHONE);
//                    String info = documentSnapshot.getString(KEY_INFO);
//                    displayTextView.setText("Name: " + name + "\n" + "Number: " + number + "\n" + "Info: " + info);

                    Persons person = documentSnapshot.toObject(Persons.class);

                    assert person != null;
                    String name = person.getName();
                    String number = person.getNumber();
                    String info = person.getInfo();


                    displayTextView.setText("Name: " + name+ "\n" + "Number: "+ number + "\n" + "Info: " + info);

                } else {
                    displayTextView.setText("");
                }
            }
        });
    }

    /**
     * methode to be invoked when the button clicked to save the data to the database
     *
     * @param view
     */
    public void submitInformation(View view) {
        //get the text typed in the edit text fiels and convert it to string
        String name = nameEditText.getText().toString();
        String number = numberEditText.getText().toString();
        String info = infoEditText.getText().toString();

         person = new Persons(name, number, info);

        //create a hashmap to save key value pairs of the data
//        Map<String, Object> person = new HashMap<>();
//        person.put(KEY_NAME, name);
//        person.put(KEY_PHONE, number);
//        person.put(KEY_INFO, info);

        //adding the key value pairs to the referenced document
        personRef.set(person)

                //interface to show a toast when the data is saved successfully
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, "Person saved successfully :)", Toast.LENGTH_SHORT).show();
                    }
                })
                //interface to show a toast when fail to save data
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Faild to save person :(", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, e.toString());
                    }
                });
    }

    /**
     * method to load the data that was saved the database
     *
     * @param view
     */
    public void loadPerson(View view) {

        personRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            //gets the document field from our database
//                        String name = documentSnapshot.getString(KEY_NAME);
//                        String number = documentSnapshot.getString(KEY_PHONE);
//                        String info = documentSnapshot.getString(KEY_INFO);

                            //Map<String, Object> person = documentSnapshot.getData();

                            Persons person = documentSnapshot.toObject(Persons.class);

                            String name = person.getName();
                            String number = person.getNumber();
                            String info = person.getInfo();


                            displayTextView.setText("Name: " + name + "\n" + "Number: " + number + "\n" + "Info: " + info);

                        } else {
                            Toast.makeText(MainActivity.this, "Person Does not exist ", Toast.LENGTH_SHORT).show();
                        }
                    }
                })

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Error getting data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //update the name field
    public void updateName(View view) {

        String name = nameEditText.getText().toString();

        //Map<String, Object> person = new HashMap<>();
        //update just certain key of our data then adding it to the data
        //person.put(KEY_NAME,name);
        /*merge our updated data to our existing document
        note that the merge method cretaes the doc if it is not exists while update doesn't
         */
        // personRef.set(person, SetOptions.merge());

        //update using the update method for certain keys,
        personRef.update(KEY_NAME, name);
    }

    //delete the whole document
    public void deletePerson(View view) {
        personRef.delete();
    }

    //delete the name field
    public void deleteName(View view) {

        personRef.update(KEY_NAME, FieldValue.delete());
    }
}
