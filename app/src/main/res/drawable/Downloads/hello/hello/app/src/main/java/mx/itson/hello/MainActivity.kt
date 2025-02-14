package mx.itson.hello

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        var nombre
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
//val se refiere a una especie de variable que es de solo lectura y que solo puede asignarse un valor una vez es lo que conocemos como constante en java
        val botonAceptar = findViewById<View>(R.id.boton_aceptar) as Button
        botonAceptar.setOnClickListener(this)

    }

    override fun onClick(view: View) {
    when(view.id){
        R.id.boton_aceptar ->{

        }
    }
    }
}

