package com.example.notesapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.notesapplication.databinding.FragmentRegisterBinding
import com.example.notesapplication.models.UserRequest
import com.example.notesapplication.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    //Create a binding object
    private var _binding : FragmentRegisterBinding? = null
    private val binding get() = _binding !!

    //=====================================================
    //So to initialise viewmodel
    // Get a reference to the ViewModel scoped to this Fragment
    //val viewModel by viewModels<MyViewModel>()

    // Get a reference to the ViewModel scoped to its Activity
    //val viewModel by activityViewModels<MyViewModel>()
    //=====================================================


    //kotlin extension to create viewmodel obj, behind the scene it will point to a AuthViewModel
    private val authViewModel by viewModels<AuthViewModel> ()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Initialize binding object in onCreateView
        _binding = FragmentRegisterBinding.inflate(inflater,container,false)

        //access the field
        binding.btnLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        binding.btnSignUp.setOnClickListener {
            //findNavController().navigate(R.id.action_registerFragment_to_mainFragment)

            //pass the dummy value Or call viewmodel by fragment
            authViewModel.registerUser(UserRequest("abc109@gmail.com","123","abc"))


        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        authViewModel.userResponseLiveData.observe(viewLifecycleOwner, Observer{
            binding.progressBar.isVisible= false
            when(it) {

                is NetworkResult.Success -> {

                    findNavController().navigate(R.id.action_registerFragment_to_mainFragment)
                }
                is NetworkResult.Error -> {
                    binding.txtError.text = it.message

                }
                is NetworkResult.Loading -> {
                    binding.progressBar.isVisible = true

                }

            }

        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}