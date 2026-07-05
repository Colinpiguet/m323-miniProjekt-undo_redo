Prompts mit CLaude AI, Sonnet 4.6

**Prompt 1:**
> „ich mache ein schulprojekt in kotlin, es geht um eine todo app mit undo und redo. kannst du mir erklären wie man daten immutable hält in kotlin? also was ist der unterschied zwischen val und var und warum sollte ich für funktionale programmierung immer val nehmen"

**Prompt 2:**
> „ok und wie würde dann so eine data class für einen Task aussehen der auch subtasks haben kann? der task soll einen namen haben und man soll ihn abhaken können. bitte noch keine logik nur die datenstruktur"

**Prompt 3:**
> „jetzt brauche ich noch einen AppState der die ganze todo liste hält plus einen undo stack und redo stack. wie macht man das immutable? also ich will keine mutable lists verwenden"

**Prompt 4:**
> „schreib mir eine pure function addTask die einen task zur liste hinzufügt. sie soll den alten state nicht verändern sondern einen neuen zurückgeben. ich verstehe noch nicht ganz wie copy() funktioniert, kannst du das erklären"

**Prompt 5:**
> „ich brauche eine funktion completeTask die einen task als erledigt markiert. wenn der task subtasks hat soll erst geprüft werden ob alle subtasks done sind, nur dann wird der parent auch auf done gesetzt. wie mache ich diesen check am besten rekursiv?"

**Prompt 6:**
> „kannst du mir die rekursive funktion allSubtasksCompleted nochmal erklären schritt für schritt? ich muss das dem lehrer erklären können und verstehe noch nicht genau was beim base case passiert"

**Prompt 7:**
> „was ist eine sealed class in kotlin und warum ist die besser als ein normales enum wenn ich verschiedene befehle wie add, complete, undo habe? ich will das mit pattern matching verwenden"

**Prompt 8:**
> „ok kannst du mir eine sealed class Command machen mit Add, Complete, Undo, Redo und Unknown. Add und Complete brauchen einen namen als parameter, undo und redo nicht"

**Prompt 9:**
> „jetzt brauche ich eine funktion parseCommand die einen string wie 'add Hausaufgaben' nimmt und daraus einen Command macht. wie splitte ich den string am besten auf und was passiert wenn jemand nur 'add' ohne namen eingibt"

**Prompt 10:**
> „was genau sind higher-order functions? kannst du mir zeigen wie ich eine executeCommand funktion schreibe die eine funktion als parameter nimmt? ich verstehe das konzept noch nicht so ganz"

**Prompt 11:**
> „ich will die ausgabe von der logik trennen. also keine printlns in meinen logik-funktionen. wie mache ich das am besten? kannst du eine renderer funktion schreiben die nur strings zurückgibt und nichts selbst ausdruckt"

**Prompt 12:**
> „die renderTask funktion soll auch subtasks anzeigen, eingerückt unter dem parent task. wie mache ich das rekursiv?"
 