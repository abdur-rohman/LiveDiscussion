package dev.rohman.livediscussion.states

import dev.rohman.livediscussion.models.Todo

sealed class TodoState {
    data class InitialTodo(val message: String = "Loading...") : TodoState()
    data class LoadedTodo(val todoList: List<Todo>) : TodoState()
    data class CreatedTodo(val todo: Todo) : TodoState()
    data class UpdatedTodo(val todo: Todo) : TodoState()
    data class DeletedTodo(val todo: Todo) : TodoState()
    data class ErrorLoadTodo(val message: String = "Oops something went wrong") : TodoState()
}
