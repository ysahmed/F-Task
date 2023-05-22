package com.waesh.f_task.view.fragment


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.waesh.f_task.databinding.FragmentAddTodoBinding

class AddTodoFragment : DialogFragment() {

    private lateinit var binding: FragmentAddTodoBinding
    private lateinit var saveButtonClickListener: SaveButtonClickListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAddTodoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = arguments
        Log.i("kkkCat", "arguments: $args")

        args?.let {
            binding.etTodo.setText(it.getString("value"))
            binding.btnSave.text = "Update"
        }

        binding.btnClose.setOnClickListener {
            dismiss()
        }

        binding.btnSave.setOnClickListener {

            if (args == null){
                val todo = binding.etTodo.text.toString().trim()
                if (todo.isEmpty()) {
                    Toast.makeText(requireActivity(), "A task cannot be empty.", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    saveButtonClickListener.onSaveTask(todo)
                    // dismiss()
                }
            } else {
                val todo = binding.etTodo.text.toString().trim()
                if (todo.isEmpty()) {
                    Toast.makeText(requireActivity(), "A task cannot be empty.", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    saveButtonClickListener.onSaveTask(args.getString("key")!!, todo)
                    // dismiss()
                }
            }
        }
    }

    fun setSaveButtonClickListener(listener: SaveButtonClickListener) {
        saveButtonClickListener = listener
    }

    fun dismissDialog(){
        dismiss()
    }

    interface SaveButtonClickListener {
        fun onSaveTask(todo: String)
        fun onSaveTask(key:String, value:String)
    }
}