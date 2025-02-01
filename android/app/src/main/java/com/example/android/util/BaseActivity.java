package com.example.android.util;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.android.R;

public abstract class BaseActivity extends AppCompatActivity {

    protected Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!shouldHideToolbar()) {
            setupToolbar();
        }
    }
    private void setupToolbar() {
        toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayShowTitleEnabled(true);
            }
        }
    }



    // Método que las actividades pueden sobrescribir para personalizar el título
    protected void setToolbarTitle(String title) {
        if (toolbar != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    // Método para ocultar la Toolbar en actividades específicas
    protected boolean shouldHideToolbar() {
        return false; // Por defecto, mostrar la Toolbar
    }

    protected abstract boolean shouldHideMenu();
}
