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
import com.example.notesapplication.utils.Helper.Companion.hideKeyboard
import com.example.notesapplication.utils.NetworkResult
import com.example.notesapplication.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    //Create a binding object
    private var _binding : FragmentRegisterBinding? = null
    private val binding get() = _binding !!


    @Inject
    lateinit var tokenManager: TokenManager

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


        //when the view is created, check whether the token is null or not
        //case 1: if null stay in FragmentRegisterBinding view
        //case 2: if not null navigate to main fragment
        // if we have splash screen use this validation check
        if (tokenManager.getToken() != null) {
            findNavController().navigate(R.id.action_registerFragment_to_mainFragment)
        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //access the field
        binding.btnLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        binding.btnSignUp.setOnClickListener {
            //findNavController().navigate(R.id.action_registerFragment_to_mainFragment)

            //pass the dummy value Or call viewmodel by fragment
            //authViewModel.registerUser(UserRequest("abc109@gmail.com","123","abc"))


            hideKeyboard(it)
            val validationResult = validateUserInput()
            if (validationResult.first) {
                val userRequest = getUserRequest()
                authViewModel.registerUser(userRequest)
            } else {
                showValidationErrors(validationResult.second)
            }
        }



        bindObservers()
    }

    private fun bindObservers() {
        authViewModel.userResponseLiveData.observe(viewLifecycleOwner, Observer{
            binding.progressBar.isVisible= false
            when(it) {

                is NetworkResult.Success -> {

                    tokenManager.saveToken(it.data!!.token)
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


    private fun showValidationErrors(error: String) {
        binding.txtError.text = String.format(resources.getString(R.string.txt_error_message, error))
    }


    private fun getUserRequest(): UserRequest {
        return binding.run {
            UserRequest(
                txtEmail.text.toString(),
                txtPassword.text.toString(),
                txtUsername.text.toString()
            )
        }
    }

    private fun validateUserInput(): Pair<Boolean, String> {
        val emailAddress = binding.txtEmail.text.toString()
        val userName = binding.txtUsername.text.toString()
        val password = binding.txtPassword.text.toString()
        return authViewModel.validateCredentials(emailAddress, userName, password, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}