package dam.movil.project1

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import dam.movil.project1.databinding.ActivityMainBinding

// ":"  indica que MainActivity extiende a AppCompactActivity
class MainActivity : AppCompatActivity() {

    //hay que incluir el binding en el gradle de la app
    private val binding : ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    //OnCreate...mirar esquema del ciclo de vida de una activity (toml)
    // https://www.javatpoint.com/android-life-cycle-of-activity
    override fun onCreate(savedInstanceState: Bundle?) {
        //savedInstanceState se usa para guardar datos ante posibles destrucciones del activity
        super.onCreate(savedInstanceState)
        with(binding) {
            // esto sirve para poner la activity a pantalla completa
            enableEdgeToEdge()

            //Y esto carga el XML que define la vista
            //setContentView(R.layout.activity_main)
            setContentView(root)
            //cuando volvamos a la pantalla...la ponemos en pantalla completa
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
        }

    }
}