package ch.heigvd.daa.labo3

import android.content.Context
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

        initAdapters()
        setClickListeners()
        initRadioButtons()
    }

    private fun setClickListeners() {

        with(binding) {
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
            radioGroupOccupation.setOnCheckedChangeListener { _, checkedId ->
                setGroupVisibility(checkedId)
            }
        }
    }

    private fun getSpinnerAdapter(resource: Int, context: Context): ArrayAdapter<CharSequence> {
        return ArrayAdapter.createFromResource(
            context, resource, android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
    }

    private fun initRadioButtons(){
        binding.radioGroupOccupation.clearCheck()
    }

    private fun initAdapters(){
        binding.spinnerNationality.adapter = getSpinnerAdapter(R.array.nationalities, this)
        binding.spinnerWorkerSector.adapter = getSpinnerAdapter(R.array.sectors, this)
    }

    private fun setStudentDefault() {
        val student = Person.exampleStudent
        setPersonDefault(student)
        with(binding) {
            editTextStudentSchool.setText(student.university)
            editTextStudentGraduationyear.setText(student.graduationYear)
        }
    }

    private fun setWorkerDefault() {
        val worker = Person.exampleWorker
        setPersonDefault(worker)
        with(binding) {
            editTextWorkerCompany.setText(worker.company)
            //spinnerWorkerSector
            editTextWorkerExperience.setText(worker.experienceYear)
        }
    }

    private fun setPersonDefault(person: Person) {
        with(binding) {
            editTextName.setText(person.firstName)
            editTextSurname.setText(person.name)
            editTextBirthdate.setText(Person.dateFormatter.format(person.birthDay.time))
            //spinnerNationality
            editTextEmail.setText(person.email)
            editTextComments.setText(person.remark)
        }
    }

    private fun setGroupVisibility(buttonId: Int){
        with(binding){
            when(buttonId){
                radioButtonStudent.id ->{
                    groupStudent.visibility = VISIBLE
                    groupWorker.visibility = GONE
                }

                radioButtonWorker.id -> {
                    groupStudent.visibility = GONE
                    groupWorker.visibility = VISIBLE
                }
                else -> {
                    groupStudent.visibility = GONE
                    groupWorker.visibility = GONE
                }
            }
        }
    }

    private fun updateUI() {
        //TODO
    }
}