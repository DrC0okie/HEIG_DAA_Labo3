package ch.heigvd.daa.labo3

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ch.heigvd.daa.labo3.databinding.ActivityMainBinding
import ch.heigvd.daa.labo3.model.Person
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Spinner
import android.widget.Toast
import ch.heigvd.daa.labo3.adapters.CustomSpinnerAdapter
import ch.heigvd.daa.labo3.model.Student
import ch.heigvd.daa.labo3.model.Worker
import java.util.Date
import java.time.Instant
import java.util.Calendar

/**
 * Main entry point activity. Allows the user to fill a form and save it
 * @author Timothée Van Hove, Léo Zmoos
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var datePicker: MaterialDatePicker<Long>

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initDatePicker()
        initClickListeners()
        initSpinners()
        binding.radioGroupOccupation.clearCheck()
    }

    /**
     * Initializes the date picker with constraints to prevent selection of future dates.
     */
    private fun initDatePicker() {

        //Forbid dates in the future and dates less than 110 years in the past
        val dateConstraints = CalendarConstraints.Builder()
            .setValidator(DateValidatorPointBackward.now())
            .setStart(Calendar.getInstance().also { it.add(Calendar.YEAR, -110) }.timeInMillis)
            .build()

        datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText(resources.getString(R.string.main_base_birthdate_dialog_title))
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .setCalendarConstraints(dateConstraints)
            .build()
    }

    /**
     * Initializes click listeners for the main form components
     */
    private fun initClickListeners() {

        with(binding) {
            imageButtonCake.setOnClickListener {
                datePicker.show(supportFragmentManager, "DatePicker")
            }

            editTextBirthdate.setOnClickListener {
                datePicker.show(supportFragmentManager, "DatePicker")
            }

            buttonCancel.setOnClickListener {
                resetForm()
            }

            buttonSave.setOnClickListener {
                if (validateInputs()) {
                    Log.i(
                        MainActivity::class.java.simpleName, if (radioButtonStudent.isChecked) {
                            createStudentFromForm()
                        } else {
                            createWorkerFromForm()
                        }.toString()
                    )
                }
            }

            radioGroupOccupation.setOnCheckedChangeListener { _, checkedId ->
                handleRadioButtonChange(checkedId)
                radioButtonWorker.error = null
            }

            datePicker.addOnPositiveButtonClickListener {
                editTextBirthdate.setText(Person.dateFormatter.format(Date(it)))
            }
        }
    }

    /**
     * Retrieves a Calendar instance based on the user input from the form.
     * @return Calendar instance representing the user-selected date or current date if parsing fails.
     */
    private fun getCalendarFromForm(): Calendar {
        return Calendar.getInstance().also {
            it.time =
                Person.dateFormatter.parse(binding.editTextBirthdate.text.toString()) ?: Date.from(
                    Instant.now()
                )
        }
    }

    /**
     * Creates a Student object from the form inputs.
     * @return Student object populated with user inputs.
     */
    private fun createStudentFromForm(): Student {
        with(binding) {
            return Student(
                editTextSurname.text.toString(),
                editTextName.text.toString(),
                getCalendarFromForm(),
                spinnerNationality.selectedItem.toString(),
                editTextStudentSchool.text.toString(),
                editTextStudentGraduationyear.text.toString().toInt(),
                editTextEmail.text.toString(),
                editTextComments.text.toString()
            )
        }
    }

    /**
     * Creates a Worker object from the form inputs.
     * @return Worker object populated with user inputs.
     */
    private fun createWorkerFromForm(): Worker {
        with(binding) {
            return Worker(
                editTextSurname.text.toString(),
                editTextName.text.toString(),
                getCalendarFromForm(),
                spinnerNationality.selectedItem.toString(),
                editTextWorkerCompany.text.toString(),
                spinnerWorkerSector.selectedItem.toString(),
                editTextWorkerExperience.text.toString().toInt(),
                editTextEmail.text.toString(),
                editTextComments.text.toString()
            )
        }
    }

    /**
     * Sets default values in the form based on the provided Person object.
     * @param person Person instance (Student or Worker) whose data is used to set form defaults.
     */
    private fun setPersonDefaults(person: Person) {
        with(binding) {
            editTextName.setText(person.firstName)
            editTextSurname.setText(person.name)
            editTextBirthdate.setText(Person.dateFormatter.format(person.birthDay.time))
            editTextEmail.setText(person.email)
            editTextComments.setText(person.remark)
            setSpinnerValue(spinnerNationality, person.nationality)

            when (person) {
                is Student -> {
                    editTextStudentSchool.setText(person.university)
                    editTextStudentGraduationyear.setText(person.graduationYear.toString())
                }

                is Worker -> {
                    setSpinnerValue(spinnerWorkerSector, person.sector)
                    editTextWorkerCompany.setText(person.company)
                    editTextWorkerExperience.setText(person.experienceYear.toString())
                }
            }
        }
    }

    /**
     * Sets a specified value in a Spinner.
     * Finds the position of the value in the spinner's adapter and sets the spinner's selection.
     * @param spinner The Spinner in which the value needs to be set.
     * @param value The value to be set in the Spinner.
     */
    private fun setSpinnerValue(spinner: Spinner, value: String) {
        val adapter = spinner.adapter as ArrayAdapter<CharSequence>
        spinner.setSelection(adapter.getPosition(value))
    }

    /**
     * Set the form group visibility state based on bool inputs
     */
    private fun setGroupVisibility(studentVisible: Boolean, workerVisible: Boolean) {
        binding.groupStudent.visibility = if (studentVisible) VISIBLE else GONE
        binding.groupWorker.visibility = if (workerVisible) VISIBLE else GONE
    }

    /**
     * Sets the visibility of student and worker groups based on the selected occupation.
     * @param buttonId The id of the selected radio button.
     */
    private fun handleRadioButtonChange(buttonId: Int) {
        with(binding) {
            when (buttonId) {
                radioButtonStudent.id -> {
                    setGroupVisibility(true, false)
                    setPersonDefaults(Person.exampleStudent)
                }

                radioButtonWorker.id -> {
                    setGroupVisibility(false, true)
                    setPersonDefaults(Person.exampleWorker)
                }

                else -> {
                    setGroupVisibility(false, false)
                    spinnerNationality.setSelection(0) // Set to "Sélectionner"
                    editTextBirthdate.setText(Person.dateFormatter.format(Date.from(Instant.now())))
                }
            }
        }
    }

    /**
     * Initializes the spinners with appropriate adapters.
     * Sets up the spinners for nationality and worker sector with data from resources.
     */
    private fun initSpinners() {
        binding.spinnerNationality.adapter = CustomSpinnerAdapter(
            this,
            android.R.layout.simple_spinner_item,
            listOf(getString(R.string.nationality_empty)) + resources.getStringArray(R.array.nationalities)
        ).also { it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }

        binding.spinnerWorkerSector.adapter = ArrayAdapter.createFromResource(
            this, R.array.sectors, android.R.layout.simple_spinner_item
        ).also { a -> a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }
    }

    /**
     * Set the text of all edit texts to an empty string
     */
    private fun clearEditTexts(vararg editTexts: EditText) {
        editTexts.forEach { it.setText("") }
    }

    /**
     * Set the selection of all spinners to 0
     */
    private fun clearSpinners(vararg spinners: Spinner) {
        spinners.forEach { it.setSelection(0) }
    }

    /**
     * Resets the form components to their default state.
     */
    private fun resetForm() {
        with(binding) {
            radioGroupOccupation.clearCheck()
            clearEditTexts(
                editTextName, editTextSurname, editTextEmail, editTextComments,
                editTextStudentSchool, editTextStudentGraduationyear,
                editTextWorkerCompany, editTextWorkerExperience
            )
            clearSpinners(spinnerWorkerSector, spinnerNationality)
        }
    }

    /**
     * Checks if all the given EditText have a value
     * @return the first null or empty EditText
     */
    private fun findFirstEmptyEditText(vararg editTexts: EditText): EditText? {
        return editTexts.find { it.text.toString().trim().isEmpty() }
    }

    /**
     * Validates user inputs in the form by checking whether all fields in the form are filled.
     * @return Boolean indicating whether the validation passed (true) or failed (false).
     */
    private fun validateInputs(): Boolean {
        with(binding) {
            if (radioGroupOccupation.checkedRadioButtonId == RadioButton.NO_ID) {
                //Show an error on the last radio button as indication
                radioButtonWorker.error = resources.getString(R.string.error_occupation_empty)
                showToast(resources.getString(R.string.error_occupation_empty))
                return false
            }

            val emptyEditText = findFirstEmptyEditText(
                editTextName, editTextSurname, editTextEmail, editTextBirthdate
            ) ?: if (radioButtonStudent.isChecked) {
                findFirstEmptyEditText(editTextStudentSchool, editTextStudentGraduationyear)
            } else {
                findFirstEmptyEditText(editTextWorkerCompany, editTextWorkerExperience)
            }

            // Shows an error on the first empty edit text
            if (emptyEditText != null) {
                emptyEditText.error = resources.getString(R.string.error_field_empty)
                return false
            }

            showToast(resources.getString(R.string.success_save))
            return true
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this@MainActivity, message, Toast.LENGTH_LONG).show()
    }
}
