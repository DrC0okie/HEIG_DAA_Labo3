package ch.heigvd.daa.labo3.adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
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
     * Provides a custom drop-down view for the spinner.
     * The first item in the drop-down is made non-visible and non-selectable to act as a prompt.
     * @param T The type of objects that this adapter is using.
     * @param position The position of the item within the adapter's data set.
     * @param convertView The old view to reuse, if possible.
     * @param parent The parent that this view will eventually be attached to.
     * @return A View corresponding to the data at the specified position.
     */
    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return if(position == 0) {
            View(context).also{it.visibility = View.GONE}
        }else{
            super.getDropDownView(position, null, parent)
        }
    }

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