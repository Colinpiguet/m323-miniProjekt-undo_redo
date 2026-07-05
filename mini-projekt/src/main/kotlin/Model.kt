package todo

// Immutable data class für einen Task.
// 'val' statt 'var' bedeutet: nach dem Erstellen kann nichts mehr geändert werden.
// Änderungen erzeugen immer eine neue Kopie (copy()), der alte Task bleibt unberührt.
data class Task(
    val name: String,
    val completed: Boolean = false,
    val subtasks: List<Task> = emptyList()
)

// Der gesamte Zustand der App – ebenfalls komplett immutable.
// undoStack: Liste vergangener States (neuester zuerst)
// redoStack: Liste von States die rückgängig gemacht wurden
data class AppState(
    val tasks: List<Task> = emptyList(),
    val undoStack: List<List<Task>> = emptyList(),
    val redoStack: List<List<Task>> = emptyList()
)