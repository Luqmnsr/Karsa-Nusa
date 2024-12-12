package com.example.karsanusa.view.authentication.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.karsanusa.R
import com.example.karsanusa.databinding.ActivityRegisterBinding
import com.example.karsanusa.view.vmfactory.SessionViewModelFactory
import com.example.karsanusa.data.result.Result
import com.example.karsanusa.view.authentication.login.LoginActivity

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    private val registerViewModel by viewModels<RegisterViewModel> {
        SessionViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
        playAnimation()
        setupPasswordValidation()
        setupFormValidation()

        setupClearButton(binding.nameEditText)
        setupClearButton(binding.emailEditText)
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() {
        binding.registerButton.setOnClickListener {
            val fullname = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (fullname.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, getString(R.string.empty_field_error), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            showLoading(true)

            registerViewModel.register(fullname, email, password).observe(this) { result ->
                showLoading(false)
                when (result) {
                    is Result.Success -> {
                        showDialog(isSuccess = true, email = email)
                    }
                    is Result.Error -> {
                        showDialog(isSuccess = false, email = email)
                    }
                    is Result.Loading -> {
                        showLoading(true)
                    }
                }
            }
        }
    }

    private fun showDialog(isSuccess: Boolean, email: String) {
        showLoading(false)
        val dialogBuilder = AlertDialog.Builder(this)

        if (isSuccess) {
            // Dialog Success
            dialogBuilder.apply {
                setTitle(getString(R.string.register_success_title))
                setMessage(getString(R.string.register_success_message, email))
                setPositiveButton(getString(R.string.register_success_positive_button)) { _, _ ->
                    val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK

                    startActivity(intent)
                    finish()
                    finish()
                }
            }
        } else {
            // Dialog Error
            dialogBuilder.apply {
                setTitle(getString(R.string.register_error_title))
                setMessage(getString(R.string.register_error_message_email_used, email))
                setPositiveButton(getString(R.string.register_error_positive_button)) { dialog, _ ->
                    dialog.dismiss()
                }
            }
        }
        //Show Dialog
        dialogBuilder.create().show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.registerButton.isEnabled = !isLoading
    }

    //Style
    private fun setupClearButton(editText: EditText) {
        val clearButtonImage: Drawable =
            ContextCompat.getDrawable(this, R.drawable.ic_close) as Drawable

        // Set up the listener to show or hide the clear button based on the text content
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty()) {
                    editText.setCompoundDrawablesWithIntrinsicBounds(
                        null,
                        null,
                        clearButtonImage,
                        null
                    )
                } else {
                    editText.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }

    private fun setupPasswordValidation() {
        binding.passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.passwordEditTextLayout.error = if (s != null && s.length < 8) {
                    binding.root.context.getString(R.string.password_error_message)
                } else {
                    null
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun setupFormValidation() {
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val nameFilled = binding.nameEditText.text.toString().isNotEmpty()
                val emailFilled = binding.emailEditText.text.toString().isNotEmpty()
                val passwordFilled = binding.passwordEditText.text.toString().isNotEmpty() &&
                        binding.passwordEditText.text.toString().length >= 8

                // Enable the signup button only if all fields are filled
                binding.registerButton.isEnabled = nameFilled && emailFilled && passwordFilled
                styleSignupButton()
            }

            override fun afterTextChanged(s: Editable?) {}
        }

        // Attach TextWatcher to each EditText
        binding.nameEditText.addTextChangedListener(textWatcher)
        binding.emailEditText.addTextChangedListener(textWatcher)
        binding.passwordEditText.addTextChangedListener(textWatcher)
    }

    private fun styleSignupButton() {
        // Set custom styles for the signup button without hardcoding
        binding.registerButton.apply {
            background = if (isEnabled) {
                ContextCompat.getDrawable(context, R.drawable.bg_button)
            } else {
                ContextCompat.getDrawable(context, R.drawable.bg_button_disable)
            }
            setTextColor(ContextCompat.getColor(context, R.color.white))
            setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.button_text_size))
            gravity = Gravity.CENTER
            text = if (isEnabled) {
                context.getString(R.string.button_register_text_enabled)
            } else {
                context.getString(R.string.button_register_text_disabled)
            }
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(100)
        val nameTextView =
            ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(100)
        val nameEditTextLayout =
            ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val emailTextView =
            ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(100)
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val passwordTextView =
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(100)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val signup = ObjectAnimator.ofFloat(binding.registerButton, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(
                title,
                nameTextView,
                nameEditTextLayout,
                emailTextView,
                emailEditTextLayout,
                passwordTextView,
                passwordEditTextLayout,
                signup
            )
            startDelay = 100
        }.start()
    }
}
