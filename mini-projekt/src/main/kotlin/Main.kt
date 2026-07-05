package todo

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
        println("Tschuess!")
        return
    }

    val command = parseCommand(input)
    val (newState, resolvedCommand) = executeCommand(state, command)

    println()
    println(renderFeedback(resolvedCommand, newState.undoStack.isNotEmpty(), newState.redoStack.isNotEmpty()))
    println()
    println("--- Todo Liste ---")
    println(renderState(newState))
    println("-----------------")
    println()
    println(renderMenu())
    println()

    runLoop(newState)
}