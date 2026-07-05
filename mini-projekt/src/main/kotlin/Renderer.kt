package todo

// ─── Rendering (pure functions) ───────────────────────────────────────────────
// Diese Funktionen erzeugen nur Strings – kein println hier.

fun renderTask(task: Task, indent: String = ""): String {
    val checkbox = if (task.completed) "[x]" else "[ ]"
    val line = "$indent$checkbox ${task.name}"
    return if (task.subtasks.isEmpty()) {
        line
    } else {
        val subtaskLines = task.subtasks.map { renderTask(it, "$indent    ") }
        line + "\n" + subtaskLines.joinToString("\n")
    }
}

fun renderState(state: AppState): String =
    if (state.tasks.isEmpty()) "  (keine Tasks vorhanden)"
    else state.tasks.map { renderTask(it) }.joinToString("\n")

fun renderMenu(): String =
    "Befehle:\n" +
            "  add <name>                 neuen Task hinzufuegen\n" +
            "  add <parent> > <subtask>   Subtask hinzufuegen  (z.B.: add Projekt > Doku)\n" +
            "  complete <name>            Task abhaken\n" +
            "  undo                       letzte Aktion rueckgaengig machen\n" +
            "  redo                       rueckgaengiges wiederherstellen\n" +
            "  exit                       beenden"

fun renderFeedback(command: Command, hasUndo: Boolean, hasRedo: Boolean): String {
    val action = when (command) {
        is Command.Add            -> "Task '${command.name}' hinzugefuegt"
        is Command.AddSubtask     -> "Subtask '${command.subtaskName}' zu '${command.parentName}' hinzugefuegt"
        is Command.Complete       -> "Task '${command.name}' abgehakt"
        is Command.Undo           -> "Undo ausgefuehrt"
        is Command.Redo           -> "Redo ausgefuehrt"
        is Command.Unknown        -> "!! Unbekannter Befehl"
        is Command.ParentNotFound -> "!! Task '${command.parentName}' nicht gefunden – zuerst mit 'add ${command.parentName}' erstellen"
    }
    val undoHint = if (hasUndo) "[undo moeglich]" else "[kein undo]"
    val redoHint = if (hasRedo) "[redo moeglich]" else "[kein redo]"
    return ">> $action   $undoHint $redoHint"
}