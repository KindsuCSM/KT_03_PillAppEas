package com.kindsu.apppastissencilla

import android.icu.util.Calendar
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kindsu.apppastissencilla.databinding.ActivityMainBinding
import com.kindsu.myapplication.DatePickerFragment
import com.kindsu.myapplication.TimePickerFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var mapPastillas : MutableMap<String, String>
    private lateinit var mapHoras : MutableMap<String, String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //Inicializamos los mapas
        mapPastillas = initMapPastillas()
        mapHoras = initMapHoras()

        //Mostramos la informacion de las listas
        showCurrentDay()
        //Inicializamos listeners
        initListeners()
    }
    // Funcion para inicializar el mapa de pastillas
    private fun initMapPastillas() : MutableMap<String, String> {
        return mutableMapOf (
            "10/11/2024" to "Furosemida",
            "14/11/2024" to "Paracetamol",
            "15/11/2024" to "Tirodril",
            "16/11/2024" to "Paracetamol",
            "17/11/2024" to "Paracetamol",
        )
    }
    // Funcion para inicializar el mapa de pastillas
    private fun initMapHoras() : MutableMap<String, String> {
        return mutableMapOf (
            "10/11/2024" to "10:00",
            "14/11/2024" to "10:30",
            "15/11/2024" to "11:30",
            "16/11/2024" to "12:30",
            "17/11/2024" to "13:30",
        )
    }
    //Funcion para formatear la fecha de Calendar a String para mostrarlo por pantalla
    private fun formatDate(date : Calendar) : String{
        val day = date.get(Calendar.DAY_OF_MONTH)
        val month = date.get(Calendar.MONTH) + 1
        val year = date.get(Calendar.YEAR)
        return "$day/$month/$year"
    }
    //Funcion para mostrar la pastilla si se encuentra la fecha en la lista y sino poner un string
    private fun showCheckBox(date : String){
        binding.cbPastillaDiaria.text = mapPastillas[date] ?: "No tiene que tomar pastilla"
    }
    //Funcion para mostrar la hora si se encuentra la fecha en la lista y sino poner un string
    private fun showTime(date : String){
        binding.tvHoraMostrarPasti.text = mapHoras[date] ?: "00:00"
    }
    private fun showName(date : String){
        binding.tvFechaMostrarPasti.text = date
    }

    //Funcion para actualizar la fecha, hora y nombre de pastilla en la pantalla
    private fun showCurrentDay(){
        // Crear instancia de calendario para obtener la fecha actual
        val calendar = Calendar.getInstance()
        val currentDate = formatDate(calendar)
        // Inicializar la app con los datos del dia actual en caso de que tenga valores
        showName(currentDate)
        showCheckBox(currentDate)
        showTime(currentDate)
        //Añadir un listener al calendarView para que recoja la fecha seleccionada por el usuario
        binding.cvCalendar.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = Calendar.getInstance().apply {
                set(year, month, dayOfMonth)
            } //Agregar la fecha a un valor
            val formattedDate = formatDate(selectedDate) // Formatear la fecha para que sea de tipo string

            //Actualizar los valores a medida que el usuario escoja fechas del CalendarView
            showName(formattedDate)
            showCheckBox(formattedDate)
            showTime(formattedDate)
        }
    }

    private fun addPillAndTimeToMap(fecha : String, nombre : String, hora : String){
        if(fecha in mapPastillas){
            mapPastillas[fecha] = nombre  //Si se encuentra en la lista, cambiamos el valor del key
        }else{
            mapPastillas.put(fecha, nombre) //Si no se encuentra en la lista, añadimos fecha y valor
        }
        if(fecha in mapHoras){
            mapHoras[fecha] = hora //Si se encuentra en la lista, cambiamos el valor del key
        }else{
            mapHoras.put(fecha, hora) //Si no se encuentra en la lista, añadimos la fecha y valor
        }
    }

    //Muestra las ventanas de fecha y tiempo
    private fun showDatePickerDialog(){
        //Instancia del fragmento de seleccion de fecha y definimos lambda para manejarla
        val datePicker = DatePickerFragment {day, month, year -> onDateSelected(day, month, year)}
        // Muestra el dialog de seleccion de fecha
        datePicker.show(supportFragmentManager, "datePicker")
    }
    private fun showTimePickerDialog(){
        //Instancia del fragmento de seleccion de hora y definimos lambda para manejarla
        val timePicker = TimePickerFragment { onTimeSelected(it) }
        // Muestra el dialog de seleccion de hora
        timePicker.show(supportFragmentManager, "timePicker")
    }
    //Asociamos la fecha y tiempo que hemos cogido de los fragments a sus respectivos elementos
    private fun onTimeSelected(time: String) {
        binding.etHora.setText("$time")
    }
    private fun onDateSelected(day : Int, month : Int, year : Int){
        binding.etFecha.setText("$day/${month + 1}/$year")
    }

    //Añadir los listeners a los EditText y al boton de añadir pastilla
    private fun initListeners(){
        binding.etFecha.setOnClickListener(){
            //Mostramos la pantallita para escoger la fecha
            showDatePickerDialog()
        }
        binding.etHora.setOnClickListener(){
            //Mostramos la pantallita para escoger la hora
            showTimePickerDialog()
        }
        binding.btnAddPastilla.setOnClickListener(){
            //Recogemos los valores que tendremos que haber introducido anteriormente en los TextEdit
            val fecha = binding.etFecha.text.toString()
            val hora = binding.etHora.text.toString()
            val nombre = binding.etNombre.text.toString()

            //Nos aseguramos que los valores contengan algo para recoger la nueva pastilla
            if(fecha.isNotEmpty() && hora.isNotEmpty() && nombre.isNotEmpty()){
                addPillAndTimeToMap(fecha, nombre, hora)
            }
            //Limpiamos todos los EditText
            clearTextViews()
        }
    }
    // Poner los EditText en blanco tras darle al btnAddPastilla
    private fun clearTextViews () {
        binding.etFecha.setText("")
        binding.etHora.setText("")
        binding.etNombre.setText("")
    }
}