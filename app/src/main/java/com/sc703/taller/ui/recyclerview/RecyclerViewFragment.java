package com.sc703.taller.ui.recyclerview;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sc703.taller.R;
import com.sc703.taller.ui.sqlite.ModeloSQL;

import java.util.ArrayList;

public class RecyclerViewFragment extends Fragment {

    private EditText edtCedula, edtNombre, edtEdad, edtActualizarNombre, edtActualizarEdad;
    private TextView tvActualizarCedula;
    private Button btnActualizar, btnCancelar, btnAgregar;
    private RecyclerView recyclerView;

    private Adaptador adaptador;
    private ModeloSQL modeloSQL;
    private SQLiteDatabase dbSQL;
    private ArrayList<Estudiantes> lista = new ArrayList<>();
    private AlertDialog.Builder builder;
    private AlertDialog dialogo;

    Integer cedula, edad;
    String nombre;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_recyclerview, container, false);
        modeloSQL = new ModeloSQL(getContext(), "EstudianteBD", null, 1);
        edtCedula = root.findViewById(R.id.edt_RVCedula);
        edtNombre = root.findViewById(R.id.edt_RVNombre);
        edtEdad = root.findViewById(R.id.edt_RVEdad);
        recyclerView = root.findViewById(R.id.recyclerview);
        btnAgregar = root.findViewById(R.id.btn_RVAgregar);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adaptador = new Adaptador(lista);
        recyclerView.setAdapter(adaptador);
        cargarDatos();
        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cedula = Integer.parseInt(edtCedula.getText().toString());
                nombre = edtNombre.getText().toString();
                edad = Integer.parseInt(edtEdad.getText().toString());

                Estudiantes estudiantes = new Estudiantes();
                estudiantes.setCedula(cedula);
                estudiantes.setNombre(nombre);
                estudiantes.setEdad(edad);

                dbSQL = modeloSQL.getWritableDatabase();
                ContentValues registro = new ContentValues();
                registro.put("ID", cedula);
                registro.put("Nombre", nombre);
                registro.put("Edad", edad);

                long res = dbSQL.insert("Estudiante", null, registro);
                if (res != -1) {
                    Toast.makeText(v.getContext(), "Se agregaron correctamente los datos",
                            Toast.LENGTH_SHORT).show();
                    edtCedula.setText("");
                    edtNombre.setText("");
                    edtEdad.setText("");

                    lista.add(estudiantes);
                    adaptador.notifyDataSetChanged();
                } else {
                    Toast.makeText(v.getContext(), "Hubo un error, no se agregaron los datos",
                            Toast.LENGTH_SHORT).show();
                }
                dbSQL.close();
            }
        });
        return root;
    }

    private void cargarDatos(){
        dbSQL = modeloSQL.getReadableDatabase();
        Cursor fila = dbSQL.rawQuery("SELECT * FROM Estudiante", null);
        for (int i = 0; i < fila.getCount(); i++){
            Estudiantes estudiantes = new Estudiantes();
            if (fila.moveToPosition(i)){
                cedula = Integer.parseInt(fila.getString(0));
                nombre = fila.getString(1);
                edad = Integer.parseInt(fila.getString(2));
                estudiantes.setCedula(cedula);
                estudiantes.setNombre(nombre);
                estudiantes.setEdad(edad);

                lista.add(i,estudiantes);
                adaptador.notifyDataSetChanged();
            }else {
                Toast.makeText(getContext(), "No se encontraron datos", Toast.LENGTH_SHORT).show();
            }
        }
        dbSQL.close();

        adaptador.setOnItemClickListener(new ItemClickListener() {
            @Override
            public void OnItemClick(int posicion, Estudiantes estudiantes) {
                builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Actualización de datos");
                builder.setCancelable(false);
                View view = LayoutInflater.from(getContext()).inflate(R.layout.dialogo_actualizar,
                        null, false);
                builder.setView(view);
                actualizarDatos(posicion,view);

                dialogo = builder.create();
                dialogo.show();
            }
        });
    }

    private void actualizarDatos(final int posicion, View view){
        tvActualizarCedula = view.findViewById(R.id.tv_ACTCedula);
        edtActualizarNombre = view.findViewById(R.id.edt_ACTNombre);
        edtActualizarEdad = view.findViewById(R.id.edt_ACTEdad);
        btnCancelar = view.findViewById(R.id.btn_ACTCancelar);
        btnActualizar = view.findViewById(R.id.btn_ACTActualizar);

        tvActualizarCedula.setText(lista.get(posicion).getCedula().toString());
        edtActualizarNombre.setText(lista.get(posicion).getNombre().toString());
        edtActualizarEdad.setText(lista.get(posicion).getEdad().toString());

        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cedula = Integer.parseInt(tvActualizarCedula.getText().toString());
                nombre = edtActualizarNombre.getText().toString();
                edad = Integer.parseInt(edtActualizarEdad.getText().toString());

                dbSQL = modeloSQL.getWritableDatabase();
                ContentValues registro = new ContentValues();
                registro.put("ID", cedula);
                registro.put("Nombre", nombre);
                registro.put("Edad", edad);

                int res = dbSQL.update("Estudiante", registro, "ID= " + cedula, null);
                if (res == 1) {
                    Toast.makeText(v.getContext(), "Se actualizaron correctamente los datos",
                            Toast.LENGTH_SHORT).show();
                    Estudiantes estudiantes = new Estudiantes();

                    estudiantes.setCedula(cedula);
                    estudiantes.setNombre(nombre);
                    estudiantes.setEdad(edad);
                    adaptador.actualizarDatos(posicion, estudiantes);
                    dialogo.dismiss();

                } else {
                    Toast.makeText(v.getContext(), "Hubo un error, no se actualizaron los datos",
                            Toast.LENGTH_SHORT).show();
                }
                dbSQL.close();
            }
        });
        btnCancelar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                dialogo.dismiss();
            }
        });
    }
}