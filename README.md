# Паттерны проектирования
Под паттернами проектирования понимается описание взаимодействия объектов и классов, адаптированных для решения общей задачи проектирования в конкретном контексте.
Существует множество различных паттернов, например, Composite, Strategy, Decorator, Abstract Factory, Bridge, Command, Iterator, Visitor.

## Паттерн Visitor
Этот паттерн особенно хорошо подходит для выполнения действий с объектами, входящими в стабильную структуру классов. Добавление нового вида посетителя не требует изменять структуру классов. Но каждый раз, когда в структуру добавляется новый подкласс, вам придется обновить все интерфейсы посетителя и добавить операцию Visit... для этого подкласса.
