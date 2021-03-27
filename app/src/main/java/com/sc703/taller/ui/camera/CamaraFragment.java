package com.sc703.taller.ui.camera;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.sc703.taller.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static java.lang.Math.round;

public class CamaraFragment extends Fragment {
    private static final int SOLICITUD_CAMARA = 1888;
    private ImageView imageView;
    private String ubicacionImagen;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_camara, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tomarFoto();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == SOLICITUD_CAMARA && resultCode == -1)
        {
            guardarImagen();
        }
    }
    private void tomarFoto() {
        Intent tomarFotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (tomarFotoIntent.resolveActivity(getContext().getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = crearImagen();
                System.out.println(photoFile);
            } catch (IOException ex) {
                Toast.makeText(getContext(), "No se pudo guardar el archivo", Toast.LENGTH_SHORT).show();
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getContext(),
                        "com.sc703.taller.fileprovider",
                        photoFile);
                tomarFotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(tomarFotoIntent, SOLICITUD_CAMARA);

            }
        }
    }

    private File crearImagen() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        ubicacionImagen = image.getAbsolutePath();
        return image;
    }
    private void guardarImagen() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(ubicacionImagen);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        getContext().sendBroadcast(mediaScanIntent);
    }
}
