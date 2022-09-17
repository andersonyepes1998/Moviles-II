package com.example.ventas_vehiculos;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class muestraCarro extends AppCompatActivity {


    EditText  etmarca, etmodelo, etvalor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muestra_carro);

        etmarca=findViewById(R.id.tvmarca);
        etmodelo=findViewById(R.id.tvmodelo);
        etvalor=findViewById(R.id.tvvalor);


        Bundle recibeDatos = getIntent().getExtras();
        String info = recibeDatos.getString("KeyDatos");
        etmarca.setText(info);
    }



    public void Regreso(View view){

    }
}