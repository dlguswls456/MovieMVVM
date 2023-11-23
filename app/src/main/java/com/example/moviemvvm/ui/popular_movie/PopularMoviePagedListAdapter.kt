package com.example.moviemvvm.ui.popular_movie

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviemvvm.data.api.POSTER_BASE_URL
import com.example.moviemvvm.data.repository.NetworkState
import com.example.moviemvvm.data.value_object.Movie
import com.example.moviemvvm.databinding.MovieListItemBinding
import com.example.moviemvvm.databinding.NetworkStateItemBinding
import com.example.moviemvvm.ui.single_movie_details.SingleMovieActivity

class PopularMoviePagedListAdapter(val context: Context) :
    PagedListAdapter<Movie, RecyclerView.ViewHolder>(MovieDiffCallback()) {

    val MOVIE_VIEW_TYPE = 1
    val NETWORK_VIEW_TYPE = 2

    private var networkState: NetworkState? = null

    fun setNetworkState(newNetworkState: NetworkState) {
        val previousState: NetworkState? = this.networkState
        val hadExtraRow: Boolean = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow: Boolean = hasExtraRow()

        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                // remove the progressbar at the end
                // 재정의한 함수가 아니라 원래 함수 사용(super)
                notifyItemRemoved(super.getItemCount()) // remove the progressbar at the end
            } else {
                notifyItemInserted(super.getItemCount()) // add the progressbar at the end
            }
        } else if (hasExtraRow && previousState != networkState) {
            notifyItemChanged(itemCount - 1)
        }
    }

    private fun hasExtraRow(): Boolean {
        return networkState != null && networkState != NetworkState.LOADED
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            NETWORK_VIEW_TYPE
        } else {
            MOVIE_VIEW_TYPE
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == MOVIE_VIEW_TYPE) {
            (holder as MovieItemViewHolder).bind(getItem(position), context)
        } else {
            (holder as NetworkStateItemViewHolder).bind(networkState)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        if (viewType == MOVIE_VIEW_TYPE) {
            val binding: MovieListItemBinding =
                MovieListItemBinding.inflate(
                    layoutInflater,
                    parent,
                    false
                )
            return MovieItemViewHolder(binding)
        } else {
            val binding: NetworkStateItemBinding =
                NetworkStateItemBinding.inflate(
                    layoutInflater,
                    parent,
                    false
                )
            return NetworkStateItemViewHolder(binding)
        }
    }

    class MovieDiffCallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    }

    class MovieItemViewHolder(private val binding: MovieListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie?, context: Context) {
            binding.cvMovieTitle.text = movie?.title
            binding.cvMovieReleaseDate.text = movie?.releaseDate

            val moviePosterURL = POSTER_BASE_URL + movie?.posterPath
            Glide.with(binding.root.context)
                .load(moviePosterURL)
                .into(binding.cvIvMoviePoster)

            binding.root.setOnClickListener {
                val intent = Intent(context, SingleMovieActivity::class.java)
                intent.putExtra("id", movie?.id)
                context.startActivity(intent)
            }
        }
    }

    class NetworkStateItemViewHolder(private val binding: NetworkStateItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(networkState: NetworkState?) {
            if (networkState != null && networkState == NetworkState.LOADING) {
                binding.progressBarItem.visibility = View.VISIBLE
            } else {
                binding.progressBarItem.visibility = View.GONE
            }

            if (networkState != null && networkState == NetworkState.ERROR) {
                binding.errorMsgItem.visibility = View.VISIBLE
                binding.errorMsgItem.text = networkState.msg
            } else if (networkState != null && networkState == NetworkState.ENDOFLIST) {
                binding.errorMsgItem.visibility = View.VISIBLE
                binding.errorMsgItem.text = networkState.msg
            } else {
                binding.errorMsgItem.visibility = View.GONE
            }
        }
    }
}