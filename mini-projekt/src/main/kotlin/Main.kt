package todo

// ─── Main / CLI-Layer ─────────────────────────────────────────────────────────
// EINZIGER Ort mit println – alle anderen Dateien sind Side-Effect-frei.
// Der Loop ist rekursiv mit tailrec (kein while, kein var state).

fun main() {
    println("=================================")
    println("       Todo App - Undo/Redo      ")
    println("=================================")
    println()
    println(renderMenu())
    println()
    runLoop(AppState())
}

tailrec fun runLoop(state: AppState) {
    print("> ")
    val input = readLine() ?: return

    if (input.trim().toLowerCase() == "exit") {
        println("Tschüss!")
        return
    }

    val command = parseCommand(input)
    val newState = executeCommand(state, command)

    // ── Alle Side-Effects (println) zentral hier ──────────────────────
    println()
    println(renderFeedback(command, newState.undoStack.isNotEmpty(), newState.redoStack.isNotEmpty()))
    println()
    println("--- Todo Liste ---")
    println(renderState(newState))
    println("-----------------")
    println()
    println(renderMenu())
    println()

    runLoop(newState)
}