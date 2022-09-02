package com.example.ventas_vehiculos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void Vehiculo(View view){
        Intent intvehiculo = new Intent(this,VehiculoActivity.class);
        startActivity(intvehiculo);
    }

    public void Factura(View view){
        Intent intfactura = new Intent(this,FacturaActivity.class);
        startActivity(intfactura);
    }
}