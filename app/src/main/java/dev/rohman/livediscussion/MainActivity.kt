package dev.rohman.livediscussion

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import dev.rohman.livediscussion.adapters.TodoAdapter
import dev.rohman.livediscussion.databinding.ActivityMainBinding
import dev.rohman.livediscussion.models.Todo
import dev.rohman.livediscussion.states.TodoState
import dev.rohman.livediscussion.viewmodels.MainViewModel

class MainActivity : AppCompatActivity() {
    private val adapter by lazy { TodoAdapter(this) }
    private val viewModel by viewModels<MainViewModel>()
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        adapter.onClickListener = object : TodoAdapter.OnClickListener {
            override fun onClickedTodo(todo: Todo) {
                viewModel.deleteTodo(todo.id)
            }
        }

        adapter.onChangedListener = object : TodoAdapter.OnChangeListener {
            override fun onChangedStatus(todo: Todo) {
                viewModel.updateTodo(todo.copy(status = !todo.status))
            }
        }

        binding.rvTodo.adapter = adapter

        viewModel.getTodo()
        viewModel.observableTodoState.observe(this) {
            when (it) {
                is TodoState.InitialTodo -> {
                    binding.pbLoading.visibility = View.VISIBLE
                    binding.tvError.visibility = View.GONE

                    showMainView(false)
                }

                is TodoState.ErrorLoadTodo -> {
                    binding.tvError.text = it.message

                    binding.pbLoading.visibility = View.GONE
                    binding.tvError.visibility = View.VISIBLE

                    showMainView(false)
                }

                is TodoState.LoadedTodo -> {
                    adapter.setData(it.todoList)

                    showMainView(true)
                }

                is TodoState.CreatedTodo -> {
                    adapter.addTodo(it.todo)
                    binding.rvTodo.smoothScrollToPosition(0)

                    showMainView(true)
                }

                is TodoState.UpdatedTodo -> {
                    adapter.updateTodo(it.todo)

                    showMainView(true)
                }

                is TodoState.DeletedTodo -> {
                    adapter.deleteTodo(it.todo)

                    showMainView(true)
                }
            }
        }

        binding.btnCreate.setOnClickListener {
            if (binding.etTask.text.isNotEmpty()) {
                viewModel.createTodo(binding.etTask.text.toString())
            } else {
                Toast.makeText(this, "Task can't be empty", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showMainView(isShow: Boolean) {
        binding.run {
            rvTodo.visibility = if (isShow) View.VISIBLE else View.GONE
            etTask.visibility = if (isShow) View.VISIBLE else View.GONE
            btnCreate.visibility = if (isShow) View.VISIBLE else View.GONE

            if (isShow) {
                binding.pbLoading.visibility = View.GONE
                binding.tvError.visibility = View.GONE
            }
        }
    }
}