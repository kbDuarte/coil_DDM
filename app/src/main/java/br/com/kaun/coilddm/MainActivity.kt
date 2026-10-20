package br.com.kaun.coilddm

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.kaun.coilddm.databinding.ActivityMainBinding
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MovieViewModel by viewModels()
    private lateinit var movieAdapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        movieAdapter = MovieAdapter(
            onEdit = ::showMovieDialog,
            onDelete = ::confirmDelete
        )
        binding.rvMovies.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = movieAdapter
        }
        binding.btnAddMovie.setOnClickListener { showMovieDialog(null) }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.movies.collect { movies ->
                    movieAdapter.submitList(movies)
                    binding.tvEmpty.visibility =
                        if (movies.isEmpty()) android.view.View.VISIBLE else android.view.View.GONE
                }
            }
        }
    }

    private fun showMovieDialog(movie: Movie?) {
        val dialogView = LayoutInflater.from(this).inflate(
            R.layout.dialog_movie, null, false
        )
        val etTitle = dialogView.findViewById<TextInputEditText>(R.id.etTitle)
        val etRating = dialogView.findViewById<TextInputEditText>(R.id.etRating)
        val etPoster = dialogView.findViewById<TextInputEditText>(R.id.etPoster)

        etTitle.setText(movie?.title.orEmpty())
        etRating.setText(movie?.rating?.toString().orEmpty())
        etPoster.setText(movie?.posterUrl.orEmpty())

        val content = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(24, 8, 24, 0)
            addView(dialogView)
        }

        val dialog = AlertDialog.Builder(this)
            .setTitle(if (movie == null) "Adicionar filme" else "Editar filme")
            .setView(content)
            .setNegativeButton("Cancelar", null)
            .setPositiveButton("Salvar", null)
            .create()

        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                val title = etTitle.text?.toString().orEmpty().trim()
                val ratingText = etRating.text?.toString().orEmpty().replace(',', '.')
                val rating = ratingText.toFloatOrNull()
                val posterUrl = etPoster.text?.toString().orEmpty().trim()

                if (title.isBlank()) {
                    etTitle.error = "Informe o nome do filme"
                    return@setOnClickListener
                }
                if (rating == null || rating !in 0f..10f) {
                    etRating.error = "Use uma nota entre 0 e 10"
                    return@setOnClickListener
                }
                if (posterUrl.isNotBlank() &&
                    !posterUrl.startsWith("https://", ignoreCase = true)
                ) {
                    etPoster.error = "Informe uma URL HTTPS válida"
                    return@setOnClickListener
                }

                viewModel.saveMovie(movie?.id, title, rating, posterUrl)
                dialog.dismiss()
                Toast.makeText(this, "Filme salvo!", Toast.LENGTH_SHORT).show()
            }
        }
        dialog.show()
    }

    private fun confirmDelete(movie: Movie) {
        AlertDialog.Builder(this)
            .setTitle("Remover filme")
            .setMessage("Deseja remover \"${movie.title}\" dos favoritos?")
            .setNegativeButton("Cancelar", null)
            .setPositiveButton("Remover") { _, _ ->
                viewModel.deleteMovie(movie.id)
                Toast.makeText(this, "Filme removido.", Toast.LENGTH_SHORT).show()
            }
            .show()
    }
}
