package me.narlove.MobileTaskTwo;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {

    Button button;
    TextView outputVal;
    TextInputEditText inputVal;
    Spinner inputSpinner;
    Spinner outputSpinner;
    // used to have radio buttons here, then i realised
    // radio buttons have to be a direct descendant of a radio group, which will
    // not work because i needed the linear layout to have rows of them
    // and i could not be bothered creating a custom class of radio group that extends
    // a different form of layout to make it work so i just swapped to buttons
    Button tempButton;
    Button currencyButton;
    Button mileageButton;
    Button distanceButton;
    Button volumeButton;
    TextView header;
    Mode currentMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // don't touch this part of teh code
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // alright you can mess with this stuff
        // grab all required elements
        // these top elements will be required for performing conversion logic
        button = findViewById(R.id.convertButton);
        outputVal = findViewById(R.id.outputValue);
        inputVal = findViewById(R.id.inputValue);
        // the spinners will be required to change but will also be required for conversion
        inputSpinner = findViewById(R.id.inputSpinner);
        outputSpinner = findViewById(R.id.outputSpinner);

        // relating to view changing generally
        header = findViewById(R.id.header);

        // relating to view changing buttons
        tempButton = findViewById(R.id.temperatureButton);
        currencyButton = findViewById(R.id.currencyButton);
        mileageButton = findViewById(R.id.mileageButton);
        distanceButton = findViewById(R.id.distanceButton);
        volumeButton = findViewById(R.id.volumeButton);

        // ensure interface changes whenever a button is selected within the group
        // the other benefit to the radio group is that only one of the three can be selected at
        // once, so no manual validation required.
        tempButton.setOnClickListener(this::onModeButtonClicked);
        currencyButton.setOnClickListener(this::onModeButtonClicked);
        mileageButton.setOnClickListener(this::onModeButtonClicked);
        distanceButton.setOnClickListener(this::onModeButtonClicked);
        volumeButton.setOnClickListener(this::onModeButtonClicked);

        currentMode = Mode.CURRENCY;

        // generic, can be reused for each mode
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedInput = inputSpinner.getSelectedItem().toString();
                String selectedOutput = outputSpinner.getSelectedItem().toString();
                String incoming = inputVal.getText().toString();

                double parsedInput = tryConvertStringDouble(incoming);

                double toSet = runConverter(currentMode, parsedInput, selectedInput, selectedOutput);

                String formatted = String.format("%.2f", toSet);

                outputVal.setText(formatted);
            }
        });
    }

    private void onModeButtonClicked(View v)
    {
        Button selected = (Button) v;

        // this is slightly finicky, ensure we update parse mode function whenever
        // mode text is changed
        currentMode = parseMode(selected.getText().toString());
        setTitle(currentMode, header);
        setOptionsByMode(currentMode, inputSpinner);
        setOptionsByMode(currentMode, outputSpinner);
    }

    private Mode parseMode(@NonNull String text)
    {
        String lowered = text.toLowerCase();

        switch (lowered)
        {
            case "currency":
                return Mode.CURRENCY;
            case "temp":
                return Mode.TEMPERATURE;
            case "mileage":
                return Mode.MILEAGE;
            case "distance":
                return Mode.DISTANCE;
            case "volume":
                return Mode.VOLUME;
            default:
                throw new IllegalArgumentException("provide an argument that is one of currency, temp, or mileage");
        }
    }

    private enum Mode
    {
        CURRENCY,
        TEMPERATURE,
        MILEAGE,
        DISTANCE,
        VOLUME
    }

    private void setTitle(@NonNull Mode mode, @NonNull TextView widget)
    {
        switch (mode)
        {
            case CURRENCY:
                widget.setText("Currency Converter");
                break;
            case MILEAGE:
                widget.setText("Mileage Converter");
                break;
            case TEMPERATURE:
                widget.setText("Temperature Converter");
                break;
            case VOLUME:
                widget.setText("Volume Converter");
                break;
            case DISTANCE:
                widget.setText("Distance Converter");
                break;
            default:
                widget.setText("An error occurred.");
                break;
        }
    }

    // from dev docs
    // https://developer.android.com/develop/ui/views/components/spinner#java
    private void setSpinnerOptions(@NonNull Spinner spinner, int arrayId)
    {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                arrayId,
                android.R.layout.simple_spinner_item
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void setOptionsByMode(@NonNull Mode mode, @NonNull Spinner widget)
    {
        switch (mode)
        {
            case TEMPERATURE:
                setSpinnerOptions(widget, R.array.temperature);
                break;
            case MILEAGE:
                setSpinnerOptions(widget, R.array.mileage);
                break;
            case CURRENCY:
                setSpinnerOptions(widget, R.array.currency);
                break;
            case VOLUME:
                setSpinnerOptions(widget, R.array.volume);
                break;
            case DISTANCE:
                setSpinnerOptions(widget, R.array.distance);
                break;
            default:
                setSpinnerOptions(widget, R.array.currency);
                break;
        }
    }

    private double tryConvertStringDouble(String input)
    {
        double value;

        if (input.isEmpty()) { value = 0.0; }
        try {
            value = Double.parseDouble(input);
        } catch (NumberFormatException e)
        {
            value = 0.0;
        }

        return value;
    }

    private double handleUsd(double input, String outCurrency)
    {
        switch (outCurrency)
        {
            case "aud":
                return 1.55 * input;
            case "eur":
                return 0.92 * input;
            case "jpy":
                return input * 148.50;
            case "gbp":
                return 0.78 * input;
            case "usd":
            default:
                return input;
        }
    }

    private double toUsd(double input, String inCurrency)
    {
        switch (inCurrency)
        {
            case "aud":
                return input / 1.55;
            case "eur":
                return input / 0.92;
            case "jpy":
                return input / 148.50;
            case "gbp":
                return input / 0.78;
            case "usd":
            default:
                return input;
        }
    }

    private double convertCurrency(double input, String inCurrency, String outCurrency)
    {
        if (inCurrency.equalsIgnoreCase(outCurrency))
        {
            return input;
        }

        inCurrency = inCurrency.toLowerCase();
        outCurrency = outCurrency.toLowerCase();

        double asUsd = toUsd(input, inCurrency);

        return handleUsd(asUsd, outCurrency);
    }

    private double asCelcius(double input, String inTemp)
    {
        switch (inTemp) {
            case "fahrenheit":
                return (input - 32) / 1.8;
            case "kelvin":
                return input - 273.15;
            case "celcius":
            default:
                return input;
        }
    }

    private double handleCelcius(double input, String outTemp)
    {
        switch (outTemp) {
            case "fahrenheit":
                return (input * 1.8) + 32;
            case "kelvin":
                return input + 273.15;
            case "celcius":
            default:
                return input;
        }
    }

    private double convertTemperature(double input, String inTemp, String outTemp)
    {
        if (inTemp.equalsIgnoreCase(outTemp))
        {
            return input;
        }

        inTemp = inTemp.toLowerCase();
        outTemp = outTemp.toLowerCase();

        double celciusVal = asCelcius(input, inTemp);

        return handleCelcius(celciusVal, outTemp);
    }

    private double convertMileage(double input, String in, String out)
    {
        in = in.toLowerCase();
        out = out.toLowerCase();

        if (in.equalsIgnoreCase(out))
        {
            return input;
        }

        // if the in and out types are not equal, then we can just convert
        // to whatever the other type is (we only offer two types for mileage)
        // this is not good for scaling but IS good for a pass task
        switch (in)
        {
            case "mpg":
                // to km/L
                return input * 0.425;
            case "km/l":
                // to mpg
                return input / 0.425;
            default:
                return 0;
        }
    }

    private double convertVolume(double input, String in, String out)
    {
        in = in.toLowerCase();
        out = out.toLowerCase();

        if (in.equalsIgnoreCase(out))
        {
            return input;
        }

        // if the in and out types are not equal, then we can just convert
        // to whatever the other type is
        // this is not good for scaling but IS good for a pass task
        switch (in)
        {
            case "gallon":
                // to litres
                return input * 3.785;
            case "liters":
                // to gallons
                return input / 3.785;
            default:
                return 0;
        }
    }

    private double convertDistance(double input, String in, String out)
    {
        in = in.toLowerCase();
        out = out.toLowerCase();

        if (in.equalsIgnoreCase(out))
        {
            return input;
        }

        // if the in and out types are not equal, then we can just convert
        // to whatever the other type is
        // this is not good for scaling but IS good for a pass task
        switch (in)
        {
            case "nautical mile":
                // to kilometres
                return input * 1.852;
            case "kilometers":
                // to nautical miles
                return input / 1.852;
            default:
                return 0;
        }
    }

    // was going to have interfaces and get complicated
    // to have this method return a reference to another method that i can then call
    // from whichever context called this function, but i figured that was too much effort
    // maybe for a credit or distinction task down the line or something
    // on further thoughts, this whole program would be simplified if i have
    // made like a generic Converter class and then have instantiations for temperature,
    // currency and mileage and used polymorphism to navigate between them, but that's ok.
    // ill clean this up if we need it for future tasks.
    private double runConverter(Mode mode, double input, String selectedIn,
                                                String selectedOut)
    {
        switch (mode)
        {
            case TEMPERATURE:
                return convertTemperature(input, selectedIn, selectedOut);
            case MILEAGE:
                return convertMileage(input, selectedIn, selectedOut);
            case VOLUME:
                return convertVolume(input, selectedIn, selectedOut);
            case DISTANCE:
                return convertDistance(input, selectedIn, selectedOut);
            case CURRENCY:
            default: // random default to avoid crashes should problems arise
                return convertCurrency(input, selectedIn, selectedOut);
        }
    }
}