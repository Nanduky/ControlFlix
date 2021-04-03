package com.nanduky.controlflix;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.nanduky.controlflix.utilidades.Utilidades;

/**
 * Creado por nanduky 25/10/2019
 */

public class ConexionSQliteHelper extends SQLiteOpenHelper {


    public ConexionSQliteHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {

        //Crear la tabla fichaMensual
        db.execSQL(Utilidades.CREAR_TABLA_FICHAMENSUAL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAntigua, int versionNueva) {

        if(versionAntigua == 1 && versionNueva == 3){
            db.execSQL(Utilidades.CREAR_TABLA_FICHAMENSUAL_V3_1);
            db.execSQL(Utilidades.CREAR_TABLA_FICHAMENSUAL_V3_2);
        }
        else {
            //Si hay una tabla con una version antigua, se borra y se vuelve a crear
            db.execSQL("DROP TABLE IF EXISTS fichaMensual ");
            onCreate(db);
        }
    }
}
