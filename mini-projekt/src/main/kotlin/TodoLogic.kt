package todo

// ─── Pure Functions ───────────────────────────────────────────────────────────
// Keine var, keine Mutation, kein println.
// Jede Funktion nimmt einen State rein und gibt einen neuen State raus.

// Prüft rekursiv ob alle Subtasks eines Tasks abgehakt sind.
// Base case: leere Liste → true (kein Subtask = nichts offen)
// Rekursiver Fall: erster Subtask muss completed sein UND der Rest auch
fun allSubtasksCompleted(subtasks: List<Task>): Boolean =
    when {
        subtasks.isEmpty() -> true
        !subtasks.first().completed -> false
        else -> allSubtasksCompleted(subtasks.drop(1))
    }

// Markiert einen Task als completed.
// Wenn der Task Subtasks hat, werden diese zuerst alle completed gesetzt.
// Danach wird geprüft ob alle Subtasks done sind → dann auch Parent = done.
fun completeTaskByName(task: Task, name: String): Task =
    when {
        task.name == name -> {
            val updatedSubtasks = task.subtasks.map { it.copy(completed = true) }
            val canComplete = task.subtasks.isEmpty() || allSubtasksCompleted(updatedSubtasks)
            task.copy(
                subtasks = updatedSubtasks,
                completed = canComplete
            )
        }
        else -> {
            // Rekursion: suche in den Subtasks nach dem Namen
            val updatedSubtasks = task.subtasks.map { completeTaskByName(it, name) }
            val autoComplete = updatedSubtasks.isNotEmpty() && allSubtasksCompleted(updatedSubtasks)
            task.copy(
                subtasks = updatedSubtasks,
                completed = autoComplete || task.completed
            )
        }
    }

// Fügt einen neuen Task zur Liste hinzu.
// Speichert den alten tasks-Stand auf dem undoStack.
// redoStack wird geleert, weil eine neue Aktion die Redo-Historie bricht.
fun addTask(state: AppState, name: String): AppState {
    val newTask = Task(name = name)
    return state.copy(
        tasks = state.tasks + newTask,
        undoStack = listOf(state.tasks) + state.undoStack,
        redoStack = emptyList()
    )
}

// Markiert einen Task (oder Subtask) als erledigt.
// Nutzt map + completeTaskByName (HOF: map nimmt eine Funktion entgegen).
fun completeTask(state: AppState, name: String): AppState {
    val updatedTasks = state.tasks.map { completeTaskByName(it, name) }
    return state.copy(
        tasks = updatedTasks,
        undoStack = listOf(state.tasks) + state.undoStack,
        redoStack = emptyList()
    )
}

// Macht die letzte Aktion rückgängig.
// Nimmt den letzten State vom undoStack und stellt ihn wieder her.
// Den aktuellen State legen wir auf den redoStack.
fun undo(state: AppState): AppState =
    when {
        state.undoStack.isEmpty() -> state  // nichts zu undoen → unveränderter State
        else -> state.copy(
            tasks = state.undoStack.first(),
            undoStack = state.undoStack.drop(1),
            redoStack = listOf(state.tasks) + state.redoStack
        )
    }

// Stellt die letzte rückgängig gemachte Aktion wieder her.
fun redo(state: AppState): AppState =
    when {
        state.redoStack.isEmpty() -> state  // nichts zu redoen → unveränderter State
        else -> state.copy(
            tasks = state.redoStack.first(),
            undoStack = listOf(state.tasks) + state.undoStack,
            redoStack = state.redoStack.drop(1)
        )
    }