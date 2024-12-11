package com.example.karsanusa.view.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.karsanusa.R
import com.example.karsanusa.data.local.entity.CarouselEntity
import com.example.karsanusa.databinding.FragmentHomeBinding
import com.example.karsanusa.view.activity.batik.BatikActivity
import com.example.karsanusa.view.adapter.CarouselAdapter
import com.example.karsanusa.view.authentication.login.LoginActivity
import com.example.karsanusa.view.authentication.welcome.WelcomeActivity
import com.google.android.material.carousel.CarouselLayoutManager
import com.google.android.material.carousel.CarouselSnapHelper
import com.google.android.material.carousel.FullScreenCarouselStrategy

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // DUMMY CODE; IGNORE FROM THIS ON WARD
        val array = resources.obtainTypedArray(R.array.carousel_images)
        val adapter = CarouselAdapter()
        val list = ArrayList<CarouselEntity>()

        list.add(CarouselEntity(array.getResourceId(0, -1)))
        list.add(CarouselEntity(array.getResourceId(1, -1)))
        list.add(CarouselEntity(array.getResourceId(2, -1)))
        list.add(CarouselEntity(array.getResourceId(3, -1)))

        val layoutManager = CarouselLayoutManager(FullScreenCarouselStrategy())
        layoutManager.carouselAlignment = CarouselLayoutManager.ALIGNMENT_CENTER
        binding.homeCarouselRecyclerView.adapter = adapter
        binding.homeCarouselRecyclerView.layoutManager = layoutManager
        CarouselSnapHelper().attachToRecyclerView(binding.homeCarouselRecyclerView)
        adapter.submitList(list)
        // END OF DUMMY CODE

        binding.dummyButtonToLogin.setOnClickListener {
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
        }

        binding.dummyButtonToWelcome.setOnClickListener {
            val intent = Intent(requireContext(), WelcomeActivity::class.java)
            startActivity(intent)
        }

        binding.button2.setOnClickListener {
            val intent = Intent(requireContext(), BatikActivity::class.java)
            startActivity(intent)
        }
    }
}
