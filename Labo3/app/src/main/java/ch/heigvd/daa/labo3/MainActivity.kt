package ch.heigvd.daa.labo3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import ch.heigvd.daa.labo3.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.imageButtonCake.setOnClickListener {
            //TODO : Material Datepicker
        }

        binding.buttonCancel.setOnClickListener {
            //TODO
        }

        binding.buttonSave.setOnClickListener {
            //TODO
        }

    }

}