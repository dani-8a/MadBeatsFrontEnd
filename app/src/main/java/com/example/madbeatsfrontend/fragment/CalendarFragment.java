package com.example.madbeatsfrontend.fragment;

import static java.security.AccessController.getContext;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.madbeatsfrontend.R;
import com.example.madbeatsfrontend.viewModel.EventsSpotsViewModel;

import java.util.Calendar;

public class CalendarFragment extends DialogFragment {

    private EventsSpotsViewModel eventsSpotsViewModel;
    private Button buttonCancel;
    private Button buttonOk;
    private CalendarView calendarView;
    private long selectedDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflar el diseño del fragmento
        View view = inflater.inflate(R.layout.fragment_date_picker, container, false);

        // Inicializar ViewModel
        eventsSpotsViewModel = new ViewModelProvider(requireActivity()).get(EventsSpotsViewModel.class);

        // Obtener referencias a los componentes de la interfaz de usuario
        calendarView = view.findViewById(R.id.calendarView);
        buttonCancel = view.findViewById(R.id.buttonCancel);
        buttonOk = view.findViewById(R.id.buttonOk);

        // Configurar listener de clic para el botón Cancel
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss(); // Cierra el diálogo cuando se hace clic en Cancelar
            }
        });

        // Configurar listener de clic para el botón OK
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Verificar si se seleccionó una fecha válida
                if (selectedDate != 0) {
                    // Convertir la fecha seleccionada a un objeto Calendar
                    Calendar selectedCalendar = Calendar.getInstance();
                    selectedCalendar.setTimeInMillis(selectedDate);

                    // Obtener el día, mes y año seleccionados
                    int selectedDay = selectedCalendar.get(Calendar.DAY_OF_MONTH);
                    int selectedMonth = selectedCalendar.get(Calendar.MONTH) + 1;
                    int selectedYear = selectedCalendar.get(Calendar.YEAR);
                    // Imprimir la fecha seleccionada en el log
                    Log.d("DatePickerFragment", "Date selected: " + selectedDay + "/" + (selectedMonth) + "/" + selectedYear);

                    // Llamar al método del ViewModel para cargar los spots por fecha
                    eventsSpotsViewModel.loadSpotsByEventDate(selectedDay, selectedMonth, selectedYear);

                    // Cerrar el diálogo
                    dismiss();
                } else {
                    // Mostrar un mensaje de error si no se seleccionó ninguna fecha
                    Toast.makeText(requireContext(), "No day selected", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Configurar listener para capturar la fecha seleccionada en el CalendarView
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                // Convertir la fecha seleccionada a milisegundos
                Calendar selectedCalendar = Calendar.getInstance();
                selectedCalendar.set(year, month, dayOfMonth);
                selectedDate = selectedCalendar.getTimeInMillis();
            }
        });

        return view;
    }
}