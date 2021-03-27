package com.sc703.taller.ui.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class ModeloSQL extends SQLiteOpenHelper {

    public ModeloSQL(@Nullable Context contexto, @Nullable String nombreBD, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(contexto, nombreBD, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Estudiante(Id INT PRIMARY KEY, Nombre VARCHAR(150),Edad INT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
