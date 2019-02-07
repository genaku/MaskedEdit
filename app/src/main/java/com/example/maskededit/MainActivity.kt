package com.example.maskededit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var bump = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button.setOnClickListener {
            editText.mask = if (bump) "8 (###) Y ###/##/##" else "+7(###) ###-##-##"
            bump = !bump
        }
        button2.setOnClickListener {
            editText.setUnmaskedString("0123")
        }
    }
}
