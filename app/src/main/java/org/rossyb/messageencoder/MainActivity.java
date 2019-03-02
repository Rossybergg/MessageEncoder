package org.rossyb.messageencoder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private static final String [] ALPHABETARRAY = {"a","b","c","d","e","f","g","h","i","j","k","l",
            "m","n","o","p","q","r","s","t","u","v","w","x","y","z","A","B","C","D","E","F","G","H",
            "I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z","1","2","3","4",
            "5","6","7","8","9","0",".",",",";","_",":","+","-","*","/"," ","@","$","#","¿","?","!",
            "¡","="};
    private static final String ALPHABETSTRING = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVW" +
            "XYZ1234567890.,;_:+-*/ @$#¿?!¡=";

    private Button BTNencrypt;
    private Button BTNdecrypt;
    private Button BTNclearInput;
    private Button BTNclearKey;
    private Button BTNclearOutput;
    private Button BTNswap;

    private EditText ETinput;
    private EditText EToutput;
    private EditText ETkey;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init () {
        BTNencrypt = (Button)findViewById(R.id.BTNencrypt);
        BTNdecrypt = (Button)findViewById(R.id.BTNdecrypt);
        BTNclearInput = (Button)findViewById(R.id.BTNclearInput);
        BTNclearKey = (Button)findViewById(R.id.BTNclearKey);
        BTNclearOutput = (Button)findViewById(R.id.BTNclearOutput);
        BTNswap = (Button)findViewById(R.id.BTNswap);

        ETinput = (EditText)findViewById(R.id.ETinput);
        EToutput = (EditText)findViewById(R.id.EToutput);
        ETkey = (EditText)findViewById(R.id.ETkey);

        BTNclearInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Clear Input", "Button Press");
                clearInput();
            }
        });

        BTNclearKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Clear key", "Button Press");
                clearKey();
            }
        });

        BTNclearOutput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Clear output", "Button Press");
                clearOutput();
            }
        });

        BTNswap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Swap Input/Output", "Button Press");
                swapMessages();
            }
        });

        BTNencrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // displays a console log to help debugging
                Log.d("Encrypt Message", "Button Press");
                // calling the function that encrypts the message
                encrypt();
                // I put these below to hide the Android keyboard when a button was pressed
                ETinput.onEditorAction(EditorInfo.IME_ACTION_DONE);
                ETkey.onEditorAction(EditorInfo.IME_ACTION_DONE);
                EToutput.onEditorAction(EditorInfo.IME_ACTION_DONE);

            }
        });

        BTNdecrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // displays a console log to help debugging
                Log.d("Decrypt", "Button Press");
                // calling the function that decrypts the message
                decrypt();
                // I put these below to hide the Android keyboard when a button was pressed
                ETinput.onEditorAction(EditorInfo.IME_ACTION_DONE);
                ETkey.onEditorAction(EditorInfo.IME_ACTION_DONE);
                EToutput.onEditorAction(EditorInfo.IME_ACTION_DONE);
            }
        });

    }

    private ArrayList<String> createShiftedAlphabet(int shift) {
        int temp = shift;
        //make copy of the alphabet
        ArrayList<String> _shiftedAlphabet = new ArrayList<>(Arrays.asList(ALPHABETARRAY));
        //create shifted alphabet
        while (temp > 0) {
            String _s = _shiftedAlphabet.remove(0);
            _shiftedAlphabet.add(_s);
            --temp;
        }
        //logs the shifted alphabet and the shift number
        Log.d("createdShiftAlphabet: ", _shiftedAlphabet.toString() + " Shift = " + shift);
        return _shiftedAlphabet;
    }

    private int loadShift() {
        int _key = 0;
                //check for the empty shift field on the user interface
        if (TextUtils.isEmpty(ETkey.getText().toString())) {
            Toast.makeText(MainActivity.this, "Key field cannot be empty",
                    Toast.LENGTH_SHORT).show();
        } else {
            _key = Integer.parseInt(ETkey.getText().toString());
        }
        // check to ensure shift number does not exceed the number of alphabet characters
        if (_key > 79) {
            Toast.makeText(MainActivity.this, "Invalid Key Number: "
                    + _key + "Must be lower than 79", Toast.LENGTH_SHORT).show();
            _key = 0;
        }
        if (_key < 0) {
            Toast.makeText(MainActivity.this, "Invalid Key Number: "
                    + _key + "Must be higher than 0", Toast.LENGTH_SHORT).show();
            _key = 0;
        }
        return _key;
    }

    private void decrypt() {
        String answer = "";
        String encryptText = "";
        ArrayList<String> _shifted = new ArrayList<>();
        int _shift = loadShift();
        _shifted = createShiftedAlphabet(_shift);
        encryptText = ETinput.getText().toString();

        for (int i = 0; i < encryptText.length(); ++i) {
            String target = encryptText.substring(i, i+1);
            int index = _shifted.indexOf(target);
            if(index == -1) {
                answer += target;
            } else {
                answer += ALPHABETARRAY[index];
            }
        }

        Log.d("Decrypt:", encryptText + " --> " + answer);
        EToutput.setText(answer);
//        printKey(_shifted);
    }

    private void encrypt() {
        String answer = "";
        String plainText = "";
        ArrayList<String> _shifted = new ArrayList<>();
        int _shift = loadShift();

        _shifted = createShiftedAlphabet(_shift);
        plainText = ETinput.getText().toString();

        for (int i = 0; i < ETinput.length(); ++i) {
            String target = plainText.substring(i, i+1);
            int index = ALPHABETSTRING.indexOf(target);
            if (index == -1) {
                answer += target;
            } else {
                answer += _shifted.get(index);
            }
        }

        Log.d("Encrypt:", plainText + " --> " + answer);
        EToutput.setText(answer);

    }

    private void clearInput() {
        ETinput.setText("");
    }

    private void clearKey() {
        ETkey.setText("");
    }

    private void clearOutput() {
        EToutput.setText("");
    }

    private void swapMessages() {
        String _temp = ETinput.getText().toString();
        ETinput.setText(EToutput.getText().toString());
        EToutput.setText(_temp);
    }

}
