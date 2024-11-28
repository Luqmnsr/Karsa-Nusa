package com.example.karsanusa.view.authentication.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextWatcher
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.karsanusa.R
import com.example.karsanusa.data.preference.UserModel
import com.example.karsanusa.databinding.ActivityLoginBinding
import com.example.karsanusa.view.activity.MainActivity
import com.example.karsanusa.view.authentication.register.RegisterActivity
import com.example.karsanusa.view.vmfactory.SessionViewModelFactory

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel by viewModels<LoginViewModel> {
        SessionViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
        playAnimation()
        setupPasswordValidation()
        setupFormValidation()
        styleSignupButton()
        setupDismissKeyboard()
        setupHyperText()

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
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            loginViewModel.saveSession(UserModel(email, "sample_token"))
            AlertDialog.Builder(this).apply {
                setTitle("Yeah!")
                setMessage("Anda berhasil login. Sudah tidak sabar untuk belajar ya?")
                setPositiveButton("Lanjut") { _, _ ->
                    val intent = Intent(context, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                }
                create()
                show()
            }
        }
        binding.hyperTextView.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

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

    private fun setupDismissKeyboard() {
        // Menyembunyikan keyboard jika pengguna mengklik area di luar EditText
        binding.root.setOnClickListener {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }

        // Agar keyboard tidak tertutup saat mengetik di EditText
        binding.emailEditText.setOnClickListener {
            // Biarkan keyboard muncul saat pengguna mengetik
        }
        binding.passwordEditText.setOnClickListener {
            // Biarkan keyboard muncul saat pengguna mengetik
        }
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
                val emailFilled = binding.emailEditText.text.toString().isNotEmpty()
                val passwordFilled = binding.passwordEditText.text.toString().isNotEmpty() &&
                        binding.passwordEditText.text.toString().length >= 8

                // Enable the signup button only if all fields are filled
                binding.loginButton.isEnabled = emailFilled && passwordFilled
            }

            override fun afterTextChanged(s: Editable?) {}
        }

        // Attach TextWatcher to each EditText
        binding.emailEditText.addTextChangedListener(textWatcher)
        binding.passwordEditText.addTextChangedListener(textWatcher)
    }

    private fun styleSignupButton() {
        // Set custom styles for login button
        binding.loginButton.apply {
            background = if (isEnabled) {
                ContextCompat.getDrawable(context, R.drawable.bg_button)
            } else {
                ContextCompat.getDrawable(context, R.drawable.bg_button_disable)
            }
            setTextColor(ContextCompat.getColor(context, R.color.white))
            setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.button_text_size))
            gravity = Gravity.CENTER
            text = if (isEnabled) {
                context.getString(R.string.button_login_text_enabled)
            } else {
                context.getString(R.string.button_login_text_disabled)
            }
        }
    }

    private fun setupHyperText() {
        val spannableTextA = getString(R.string.hypertext)
        val spannableTextB = getString(R.string.hypertext_spanned)
        val fullText = spannableTextA + spannableTextB // "Belum punya akun? Daftar Sekarang"
        val startIndex = fullText.indexOf(spannableTextB)
        val endIndex = startIndex + spannableTextB.length

        val spannableString = SpannableString(fullText)

        // ClickableSpan untuk menangani klik dan mempertahankan underline
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intent)
            }

            override fun updateDrawState(ds: android.text.TextPaint) {
                super.updateDrawState(ds)
                ds.color = ContextCompat.getColor(this@LoginActivity, R.color.hypertext_auth_500)
                ds.isUnderlineText = true // Pastikan underline tetap ada
            }
        }

        spannableString.setSpan(clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        // Atur teks ke TextView
        binding.hyperTextView.text = spannableString
        binding.hyperTextView.movementMethod = android.text.method.LinkMovementMethod.getInstance()

        // Atur OnTouchListener untuk mengubah warna saat disentuh
        binding.hyperTextView.setOnTouchListener { view, event ->
            when (event.action) {
                android.view.MotionEvent.ACTION_DOWN -> {
                    // Ubah warna teks saat disentuh
                    spannableString.setSpan(
                        ForegroundColorSpan(ContextCompat.getColor(this, R.color.hypertext_auth_200)),
                        startIndex,
                        endIndex,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    binding.hyperTextView.text = spannableString
                }
                android.view.MotionEvent.ACTION_UP -> {
                    // Kembalikan warna asli
                    spannableString.setSpan(
                        ForegroundColorSpan(ContextCompat.getColor(this, R.color.hypertext_auth_500)),
                        startIndex,
                        endIndex,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    binding.hyperTextView.text = spannableString
                    view.performClick() // Panggil performClick untuk aksesibilitas
                }
            }
            false // Teruskan event ke LinkMovementMethod
        }

        // Tambahkan performClick eksplisit
        binding.hyperTextView.setOnClickListener {
            // Jika diperlukan aksi tambahan, tambahkan di sini
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(100)
        val message =
            ObjectAnimator.ofFloat(binding.messageTextView, View.ALPHA, 1f).setDuration(100)
        val emailTextView =
            ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(100)
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val passwordTextView =
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(100)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val login = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(
                title,
                message,
                emailTextView,
                emailEditTextLayout,
                passwordTextView,
                passwordEditTextLayout,
                login
            )
            startDelay = 100
        }.start()
    }

}
