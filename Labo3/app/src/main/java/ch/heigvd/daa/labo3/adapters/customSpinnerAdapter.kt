package ch.heigvd.daa.labo3.adapters

import android.content.Context
import android.widget.ArrayAdapter

class CustomSpinnerAdapter<T>(context: Context, resource: Int, objects: List<T>) :
    ArrayAdapter<T>(context, resource, objects) {

    override fun isEnabled(position: Int): Boolean {
        // Make the first item non-selectable
        return position != 0
    }
}