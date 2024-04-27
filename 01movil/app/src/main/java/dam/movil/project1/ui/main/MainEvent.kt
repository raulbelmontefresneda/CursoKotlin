package dam.movil.project1.ui.main

sealed class MainEvent {
    class Sumar(val incremento:Int) : MainEvent()
    class Restar(val incremento:Int) : MainEvent()
    object ErrorMostrado: MainEvent()
}