package com.example.ventas_vehiculos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class FacturaActivity extends AppCompatActivity {

    EditText etcodigo, ettime, etplaca, tvmarca, tvmodelo, tvvalor;
    CheckBox cbactivo;
    ClsOpenHelper admin = new ClsOpenHelper(this, "consesionario.db", null, 1);
    long resp;
    String codigo, time, placa;
    int sw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_factura);

        etcodigo=findViewById(R.id.etcodigo);
        ettime=findViewById(R.id.ettime);
        etplaca=findViewById(R.id.etplaca);
        cbactivo=findViewById(R.id.cbactivo);
        sw=0;
    }

    public void Guardar (View view){
        codigo=etcodigo.getText().toString();
        time=ettime.getText().toString();
        placa=etplaca.getText().toString();

        if (codigo.isEmpty() || time.isEmpty()){

            Toast.makeText(this, "Todos los datos son requeridos", Toast.LENGTH_SHORT).show();
            etplaca.requestFocus();
        }else{
            SQLiteDatabase db = admin.getWritableDatabase();
            ContentValues registrar = new ContentValues();
            ContentValues resgistarVehiculo = new ContentValues();

            if (sw==0){
                registrar.put("cod_factura",codigo);
                registrar.put("fecha",time);
                registrar.put("placa",placa);
                resp = db.insert("TblFactura", null,registrar);
            }else{
                Toast.makeText(this, "No existe un vehiculo con esa placa", Toast.LENGTH_SHORT).show();
                sw=0;
            }

            if (resp>0){
                Toast.makeText(this, "Factura guardada", Toast.LENGTH_SHORT).show();
                resgistarVehiculo.put("activo", "no");
                db.update("TblVehiculo",resgistarVehiculo,"placa='"+placa+"'",null);
                Limpiar_campos();
            }
            else
                Toast.makeText(this, "Error al guardar la Factura", Toast.LENGTH_SHORT).show();
            db.close();
        }
    }


    public void Buscar(View view){

        placa = etplaca.getText().toString();
        if (placa.isEmpty()){
            Toast.makeText(this, "La placa esta requerida", Toast.LENGTH_SHORT).show();
            etplaca.requestFocus();
        }
        else{
            SQLiteDatabase db = admin.getWritableDatabase();
            Cursor fila = db.rawQuery("select * from TblVehiculo where placa ='" + placa + "'",null);
            //solo se colocan comillas simples cuando son texto.
            if (fila.moveToNext()) {
                sw=1;
                tvvalor.setText(fila.getString(1));
                tvmodelo.setText(fila.getString(2));
                tvmarca.setText(fila.getString(3));
               /* if (fila.getString(4).equals("si")){
                    etcbactivo.setChecked(true);
                }else{
                    etcbactivo.setChecked(false);
                }*/

            }
            else {
                etplaca.setText("");
                Toast.makeText(this, "Vehiculo no registrado", Toast.LENGTH_SHORT).show();
                tvmarca.setText("");
                tvmodelo.setText("");
                tvvalor.setText("");
            }
        }

    }


    public void Consultar(View view){
        codigo = etcodigo.getText().toString();
        if (codigo.isEmpty()){
            Toast.makeText(this, "El codigo es requerido", Toast.LENGTH_SHORT).show();
            etcodigo.requestFocus();
        }
        else{
            SQLiteDatabase db = admin.getReadableDatabase();
            Cursor fila = db.rawQuery("select * from TblFactura where cod_factura ='" + codigo + "'",null);
            //solo se colocan comillas simples cuando son texto.
            if (fila.moveToNext()) {
                sw=1;
                etcodigo.setText(fila.getString(0));
                ettime.setText(fila.getString(1));
                etplaca.setText(fila.getString(2));
                if (fila.getString(3).equals("si")){
                    cbactivo.setChecked(true);
                }else{
                    cbactivo.setChecked(false);
                }

            }
            else
                Toast.makeText(this, "Factura no registrada", Toast.LENGTH_SHORT).show();
            db.close();
        }
    }

    public void Anular(View view){
        if (sw == 0){
            Toast.makeText(this, "Primero debe de consultar", Toast.LENGTH_SHORT).show();
            etcodigo.requestFocus();
        }else{
            SQLiteDatabase db = admin.getWritableDatabase();
            ContentValues registro=new ContentValues();
            registro.put("activo","no");
            resp=db.update("TblFactura",registro,"cod_factura='"+codigo+ "'",null);
            if (resp>0){
                Toast.makeText(this, "Registro Anulado", Toast.LENGTH_SHORT).show();
                Limpiar_campos();
            }
            else{
                Toast.makeText(this, "Error anulando Factura", Toast.LENGTH_SHORT).show();
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
        etcodigo.setText("");
        ettime.setText("");
        cbactivo.setChecked(false);
        etplaca.setText("");
        etcodigo.requestFocus();
        sw=0;
    }
}