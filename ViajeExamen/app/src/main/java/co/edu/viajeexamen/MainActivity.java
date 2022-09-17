package co.edu.viajeexamen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText etcodigo, etdestino, etpersona, etvalor;
    OpenHelper admin=new OpenHelper(this,"Viaje.db",null, 1);
    String codigo, destino,persona,valor;
    long resp;
    int sw;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etcodigo=findViewById(R.id.etcodigo);
        etdestino=findViewById(R.id.etdestino);
        etpersona=findViewById(R.id.etpersona);
        etvalor=findViewById(R.id.etvalor);
        sw=0;

    }
    public void Guardar(View view){
        codigo=etcodigo.getText().toString();
        destino=etdestino.getText().toString();
        persona=etpersona.getText().toString();
        valor=etvalor.getText().toString();
        if(codigo.isEmpty() || destino.isEmpty() ||
                persona.isEmpty() || valor.isEmpty()){
            Toast.makeText(this, "Todos los datos son requeridos", Toast.LENGTH_SHORT).show();
            etcodigo.requestFocus();
        }
        else{
            SQLiteDatabase db = admin.getWritableDatabase();
            ContentValues registro = new ContentValues();
            registro.put("cod_viaje",codigo);
            registro.put("destino",destino);
            registro.put("personas",persona);
            registro.put("valor",Integer.parseInt(valor));
            if (sw== 0)
                resp = db.insert("TblViaje",null,registro);
            else{
                resp = db.update("TblViaje",registro, "cod_viaje='"+codigo+"'", null);
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
        codigo = etcodigo.getText().toString();
        if (codigo.isEmpty()){
            Toast.makeText(this, "El codigo esta requerido", Toast.LENGTH_SHORT).show();
            etcodigo.requestFocus();
        }
        else{
            SQLiteDatabase db = admin.getReadableDatabase();
            Cursor fila = db.rawQuery("select * from TblViaje where cod_viaje ='" + codigo + "'",null);
            //solo se colocan comillas simples cuando son texto.
            if (fila.moveToNext()) {
                sw=1;
                etdestino.setText(fila.getString(1));
                etpersona.setText(fila.getString(2));
                etvalor.setText(fila.getString(3));
             /*   if (fila.getString(4).equals("si")){
                    etcbactivo.setChecked(true);
                }else{
                    etcbactivo.setChecked(false);
                } */

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
            resp=db.delete("TblViaje","cod_viaje='"+codigo+ "'",null);
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


    private void Limpiar_campos(){
        etcodigo.setText("");
        etdestino.setText("");
        etpersona.setText("");
        etvalor.setText("");
        etcodigo.requestFocus();
        sw=0;
    }

}