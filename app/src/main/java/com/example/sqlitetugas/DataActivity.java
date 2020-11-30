package com.example.sqlitetugas;

import androidx.appcompat.app.AppCompatActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;

public class DataActivity extends AppCompatActivity {

    List<Data> dataList;
    SQLiteDatabase mDatabase;
    ListView listViewDatas;
    DataAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        listViewDatas = (ListView) findViewById(R.id.listViewData);
        dataList = new ArrayList<>();

        mDatabase = openOrCreateDatabase(MainActivity.DATABASE_NAME, MODE_PRIVATE, null);

        showDatasFromDatabase();
    }

    private void showDatasFromDatabase() {
        Cursor cursorDatas = mDatabase.rawQuery("SELECT * FROM datas", null);

        if (cursorDatas.moveToFirst()) {
            do {
                dataList.add(new Data(
                        cursorDatas.getInt(0),
                        cursorDatas.getString(1),
                        cursorDatas.getString(2)
                ));
            } while (cursorDatas.moveToNext());
        }

        cursorDatas.close();

        adapter = new DataAdapter(this, R.layout.list_layout_data, dataList, mDatabase);

        listViewDatas.setAdapter(adapter);
    }
}