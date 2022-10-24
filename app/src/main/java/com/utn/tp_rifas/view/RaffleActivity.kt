package com.utn.tp_rifas.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.Observer
import com.utn.tp_rifas.R
import com.utn.tp_rifas.databinding.ActivityMainBinding
import com.utn.tp_rifas.viewmodel.RaffleViewModel

class RaffleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var raffleViewModel : RaffleViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        raffleViewModel = RaffleViewModel(this)

        binding.btSaveRaffleNumber.setOnClickListener {
            raffleViewModel!!.validateInputs(
                binding.etName.text.toString(),
                binding.etLastName.text.toString(),
                binding.etRaffleNumber.text.toString()
            )
        }

        setupViewModelObserver()
        setupEditText()

    }

    private fun setupViewModelObserver(){
        raffleViewModel?.dataValidationMutable?.observe(this, Observer { dataValidation -> dataValidation?.let {
            if (!dataValidation.nameError.isNullOrEmpty()){
                binding.tfName.error = dataValidation.nameError
            }
            if (!dataValidation.lastNameError.isNullOrEmpty()){
                binding.tfLastName.error = dataValidation.lastNameError
            }
            if (!dataValidation.raffleNumberError.isNullOrEmpty()){
                binding.tfRaffleNumber.error = dataValidation.raffleNumberError
            }
        } })
    }

    private fun setupEditText() {
        binding.etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.tfName.error = null
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // Nothing use
            }
            override fun afterTextChanged(p0: Editable?) {
                // Nothing use
            }
        })
        binding.etLastName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.tfLastName.error = null
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // Nothing use
            }
            override fun afterTextChanged(p0: Editable?) {
                // Nothing use
            }
        })
        binding.etRaffleNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.tfRaffleNumber.error = null
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // Nothing use
            }
            override fun afterTextChanged(p0: Editable?) {
                // Nothing use
            }
        })
    }
}