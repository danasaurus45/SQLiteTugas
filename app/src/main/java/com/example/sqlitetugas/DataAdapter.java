package com.example.sqlitetugas;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DataAdapter extends ArrayAdapter<Data> {

    Context mCtx;
    int listLayoutRes;
    List<Data> dataList;
    SQLiteDatabase mDatabase;

    public DataAdapter(Context mCtx, int listLayoutRes, List<Data> dataList, SQLiteDatabase mDatabase) {
        super(mCtx, listLayoutRes, dataList);

        this.mCtx = mCtx;
        this.listLayoutRes = listLayoutRes;
        this.dataList = dataList;
        this.mDatabase = mDatabase;
    }

    private void updateData(final Data data) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.dialog_update_data, null);
        builder.setView(view);

        final EditText etName = view.findViewById(R.id.editTextName);
        final EditText etPhone = view.findViewById(R.id.editTextPhone);

        etName.setText(data.getName());
        etPhone.setText(data.getPhone());

        final AlertDialog dialog = builder.create();
        dialog.show();

        view.findViewById(R.id.buttonUpdate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = etName.getText().toString().trim();
                String phone = etPhone.getText().toString().trim();

                if (name.isEmpty()) {
                    etName.setError("Name can't be blank");
                    etName.requestFocus();
                    return;
                }

                if (phone.isEmpty()) {
                    etPhone.setError("Phone number can't be blank");
                    etPhone.requestFocus();
                    return;
                }

                String sql = "UPDATE datas \n" +
                        "SET name = ?, \n" +
                        "phone = ? \n" +
                        "WHERE id = ?;\n";

                mDatabase.execSQL(sql, new String[]{name, phone, String.valueOf(data.getId())});
                Toast.makeText(mCtx, "Data Updated", Toast.LENGTH_SHORT).show();
                reloadDatasFromDatabase();

                dialog.dismiss();
            }
        });
    }
    
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(listLayoutRes, null);

        final Data data = dataList.get(position);

        TextView tvName = view.findViewById(R.id.textViewName);
        TextView tvPhone = view.findViewById(R.id.textViewTelephone);

        tvName.setText(data.getName());
        tvPhone.setText(data.getPhone());

        Button btnDelete = view.findViewById(R.id.buttonDelete);
        Button btnEdit = view.findViewById(R.id.buttonEdit);

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateData(data);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);
                builder.setTitle("Are you sure?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String sql = "DELETE FROM datas WHERE id = ?";
                        mDatabase.execSQL(sql, new Integer[]{data.getId()});
                        reloadDatasFromDatabase();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        return view;
    }

    private void reloadDatasFromDatabase() {
        Cursor cursorDatas = mDatabase.rawQuery("SELECT * FROM datas", null);
        if (cursorDatas.moveToFirst()) {
            dataList.clear();
            do {
                dataList.add(new Data(
                        cursorDatas.getInt(0),
                        cursorDatas.getString(1),
                        cursorDatas.getString(2)
                ));
            } while (cursorDatas.moveToNext());
        }
        cursorDatas.close();
        notifyDataSetChanged();
    }
}
