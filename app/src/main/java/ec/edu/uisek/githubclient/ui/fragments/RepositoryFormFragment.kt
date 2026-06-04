package ec.edu.uisek.githubclient.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.MaterialToolbar
import ec.edu.uisek.githubclient.MainActivity
import ec.edu.uisek.githubclient.R
import ec.edu.uisek.githubclient.models.Repository

class RepositoryFormFragment : Fragment() {

    private var repoToEdit: Repository? = null

    companion object {
        fun newInstance(repo: Repository? = null): RepositoryFormFragment {
            val fragment = RepositoryFormFragment()
            repo?.let {
                val args = Bundle()
                args.putLong("id", it.id)
                args.putString("name", it.name)
                args.putString("description", it.description ?: "")
                args.putString("language", it.language)
                fragment.arguments = args
            }
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            repoToEdit = Repository(
                id = it.getLong("id"),
                name = it.getString("name") ?: "",
                description = it.getString("description"),
                language = it.getString("language") ?: "Kotlin"
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_repository_form, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar: MaterialToolbar = view.findViewById(R.id.toolbar)
        val etName: EditText = view.findViewById(R.id.etRepoName)
        val etLanguage: EditText = view.findViewById(R.id.etRepoLanguage)
        val etDescription: EditText = view.findViewById(R.id.etRepoDescription)
        val btnSave: Button = view.findViewById(R.id.btnSave)

        toolbar.title = if (repoToEdit == null) "Nuevo Repositorio" else "Editar Repositorio"
        toolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }

        repoToEdit?.let {
            etName.setText(it.name)
            etName.isEnabled = false
            etLanguage.setText(it.language)
            etDescription.setText(it.description)
        }

        btnSave.setOnClickListener {
            val name = etName.text.toString().trim()
            val language = etLanguage.text.toString().trim()
            val description = etDescription.text.toString().trim()
            
            if (name.isEmpty()) {
                Toast.makeText(requireContext(), "El nombre es obligatorio", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val finalLanguage = if (language.isEmpty()) "Kotlin" else language

            val mainActivity = activity as? MainActivity
            if (repoToEdit == null) {
                mainActivity?.addRepository(name, description, finalLanguage)
                Toast.makeText(requireContext(), "Proyecto Creado Correctamente", Toast.LENGTH_SHORT).show()
            } else {
                mainActivity?.updateRepository(repoToEdit!!.id, description, finalLanguage)
                Toast.makeText(requireContext(), "Proyecto Actualizado Correctamente", Toast.LENGTH_SHORT).show()
            }
            
            parentFragmentManager.popBackStack()
        }
    }
}
