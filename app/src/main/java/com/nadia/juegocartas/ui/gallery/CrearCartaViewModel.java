package com.nadia.juegocartas.ui.gallery;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.nadia.juegocartas.modelos.Carta;
import com.nadia.juegocartas.modelos.Personaje;

public class CrearCartaViewModel extends ViewModel {

    private final MutableLiveData<Integer> selectedCaraId = new MutableLiveData<>();
    private final MutableLiveData<Integer> selectedCabezaId = new MutableLiveData<>();
    private final MutableLiveData<Integer> selectedCuerpoId = new MutableLiveData<>();

    private final MediatorLiveData<Personaje> personajeSeleccionado = new MediatorLiveData<>();

    public CrearCartaViewModel() {
        personajeSeleccionado.addSource(selectedCaraId, id -> actualizarPersonaje());
        personajeSeleccionado.addSource(selectedCabezaId, id -> actualizarPersonaje());
        personajeSeleccionado.addSource(selectedCuerpoId, id -> actualizarPersonaje());
    }

    private void actualizarPersonaje() {
        Integer caraId = selectedCaraId.getValue();
        Integer cabezaId = selectedCabezaId.getValue();
        Integer cuerpoId = selectedCuerpoId.getValue();

        if (caraId != null && cabezaId != null && cuerpoId != null) {
            Personaje personaje = new Personaje();
            personaje.setCaraId(caraId);
            personaje.setCabezaId(cabezaId);
            personaje.setCuerpoId(cuerpoId);

            // Aquí podés setear atributos opcionales según tu lógica o datos locales
            // Por ejemplo:
            personaje.setImagen("https://midominio.com/imagenes/caras/" + caraId + ".png");

            personajeSeleccionado.setValue(personaje);
        }
    }

    // Métodos de selección
    public void seleccionarCara(int caraId) { selectedCaraId.setValue(caraId); }
    public void seleccionarCabeza(int cabezaId) { selectedCabezaId.setValue(cabezaId); }
    public void seleccionarCuerpo(int cuerpoId) { selectedCuerpoId.setValue(cuerpoId); }

    public LiveData<Personaje> getPersonajeSeleccionado() { return personajeSeleccionado; }
}

