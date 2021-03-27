package com.sc703.taller.ui.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sc703.taller.R;

import org.w3c.dom.Text;

public class SQLiteFragment extends Fragment {

    private Button btnAgregar, btnEditar, btnBuscar, btnEliminar;
    private EditText edtCedula, edtNombre, edtEdad;
    private SQLiteDatabase conexionBD;
    private ModeloSQL modeloSQL;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root =  inflater.inflate(R.layout.fragment_sqlite, container, false);

        edtCedula = root.findViewById(R.id.edt_SQLCedula);
        edtNombre = root.findViewById(R.id.edt_SQLNombre);
        edtEdad = root.findViewById(R.id.edt_SQLEdad);
        btnAgregar = root.findViewById(R.id.btn_SQLAgregar);
        btnEditar = root.findViewById(R.id.btn_SQLEditar);
        btnBuscar = root.findViewById(R.id.btn_SQLBuscar);
        btnEliminar = root.findViewById(R.id.btn_SQLEliminar);

        modeloSQL = new ModeloSQL(getContext(),"EstudianteBD",null, 1);

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregarBD();
            }
        });
        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editarBD();
            }
        });
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscarBD();
            }
        });
        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminarBD();
            }
        });

        return root;
    }
    private void agregarBD(){
        conexionBD = modeloSQL.getWritableDatabase();

        int id = Integer.parseInt(edtCedula.getText().toString());
        String nombre = edtNombre.getText().toString();
        int edad = Integer.parseInt(edtEdad.getText().toString());

        ContentValues registro = new ContentValues();
        registro.put("Id",id);
        registro.put("Nombre",nombre);
        registro.put("Edad",edad);

        long res = conexionBD.insert("Estudiante", null, registro);

        if (res != -1){
            Toast.makeText(getContext(),"Datos agregados correctamente", Toast.LENGTH_SHORT).show();
            edtCedula.setText("");
            edtNombre.setText("");
            edtEdad.setText("");
        }else{
            Toast.makeText(getContext(),"Hubo un error, los datos no se agregaron", Toast.LENGTH_SHORT).show();
        }
        conexionBD.close();
    }
    private void buscarBD(){
        conexionBD = modeloSQL.getReadableDatabase();

        int id = Integer.parseInt(edtCedula.getText().toString());

        Cursor fila = conexionBD.rawQuery("SELECT * FROM Estudiante WHERE ID =" + id, null);

        if(fila.moveToFirst()){
            edtNombre.setText(fila.getString(1));
            edtEdad.setText(fila.getString(2));
        }else{
            Toast.makeText(getContext(),"No se pudo encontrar los datos solicitados", Toast.LENGTH_SHORT).show();
            edtCedula.setText("");
        }
        conexionBD.close();
    }
    private void editarBD() {
        conexionBD = modeloSQL.getWritableDatabase();

        int id = Integer.parseInt(edtCedula.getText().toString());
        String nombre = edtNombre.getText().toString();
        int edad = Integer.parseInt(edtEdad.getText().toString());

        ContentValues registro = new ContentValues();
        registro.put("Id", id);
        registro.put("Nombre", nombre);
        registro.put("Edad", edad);

        long res = conexionBD.update("Estudiante", registro, "ID=" + id, null);

        if (res == 1) {
            Toast.makeText(getContext(), "Datos editados correctamente", Toast.LENGTH_SHORT).show();
            edtCedula.setText("");
            edtNombre.setText("");
            edtEdad.setText("");
        } else {
            Toast.makeText(getContext(), "Hubo un error, los datos no se editaron", Toast.LENGTH_SHORT).show();
        }

        conexionBD.close();
    }
    private void eliminarBD() {
        conexionBD = modeloSQL.getWritableDatabase();

        int id = Integer.parseInt(edtCedula.getText().toString());

        long res = conexionBD.delete("Estudiante", "ID=" + id, null);

        if (res == 1) {
            Toast.makeText(getContext(), "Datos eliminados correctamente", Toast.LENGTH_SHORT).show();
            edtCedula.setText("");
            edtNombre.setText("");
            edtEdad.setText("");
        } else {
            Toast.makeText(getContext(), "Hubo un error, los datos no se eliminaron", Toast.LENGTH_SHORT).show();
        }

        conexionBD.close();
    }
}