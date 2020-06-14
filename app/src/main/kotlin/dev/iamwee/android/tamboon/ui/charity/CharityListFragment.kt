package dev.iamwee.android.tamboon.ui.charity

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dev.iamwee.android.tamboon.R
import dev.iamwee.android.tamboon.data.CharityInfo
import dev.iamwee.android.tamboon.extensions.errorMessage
import kotlinx.android.synthetic.main.fragment_charity_list.*

@AndroidEntryPoint
class CharityListFragment : Fragment(R.layout.fragment_charity_list) {

    private val viewModel: CharityViewModel by viewModels()

    private val adapter by lazy {
        CharityListAdapter {
            (activity as? Delegate)?.onCharityClicked(it)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerViewCharities.adapter = adapter
        swipeRefreshLayoutCharities.setOnRefreshListener {
            viewModel.getCharities()
        }

        if (adapter.currentList.isEmpty()) {
            viewModel.getCharities()
            swipeRefreshLayoutCharities.isRefreshing = true
        }

        viewModel.charities.observe(viewLifecycleOwner, Observer {
            swipeRefreshLayoutCharities.isRefreshing = false
            adapter.submitList(it)
        })

        viewModel.error.observe(viewLifecycleOwner, Observer {
            Snackbar.make(view, it.errorMessage, Snackbar.LENGTH_LONG).show()
        })
    }

    interface Delegate {
        fun onCharityClicked(info: CharityInfo)
    }

}