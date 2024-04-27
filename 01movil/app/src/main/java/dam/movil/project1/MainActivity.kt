package dam.movil.project1


import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import dam.movil.project1.databinding.ActivityMainBinding

//private const val TEXT_VIEW = "text_view"

// ":"  indica que MainActivity extiende a AppCompactActivity
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {




    //hay que incluir el binding en el gradle de la app
    private val binding : ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
/*
    private val viewModel: MainViewModel by viewModels {
        MainViewModelFactory()
    }
    Al inyectar....*/
    private val viewModel: MainViewModel by viewModels ()

    //OnCreate...mirar esquema del ciclo de vida de una activity (toml)
    // https://www.javatpoint.com/android-life-cycle-of-activity

    override fun onCreate(savedInstanceState: Bundle?) {
        //savedInstanceState se usa para guardar datos ante posibles destrucciones del activity
        super.onCreate(savedInstanceState)

        // esto sirve para poner la activity a pantalla completa
        enableEdgeToEdge()

        with(binding) {

            //Y esto carga el XML que define la vista
            //setContentView(R.layout.activity_main)
            setContentView(root)
            //cuando volvamos a la pantalla...la ponemos en pantalla completa
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }

            //al usar el ViewModel esto ya no es necesario.
            // COn el viewmodel los valores no se pierden al girar la pantalla
            /*
            savedInstanceState?.let {
                text1.text = savedInstanceState.getString(TEXT_VIEW)
            }
            */

            buttonSumar.setOnClickListener {
                //al usar el ViewModel esta lógica ya no va aquí.
                /*val value = text1.text.toString().toInt()
                text1.text = (value + 1).toString()*/

                //hay que llamar al los métodos espejos del ViewModel
                //viewModel.sumar()
                viewModel.handleEvent(MainEvent.Sumar(1))

            }

            buttonRestar.setOnClickListener {
                viewModel.handleEvent(MainEvent.Restar(1))
            }
        }

        /* en la arquitectura MVVM el ModelView llama a la View
        cuando se produce un cambio en el "uiState"
        Este es el método en el VIEW que hace que se repinte
        con los nuevos valores del estado definido en el ModelView
         */

        /* corutina:  es un hilo que se ejecuta dento de un ámbito/scope
        cuando en este caso cuando se termina el ciclo de vida del activity
        e hilo se destruye... asñi al girar la pantalla el hilo que repinta
        deja de ejecutarse. Se destruye.
         */
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                withContext(Dispatchers.Main.immediate ) {
                    viewModel.uiError.collect {
                        Timber.d("error mostrado Channel")
                        Toast.makeText(this@MainActivity, it, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }
//al usar el ViewModel esto ya no es necesario.
// COn el viewmodel los valores no se pierden al girar la pantalla
/*
override fun onSaveInstanceState(outState: Bundle) {
super.onSaveInstanceState(outState)
//esto es para probar el git
outState.putString(TEXT_VIEW, binding.text1.text.toString())
}
*/

}