package todo

// Sealed class = Pattern Matching freundlich.
// Alle möglichen Befehle sind hier zentral definiert.
// Der Compiler prüft ob im 'when' alle Fälle abgedeckt sind.
sealed class Command {
    data class Add(val name: String) : Command()
    data class AddSubtask(val parentName: String, val subtaskName: String) : Command()
    data class Complete(val name: String) : Command()
    object Undo : Command()
    object Redo : Command()
    object Unknown : Command()
}