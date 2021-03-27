package com.sc703.taller.ui.info;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.sc703.taller.R;
public class InfoFragment extends Fragment {

    Button btnLlamarMinistra, btnLlamarTecnica, btnLlamarPrimerCiclo, btnLlamarTercerCiclo,
            btnCorreoMinistra, btnCorreoTecnica, btnCorreoPrimerCiclo, btnCorreoTercerCiclo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_info, container, false);

        btnLlamarMinistra = root.findViewById(R.id.btn_LlamarMinistra);
        btnLlamarTecnica = root.findViewById(R.id.btn_LlamarTecnica);
        btnLlamarPrimerCiclo = root.findViewById(R.id.btn_Llamar1er2doCiclo);
        btnLlamarTercerCiclo = root.findViewById(R.id.btn_Llamar3er4toCiclo);
        btnCorreoMinistra = root.findViewById(R.id.btn_CorreoMinistra);
        btnCorreoTecnica = root.findViewById(R.id.btn_CorreoTecnica);
        btnCorreoPrimerCiclo = root.findViewById(R.id.btn_Correo1er2doCiclo);
        btnCorreoTercerCiclo = root.findViewById(R.id.btn_Correo3er4toCiclo);

        btnLlamarMinistra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:22568132"));
                startActivity(intent);
            }
        });
        btnLlamarTecnica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:22219130"));
                startActivity(intent);
            }
        });
        btnLlamarPrimerCiclo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:22217685"));
                startActivity(intent);
            }
        });
        btnLlamarTercerCiclo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:22214993"));
                startActivity(intent);
            }
        });
        btnCorreoMinistra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] to = {"despachoministra@mep.go.cr"};
                String subject = "Asunto del correo";
                String mail = "Mensaje del correo";

                Intent correo = new Intent(Intent.ACTION_SEND);
                correo.setData(Uri.parse("mailto:"));
                correo.setType("text/plain");
                correo.putExtra(Intent.EXTRA_SUBJECT, subject);
                correo.putExtra(Intent.EXTRA_EMAIL, to);
                correo.putExtra(Intent.EXTRA_TEXT, mail);

                try {
                    startActivity(Intent.createChooser(correo, "Enviar Correo"));
                } catch (android.content.ActivityNotFoundException e) {
                    Toast.makeText(getContext(), "No existe ninguna app de correo",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnCorreoTecnica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] to = {"detce@mep.go.cr"};
                String subject = "Asunto del correo";
                String mail = "Mensaje del correo";

                Intent correo = new Intent(Intent.ACTION_SEND);
                correo.setData(Uri.parse("mailto:"));
                correo.setType("text/plain");
                correo.putExtra(Intent.EXTRA_SUBJECT, subject);
                correo.putExtra(Intent.EXTRA_EMAIL, to);
                correo.putExtra(Intent.EXTRA_TEXT, mail);

                try {
                    startActivity(Intent.createChooser(correo, "Enviar Correo"));
                } catch (android.content.ActivityNotFoundException e) {
                    Toast.makeText(getContext(), "No existe ninguna app de correo",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnCorreoPrimerCiclo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] to = {"primero.segundo.ciclos@mep.go.cr"};
                String subject = "Asunto del correo";
                String mail = "Mensaje del correo";

                Intent correo = new Intent(Intent.ACTION_SEND);
                correo.setData(Uri.parse("mailto:"));
                correo.setType("text/plain");
                correo.putExtra(Intent.EXTRA_SUBJECT, subject);
                correo.putExtra(Intent.EXTRA_EMAIL, to);
                correo.putExtra(Intent.EXTRA_TEXT, mail);

                try {
                    startActivity(Intent.createChooser(correo, "Enviar Correo"));
                } catch (android.content.ActivityNotFoundException e) {
                    Toast.makeText(getContext(), "No existe ninguna app de correo",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnCorreoTercerCiclo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] to = {"tercerciclodiversi@mep.go.cr"};
                String subject = "Asunto del correo";
                String mail = "Mensaje del correo";

                Intent correo = new Intent(Intent.ACTION_SEND);
                correo.setData(Uri.parse("mailto:"));
                correo.setType("text/plain");
                correo.putExtra(Intent.EXTRA_SUBJECT, subject);
                correo.putExtra(Intent.EXTRA_EMAIL, to);
                correo.putExtra(Intent.EXTRA_TEXT, mail);

                try {
                    startActivity(Intent.createChooser(correo, "Enviar Correo"));
                } catch (android.content.ActivityNotFoundException e) {
                    Toast.makeText(getContext(), "No existe ninguna app de correo",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        return root;
    }
}