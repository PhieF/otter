package com.github.apognu.otter.fragments

import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.github.apognu.otter.R
import com.github.apognu.otter.adapters.FavoritesAdapter
import com.github.apognu.otter.repositories.FavoritesRepository
import com.github.apognu.otter.utils.*
import kotlinx.android.synthetic.main.fragment_favorites.*
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class FavoritesFragment : FunkwhaleFragment<Track, FavoritesAdapter>() {
  override val viewRes = R.layout.fragment_favorites
  override val recycler: RecyclerView get() = favorites

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    adapter = FavoritesAdapter(context, FavoriteListener())
    repository = FavoritesRepository(context)

    watchEventBus()
  }

  override fun onResume() {
    super.onResume()

    GlobalScope.launch(Main) {
      RequestBus.send(Request.GetCurrentTrack).wait<Response.CurrentTrack>()?.let { response ->
        adapter.currentTrack = response.track
        adapter.notifyDataSetChanged()
      }
    }

    play.setOnClickListener {
      CommandBus.send(Command.ReplaceQueue(adapter.data.shuffled()))
    }
  }

  private fun watchEventBus() {
    GlobalScope.launch(Main) {
      EventBus.get().collect { message ->
        when (message) {
          is Event.TrackPlayed -> {
            GlobalScope.launch(Main) {
              RequestBus.send(Request.GetCurrentTrack).wait<Response.CurrentTrack>()?.let { response ->
                adapter.currentTrack = response.track
                adapter.notifyDataSetChanged()
              }
            }
          }
        }
      }
    }
  }

  inner class FavoriteListener : FavoritesAdapter.OnFavoriteListener {
    override fun onToggleFavorite(id: Int, state: Boolean) {
      (repository as? FavoritesRepository)?.let { repository ->
        when (state) {
          true -> repository.addFavorite(id)
          false -> repository.deleteFavorite(id)
        }
      }
    }
  }
}
