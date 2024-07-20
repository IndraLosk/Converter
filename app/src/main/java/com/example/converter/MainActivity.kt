package com.example.converter

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.converter.data.repository.Repository
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.runBlocking

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    private lateinit var putResult:String
    private lateinit var editText: EditText

    val res: Double = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        editText = findViewById(R.id.editTextText)

        val spinner: Spinner = findViewById(R.id.spinner)

        val list = resources.getStringArray(R.array.currency_array)
        val adapter = ArrayAdapter(this, R.layout.spinner_item, list)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                view?.let {
                    val selectedItem = parent.getItemAtPosition(position)
                    putResult = selectedItem.toString()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                putResult = "USD"
            }
        }
    }

    fun getCourseValue(courseValue:String): Array<String> {
        var res = 0.0
        var value: Double?
        runBlocking {
            val repository = Repository()
            val response = repository.getCourses()
            if (response.isSuccessful) {
                val courses = response.body()
                value = when (courseValue) {
                    "USD" -> courses?.Valute?.USD?.Value
                    "EUR" -> courses?.Valute?.EUR?.Value
                    "GBP" -> courses?.Valute?.GBP?.Value
                    else -> null
                }
                if(value!=null)
                    res = value.toString().toDouble() * editText.text.toString().toDouble()
            }
        }
        return arrayOf(String.format("%.2f", res), editText.text.toString(), courseValue)
    }

    fun getResult(view: View) {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        if (networkInfo != null && networkInfo.isConnected) {
            try {
                val intent = Intent(this, ResultActivity::class.java)
                val output: Array<String> = getCourseValue(putResult)
                intent.putExtra("res", output[0])
                intent.putExtra("num", output[1])
                intent.putExtra("cod", output[2])
                startActivity(intent)
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                editText.text.clear()

            }catch (e: NumberFormatException) {
                Snackbar.make(view, "Ошибка ввода данных", Snackbar.LENGTH_SHORT).show()
            }
        } else {
            Snackbar.make(view, "Отсутвует подключение к интернету", Snackbar.LENGTH_SHORT).show()
        }
    }
}