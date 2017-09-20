package com.inside.developed.databaseusers;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class UserActivity extends AppCompatActivity {

    EditText firstName, lastName, year;
    Intent intent;
    DBHelper dbHelper;
    ContentValues cv;
    SQLiteDatabase db;
    String id;
    Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        intent = getIntent();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(intent.getStringExtra("firstName"));
        setSupportActionBar(toolbar);

        firstName = (EditText)findViewById(R.id.firstName);
        lastName = (EditText)findViewById(R.id.lastName);
        year = (EditText)findViewById(R.id.year);

        id = intent.getStringExtra("id");
        firstName.setText(intent.getStringExtra("firstName"));
        lastName.setText(intent.getStringExtra("lastName"));
        year.setText(intent.getStringExtra("year"));

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(UserActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        year.setText(sdf.format(myCalendar.getTime()));
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_user_activity, menu);

        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_changes:

                dbHelper = new DBHelper(this);
                cv = new ContentValues();
                db = dbHelper.getWritableDatabase();

                if (id.equalsIgnoreCase("")) {
                    break;
                }

                // подготовим значения для обновления
                cv.put("firstname", firstName.getText().toString());
                cv.put("lastname",  lastName.getText().toString());
                cv.put("year",  year.getText().toString());
                // обновляем по id
                int updCount = db.update("user", cv, "id = ?",
                        new String[] { id });

                dbHelper.close();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
