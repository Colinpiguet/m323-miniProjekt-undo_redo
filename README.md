# Undo/Redo CLI Todo App – Kotlin

Mini Projekt | Modul 323 – Funktionale Programmierung
 
---

## Projektbeschreibung

Eine CLI-basierte Todo-Applikation mit Undo/Redo-Funktionalität.
Umgesetzt in **Kotlin** mit funktionalen Programmierkonzepten.

### Befehle
| Befehl | Beschreibung |
|---|---|
| `add <name>` | Fügt einen neuen Task hinzu |
| `add <parent> > <subtask>` | Fügt einen Subtask zu einem bestehenden Task hinzu |
| `complete <name>` | Markiert einen Task als erledigt |
| `undo` | Macht die letzte Aktion rückgängig |
| `redo` | Stellt die letzte rückgängig gemachte Aktion wieder her |
| `exit` | Beendet das Programm |

### Beispiel
```
> add Projekt
>> Task 'Projekt' hinzugefügt   [undo möglich] [kein redo]
 
--- Todo Liste ---
[ ] Projekt
-----------------
 
> add Projekt > Dokumentation
>> Subtask 'Dokumentation' zu 'Projekt' hinzugefügt
 
--- Todo Liste ---
[ ] Projekt
    [ ] Dokumentation
-----------------
 
> complete Dokumentation
>> Task 'Dokumentation' abgehakt
 
--- Todo Liste ---
[x] Projekt
    [x] Dokumentation
-----------------
 
> undo
>> Undo ausgefuehrt   [undo möglich] [redo möglich]
 
> redo
>> Redo ausgefuehrt   [undo möglich] [kein redo]
```
 
---

## Projektstruktur

```
src/main/kotlin/todo/
├── Model.kt            → Immutable Datenstrukturen (Task, AppState)
├── Command.kt          → Sealed class mit allen Befehlen
├── TodoLogic.kt        → Pure functions (addTask, completeTask, undo, redo)
├── CommandExecutor.kt  → parseCommand + executeCommand (HOF)
├── Renderer.kt         → Pure render-Funktionen, kein println
└── Main.kt             → CLI-Loop, EINZIGER Ort mit println
```
 
---

## Funktionale Konzepte im Einsatz

| Konzept | Wo | Beispiel |
|---|---|---|
| Pure Functions | `TodoLogic.kt`, `Renderer.kt` | `addTask(state, name): AppState` |
| Immutable Data | `Model.kt` | `val name: String`, `copy()` statt mutation |
| Rekursion | `allSubtasksCompleted`, `renderTask`, `runLoop` | `allSubtasksCompleted(subtasks.drop(1))` |
| Pattern Matching | `CommandExecutor.kt`, `Renderer.kt` | `when (command) { is Command.Add -> ... }` |
| Map / Filter | `TodoLogic.kt`, `Renderer.kt` | `state.tasks.map { completeTaskByName(it, name) }` |
| HOF | `CommandExecutor.kt` | `executeCommand(..., onUnknown: (AppState) -> AppState)` |
| Side-Effects isoliert | `Main.kt` | Alle `println` nur in `runLoop()` |
 
---

## Starten

In IntelliJ: grüner Play-Button neben `fun main()` in `Main.kt`

Oder über Terminal:
```bash
kotlinc src/main/kotlin/todo/*.kt -include-runtime -d app.jar
java -jar app.jar
```