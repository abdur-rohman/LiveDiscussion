package dev.rohman.livediscussion.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dev.rohman.livediscussion.models.Todo
import dev.rohman.livediscussion.services.TodoService
import dev.rohman.livediscussion.states.TodoState
import dev.rohman.livediscussion.utils.ClientUtil
import dev.rohman.livediscussion.utils.ConstantUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val _todoState = MutableLiveData<TodoState>(TodoState.InitialTodo())
    val observableTodoState: LiveData<TodoState> get() = _todoState

    private val _todoService by lazy { ClientUtil.service<TodoService>(ConstantUtil.BASE_URL) }

    fun getTodo() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = _todoService.getTodos()
                val state = if (response.data.isEmpty())
                    TodoState.ErrorLoadTodo(message = "Oops todo was empty!") else
                    TodoState.LoadedTodo(todoList = response.data)

                _todoState.postValue(state)
            } catch (ex: Exception) {
                handleError(ex)
            }
        }
    }

    fun deleteTodo(id: Int) {
        _todoState.value = TodoState.InitialTodo()

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = _todoService.deleteTodo(id)
                val state = TodoState.DeletedTodo(todo = response.data)

                _todoState.postValue(state)
            } catch (ex: Exception) {
                handleError(ex)
            }
        }
    }

    fun updateTodo(todo: Todo) {
        _todoState.value = TodoState.InitialTodo()

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = _todoService.updateTodo(todo.id, todo)
                val state = TodoState.UpdatedTodo(todo = response.data)

                _todoState.postValue(state)
            } catch (ex: Exception) {
                handleError(ex)
            }
        }
    }

    fun createTodo(task: String) {
        _todoState.value = TodoState.InitialTodo()

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = _todoService.createTodo(body = hashMapOf("task" to task))
                val state = TodoState.CreatedTodo(todo = response.data)

                _todoState.postValue(state)
            } catch (ex: Exception) {
                handleError(ex)
            }
        }
    }

    private fun handleError(ex: Exception) {
        ex.printStackTrace()

        val message = ex.message
        val state = if (message.isNullOrEmpty())
            TodoState.ErrorLoadTodo() else
            TodoState.ErrorLoadTodo(message = message)

        _todoState.postValue(state)
    }
}