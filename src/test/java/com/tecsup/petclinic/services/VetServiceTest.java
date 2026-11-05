package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.VetDTO;
import com.tecsup.petclinic.entities.Vet;
import com.tecsup.petclinic.exceptions.VetNotFoundException;
import com.tecsup.petclinic.mappers.VetMapper;
import com.tecsup.petclinic.repositories.VetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VetServiceTest {

    @Mock
    private VetRepository vetRepository;

    @Mock
    private VetMapper vetMapper;

    @InjectMocks
    private VetServiceImpl vetService;

    private Vet activeVet;
    private Vet inactiveVet;

    @BeforeEach
    void setUp() {
        activeVet = new Vet(1, "Ana", "Rojas", true);
        inactiveVet = new Vet(2, "Luis", "Perez", false);
    }

    @Test
    void testDeactivateVet() throws VetNotFoundException {
        VetDTO expected = new VetDTO(1, "Ana", "Rojas", false);
        when(vetRepository.findById(1)).thenReturn(Optional.of(activeVet));
        when(vetRepository.save(activeVet)).thenReturn(activeVet);
        when(vetMapper.mapToDto(activeVet)).thenReturn(expected);

        VetDTO result = vetService.deactivate(1);

        assertFalse(activeVet.getActive());
        assertFalse(result.getActive());
        verify(vetRepository).save(activeVet);
        verify(vetRepository, never()).delete(activeVet);
    }

    @Test
    void testReactivateVet() throws VetNotFoundException {
        VetDTO expected = new VetDTO(2, "Luis", "Perez", true);
        when(vetRepository.findById(2)).thenReturn(Optional.of(inactiveVet));
        when(vetRepository.save(inactiveVet)).thenReturn(inactiveVet);
        when(vetMapper.mapToDto(inactiveVet)).thenReturn(expected);

        VetDTO result = vetService.reactivate(2);

        assertTrue(inactiveVet.getActive());
        assertTrue(result.getActive());
        verify(vetRepository).save(inactiveVet);
        verify(vetRepository, never()).delete(inactiveVet);
    }

    @Test
    void testFindActiveVets() {
        VetDTO activeVetDTO = new VetDTO(1, "Ana", "Rojas", true);
        when(vetRepository.findByActiveTrue()).thenReturn(List.of(activeVet));
        when(vetMapper.mapToDtoList(List.of(activeVet))).thenReturn(List.of(activeVetDTO));

        List<VetDTO> result = vetService.findActiveVets();

        assertEquals(1, result.size());
        assertTrue(result.get(0).getActive());
        verify(vetRepository).findByActiveTrue();
        verify(vetRepository, never()).findByActiveFalse();
    }
}
