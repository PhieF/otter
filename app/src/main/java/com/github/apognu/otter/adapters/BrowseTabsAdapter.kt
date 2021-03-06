package com.github.apognu.otter.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.github.apognu.otter.R
import com.github.apognu.otter.fragments.AlbumsGridFragment
import com.github.apognu.otter.fragments.ArtistsFragment
import com.github.apognu.otter.fragments.FavoritesFragment
import com.github.apognu.otter.fragments.PlaylistsFragment

class BrowseTabsAdapter(val context: Fragment, manager: FragmentManager) : FragmentPagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
  var tabs = mutableListOf<Fragment>()

  override fun getCount(): Int {
    return 4
  }

  override fun getItem(position: Int): Fragment {
    tabs.getOrNull(position)?.let {
      return it
    }

    val fragment = when (position) {
      0 -> ArtistsFragment()
      1 -> AlbumsGridFragment()
      2 -> PlaylistsFragment()
      3 -> FavoritesFragment()
      else -> ArtistsFragment()
    }

    tabs.add(position, fragment)

    return fragment
  }

  override fun getPageTitle(position: Int): String {
    return when (position) {
      0 -> context.getString(R.string.artists)
      1 -> context.getString(R.string.albums)
      2 -> context.getString(R.string.playlists)
      3 -> context.getString(R.string.favorites)
      else -> ""
    }
  }
}