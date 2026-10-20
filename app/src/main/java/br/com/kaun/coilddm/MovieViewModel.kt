package br.com.kaun.coilddm

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MovieViewModel : ViewModel() {
    private val _movies = MutableStateFlow(
        listOf(
            Movie(1, "Interestelar", 9.5f, "https://image.tmdb.org/t/p/w500/gEU2QniE6E77NI6lCU6MxlNBvIx.jpg"),
            Movie(2, "A Origem", 9.0f, "https://image.tmdb.org/t/p/w500/oYuLEt3zVCKq57qu2F8dT7NIa6f.jpg"),
            Movie(3, "O Senhor dos Anéis: A Sociedade do Anel", 9.2f, "https://image.tmdb.org/t/p/w500/6oom5QYQ2yQTMJIbnvbkBL9cHo6.jpg"),
            Movie(4, "Homem-Aranha: No Aranhaverso", 8.8f, "https://image.tmdb.org/t/p/w500/iiZZdoQBEYBv6id8su7ImL0oCbD.jpg")
        )
    )
    val movies: StateFlow<List<Movie>> = _movies.asStateFlow()
    private var nextId = 5L

    fun saveMovie(id: Long?, title: String, rating: Float, posterUrl: String) {
        val cleanTitle = title.trim()
        if (cleanTitle.isBlank() || rating !in 0f..10f) return
        val current = _movies.value
        _movies.value = if (id == null) {
            current + Movie(nextId++, cleanTitle, rating, posterUrl.trim())
        } else {
            current.map { movie ->
                if (movie.id == id) movie.copy(
                    title = cleanTitle,
                    rating = rating,
                    posterUrl = posterUrl.trim()
                ) else movie
            }
        }
    }

    fun deleteMovie(id: Long) {
        _movies.value = _movies.value.filterNot { it.id == id }
    }
}
