package ch.heigvd.daa.labo3.adapters

import android.content.Context
import android.widget.ArrayAdapter

/**
 * Custom adapter for a Spinner view that sets the first item in the list to be non-selectable.
 * @author Timothée Van Hove, Léo Zmoos
 * @param T Type of objects that this adapter is using.
 * @param context The current context.
 * @param resource The resource ID to use when instantiating views.
 * @param objects The objects to represent in the ListView.
 */
class CustomSpinnerAdapter<T>(context: Context, resource: Int, objects: List<T>) :
    ArrayAdapter<T>(context, resource, objects) {

    /**
     * Determines if the specified position in the list is selectable.
     * @param position The position of the item within the adapter's data set.
     * @return True if the item at the specified position is selectable, false otherwise.
     */
    override fun isEnabled(position: Int): Boolean {
        // Make the first item non-selectable
        return position != 0
    }
}