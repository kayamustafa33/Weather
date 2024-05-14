package com.kotlin.weather.view.mainView

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.kotlin.weather.adapter.FavoriteAdapter
import com.kotlin.weather.databinding.FragmentFavoriteBinding
import com.kotlin.weather.model.Favorite
import com.kotlin.weather.viewModel.FavoriteViewModel

class FavoriteFragment : Fragment(), FavoriteAdapter.OnItemClickListener {

    private lateinit var binding : FragmentFavoriteBinding
    private lateinit var favoriteAdapter: FavoriteAdapter
    private lateinit var favoriteList : ArrayList<Favorite>

    //View modelimizi application parametresine uygun çağırdık
    private val favoriteViewModel: FavoriteViewModel by activityViewModels {
        ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentFavoriteBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Adapter için bileşenleri çağırdık ve yerine koyduk
        favoriteList = ArrayList()
        binding.favoriteRV.layoutManager = LinearLayoutManager(requireContext())
        favoriteAdapter = FavoriteAdapter(favoriteList,this)
        binding.favoriteRV.adapter = favoriteAdapter
        observeFavorite()
    }

    //Favoride ekli elemanları gözlemledik ve listeye ekledik
    private fun observeFavorite() {
        favoriteViewModel.favoritesData.observe(viewLifecycleOwner) {
            it?.let { list ->
                favoriteList.clear()
                favoriteList.addAll(list)
                favoriteAdapter.notifyDataSetChanged()
            }

            if(it.isNullOrEmpty()) binding.noDataText.visibility = View.VISIBLE else binding.noDataText.visibility = View.GONE
        }
    }

    //Eleman silinince listeden sildik
    override fun deleteItem(favorite: Favorite, position: Int?, holder: FavoriteAdapter.ViewHolder) {
        favorite.location?.let { location ->
            favoriteViewModel.deleteLocation(location) {
                favoriteList.remove(favorite)
                favoriteAdapter.notifyItemChanged(position!!)
            }
        }
    }
}