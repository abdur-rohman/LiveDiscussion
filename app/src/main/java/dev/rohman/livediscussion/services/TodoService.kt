package dev.rohman.livediscussion.services

import dev.rohman.livediscussion.models.Base
import dev.rohman.livediscussion.models.Todo
import retrofit2.http.*

interface TodoService {
    @GET("v1/todos")
    suspend fun getTodos(): Base<List<Todo>>

    @POST("v1/todos")
    suspend fun createTodo(@Body body: HashMap<String, Any>): Base<Todo>

    @PUT("v1/todos/{id}")
    suspend fun updateTodo(@Path("id") id: Int, @Body body: Todo): Base<Todo>

    @DELETE("v1/todos/{id}")
    suspend fun deleteTodo(@Path("id") id: Int): Base<Todo>
}