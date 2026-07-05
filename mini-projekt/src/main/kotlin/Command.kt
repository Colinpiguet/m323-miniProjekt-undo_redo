package todo

sealed class Command {
    data class Add(val name: String) : Command()
    data class AddSubtask(val parentName: String, val subtaskName: String) : Command()
    data class Complete(val name: String) : Command()
    object Undo : Command()
    object Redo : Command()
    object Unknown : Command()
    data class ParentNotFound(val parentName: String) : Command()
}