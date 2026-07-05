package todo

// ─── Input Parsing ────────────────────────────────────────────────────────────
// Pure function: wandelt einen rohen String in einen Command um.
// Syntax für Subtask: add <parent> > <subtask>
// Beispiel:           add Projekt > Dokumentation schreiben
fun parseCommand(input: String): Command {
    val trimmed = input.trim()
    val parts = trimmed.split(" ", limit = 2)
    return when (parts.firstOrNull()?.toLowerCase()) {
        "add" -> {
            if (parts.size < 2) return Command.Unknown
            val arg = parts[1]
            if (arg.contains(">")) {
                val split = arg.split(">", limit = 2)
                val parent = split[0].trim()
                val sub = split[1].trim()
                if (parent.isNotEmpty() && sub.isNotEmpty())
                    Command.AddSubtask(parent, sub)
                else Command.Unknown
            } else {
                Command.Add(arg.trim())
            }
        }
        "complete" -> if (parts.size == 2) Command.Complete(parts[1].trim()) else Command.Unknown
        "undo"     -> Command.Undo
        "redo"     -> Command.Redo
        else       -> Command.Unknown
    }
}

// ─── Command Executor (HOF) ───────────────────────────────────────────────────
// Higher-Order Function: nimmt onUnknown als Funktion entgegen.
fun executeCommand(
    state: AppState,
    command: Command,
    onUnknown: (AppState) -> AppState = { it }
): AppState =
    when (command) {
        is Command.Add        -> addTask(state, command.name)
        is Command.AddSubtask -> addSubtask(state, command.parentName, command.subtaskName)
        is Command.Complete   -> completeTask(state, command.name)
        is Command.Undo       -> undo(state)
        is Command.Redo       -> redo(state)
        is Command.Unknown    -> onUnknown(state)
    }