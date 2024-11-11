package com.kindsu.myapplication

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import com.kindsu.apppastissencilla.R

/*Creamos una clase TimePickerFragment que extiende de DialoFragment e implementa
* TimePickerDialog.OnTimeSetListener que recibirá una cadena y devuelve Unit (formato hora)*/
class TimePickerFragment (val listener: (String) -> Unit) :
    DialogFragment(), TimePickerDialog.OnTimeSetListener {
    //Sobreescribimos el metodo de DialogFragment()
    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        //Le asignamos los valores que introduce el usuario
        listener("$hourOfDay:$minute")
    }

    //Sobrescribimos el metodo del DialogFragment que se ejecutará cuando se crea la pestaña
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        //Obtenemos los valores que introduce el usuario al clickar sobre la hora
        val calendar = Calendar.getInstance()           //Instanciamos clase calendario
        val hour = calendar.get(Calendar.HOUR_OF_DAY)   //Obtenemos la hora
        val minute = calendar.get(Calendar.MINUTE)      //Obtenemos los minutos

        //Instancia de TimePicjerDialog con la hora que introduce el usuario
        val picker = TimePickerDialog(activity as Context, this, hour, minute, true)
        return picker
    }
}
