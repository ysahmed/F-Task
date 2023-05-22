package com.waesh.f_task.view.fragment


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.waesh.f_task.adapter.ItemClickListener
import com.waesh.f_task.adapter.TasksAdapter
import com.waesh.f_task.databinding.FragmentListBinding
import com.waesh.f_task.model.Task

class ListFragment : Fragment(), AddTodoFragment.SaveButtonClickListener {

    private lateinit var databaseRef: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var authId: String
    private lateinit var binding: FragmentListBinding
    private lateinit var dialog: AddTodoFragment
    private lateinit var navController: NavController
    private lateinit var adapter: TasksAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        auth = FirebaseAuth.getInstance()
        authId = auth.currentUser!!.uid
        databaseRef = FirebaseDatabase.getInstance().getReference("Tasks").child(authId)

        //Log.i("kkkCat", "List: ${auth.currentUser?.uid.toString()}")

        initRecyclerView()

        retrieveData()

        binding.fabAddTodo.setOnClickListener {
            dialog = AddTodoFragment()
            dialog.setSaveButtonClickListener(this)
            dialog.show(
                childFragmentManager,
                "AddTodoFragment"
            )
        }
    }

    private fun initRecyclerView() {
        adapter = TasksAdapter(object : ItemClickListener {
            override fun onClickEdit(task: Task) {
                val args = Bundle()
                args.putString("key", task.key)
                args.putString("value", task.value)
                dialog = AddTodoFragment()
                dialog.arguments = args
                dialog.setSaveButtonClickListener(this@ListFragment)
                dialog.show(
                    childFragmentManager,
                    "AddTodoFragment"
                )
            }

            override fun onClickDelete(task: Task) {
                databaseRef.child(task.key).removeValue()
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            "Deleted".makeToast()
                        }
                    }
                    .addOnFailureListener {
                        it.message?.makeToast()
                    }
            }
        })

        binding.rvList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvList.adapter = adapter
    }

    private fun retrieveData() {
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list: MutableList<Task> = mutableListOf()
                for (snap in snapshot.children) {
                    // Log.i("kkkCat", "snap: ${snap.key}::${snap.value}")
                    list.add(Task(snap.key!!, snap.value.toString()))
                }

                adapter.submitList(list)
            }

            override fun onCancelled(error: DatabaseError) {
                "Canceled".makeToast()
            }
        })
    }

    override fun onSaveTask(todo: String) {
        /*Log.i("kkkCat", "onSave: task: $todo")
        Log.i("kkkCat", "onSave: key: ${databaseRef.push().key!!}")
        Log.i("kkkCat", "onSave: auth: $authId")*/

        databaseRef
            .push()
            .setValue(todo)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    dialog.dismissDialog()
                    "Saved".makeToast()
                }
            }.addOnFailureListener {
                it.message?.makeToast()
            }
    }

    override fun onSaveTask(key: String, value: String) {
        val task = HashMap<String, Any>()
        task[key] = value
        databaseRef.updateChildren(task).addOnCompleteListener {
            if (it.isSuccessful){
                dialog.dismissDialog()
                "Task is updated".makeToast()
            }
        }
            .addOnFailureListener {
                it.message?.makeToast()
            }
    }

    private fun String.makeToast() {
        Toast.makeText(requireContext(), this, Toast.LENGTH_SHORT).show()
    }
}