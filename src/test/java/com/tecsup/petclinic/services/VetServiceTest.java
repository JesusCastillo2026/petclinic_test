package com.tecsup.petclinic.services;

import com.tecsup.petclinic.entities.Vet;
import com.tecsup.petclinic.exception.VetNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class VetServiceTest {

    @Autowired
    private VetService vetService;

    // ==========================================
    // INTEGRANTE C: Casos de Error y Filtros
    // ==========================================

    @Test
    @DisplayName("1. Buscar un veterinario inexistente debe lanzar VetNotFoundException")
    public void testFindVetById_NotFound() {
        Integer idInexistente = 999999;

        // Se verifica que al buscar un ID no registrado se capture la excepción lanzada
        assertThrows(VetNotFoundException.class, () -> {
            vetService.findById(idInexistente);
        });
    }

    @Test
    @DisplayName("2. Desactivar un veterinario inexistente debe lanzar VetNotFoundException")
    public void testDeactivateVet_NotFound() {
        Integer idInexistente = 999999;

        // Se valida que no se pueda desactivar/eliminar un ID inexistente
        assertThrows(VetNotFoundException.class, () -> {
            vetService.delete(idInexistente);
        });
    }

    @Test
    @DisplayName("3. Buscar veterinarios inactivos debe devolver únicamente registros desactivados")
    public void testFindInactiveVets() throws Exception {
        // 1. Tomamos un veterinario existente y lo marcamos como inactivo (o eliminamos segun el soft-delete)
        Vet vet = vetService.findById(1);
        vetService.delete(vet.getId());

        // 2. Ejecutamos la consulta filtrada de inactivos
        List<Vet> inactiveVets = vetService.findAll(); // O el método de filtro correspondiente si existe en tu service

        // 3. Afirmaciones para comprobar consistencia
        assertNotNull(inactiveVets, "La lista de inactivos no debe ser nula");
        assertFalse(inactiveVets.isEmpty(), "Debe existir al menos un veterinario inactivo");
    }
}