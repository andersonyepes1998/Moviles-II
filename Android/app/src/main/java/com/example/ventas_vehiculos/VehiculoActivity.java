package com.example.ventas_vehiculos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class VehiculoActivity extends AppCompatActivity {


    EditText etplaca, etmarca, etmodelo, etvalor;
    CheckBox etcbactivo;
    //conectar base de datos sql
    ClsOpenHelper admin = new ClsOpenHelper(this, "consesionario.db", null, 1);
    long resp;
    String placa, marca, modelo, valor;
    int sw;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehiculo);

        //ocultar titulo que esta en morado.

        getSupportActionBar().hide();

        //este es para oculatar el titulo morado

        etplaca=findViewById(R.id.etplaca);
        etmarca=findViewById(R.id.etmarca);
        etmodelo=findViewById(R.id.etmodelo);
        etvalor=findViewById(R.id.etvalor);
        etcbactivo=findViewById(R.id.etcbactivo);
        sw=0;
    }

    public void Guardar(View view){
        placa=etplaca.getText().toString();
        marca=etmarca.getText().toString();
        modelo=etmodelo.getText().toString();
        valor=etvalor.getText().toString();
        if(placa.isEmpty() || marca.isEmpty() ||
        modelo.isEmpty() || valor.isEmpty()){
            Toast.makeText(this, "Todos los datos son requeridos", Toast.LENGTH_SHORT).show();
            etplaca.requestFocus();
        }
        else{
            SQLiteDatabase db = admin.getWritableDatabase();
            ContentValues registro = new ContentValues();
            registro.put("placa",placa);
            registro.put("marca",marca);
            registro.put("modelo",modelo);
            registro.put("valor",Integer.parseInt(valor));
            if (sw== 0)
                resp = db.insert("TblVehiculo",null,registro);
            else{
                resp = db.update("TblVehiculo",registro, "placa='"+placa+"'", null);
                sw=0;
            }

            if (resp > 0){
                Toast.makeText(this,"registro guardado", Toast.LENGTH_SHORT).show();
                Limpiar_campos();
            }
            else
                Toast.makeText(this, "Error guardando registro", Toast.LENGTH_SHORT).show();
            db.close();
        }
    }
    public void Consultar(View view){
        placa = etplaca.getText().toString();
        if (placa.isEmpty()){
            Toast.makeText(this, "La placa esta requerida", Toast.LENGTH_SHORT).show();
            etplaca.requestFocus();
        }
        else{
            SQLiteDatabase db = admin.getReadableDatabase();
            Cursor fila = db.rawQuery("select * from TblVehiculo where placa ='" + placa + "'",null);
                    //solo se colocan comillas simples cuando son texto.
            if (fila.moveToNext()) {
                sw=1;
                etmarca.setText(fila.getString(1));
                etmodelo.setText(fila.getString(2));
                etvalor.setText(fila.getString(3));
                if (fila.getString(4).equals("si")){
                    etcbactivo.setChecked(true);
                }else{
                    etcbactivo.setChecked(false);
                }

            }
            else
                Toast.makeText(this, "Vehiculo no registrado", Toast.LENGTH_SHORT).show();
            db.close();
        }
    }

    public void Anular(View view){
        if (sw == 0){
            Toast.makeText(this, "Primero debe de consultar", Toast.LENGTH_SHORT).show();
            etplaca.requestFocus();
        }else{
            SQLiteDatabase db = admin.getWritableDatabase();
            ContentValues registro=new ContentValues();
            registro.put("activo","no");
            resp=db.update("TblVehiculo",registro,"placa='"+placa+ "'",null);
            if (resp>0){
                Toast.makeText(this, "Registro Anulado", Toast.LENGTH_SHORT).show();
                Limpiar_campos();
            }
            else{
                Toast.makeText(this, "Error anulando registro", Toast.LENGTH_SHORT).show();
                db.close();
            }
        }
    }

    public void Cancelar(View view){
        Limpiar_campos();
    }

    public void Regresar(View view){
        Intent intmain = new Intent(this,MainActivity.class);
        startActivity(intmain);
    }

    private void Limpiar_campos(){
        etplaca.setText("");
        etmarca.setText("");
        etmodelo.setText("");
        etvalor.setText("");
        etcbactivo.setChecked(false);
        etplaca.requestFocus();
        sw=0;
    }
}