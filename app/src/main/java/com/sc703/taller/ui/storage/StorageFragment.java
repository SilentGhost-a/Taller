package com.sc703.taller.ui.storage;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sc703.taller.R;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import static android.app.Activity.RESULT_OK;

public class StorageFragment extends Fragment {

    private StorageReference almacenamiento;
    private ImageView imvImagen;
    private EditText edtNombreImg;
    private Button btnBuscar, btnCargar, btnDescargar;
    private Uri rutaImg;
    private Integer codigoSolicitud = 1234;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_storage, container, false);
        imvImagen = root.findViewById(R.id.imvImagen);
        edtNombreImg = root.findViewById(R.id.edt_NombreImg);
        btnBuscar = root.findViewById(R.id.btn_Buscar);
        btnCargar = root.findViewById(R.id.btn_Cargar);
        btnDescargar = root.findViewById(R.id.btn_Descargar);

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v){
                buscarArchivo(v);
            }
        });

        btnCargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarArchivo(v);
            }
        });

        btnDescargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                descargarArchivo();
            }
        });
        return root;
    }
    private void buscarArchivo(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(intent.createChooser(intent, "Seleccione una Imagen"), codigoSolicitud);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == codigoSolicitud && resultCode == RESULT_OK && data != null) {
            rutaImg = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), rutaImg);
                imvImagen.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "No se pudo cargar la imagen", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void cargarArchivo(View view){
        almacenamiento = FirebaseStorage.getInstance().getReference().child("Imagenes");
        if (rutaImg != null){
            final ProgressDialog dialog = new ProgressDialog(getContext());
            dialog.setTitle("Cargando Imagen");
            dialog.show();

            StorageReference ref = almacenamiento.child(edtNombreImg.getText().toString());
            ref.putFile(rutaImg)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            dialog.dismiss();
                            Toast.makeText(getContext(), "Imagen cargada correctamente", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialog.dismiss();
                            Toast.makeText(getContext(), "No se pudo cargar la imagen", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            Long progreso = ((100*snapshot.getBytesTransferred())/snapshot.getTotalByteCount());
                            dialog.setMessage("Progreso de carga: " + progreso + "%");
                        }
                    });

        }else {
            Toast.makeText(getContext(), "No se encontró una imagen para cargar", Toast.LENGTH_SHORT).show();
        }
    }

    private void descargarArchivo(){
        almacenamiento = FirebaseStorage.getInstance().getReference().child("Imagenes/" + edtNombreImg.getText().toString());

        File temporal = null;

        try{
            temporal = File.createTempFile("images", ".jpg");
        }catch (IOException e){
            e.printStackTrace();
        }

        final File archivoFinal = temporal;

        almacenamiento.getFile(temporal)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        String archivo = archivoFinal.getAbsolutePath();
                        imvImagen.setImageBitmap(BitmapFactory.decodeFile(archivo));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "No se encontró la imagen deseada", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}