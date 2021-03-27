package com.sc703.taller;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.FontResourcesParserCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;


public class MainActivity extends AppCompatActivity {

    private EditText edt_Correo, edt_Contrasena;
    private FirebaseAuth Autenticador;
    final private int CODIGO_SOLICITUD_PERMISO = 45678;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edt_Contrasena = findViewById(R.id.edt_Contrasena);
        edt_Correo = findViewById(R.id.edt_Correo);

        Autenticador = FirebaseAuth.getInstance();

        solicitarPermisos();

    }
    @Override
    protected void onStart(){
        super.onStart();
        FirebaseUser usuario = Autenticador.getCurrentUser();
        actualizarInterfaz(usuario);
    }

    private void actualizarInterfaz (FirebaseUser usuario){
        if (usuario != null){
            Intent intent = new Intent(this, PrincipalActivity.class);
            startActivity(intent);
        }
    }
    private boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }
    private boolean validarContrasena(String contrasena) {
        final String PATRON_CONTRASENA = "^(?=.*[0-9])(?=.*[a-z])(?=.*[!@#$%^&+=])(?=\\S+$).{4,}$";
        Pattern pattern = Pattern.compile(PATRON_CONTRASENA);
        return pattern.matcher(contrasena).matches();
    }

    public void registro (View view){

        String correo = edt_Correo.getText().toString();
        String contrasena = edt_Contrasena.getText().toString();
        System.out.println(validarContrasena(contrasena));
        if (validarEmail(correo)){
            if (!TextUtils.isEmpty(contrasena) && validarContrasena(contrasena) && contrasena.length() >= 6){
                Autenticador.createUserWithEmailAndPassword(correo,contrasena)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                System.out.println(task.isComplete());
                                if (task.isSuccessful()) {
                                    FirebaseUser usuario = Autenticador.getCurrentUser();
                                    actualizarInterfaz(usuario);
                                } else {
                                    Toast.makeText(getApplicationContext(), R.string.toast_failed_new_user,
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }else if (contrasena.length() < 6) {
                Toast.makeText(getApplicationContext(), R.string.toast_pwd_min1,
                        Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getApplicationContext(), R.string.toast_pwd_min1,
                        Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(getApplicationContext(), R.string.toast_invalid_email,
                    Toast.LENGTH_LONG).show();
        }
    }
    public void ingreso (View view){

        String correo = edt_Correo.getText().toString();
        String contrasena = edt_Contrasena.getText().toString();

        Autenticador.signInWithEmailAndPassword(correo,contrasena)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (validarEmail(correo)){
                            if (task.isSuccessful()) {
                                FirebaseUser usuario = Autenticador.getCurrentUser();
                                actualizarInterfaz(usuario);
                            }else {
                                Toast.makeText(getApplicationContext(), R.string.toast_login_failed,
                                        Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Toast.makeText(getApplicationContext(), R.string.toast_invalid_email,
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void solicitarPermisos(){
        int permisoGPS = ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
        int permisoTelefono = ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE);
        int permisoInternet = ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.INTERNET);
        int permisoCamara = ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA);
        int permisoEscrituraAlmacenamiento = ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permisoLecturaAlmacenamiento = ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permisoGPS != PackageManager.PERMISSION_GRANTED ||
                permisoTelefono != PackageManager.PERMISSION_GRANTED || permisoInternet != PackageManager.PERMISSION_GRANTED ||
                permisoCamara != PackageManager.PERMISSION_GRANTED || permisoEscrituraAlmacenamiento != PackageManager.PERMISSION_GRANTED ||
                permisoLecturaAlmacenamiento != PackageManager.PERMISSION_GRANTED){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.CALL_PHONE,
                        Manifest.permission.INTERNET,
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE}, CODIGO_SOLICITUD_PERMISO);
            }

        }
    }
}