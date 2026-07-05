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

// Fügt einen Subtask zu einem bestimmten Parent-Task hinzu (rekursiv).
fun addSubtaskByName(task: Task, parentName: String, subtaskName: String): Task =
    when {
        task.name == parentName ->
            task.copy(subtasks = task.subtasks + Task(subtaskName))
        else ->
            task.copy(subtasks = task.subtasks.map { addSubtaskByName(it, parentName, subtaskName) })
    }

// Markiert einen Task als completed.
// Wenn der Task Subtasks hat, werden diese zuerst alle completed gesetzt.
// Danach wird geprüft ob alle Subtasks done sind → dann auch Parent = done.
fun completeTaskByName(task: Task, name: String): Task =
    when {
        task.name == name -> {
            val updatedSubtasks = task.subtasks.map { it.copy(completed = true) }
            val canComplete = task.subtasks.isEmpty() || allSubtasksCompleted(updatedSubtasks)
            task.copy(subtasks = updatedSubtasks, completed = canComplete)
        }
        else -> {
            val updatedSubtasks = task.subtasks.map { completeTaskByName(it, name) }
            val autoComplete = updatedSubtasks.isNotEmpty() && allSubtasksCompleted(updatedSubtasks)
            task.copy(subtasks = updatedSubtasks, completed = autoComplete || task.completed)
        }
    }

// Fügt einen neuen Top-Level-Task zur Liste hinzu.
fun addTask(state: AppState, name: String): AppState =
    state.copy(
        tasks = state.tasks + Task(name),
        undoStack = listOf(state.tasks) + state.undoStack,
        redoStack = emptyList()
    )

// Fügt einen Subtask zu einem bestehenden Task hinzu.
fun addSubtask(state: AppState, parentName: String, subtaskName: String): AppState =
    state.copy(
        tasks = state.tasks.map { addSubtaskByName(it, parentName, subtaskName) },
        undoStack = listOf(state.tasks) + state.undoStack,
        redoStack = emptyList()
    )

// Markiert einen Task (oder Subtask) als erledigt.
// Nutzt map + completeTaskByName (HOF: map nimmt eine Funktion entgegen).
fun completeTask(state: AppState, name: String): AppState =
    state.copy(
        tasks = state.tasks.map { completeTaskByName(it, name) },
        undoStack = listOf(state.tasks) + state.undoStack,
        redoStack = emptyList()
    )

// Macht die letzte Aktion rückgängig.
// Legt den aktuellen State auf den redoStack → redo ist danach möglich.
// WICHTIG: nach undo darf man NICHT complete/add machen wenn man redo will,
// da jede neue Aktion den redoStack leert (so funktioniert Undo/Redo überall).
fun undo(state: AppState): AppState =
    when {
        state.undoStack.isEmpty() -> state
        else -> state.copy(
            tasks = state.undoStack.first(),
            undoStack = state.undoStack.drop(1),
            redoStack = listOf(state.tasks) + state.redoStack
        )
    }

// Stellt die letzte rückgängig gemachte Aktion wieder her.
fun redo(state: AppState): AppState =
    when {
        state.redoStack.isEmpty() -> state
        else -> state.copy(
            tasks = state.redoStack.first(),
            undoStack = listOf(state.tasks) + state.undoStack,
            redoStack = state.redoStack.drop(1)
        )
    }