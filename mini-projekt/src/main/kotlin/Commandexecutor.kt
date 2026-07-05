package todo

// ─── Input Parsing ────────────────────────────────────────────────────────────
// Pure function: wandelt einen rohen String in einen Command um.
// Kein println, keine Seiteneffekte – nur Input → Output.
fun parseCommand(input: String): Command {
    val parts = input.trim().split(" ", limit = 2)
    return when (parts.firstOrNull()?.toLowerCase()) {
        "add"      -> if (parts.size == 2) Command.Add(parts[1]) else Command.Unknown
        "complete" -> if (parts.size == 2) Command.Complete(parts[1]) else Command.Unknown
        "undo"     -> Command.Undo
        "redo"     -> Command.Redo
        else       -> Command.Unknown
    }
}

// ─── Command Executor (HOF) ───────────────────────────────────────────────────
// Higher-Order Function: nimmt eine Transformationsfunktion entgegen.
// So kann executeCommand generisch bleiben und die eigentliche Logik
// wird von aussen reingegeben – klassisches HOF-Muster.
fun executeCommand(
    state: AppState,
    command: Command,
    onUnknown: (AppState) -> AppState = { it }   // default: State unverändert
): AppState =
    when (command) {
        is Command.Add      -> addTask(state, command.name)
        is Command.Complete -> completeTask(state, command.name)
        is Command.Undo     -> undo(state)
        is Command.Redo     -> redo(state)
        is Command.Unknown  -> onUnknown(state)
    }