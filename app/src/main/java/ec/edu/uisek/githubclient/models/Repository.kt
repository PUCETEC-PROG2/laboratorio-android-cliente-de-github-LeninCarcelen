package ec.edu.uisek.githubclient.models

data class Repository(
    val id: Long,
    val name: String,
    val description: String?,
    val language: String = "Kotlin"
)