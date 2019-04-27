package com.es3649.execsec;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class LoaderActivity extends AppCompatActivity {

    private EditText IPEditText;
    private EditText portEditText;
    private EditText authEditText;
    private Button loadButton;
    private boolean loading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loader);

        TextWatcher tw = new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            public void afterTextChanged(Editable editable) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                updateButtonEnabled();
            }
        };

        IPEditText = findViewById(R.id.laIPEditText);
        portEditText = findViewById(R.id.laPortEditText);
        authEditText = findViewById(R.id.laAuthenticationEditText);
        loadButton = findViewById(R.id.laLoadButton);

        IPEditText.addTextChangedListener(tw);
        portEditText.addTextChangedListener(tw);
        authEditText.addTextChangedListener(tw);

        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new LoadDataAsync().execute(IPEditText.getText().toString(),
                        portEditText.getText().toString(),
                        authEditText.getText().toString());
            }
        });
    }

    /**
     * Makes a couple UI changes to the load button when loading starts.
     * Actual Loading is handles by an Async task
     *
     */
    private void startLoadUI() {
        // make some UI changed
        loading = true;
        loadButton.setText(R.string.laLoading);
        updateButtonEnabled();
    }

    /**
     * Makes small UI changes to the load button when loading finishes.
     * setResultText() updates the result text upon completion
     */
    private void finishLoadUI() {
        loading = false;
        loadButton.setText(R.string.laLoad);
        updateButtonEnabled();
    }

    /**
     * enables the button if each text view has stuff inside and if we aren't currently in the
     * middle of loading something up
     */
    private void updateButtonEnabled() {
        if (loading
                || IPEditText.getText().length() == 0
                || portEditText.getText().length() == 0
                || authEditText.getText().length() == 0) {
            loadButton.setEnabled(false);
        } else {
            loadButton.setEnabled(true);
        }
    }

    /**
     * Updates the result text UIs after data is loaded.
     *
     * @param result the results directly from the asyncTask
     */
    private void setResultText(LoadResult result) {
        findViewById(R.id.laStatusTextLinLay).setVisibility(View.VISIBLE);
        TextView statusTextView = findViewById(R.id.laStatusMessageText);

        // build the response messages and set it
        statusTextView.setText(String.format(Locale.getDefault(),
                getString(R.string.laLoadStatus), result.getSuccess(),
                result.getFailed(), result.getTotal()));

        if (result.getFailed() != 0 && result.getTotal() != 0) {
            TextView errorTextView = findViewById(R.id.laErrorTextView);
            errorTextView.setText(result.getError());
            errorTextView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * This is honestly just a little dataBall for the Async Task to use
     */
    private class LoadResult {
        /**
         * @param total the total number of entries which were to be loaded
         * @param failed the number of entries which failed to load
         * @param error the verbatim string of entries which failed
         */
        LoadResult(int total, int failed, String error) {
            this.total = total;
            this.failed = failed;
            this.error = error;
        }

        private int total;
        private int failed;
        private String error;

        int getTotal() {return total;}
        int getFailed() {return failed;}
        String getError() {return error;}
        int getSuccess() {return total - failed;}
    }

    /**
     *
     */
    class LoadDataAsync extends AsyncTask<String, Integer, LoadResult> {
        private static final String TAG = "LoadDataAsync";

        @Override
        protected void onPreExecute() {
            startLoadUI();
        }

        @Override
        protected LoadResult doInBackground(String... strings) {
            // make sure that we only got exactly 3 strings
            if (strings.length != 3) {
                Log.e(TAG, String.format("Expected 3 arguments, got %d", strings.length));
                Toast.makeText(getApplicationContext(), "Expected 3 arguments", Toast.LENGTH_SHORT).show();
            }

            // make an API call
            try {
                ServerProxy s = new ServerProxy();
            } catch {

            }

            return null;
        }

        @Override
        protected void onCancelled() {
            finishLoadUI();
        }

        @Override
        protected void onPostExecute(LoadResult result) {
            finishLoadUI();
            setResultText(result);
        }
    }
}
