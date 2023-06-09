package com.example.vetuserapp.view.auth.component

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.widget.EditText
import androidx.appcompat.content.res.AppCompatResources
import com.example.vetuserapp.R
import com.google.android.material.textfield.TextInputLayout

class CustomTextAuth: TextInputLayout {

    private lateinit var inputText: EditText
    var validPassword = false
    var validEmail = false
    var validUser = false


    constructor(context: Context) : super(context) {
        init(context)
    }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    private fun init(context: Context) {

        when(this.tag){
            "password" -> {
                this.hint = "Password"
                this.startIconDrawable = AppCompatResources.getDrawable(context,R.drawable.ic_lock)
            }
            "email" -> {
                this.hint = "Email"
                this.startIconDrawable = AppCompatResources.getDrawable(context,R.drawable.ic_email)

            }
            "username" -> {
                this.hint = "Name"
                this.startIconDrawable = AppCompatResources.getDrawable(context, R.drawable.ic_profile)
            }
            else ->
                this.hint = "Salah Tag kayaknya... Atau Tagnya ngga kebaca"

        }
        addOnEditTextAttachedListener { textInputLayout ->
            inputText = textInputLayout.editText!!
            when(textInputLayout.tag){
                "password" -> {
                    inputText.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
                    textInputLayout.endIconMode = END_ICON_PASSWORD_TOGGLE
                }
                "email" -> inputText.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            }

            inputText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                    // Do nothing.
                }
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    when(textInputLayout.tag) {
                        "password" -> {
                            validPassword = passValid(s.toString())

                            if (validPassword)
                                textInputLayout.error = null
                            else
                                textInputLayout.error = "Kurang Bagus Passwordnya"
                        }
                        "email" -> {
                            validEmail = emailValid(s.toString())

                            if (validEmail)
                                textInputLayout.error=null
                            else
                                textInputLayout.error = "Kurang Bener Emailnya"
                        }
                        "username" -> {
                            validUser = userValid(s.toString())
                            if (validUser)
                                textInputLayout.error=null
                            else
                                textInputLayout.error = "Jangan Dikosongin"
                        }
                    }


                }
                override fun afterTextChanged(s: Editable) {
                    // Do nothing.
                }
            })
        }
    }

    private fun passValid(s: String) : Boolean {
        return s.length>=6 || s.isEmpty()
    }

    private fun emailValid(s: String) : Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(s).matches() || s.isEmpty()
    }

    private fun userValid(s:String) : Boolean{
        return s.isNotEmpty()
    }
}