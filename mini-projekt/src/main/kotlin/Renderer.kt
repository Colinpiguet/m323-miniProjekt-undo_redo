package todo

// ─── Rendering (pure functions) ───────────────────────────────────────────────
// Diese Funktionen erzeugen nur Strings – sie drucken nichts selbst aus.
// Das println passiert ausschliesslich in Main.kt (Side-Effect Isolation).

// Formatiert einen einzelnen Task als String.
// Rekursiv: Subtasks werden eingerückt darunter angezeigt.
fun renderTask(task: Task, indent: String = ""): String {
    val checkbox = if (task.completed) "[x]" else "[ ]"
    val line = "$indent$checkbox ${task.name}"
    return if (task.subtasks.isEmpty()) {
        line
    } else {
        val subtaskLines = task.subtasks.map { renderTask(it, "$indent  ") }
        line + "\n" + subtaskLines.joinToString("\n")
    }
}

// Formatiert die gesamte Todo-Liste als String.
// Nutzt map (HOF) um jeden Task zu rendern, dann joinToString.
fun renderState(state: AppState): String {
    if (state.tasks.isEmpty()) return "  (keine Tasks vorhanden)"
    return state.tasks.map { renderTask(it) }.joinToString("\n")
}

// Gibt eine Rückmeldung zur ausgeführten Aktion zurück.
// Pattern Matching auf Command – kein when-else nötig dank sealed class.
fun renderFeedback(command: Command): String =
    when (command) {
        is Command.Add      -> "✓ Task '${command.name}' hinzugefügt"
        is Command.Complete -> "✓ Task '${command.name}' abgehakt"
        is Command.Undo     -> "↩ Undo ausgeführt"
        is Command.Redo     -> "↪ Redo ausgeführt"
        is Command.Unknown  -> "✗ Unbekannter Befehl. Befehle: add <name> | complete <name> | undo | redo"
    }