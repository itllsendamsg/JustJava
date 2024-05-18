package com.example.android.justjava;

/**
 * Add your package below. Package name can be found in the project's AndroidManifest.xml file.
 * This is the package name our example uses:
 * <p>
 * package com.example.android.justjava;
 */

import android.annotation.TargetApi;
import android.content.Intent;
import android.icu.text.NumberFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity
{

    int quantity = 1;


    /**
     * This method is called when the plus button is clicked.
     */
    public void increment(View view)
    {
        if (quantity == 100)
        {
            //Show an error message as a toast
            Toast.makeText(this, "You cannot have more than 100 coffees", Toast.LENGTH_SHORT).show();
            //Exit this method early because there is nothing to do
            return;
        }

        quantity = quantity + 1;
        displayQuantity(quantity);
    }

    /**
     * This method is called when the minus button is clicked.
     */
    public void decrement(View view)
    {
        if (quantity == 1)
        {
            //Show an error message as a toast
            Toast.makeText(this, "You cannot have less than 1 coffee", Toast.LENGTH_SHORT).show();
            //Exit this method early because there is nothing to do
            return;
        }

        quantity = quantity - 1;
        displayQuantity(quantity);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view)
    {
        //Figure out if the user wants the whipped cream topping
        CheckBox whippedCreamCheckBox = (CheckBox) findViewById(R.id.whipped_cream_checkbox);
        boolean hasWhippedCream = whippedCreamCheckBox.isChecked();
        Log.v("Main Activity", "Has whipped cream " + hasWhippedCream);

        //Figure out if the user wants the chocolate topping
        CheckBox chocolateToppingCheckbox = (CheckBox) findViewById(R.id.chocolate_topping_checkbox);
        boolean hasChocolateTopping = chocolateToppingCheckbox.isChecked();
        Log.v("Main Activity", "Has chocolate toppping " + hasChocolateTopping);

        //Figure out if there is any input in the text field
        EditText nameProvidedInTheTextFieldEditText = (EditText) findViewById(R.id.name_field);
        String name = nameProvidedInTheTextFieldEditText.getText().toString();
        Log.v("Main Activity", "Name: " + name);


        int price = calculatePrice(hasWhippedCream, hasChocolateTopping);
        Log.v("MainActivity", "The price is " + price);

        String priceMessage = createOrderSummary(name, price, hasWhippedCream, hasChocolateTopping);

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, "Just Java order for: " + name);
        intent.putExtra(Intent.EXTRA_TEXT, priceMessage + name);
        if (intent.resolveActivity(getPackageManager()) != null)
        {
            startActivity(intent);
        }

        /*Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("geo:47.6, -122.3"));
        if (intent.resolveActivity(getPackageManager()) != null)
        {
            startActivity(intent);
        }*/

    }

    /**
     * Calculates the price of the order.
     *
     * @param addWhippedCream is whether or not the user wants the whipped cream topping
     * @param addChocolate    is whether or not the user wants the chocolate topping
     * @return total price
     */

    private int calculatePrice(boolean addWhippedCream, boolean addChocolate)
    {
        // Price of one cup of coffee
        int basePrice = 5;

        //Add £1 if the user wants the whipped cream
        if (addWhippedCream)
        {
            basePrice = basePrice + 1;
        }

        //Add £2 is the user wants the chocolate topping
        if (addChocolate)
        {
            basePrice = basePrice + 2;
        }
        //Calculate the total order price by multiplying by quantity
        return quantity * basePrice;
    }

    /**
     * Create summary of the order
     *
     * @param name                of the Customer
     * @param price               of the order
     * @param addWhippedCream     is whether or not the user wants the whipped cream topping
     * @param addChocolateTopping is whether or not the user wants the chocolate topping
     * @return text summary
     */
    @TargetApi(Build.VERSION_CODES.N)
    private String createOrderSummary(String name, int price, boolean addWhippedCream, boolean addChocolateTopping)
    {
        String priceMessage = getString(R.string.order_summary_name, name);
        priceMessage += "\n" + getString(R.string.order_summary_whipped_cream, addWhippedCream);
        priceMessage += "\n" + getString(R.string.order_summary_chocolate, addChocolateTopping);
        priceMessage += "\n" + getString(R.string.order_summary_quantity, quantity);
        priceMessage += "\n" + getString(R.string.order_summary_price,
                NumberFormat.getCurrencyInstance().format(price));
        priceMessage += "\n" + getString(R.string.thank_you);
        return priceMessage;
    }


    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQuantity(int amount)
    {
        TextView quantityTextView = (TextView) findViewById(
                R.id.quantity_text_view);
        quantityTextView.setText("" + amount);
    }

}