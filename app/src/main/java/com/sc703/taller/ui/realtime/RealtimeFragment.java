package com.sc703.taller.ui.realtime;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sc703.taller.R;

public class RealtimeFragment extends Fragment {

    TextView tv_realtime;
    private FirebaseDatabase fDatabase = FirebaseDatabase.getInstance();

    private DatabaseReference bdRef = fDatabase.getReference();

    private DatabaseReference noticia = bdRef.child("Noticias");

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_realtime, container, false);
        tv_realtime = root.findViewById(R.id.tv_realtime);
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();

        noticia.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String texto = "Este es el texto de la base de datos: " + snapshot.getValue().toString();
                tv_realtime.setText(texto);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                String texto = "No se pudo conectar a la base de datos";
                tv_realtime.setText(texto);
            }
        });
    }
}