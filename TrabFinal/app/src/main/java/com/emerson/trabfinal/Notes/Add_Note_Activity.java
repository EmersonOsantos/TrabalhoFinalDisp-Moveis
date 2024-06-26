package com.emerson.trabfinal.Notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.emerson.trabfinal.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.emerson.trabfinal.Objects.Note;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Add_Note_Activity extends AppCompatActivity {
    TextView Uid_User, Mail_User, Date_ActualHour, Date, State;
    EditText Title, Description;
    Button Btn_calendar;
    int day, month, year;

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    DatabaseReference DB_Firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        InitVar();
        ObtainData();
        Obtain_Date_Hour();

        Btn_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                day = calendar.get(Calendar.DAY_OF_MONTH);
                month = calendar.get(Calendar.MONTH);
                year = calendar.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(Add_Note_Activity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int YearSelected, int MonthSelected, int DaySelected) {

                        String dayFormated, monthFormated;

                        //OBTAIN DAY
                        if (DaySelected < 10) {
                            dayFormated = "0" + String.valueOf(DaySelected);
                        } else {
                            dayFormated = String.valueOf(DaySelected);
                        }

                        //OBTAIN MONTH
                        int Month = MonthSelected + 1;
                        if (Month < 10) {
                            monthFormated = "0" + String.valueOf(Month);
                        } else {
                            monthFormated = String.valueOf(Month);
                        }

                        //SET DATE IN TV
                        Date.setText(dayFormated + "/" + monthFormated + "/" + YearSelected);

                    }
                }
                        , year, month, day);
                datePickerDialog.show();
            }
        });
    }

    private void InitVar() {
        Uid_User = findViewById(R.id.Uid_User);
        Mail_User = findViewById(R.id.Mail_User);
        Date_ActualHour = findViewById(R.id.Date_ActualHour);
        Date = findViewById(R.id.Date);
        State = findViewById(R.id.State);

        Title = findViewById(R.id.Title);
        Description = findViewById(R.id.Description);
        Btn_calendar = findViewById(R.id.Btn_calendar);

        DB_Firebase = FirebaseDatabase.getInstance().getReference("Users");

        firebaseAuth = FirebaseAuth.getInstance();
        user =firebaseAuth.getCurrentUser();
    }

    private void ObtainData() {
        String uid_recovered = getIntent().getStringExtra("Uid");
        String mail_recovered = getIntent().getStringExtra("Gmail");

        Uid_User.setText(uid_recovered);
        Mail_User.setText(mail_recovered);
    }

    private void Obtain_Date_Hour() {
        String Date_Hour_Register = new SimpleDateFormat("dd-MM-yyyy/HH:mm:ss a", Locale.getDefault()).format(System.currentTimeMillis());
        Date_ActualHour.setText(Date_Hour_Register);
    }

    private void Add_Note(){

        //DATA OBTAIN
        String uid_user = Uid_User.getText().toString();
        String mail_user = Mail_User.getText().toString();
        String date_hour_actual = Date_ActualHour.getText().toString();
        String title = Title.getText().toString();
        String description = Description.getText().toString();
        String date = Date.getText().toString();
        String state = State.getText().toString();
        String id_Note = DB_Firebase.push().getKey();


        //VALIDATE DATA
        if (!uid_user.equals("") && !mail_user.equals("") && !date_hour_actual.equals("") &&
                !title.equals("") && !description.equals("") && !date.equals("") && !state.equals("")) {

            Note note = new Note(id_Note,
                    uid_user,
                    mail_user,
                    date_hour_actual,
                    title,
                    description,
                    date,
                    state);

            //SET THE NAME OF THE DB
            String Name_DB = "Published notes";

            assert id_Note != null;
            DB_Firebase.child(user.getUid()).child(Name_DB).child(id_Note).setValue(note);
            Toast.makeText(this, "Nota salva com sucesso", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
        else {
            Toast.makeText(this, "preencha todos os campos", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_add, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.Add_NoteDB) {
            Add_Note();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}