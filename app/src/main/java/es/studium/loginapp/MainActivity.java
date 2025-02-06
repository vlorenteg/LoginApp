package es.studium.loginapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // Componentes de GUI
    TextView bienvenido;
    Button btnBorrarCredenciales;

    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Configurar la Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); // Establecer la Toolbar como ActionBar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Mostrar la flecha de retroceso

        // Obtener los valores pasados desde Login
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String nombre = extras.getString("nombre");
            boolean sharedPrefsClicked = extras.getBoolean("sharedPrefsClicked");

            btnBorrarCredenciales = findViewById(R.id.btnBorrarCredenciales);
            // Si el switch fue activado, habilitar el botón de 'borrar credenciales'
            btnBorrarCredenciales.setEnabled(sharedPrefsClicked);

            btnBorrarCredenciales.setOnClickListener(v -> {
                // Obtener la referencia a la colección 'LoginCredentials'
                sharedpreferences = getSharedPreferences("LoginCredentials", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.clear(); // Elimina todas las claves y valores dentro del archivo de preferencias compartidas, pero el archivo en sí permanece.
                editor.apply(); // Aplicar cambios de manera asíncrona

                // Mostrar mensaje de credenciales borradas
                mostrarToast(getString(R.string.credencialesBorradas));

                // Deshabilitar el botón después de borrar credenciales
                btnBorrarCredenciales.setEnabled(false);
            });

            bienvenido = findViewById(R.id.textViewBienvenido);
            String greeting = getResources().getString(R.string.bienvenido) + " " + nombre;
            bienvenido.setText(greeting);
        } else {
            // Manejo de error si no se reciben los valores esperados
            mostrarToast("No se recibieron datos del login");
        }
    }

    // Método para mostrar un Toast en el centro de la pantalla
    private void mostrarToast(String mensaje) {
        Toast toast = Toast.makeText(MainActivity.this, mensaje, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    // Método para manejar el comportamiento de la flecha de retroceso en la Toolbar
    @Override
    public boolean onSupportNavigateUp() {
        // Comportamiento al presionar la flecha de retroceso
        onBackPressed(); // Llama al método onBackPressed() para cerrar la actividad
        return true;
    }
}
