package com.example.parche_ud.auth
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.parche_ud.MainActivity
import com.example.parche_ud.R
import com.example.parche_ud.databinding.ActivityLoginBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.concurrent.TimeUnit

class LoginActivity : AppCompatActivity() {
    private lateinit var binding:ActivityLoginBinding

    //Variable que me ayuda a autenticar el usuario
    val auth = FirebaseAuth.getInstance()

    //Me maneja el id del usuario
    private var verificarID : String? = null

    //dialogo e alerta
    private  lateinit var dialogo : AlertDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Crea una alerta de dialogo con su constructor que carga el layout de carga no se puede cancelar
        dialogo = AlertDialog.Builder(this).setView(R.layout.cargar_layout).setCancelable(false).create()


        //Me ayuda a enviar el codigo a la funcion que verifica el numero
        binding.enviarCodigo.setOnClickListener{
            if(binding.numeroUsuario.text!!.isEmpty()){
                val mensaje = getString(R.string.mensaje3)
                binding.numeroUsuario.error = mensaje//Mensaje de error
            }else{
                enviarCodigo(binding.numeroUsuario.text.toString())//envia el numero de telefono a la funcion
            }
        }

        //Me ayuda a enviar el codigo a la funcion que verifica el codigo
        binding.verificarCodigo.setOnClickListener{
            if(binding.usuarioCodigo.text!!.isEmpty()){
                val mensaje = getString(R.string.mensaje4)
                binding.usuarioCodigo.error = mensaje//Mensaje de error
            }else{
                verificarCodigo(binding.usuarioCodigo.text.toString())//envia el codigo a la funcion
            }

        }
    }

    //esta función se utiliza para verificar el código de verificación proporcionado
    // por el usuario y autenticarlo en la aplicación de Android utilizando Firebase Authentication.
    private fun verificarCodigo(codigo: String) {
//        binding.sendOtp.showLoadingButton()

        dialogo.show()
        val credential = PhoneAuthProvider.getCredential(verificarID!!, codigo)//Crea un objeto PhoneAuthCredential
        signInWithPhoneAuthCredential(credential)
    }

    private fun enviarCodigo(numero: String) {

//        binding.sendOtp.showLoadingButton() //metodo del boton de github

        dialogo.show()
        //Envía un código de verificación al teléfono del usuario
        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            //Se llama cuando se completa la verificación automáticamente para autenticar
            //al usuario con las credenciales de verificación de teléfono proporcionadas.
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
//                binding.sendOtp.showNormalButton()

                //dialogo.dismiss()
                signInWithPhoneAuthCredential(credential)
            }

            //Se llama cuando se produce un error durante el proceso de verificación.
            override fun onVerificationFailed(e: FirebaseException) {

            }

            //Se llama cuando se ha enviado el código de verificación al número de teléfono
            // del usuario
            override fun onCodeSent(
                verificarID: String,//es el ID de la verificación que se ha enviado al número de teléfono
                token: PhoneAuthProvider.ForceResendingToken//que se utiliza para volver a enviar el código de verificación si el usuario no lo ha recibido
            ) {
                this@LoginActivity.verificarID = verificarID

                dialogo.dismiss()
//                binding.sendOtp.showNormalButton()
                binding.numeroLayout.visibility = GONE
                binding.codigoLayout.visibility = VISIBLE
            }
        }

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber("+57$numero")  //establece el número de teléfono que se va a verificar.
            .setTimeout(60L, TimeUnit.SECONDS) //establece el tiempo límite en segundos para la verificación
            .setActivity(this) //establece la actividad actual como la actividad a la que se le devolverá el resultado de la verificación.
            .setCallbacks(callbacks) //e utilizará para recibir actualizaciones del estado de la verificación.
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)//e utiliza para iniciar la verificación del número de teléfono con las opciones configuradas anteriormente.
    }


    //Acceso del usuario
    //iniciar sesión en Firebase usando las credenciales de autenticación de teléfono
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->//se verifica si la operación de inicio de sesión fue exitosa
//                binding.sendOtp.showNormalButton()

                if (task.isSuccessful) {

                    verificarUsuarioExiste(binding.numeroUsuario.text.toString())

                } else {

                    dialogo.dismiss()
                    Toast.makeText(this, task.exception!!.message, Toast.LENGTH_SHORT).show()//Mensaje de error si no es correcta la autenticacion
                }
            }
    }

    //Me ayuda a ver si es correcta la autenticacion
    private fun verificarUsuarioExiste(numero: String) {

        FirebaseDatabase.getInstance().getReference("usuarios").child("+57"+numero)//Revisa en la tabla usuarios si existe el numero que el usuario ingreso
            .addValueEventListener(object : ValueEventListener{

                //Error en la base d datos
                override fun onCancelled(error: DatabaseError) {
                    dialogo.dismiss()
                    Toast.makeText(this@LoginActivity,error.message,Toast.LENGTH_SHORT).show()
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        //dialogo.dismiss()
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))//Si existe en la base de datos ingresa a la aplicacion
                        finish()
                    }
                    else{
                        startActivity(Intent(this@LoginActivity,RegistroActivity::class.java))//Si no existe en la base de datos ingresa al Registro de la aplicacion
                        finish()
                    }
                }
            })
    }
}