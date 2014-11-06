package com.taskworld.android.restfulandroidkotlin.fragments

import com.taskworld.android.restfulandroidkotlin.R
import android.view.View
import kotlin.properties.Delegates
import com.taskworld.android.restfulandroidkotlin.extensions.bindView
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ImageView
import android.view.LayoutInflater
import com.taskworld.android.restfulandroidkotlin.model.Movie
import android.support.v7.widget.LinearLayoutManager
import com.taskworld.android.restfulandroidkotlin.network.resource.client.ResourceClient
import com.taskworld.android.restfulandroidkotlin.network.resource.router.ResourceRouterImpl
import android.os.Bundle
import com.squareup.picasso.Picasso

/**
 * Created by Kittinun Vantasin on 11/6/14.
 */

class MovieGridFragment : BaseSpiceFragment() {

    override val mContentLayoutResourceId: Int = R.layout.fragment_movie_grid

    var mItems by Delegates.observable(arrayListOf<Movie>(), { meta, oldItems, newItems ->
        mMovieAdapter.notifyDataSetChanged()
    })

    //widgets
    val rvMovie by Delegates.lazy { getRootView()!!.bindView<RecyclerView>(R.id.rvMovie) }
    val mMovieAdapter by Delegates.lazy { MovieRecycleViewAdapter() }

    //data
    var mAction: String? = null

    class object {

        val ARG_MOVIE_ACTION = "movie_action"

        fun newInstance(action: String): MovieGridFragment {
            val fragment = MovieGridFragment()
            val args = Bundle()
            args.putString(ARG_MOVIE_ACTION, action)
            fragment.setArguments(args)
            return fragment
        }
    }

    override fun setUp() {
    }

    override fun setUpUI(view: View?) {
        rvMovie.setLayoutManager(createLayoutManager())
        //set adapter
        rvMovie.setAdapter(mMovieAdapter)

        val client = ResourceClient.Builder()
                .setRouter(ResourceRouterImpl.newInstance(mAction!!))
                .setSpiceManager(getServiceSpiceManager()).build()
        client.findAll(javaClass<Movie>())
    }

    override fun handleArguments(args: Bundle) {
        mAction = args.getString(ARG_MOVIE_ACTION)
    }

    fun onEvent(items: Movie.ResultList) {
        mItems = items.getResults().toArrayList()
    }

    fun createLayoutManager(): RecyclerView.LayoutManager {
        return LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false)
    }

    inner class MovieRecycleViewAdapter() : RecyclerView.Adapter<MovieRecycleViewAdapter.MovieViewHolder>() {

        override fun onCreateViewHolder(container: ViewGroup?, viewType: Int): MovieViewHolder? {
            val view = LayoutInflater.from(container?.getContext()).inflate(R.layout.recycle_view_item_movie, container, false)
            return MovieViewHolder(view)
        }

        override fun onBindViewHolder(viewHolder: MovieViewHolder, position: Int) {
            val m = mItems.get(position)
            viewHolder.tvMovieTitle.setText(m.getTitle())
            viewHolder.tvMoviePopularScore.setText(m.getPopularity().toString())
            Picasso.with(getActivity()).load("https://image.tmdb.org/t/p/w500/" + m.getPosterPath()).into(viewHolder.ivMovieCover)
        }

        override fun getItemCount(): Int {
            return mItems.size
        }

        inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val tvMovieTitle by Delegates.lazy { itemView.bindView<TextView>(R.id.tvMovieTitle) }
            val tvMoviePopularScore by Delegates.lazy { itemView.bindView<TextView>(R.id.tvMoviePopularScore) }
            val ivMovieCover by Delegates.lazy { itemView.bindView<ImageView>(R.id.ivMovieCover) }
        }
    }
}