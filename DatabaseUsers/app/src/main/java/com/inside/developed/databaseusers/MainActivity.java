package com.inside.developed.databaseusers;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static android.R.attr.password;
import static android.R.attr.y;

public class MainActivity extends AppCompatActivity {

    final String LOG_TAG = "myLogs";
    Context context;
    private List<User> userList = new ArrayList<>();
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    DBHelper dbHelper;
    ContentValues cv;
    SQLiteDatabase db;
    EditText year, firstName, lastName;
    Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        userAdapter = new UserAdapter(userList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(userAdapter);

        dbHelper = new DBHelper(this);
        cv = new ContentValues();


        prepareUserData();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_user:
               // Toast.makeText(this, "Add Users", Toast.LENGTH_SHORT).show();
                LayoutInflater layoutInflater = LayoutInflater.from(this);


                View promptView = layoutInflater.inflate(R.layout.alert_dialog, null);

                final AlertDialog alertD = new AlertDialog.Builder(this).create();

                firstName = (EditText) promptView.findViewById(R.id.firstName);
                lastName = (EditText) promptView.findViewById(R.id.lastName);
                year = (EditText) promptView.findViewById(R.id.year);

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
                        new DatePickerDialog(MainActivity.this, date, myCalendar
                                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                    }
                });

                Button btnSave = (Button) promptView.findViewById(R.id.btnSave);
                Button btnCancel = (Button) promptView.findViewById(R.id.btnCancel);

                btnSave.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        db = dbHelper.getWritableDatabase();

                        String fName = firstName.getText().toString();
                        String lName =  lastName.getText().toString();
                        String yYear =  year.getText().toString();

                        cv.put("firstname", fName);
                        cv.put("lastname", lName);
                        cv.put("year", yYear);
                        long rowID = db.insert("user", null, cv);
                        Toast.makeText(MainActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                        dbHelper.close();
                        userList.clear();
                        prepareUserData();
                        alertD.dismiss();
                    }
                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        Toast.makeText(MainActivity.this, "Canceled", Toast.LENGTH_SHORT).show();
                        alertD.dismiss();

                    }
                });

                alertD.setView(promptView);

                alertD.show();

                break;

            case R.id.del_all:
                db = dbHelper.getWritableDatabase();
                int clearCount = db.delete("user", null, null);
                Toast.makeText(MainActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                dbHelper.close();
                userList.clear();
                prepareUserData();

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        year.setText(sdf.format(myCalendar.getTime()));
    }

    private void prepareUserData() {


        db = dbHelper.getWritableDatabase();
        Log.d(LOG_TAG, "--- Rows in mytable: ---");
        // делаем запрос всех данных из таблицы USERS, получаем Cursor
        Cursor c = db.query("user", null, null, null, null, null, null);

        if (c.moveToFirst()) {

            int idColIndex = c.getColumnIndex("id");
            int firstnameColIndex = c.getColumnIndex("firstname");
            int lastnameColIndex = c.getColumnIndex("lastname");
            int yearColIndex = c.getColumnIndex("year");

            do {
                User user = new User(c.getString(idColIndex),c.getString(firstnameColIndex), c.getString(lastnameColIndex), c.getString(yearColIndex));
                userList.add(user);

            } while (c.moveToNext());
        } else
            Log.d(LOG_TAG, "0 rows");
        c.close();
        dbHelper.close();


//
//        User user = new User("Android", "Trainee", "2017");
//        userList.add(user);
//
//        user = new User("Android", "Junior", "2017");
//        userList.add(user);

        userAdapter.notifyDataSetChanged();
    }
    @Override
    public void onStart(){
        super.onStart();
        userList.clear();
        prepareUserData();
    }
    @Override
    public void onPause() {
        super.onPause();
    }

}
