package ec.edu.uisek.githubclient.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ec.edu.uisek.githubclient.MainActivity
import ec.edu.uisek.githubclient.R
import ec.edu.uisek.githubclient.models.Repository
import ec.edu.uisek.githubclient.ui.adapters.RepositoryAdapter

class RepositoryFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RepositoryAdapter
    private var pendingRepos: List<Repository>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_repository, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        
        adapter = RepositoryAdapter(
            mutableListOf(),
            onEdit = { repo ->
                (activity as MainActivity).onEditRepository(repo)
            },
            onDelete = { repo ->
                (activity as MainActivity).onDeleteRepository(repo)
            }
        )
        recyclerView.adapter = adapter
        
        // Cargar datos desde la actividad al crearse la vista
        val mainActivity = activity as? MainActivity
        mainActivity?.let {
            updateList(it.getRepositories())
        }
        
        pendingRepos?.let {
            adapter.updateData(it)
            pendingRepos = null
        }
    }

    fun updateList(repos: List<Repository>) {
        if (::adapter.isInitialized) {
            adapter.updateData(repos)
        } else {
            pendingRepos = repos
        }
    }
}
