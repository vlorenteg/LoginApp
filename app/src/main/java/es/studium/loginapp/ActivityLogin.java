package es.studium.loginapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ActivityLogin extends AppCompatActivity implements View.OnClickListener {

    // componentes de GUI
    EditText editTextUsuario, editTextClave;
    Button btnAcceder;
    SwitchCompat switchGuardarCredenciales;

    // nombre de las preferencias compartidas
    public static final String LoginCredentials = "LoginCredentials";
    // nombres de las claves en las preferencias compartidas
    public static final String Usuario = "usuarioKey";
    public static final String Clave = "claveKey";
    SharedPreferences sharedpreferences;
    // datos fijos: nombre del usuario y el número DNI
    String nombre = "Valentin";
    String dni = "29507228E";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // obtener una referencia a la colección de preferencias compartidas
        sharedpreferences = getSharedPreferences(LoginCredentials, Context.MODE_PRIVATE);

        // obtener los valores del usuario y la clave
        String isSharedUsuario = sharedpreferences.getString(Usuario, "");
        String isSharedClave = sharedpreferences.getString(Clave, "");

        // si el nombre y la clave ya están guardadas en las preferencias compartidas
        if ((isSharedUsuario.equals(nombre)) && (isSharedClave.equals(dni))) {
            // pasar a la ventana principal a través del Intent
            Intent intentMain = new Intent(this, MainActivity.class);
            intentMain.putExtra("nombre", nombre);
            intentMain.putExtra("sharedPrefsClicked", true);
            startActivity(intentMain);
        }
        else {
            // establecer el contenido de la vista de login
            setContentView(R.layout.activity_login);
            // enlazar los elementos del código con los de la vista
            editTextUsuario = findViewById(R.id.username);
            editTextClave = findViewById(R.id.clave);
            switchGuardarCredenciales = findViewById(R.id.switchSaveCredentials);
            switchGuardarCredenciales.setChecked(false);
            btnAcceder = findViewById(R.id.btnAcceder);
            btnAcceder.setEnabled(false);
            btnAcceder.setOnClickListener(this);

            // objeto textWatcher nos permite supervisar cuando el contenido de los editTexts cambia
            TextWatcher textWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                // si ocurre un cambio dentro de los editText
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    // si los editTexts no están vacíos - se habilita el botón 'Acceder'
                    if (!(editTextUsuario.getText().toString().isBlank()) && !(editTextClave.getText().toString().isBlank())) {
                        btnAcceder.setEnabled(true);
                    } else {
                        btnAcceder.setEnabled(false);
                    }
                }
                @Override
                public void afterTextChanged(Editable s) {}
            };

            editTextUsuario.addTextChangedListener(textWatcher);
            editTextClave.addTextChangedListener(textWatcher);
        }
    }

    @Override
    public void onClick(View v) {
        String usuario = editTextUsuario.getText().toString();
        String clave = editTextClave.getText().toString();

        // comprobar si los campos están vacíos antes de comparar
        if (usuario.isEmpty() || clave.isEmpty()) {
            showErrorDialog("Por favor, ingrese tanto el usuario como la clave.");
            return;
        }

        // comprobar credenciales introducidas con los datos fijos
        if (usuario.equals(nombre) && clave.equals(dni)) {
            // si el switch está activado
            if (switchGuardarCredenciales.isChecked()) {
                // guardar los valores en las preferencias compartidas
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(Usuario, usuario);
                editor.putString(Clave, clave);
                editor.apply();
            }

            // pasar a la pantalla principal a través de Intent
            Intent intentMain = new Intent(this, MainActivity.class);
            // guardar los valores del nombre del usuario y un booleano (true si el switch está activado)
            intentMain.putExtra("nombre", usuario);
            intentMain.putExtra("sharedPrefsClicked", switchGuardarCredenciales.isChecked());
            startActivity(intentMain);
        } else {
            // mostrar un diálogo de credenciales incorrectas
            showErrorDialog("Credenciales incorrectas");
        }
    }

    // Método para mostrar un diálogo con mensaje de error
    private void showErrorDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
