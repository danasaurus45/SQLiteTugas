package com.example.sqlitetugas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String DATABASE_NAME = "sqlite";

    TextView tvViewData;
    EditText etName, etPhone;

    SQLiteDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvViewData = (TextView) findViewById(R.id.textViewViewData);
        etName = (EditText) findViewById(R.id.editTextName);
        etPhone = (EditText) findViewById(R.id.editTextPhone);

        findViewById(R.id.buttonInsert).setOnClickListener(this);
        tvViewData.setOnClickListener(this);

        mDatabase = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        createDataTable();
    }

    private boolean inputsAreCorrect(String name, String phone) {
        if (name.isEmpty()) {
            etName.setError("Please enter a name");
            etName.requestFocus();
            return false;
        }

        if (phone.isEmpty()) {
            etPhone.setError("Please enter phone number");
            etPhone.requestFocus();
            return false;
        }
        return true;
    }

    private void addData() {
        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        if(inputsAreCorrect(name,phone)) {
            String insertSQL = "INSERT INTO datas \n" +
                    "(name, phone)\n" +
                    "VALUES \n" +
                    "(?, ?);";

            mDatabase.execSQL(insertSQL, new String[]{name, phone} );

            Toast.makeText(this, "Data Added Successfully", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonInsert:

                addData();

                break;
            case R.id.textViewViewData:

                startActivity(new Intent(this, DataActivity.class));

                break;
        }
    }

    private void createDataTable() {
        mDatabase.execSQL(
                "CREATE TABLE IF NOT EXISTS datas (\n" +
                        "    id INTEGER NOT NULL CONSTRAINT datas_pk PRIMARY KEY AUTOINCREMENT,\n" +
                        "    name varchar(200) NOT NULL,\n" +
                        "    phone varchar(200) NOT NULL\n" +
                        ");"
        );
    }
}