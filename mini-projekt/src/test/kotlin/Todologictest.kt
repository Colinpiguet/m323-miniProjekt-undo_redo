package todo

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.test.assertIs

class TodoLogicTest {

    // ── addTask ───────────────────────────────────────────────────────────────

    @Test
    fun `addTask fuegt einen Task hinzu`() {
        val state = AppState()
        val result = addTask(state, "Hausaufgaben")
        assertEquals(1, result.tasks.size)
        assertEquals("Hausaufgaben", result.tasks.first().name)
    }

    @Test
    fun `addTask veraendert den alten State nicht`() {
        val state = AppState()
        addTask(state, "Hausaufgaben")
        assertTrue(state.tasks.isEmpty())
    }

    @Test
    fun `addTask legt alten Stand auf undoStack`() {
        val state = AppState(tasks = listOf(Task("Alt")))
        val result = addTask(state, "Neu")
        assertEquals(1, result.undoStack.size)
        assertEquals(listOf(Task("Alt")), result.undoStack.first())
    }

    @Test
    fun `addTask leert den redoStack`() {
        val state = AppState(redoStack = listOf(listOf(Task("Redo"))))
        val result = addTask(state, "Neu")
        assertTrue(result.redoStack.isEmpty())
    }

    // ── addSubtask ────────────────────────────────────────────────────────────

    @Test
    fun `addSubtask fuegt Subtask zu bestehendem Task hinzu`() {
        val state = AppState(tasks = listOf(Task("Projekt")))
        val result = addSubtask(state, "Projekt", "Doku")
        assertEquals(1, result.tasks.first().subtasks.size)
        assertEquals("Doku", result.tasks.first().subtasks.first().name)
    }

    @Test
    fun `addSubtask veraendert alten State nicht`() {
        val state = AppState(tasks = listOf(Task("Projekt")))
        addSubtask(state, "Projekt", "Doku")
        assertTrue(state.tasks.first().subtasks.isEmpty())
    }

    // ── completeTask ──────────────────────────────────────────────────────────

    @Test
    fun `completeTask markiert Task als completed`() {
        val state = AppState(tasks = listOf(Task("Lernen")))
        val result = completeTask(state, "Lernen")
        assertTrue(result.tasks.first().completed)
    }

    @Test
    fun `completeTask veraendert alten State nicht`() {
        val state = AppState(tasks = listOf(Task("Lernen")))
        completeTask(state, "Lernen")
        assertFalse(state.tasks.first().completed)
    }

    @Test
    fun `completeTask Parent bleibt offen wenn Subtask noch offen`() {
        val state = AppState(tasks = listOf(
            Task("Projekt", subtasks = listOf(Task("Teil 1"), Task("Teil 2")))
        ))
        val result = completeTask(state, "Teil 1")
        assertFalse(result.tasks.first().completed)
    }

    @Test
    fun `completeTask Parent wird completed wenn alle Subtasks done`() {
        val state = AppState(tasks = listOf(
            Task("Projekt", subtasks = listOf(Task("Teil 1"), Task("Teil 2")))
        ))
        val s1 = completeTask(state, "Teil 1")
        val s2 = completeTask(s1, "Teil 2")
        assertTrue(s2.tasks.first().completed)
    }

    // ── allSubtasksCompleted ──────────────────────────────────────────────────

    @Test
    fun `allSubtasksCompleted gibt true fuer leere Liste`() {
        assertTrue(allSubtasksCompleted(emptyList()))
    }

    @Test
    fun `allSubtasksCompleted gibt false wenn ein Subtask offen`() {
        val subtasks = listOf(Task("A", completed = true), Task("B", completed = false))
        assertFalse(allSubtasksCompleted(subtasks))
    }

    @Test
    fun `allSubtasksCompleted gibt true wenn alle completed`() {
        val subtasks = listOf(Task("A", completed = true), Task("B", completed = true))
        assertTrue(allSubtasksCompleted(subtasks))
    }

    // ── taskExists ────────────────────────────────────────────────────────────

    @Test
    fun `taskExists findet Top-Level Task`() {
        val tasks = listOf(Task("Projekt"))
        assertTrue(taskExists(tasks, "Projekt"))
    }

    @Test
    fun `taskExists findet Task in Subtasks`() {
        val tasks = listOf(Task("Projekt", subtasks = listOf(Task("Doku"))))
        assertTrue(taskExists(tasks, "Doku"))
    }

    @Test
    fun `taskExists gibt false fuer nicht existierenden Task`() {
        val tasks = listOf(Task("Projekt"))
        assertFalse(taskExists(tasks, "Nichtvorhanden"))
    }

    @Test
    fun `taskExists gibt false fuer leere Liste`() {
        assertFalse(taskExists(emptyList(), "X"))
    }

    // ── undo ──────────────────────────────────────────────────────────────────

    @Test
    fun `undo stellt vorherigen State wieder her`() {
        val state = AppState()
        val after = addTask(state, "Task A")
        val undone = undo(after)
        assertTrue(undone.tasks.isEmpty())
    }

    @Test
    fun `undo legt aktuellen State auf redoStack`() {
        val after = addTask(AppState(), "Task A")
        val undone = undo(after)
        assertTrue(undone.redoStack.isNotEmpty())
    }

    @Test
    fun `undo bei leerem Stack aendert State nicht`() {
        val state = AppState(tasks = listOf(Task("X")))
        assertEquals(state, undo(state))
    }

    // ── redo ──────────────────────────────────────────────────────────────────

    @Test
    fun `redo stellt rueckgaengig gemachten State wieder her`() {
        val after = addTask(AppState(), "Task A")
        val undone = undo(after)
        val redone = redo(undone)
        assertEquals(1, redone.tasks.size)
        assertEquals("Task A", redone.tasks.first().name)
    }

    @Test
    fun `redo bei leerem Stack aendert State nicht`() {
        val state = AppState(tasks = listOf(Task("X")))
        assertEquals(state, redo(state))
    }

    @Test
    fun `neue Aktion nach undo leert redoStack`() {
        val s1 = addTask(AppState(), "A")
        val s2 = addTask(s1, "B")
        val s3 = undo(s2)
        val s4 = addTask(s3, "C")
        assertTrue(s4.redoStack.isEmpty())
    }

    // ── parseCommand ──────────────────────────────────────────────────────────

    @Test
    fun `parseCommand parst add-Befehl`() {
        assertIs<Command.Add>(parseCommand("add Hausaufgaben"))
        assertEquals("Hausaufgaben", (parseCommand("add Hausaufgaben") as Command.Add).name)
    }

    @Test
    fun `parseCommand parst add-Subtask-Befehl`() {
        val cmd = parseCommand("add Projekt > Doku")
        assertIs<Command.AddSubtask>(cmd)
        assertEquals("Projekt", (cmd as Command.AddSubtask).parentName)
        assertEquals("Doku", cmd.subtaskName)
    }

    @Test
    fun `parseCommand parst complete-Befehl`() {
        assertIs<Command.Complete>(parseCommand("complete Lernen"))
    }

    @Test
    fun `parseCommand parst undo und redo`() {
        assertIs<Command.Undo>(parseCommand("undo"))
        assertIs<Command.Redo>(parseCommand("redo"))
    }

    @Test
    fun `parseCommand gibt Unknown fuer unbekannten Befehl`() {
        assertIs<Command.Unknown>(parseCommand("blabla"))
        assertIs<Command.Unknown>(parseCommand("add"))
    }
}