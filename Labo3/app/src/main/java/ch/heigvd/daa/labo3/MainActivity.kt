package ch.heigvd.daa.labo3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ArrayAdapter
import ch.heigvd.daa.labo3.databinding.ActivityMainBinding
import ch.heigvd.daa.labo3.model.Person

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.radioGroupOccupation.clearCheck()
        setClickListeners()
    }

    private fun setClickListeners() {

        with(binding) {

            ArrayAdapter.createFromResource(
                this@MainActivity,
                R.array.nationalities,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerNationality.adapter = adapter
            }

            imageButtonCake.setOnClickListener {
                //TODO : Material Datepicker (make a function)
            }

            editTextBirthdate.setOnClickListener {
                //TODO : call the same function as the imageButtonCake onClickListener
            }

            buttonCancel.setOnClickListener {
                //TODO
            }

            buttonSave.setOnClickListener {
                //TODO
            }
            radioGroupOccupation.setOnCheckedChangeListener { _, _ ->
                setGroup()
            }
        }
    }

    private fun setStudentDefault(){
        val student = Person.exampleStudent
        setPersonDefault(student)
        with(binding){
            editTextStudentSchool.setText(student.university)
            editTextStudentGraduationyear.setText(student.graduationYear)
        }
    }

    private fun setWorkerDefault(){
        val worker = Person.exampleWorker
        setPersonDefault(worker)
        with(binding){
            editTextWorkerCompany.setText(worker.company)
            //spinnerWorkerSector
            editTextWorkerExperience.setText(worker.experienceYear)
        }
    }

    private fun setPersonDefault(person: Person){
        with(binding){
            editTextName.setText(person.firstName)
            editTextSurname.setText(person.name)
            editTextBirthdate.setText(Person.dateFormatter.format(person.birthDay.time))
            //spinnerNationality
            editTextEmail.setText(person.email)
            editTextComments.setText(person.remark)
        }
    }

    private fun setGroup() {
        with(binding) {
            when (radioGroupOccupation.checkedRadioButtonId) {
                radioButtonStudent.id -> {
                    groupStudent.visibility = VISIBLE
                    groupWorker.visibility = GONE
                    setStudentDefault()
                }

                radioButtonWorker.id -> {
                    groupStudent.visibility = GONE
                    groupWorker.visibility = VISIBLE
                    setWorkerDefault()
                }

                else -> {
                    groupStudent.visibility = GONE
                    groupWorker.visibility = GONE
                }
            }
        }
    }
}