package dev.rohman.livediscussion.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.rohman.livediscussion.databinding.ItemTodoBinding
import dev.rohman.livediscussion.models.Todo

class TodoAdapter(private val context: Context) : RecyclerView.Adapter<TodoAdapter.ViewHolder>() {
    interface OnClickListener {
        fun onClickedTodo(todo: Todo)
    }

    interface OnChangeListener {
        fun onChangedStatus(todo: Todo)
    }

    lateinit var onClickListener: OnClickListener
    lateinit var onChangedListener: OnChangeListener

    private val todoList = mutableListOf<Todo>()

    fun setData(data: List<Todo>) {
        todoList.clear()
        todoList.addAll(data)

        notifyDataSetChanged()
    }

    fun addTodo(todo: Todo) {
        todoList.add(0, todo)
        notifyItemInserted(0)
    }

    fun updateTodo(todo: Todo) {
        val index = todoList.indexOfFirst { it.id == todo.id }

        if (index >= 0) {
            todoList[index] = todo
            notifyItemChanged(index)
        }
    }

    fun deleteTodo(todo: Todo) {
        val index = todoList.indexOfFirst { it.id == todo.id }

        if (index >= 0) {
            todoList.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    inner class ViewHolder(val binding: ItemTodoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindData(todo: Todo) {
            binding.tvTask.text = todo.task
            binding.ivCheck.setImageResource(
                if (todo.status) android.R.drawable.checkbox_on_background
                else android.R.drawable.checkbox_off_background
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTodoBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val todo = todoList[position]

        holder.bindData(todo)

        holder.binding.ivCheck.setOnClickListener { onChangedListener.onChangedStatus(todo) }
        holder.binding.ivDelete.setOnClickListener { onClickListener.onClickedTodo(todo) }
    }

    override fun getItemCount(): Int = todoList.size
}