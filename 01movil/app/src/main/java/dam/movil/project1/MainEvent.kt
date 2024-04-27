package dam.movil.project1

sealed class MainEvent {
    class Sumar(val incremento:Int) : MainEvent()
    class Restar(val incremento:Int) : MainEvent()
    object ErrorMostrado: MainEvent()
}