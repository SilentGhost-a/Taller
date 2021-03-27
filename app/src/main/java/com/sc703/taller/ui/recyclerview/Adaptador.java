package com.sc703.taller.ui.recyclerview;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sc703.taller.R;
import com.sc703.taller.ui.sqlite.ModeloSQL;

import java.util.List;

public class Adaptador extends RecyclerView.Adapter<Adaptador.Holder> {

    private List<Estudiantes> lista;
    private ItemClickListener itemClickListener;
    private ModeloSQL modeloSQL;
    private SQLiteDatabase dbSQL;
    private AlertDialog.Builder builder;
    private AlertDialog dialogo;
    private Button btnEliminar,btnCancelar;

    public Adaptador(List<Estudiantes> lista){
        this.lista = lista;
    }

    public static class Holder extends RecyclerView.ViewHolder{
        TextView tvCedula, tvNombre, tvEdad, tvEliminar;

        public Holder(View item){
            super(item);
            tvCedula = item.findViewById(R.id.tv_Item_Cedula);
            tvNombre = item.findViewById(R.id.tv_Item_Nombre);
            tvEdad = item.findViewById(R.id.tv_Item_Edad);
            tvEliminar = item.findViewById(R.id.tv_Item_Eliminar);
        }
    }

    @NonNull
    @Override
    public Adaptador.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item_row = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false);
        return new Holder(item_row);
    }

    @Override
    public void onBindViewHolder(@NonNull Adaptador.Holder holder, int position) {
        final Estudiantes estudiantes = lista.get(position);

        holder.tvCedula.setText("Cédula: " + String.format(String.valueOf(Integer.parseInt(
                estudiantes.getCedula().toString()))));
        holder.tvNombre.setText("Nombre: " + String.format(String.valueOf(estudiantes.getNombre())));
        holder.tvEdad.setText("Edad: " + String.format(String.valueOf(Integer.parseInt(
                estudiantes.getEdad().toString()))));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.OnItemClick(position, estudiantes);
            }
        });

        holder.tvEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Eliminar datos");
                builder.setCancelable(false);
                View view = LayoutInflater.from(v.getContext()).inflate(R.layout.dialogo_eliminar,
                        null, false);
                builder.setView(view);

                dialogo = builder.create();
                dialogo.show();

                btnCancelar = view.findViewById(R.id.btn_ELICancelar);
                btnEliminar = view.findViewById(R.id.btn_ELIEliminar);

                btnEliminar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        modeloSQL = new ModeloSQL(v.getContext(), "EstudianteBD", null, 1);
                        dbSQL = modeloSQL.getWritableDatabase();

                        int cedula = Integer.parseInt(estudiantes.getCedula().toString());
                        int res = dbSQL.delete("Estudiante", "ID= " + cedula, null);

                        if (res == 1) {
                            Toast.makeText(v.getContext(), "Se eliminaron correctamente los datos",
                                    Toast.LENGTH_SHORT).show();
                            lista.remove(position);
                            dialogo.dismiss();

                        } else {
                            Toast.makeText(v.getContext(), "Hubo un error, no se eliminaron los datos",
                                    Toast.LENGTH_SHORT).show();
                            dialogo.dismiss();
                        }

                        dbSQL.close();
                        notifyDataSetChanged();
                    }
                });
                btnCancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogo.dismiss();
                    }
                });
            }
        });
    }
    @Override
    public int getItemCount() {
        return lista.size();
    }

    public void setOnItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;

    }

    public void actualizarDatos(int posicion, Estudiantes estudiantes){
        lista.remove(posicion);
        lista.add(posicion,estudiantes);
        notifyDataSetChanged();
    }

}
