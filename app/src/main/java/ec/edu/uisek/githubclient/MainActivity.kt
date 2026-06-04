package ec.edu.uisek.githubclient

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ec.edu.uisek.githubclient.models.Repository
import ec.edu.uisek.githubclient.ui.fragments.RepositoryFormFragment
import ec.edu.uisek.githubclient.ui.fragments.RepositoryFragment

class MainActivity : AppCompatActivity() {

    // Lista estática de repositorios para el ejercicio (Fuente de verdad)
    private val staticRepositories = mutableListOf(
        Repository(1, "android-kotlin-samples", "Muestras de código de Android en Kotlin", "Kotlin"),
        Repository(2, "jetpack-compose-lab", "Ejercicios prácticos con Jetpack Compose", "Kotlin"),
        Repository(3, "github-client-ui", "Interfaz visual de un cliente de GitHub", "XML"),
        Repository(4, "architecture-patterns", "Ejemplos de MVVM y Clean Architecture", "Java")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, RepositoryFragment(), "REPO_FRAGMENT")
                .commit()
        }

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, RepositoryFormFragment.newInstance())
                .addToBackStack(null)
                .commit()
        }
    }

    fun getRepositories(): List<Repository> {
        return staticRepositories
    }

    fun addRepository(name: String, description: String, language: String) {
        val newId = (staticRepositories.maxOfOrNull { it.id } ?: 0) + 1
        staticRepositories.add(Repository(newId, name, description, language))
    }

    fun updateRepository(id: Long, description: String, language: String) {
        val index = staticRepositories.indexOfFirst { it.id == id }
        if (index != -1) {
            val oldRepo = staticRepositories[index]
            staticRepositories[index] = oldRepo.copy(description = description, language = language)
        }
    }

    fun onEditRepository(repo: Repository) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, RepositoryFormFragment.newInstance(repo))
            .addToBackStack(null)
            .commit()
    }

    fun onDeleteRepository(repo: Repository) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar repositorio")
            .setMessage("¿Estás seguro de eliminar ${repo.name}?")
            .setPositiveButton("Eliminar") { _, _ ->
                staticRepositories.removeAll { it.id == repo.id }
                val fragment = supportFragmentManager.findFragmentByTag("REPO_FRAGMENT") as? RepositoryFragment
                fragment?.updateList(staticRepositories)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}
