package todo

// Pure function: wandelt einen rohen String in einen Command um.
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

// Higher-Order Function: executeCommand nimmt onUnknown als Funktion entgegen.
fun executeCommand(
    state: AppState,
    command: Command,
    onUnknown: (AppState) -> AppState = { it }
): Pair<AppState, Command> {
    // Bei AddSubtask zuerst prüfen ob der Parent existiert.
    // Falls nicht → ParentNotFound zurückgeben, State unverändert lassen.
    val resolvedCommand = when (command) {
        is Command.AddSubtask ->
            if (!taskExists(state.tasks, command.parentName))
                Command.ParentNotFound(command.parentName)
            else command
        else -> command
    }

    val newState = when (resolvedCommand) {
        is Command.Add           -> addTask(state, resolvedCommand.name)
        is Command.AddSubtask    -> addSubtask(state, resolvedCommand.parentName, resolvedCommand.subtaskName)
        is Command.Complete      -> completeTask(state, resolvedCommand.name)
        is Command.Undo          -> undo(state)
        is Command.Redo          -> redo(state)
        is Command.Unknown       -> onUnknown(state)
        is Command.ParentNotFound -> state  // State bleibt unverändert
    }

    return Pair(newState, resolvedCommand)
}