package dam.movil.project1

import androidx.lifecycle.ViewModel
//YA LA HE BORRADO import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.random.Random
import dam.movil.project1.R
import dam.movil.project1.StringProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

//el MainViewModel muere con la destrucción de
// la pantalla tiene un ciclo de vida distinto al activity
// Dentro de la arquitectura MVVM (Model - View - Viewmodel) :
// MAinViewModel es el Viewmodel
// MainActivity= pantalla es la Vista ..
// ...y el Model... ¿el MainState? ya veremos...
// LA vista y el Modelo no se hablan..
// siempre lo hacen a través del ViewModel que sirve de nexo
// y esto hace que puedan hacerse pruebas unitarias al modelo sin necesidad de una
//pantalla(view)
@HiltViewModel
class MainViewModel @Inject constructor(
    private val stringProvider: StringProvider,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainState(0,null))

    //ANTES: val uiState: StateFlow<MainState> get() = _uiState
    val uiState: StateFlow<MainState> = _uiState.asStateFlow()

    /*
     * el Channel es un tipo de StateFlow de UNA SOLA lectura
    *  Se suele usar para el tratamiento de mensajes de error
    * */
    private val _uiError = Channel<String>()
    val uiError = _uiError.receiveAsFlow()

    /*
     * Estamos implementando un PAtron Command
     */
    fun handleEvent(event: MainEvent) {
        when (event) {

            MainEvent.ErrorMostrado ->{
                _uiState.update{_uiState.value.copy(error = null)}

            }

            is MainEvent.Restar -> restar(event.incremento)

            is MainEvent.Sumar -> sumar(event.incremento)
        }
    }

    private fun operacion( op:(Int,Int) -> Int,incremento: Int){
        if (Random.nextBoolean())
            _uiState.update{_uiState.value.copy(contador = op(_uiState.value.contador,incremento))}
        else{
            //_uiState.update{_uiState.value.copy(error = "Error aleatorio")}
            viewModelScope.launch {
                _uiError.send(stringProvider.getString(R.string.error))
            }
            Timber.d(stringProvider.getString(R.string.error))
        }

    }

    private fun sumar(incremento:Int) {

        operacion(Int::plus, incremento)
    }

    private fun restar(incremento : Int) {
        operacion(Int::minus,incremento)
    }

}
/* cuando incluya inyecciones borraremos la clase MainViewModelFactory
class MainViewModelFactory():ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(

            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
*/