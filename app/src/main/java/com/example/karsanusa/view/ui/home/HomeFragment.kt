package com.example.karsanusa.view.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.karsanusa.R
import com.example.karsanusa.data.local.entity.CarouselEntity
import com.example.karsanusa.databinding.FragmentHomeBinding
import com.example.karsanusa.view.activity.batik.BatikActivity
import com.example.karsanusa.view.adapter.CarouselAdapter
import com.example.karsanusa.view.adapter.NewsAdapter
import com.example.karsanusa.view.authentication.login.LoginActivity
import com.example.karsanusa.view.authentication.welcome.WelcomeActivity
import com.example.karsanusa.view.vmfactory.HomeViewModelFactory
import com.google.android.material.carousel.CarouselLayoutManager
import com.google.android.material.carousel.CarouselSnapHelper
import com.google.android.material.carousel.FullScreenCarouselStrategy
import com.example.karsanusa.data.result.Result
import com.example.karsanusa.view.ui.DetailActivity

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var newsAdapter: NewsAdapter

    private val homeViewModel: HomeViewModel by viewModels {
        HomeViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerViews()
        observeViewModel()

        val array = resources.obtainTypedArray(R.array.carousel_images)
        val adapter = CarouselAdapter()
        val list = ArrayList<CarouselEntity>()

        for (i in 0 until array.length()) {
            list.add(CarouselEntity(array.getResourceId(i, -1)))
        }
        array.recycle()

        val layoutManager = CarouselLayoutManager(FullScreenCarouselStrategy()).apply {
            carouselAlignment = CarouselLayoutManager.ALIGNMENT_CENTER
        }

        binding.homeCarouselRecyclerView.layoutManager = layoutManager
        binding.homeCarouselRecyclerView.adapter = adapter
        CarouselSnapHelper().attachToRecyclerView(binding.homeCarouselRecyclerView)
        adapter.submitList(list)

        binding.dummyButtonToLogin.setOnClickListener {
            startActivity(Intent(requireContext(), LoginActivity::class.java))
        }

        binding.dummyButtonToWelcome.setOnClickListener {
            startActivity(Intent(requireContext(), WelcomeActivity::class.java))
        }

        binding.button2.setOnClickListener {
            startActivity(Intent(requireContext(), BatikActivity::class.java))
        }
    }

    private fun setupRecyclerViews() {
        newsAdapter = NewsAdapter { story ->
            val intent = Intent(requireContext(), DetailActivity::class.java).apply {
                putExtra("EXTRA_STORY_ID", story.position)
            }
            startActivity(intent)
        }

        binding.rvCulturalNews.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = newsAdapter
        }
    }

    private fun observeViewModel() {
        homeViewModel.getSession().observe(viewLifecycleOwner) { user ->
            if (!user.isLogin) {
                startActivity(Intent(requireContext(), WelcomeActivity::class.java))
                requireActivity().finish()
            }
        }

        homeViewModel.getNews().observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBarCulturalNews.visibility = View.VISIBLE
                    binding.tvEmptyMessage.visibility = View.GONE
                }
                is Result.Success -> {
                    binding.progressBarCulturalNews.visibility = View.GONE
                    binding.tvEmptyMessage.visibility = View.GONE
                    newsAdapter.submitList(result.data)
                }
                is Result.Error -> {
                    binding.progressBarCulturalNews.visibility = View.GONE
                    binding.tvEmptyMessage.visibility = View.VISIBLE
                    binding.tvEmptyMessage.text = getString(R.string.failed_to_load_news)
                    Toast.makeText(requireContext(), "Error: ${result.error}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
