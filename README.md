# HEIG_DAA_Labo3
Interactions avec l’utilisateur (Approche MVC)





## Questions

> 4.1 Pour le champ remark, destiné à accueillir un texte pouvant être plus long qu’une seule ligne, quelle configuration particulière faut-il faire dans le fichier XML pour que son comportement soit correct ? Nous pensons notamment à la possibilité de faire des retours à la ligne, d’activer le correcteur orthographique et de permettre au champ de prendre la taille nécessaire.

* Pour activer la saisie sur plusieurs lignes, il faut définir l'attribut `inputType` comme `textMultiLine`. 
* Pour activer le correcteur orthographique, il faut définir l'attribut `inputType` comme `textAutoCorrect` et/ou `textAutoComplete`, selon le cas.
* Pour permettre à l'EditText d'augmenter sa taille au fur et à mesure que des lignes de texte sont ajoutées, il faut fixer l'attribut `maxLines` à une valeur appropriée (ou ne pas le fixer pour qu'il n'y ait pas de maximum). Il faut aussi que l'attribut `height` soit défini sur `wrap_content` pour permettre la croissance de la taille.



Voici notre configuration pour le champ `remark`:
```XML
        <EditText
            android:id="@+id/edit_text_comments"
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:autofillHints="@string/additional_remarks_title"
            android:gravity="top"
            android:inputType="textAutoComplete|textMultiLine"
            android:minHeight="100dp"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toBottomOf="@id/text_view_comments" />
```



> 4.2 Pour afficher la date sélectionnée via le DatePicker nous pouvons utiliser un DateFormat permettant par exemple d’afficher 12 juin 1996 à partir d’une instance de Date. Le formatage des dates peut être relativement différent en fonction des langues, la traduction des mois par exemple, mais également des habitudes régionales différentes : la même date en anglais britannique serait 12th June 1996 et en anglais américain June 12, 1996. Comment peut-on gérer cela au mieux ?

Pour gérer le formatage de la date dans différentes langues, nous devons utiliser `DateFormat` avec prise en charge des paramètres linguistiques. Cela permet de s'assurer que la date est formatée selon les conventions de la locale actuelle de l'utilisateur. Par exemple:

```kotlin
// Obtient le formateur de date avec le style de formatage par défaut pour la locale de l'utilisateur.
val dateFormatter = DateFormat.getDateInstance()
```

Dans notre application, pour afficher la date actuelle nous avons utilisé le `dateFormatter` déjà présent dans la classe Person:
```kotlin
editTextBirthdate.setText(Person.dateFormatter.format(Date.from(Instant.now())))
```



> 4.3 Est-il possible de limiter les dates sélectionnables dans le dialogue, en particulier pour une date de naissance il est peu probable d’avoir une personne née il y a plus de 110 ans ou à une date dans le futur. Comment pouvons-nous mettre cela en place ?

Oui, c'est possible de limiter les dates sélectionnables dans un MaterialDatePicker. Pour ce faire, il faut définir un objet `CalendarConstraints` lors de la création de notre datePicker. Cet objet va ajouter des contraintes temporelles minimum et maximum.

Voici par exemple l'implémentation de notre méthode initDatePicker :

```kotlin
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
```



> 4.4 Lors du remplissage des champs textuels, vous pouvez constater que le bouton « suivant » présent sur le clavier virtuel permet de sauter automatiquement au prochain champ à saisir.
>
> 1) Est-ce possible de spécifier son propre ordre de remplissage du questionnaire ?
> 2) Arrivé sur le dernier champ, est-il possible de faire en sorte que ce bouton soit lié au bouton de validation du questionnaire ?

1. Oui c'est possible de définir un ordre avec l'attribut `android:nextFocusDown` dans le layout. Cet attribut défini l'ID de la prochaine vue sur laquelle mettre le focus quand le bouton `next` est appuyé

2. Oui, c'est possible avec l'attribut `android:imeOptions="actionSend"` ou `android:imeOptions="actionDone"` dans le dernier `EditText` du layout. Ensuite il faut implémenter un `OnEditorActionListener` sur cet EditText dans le code de l'activité pour gérer l'action. Par exemple:

   ```xml
   <EditText
       android:id="@+id/editText"
       android:layout_width="match_parent"
       android:layout_height="wrap_content"
       android:imeOptions="actionSend" />
   ```

   ````kotlin
   binding.editText.setOnEditorActionListener { _, actionId, _ ->
       if (actionId == EditorInfo.IME_ACTION_SEND) {
           submitForm() // Methode qui va envoyer le formulaire (qu'il faut implémenter soi-même)
           true
       } else false
   }
   ````

   
