package com.example.cameraxdemo.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.cameraxdemo.R
import com.example.cameraxdemo.adapter.SavedImagesAdapter
import com.example.cameraxdemo.databinding.FragmentHomeBinding
import com.example.cameraxdemo.util.ImageMenuBottomSheetFragment
import kotlinx.coroutines.flow.collect

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel by activityViewModels<HomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentHomeBinding.bind(view)

        viewModel.getUris(requireContext())

        val savedImagesAdapter = SavedImagesAdapter()
        binding.rvImageList.adapter = savedImagesAdapter

        binding.rvImageList.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = savedImagesAdapter
        }

        savedImagesAdapter.setOnItemClickListener { imageUri ->

            val action = HomeFragmentDirections.actionHomeFragmentToViewImageFragment(imageUri, false)
            findNavController().navigate(action)

        }

        savedImagesAdapter.setOnItemLongClickListener {
            val imageMenuBottomSheet = ImageMenuBottomSheetFragment(it)
            imageMenuBottomSheet.show(childFragmentManager, imageMenuBottomSheet.tag)

            imageMenuBottomSheet.setOnDismissedListener {
                viewModel.getUris(requireContext())
            }
        }

        lifecycleScope.launchWhenStarted {

            viewModel.savedImagesUri.collect {

                savedImagesAdapter.submitList(it)
            }
        }

        binding.fab.setOnClickListener {
            if(hasCameraPermission()){
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CAMERA), 0)
            } else {
                findNavController().navigate(R.id.action_homeFragment_to_cameraFragment)
            }
        }
    }

    private fun hasCameraPermission() =
        ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}