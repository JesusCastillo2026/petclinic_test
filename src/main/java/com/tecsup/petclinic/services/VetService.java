package com.tecsup.petclinic.services;

import com.tecsup.petclinic.entities.Vet;
import com.tecsup.petclinic.exception.VetNotFoundException;
import com.tecsup.petclinic.repositories.VetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VetService {

    @Autowired
    private VetRepository vetRepository;

    public Vet findById(Integer id) throws VetNotFoundException {
        return vetRepository.findById(id)
                .orElseThrow(() -> new VetNotFoundException("Veterinario no encontrado con ID: " + id));
    }

    public void delete(Integer id) throws VetNotFoundException {
        Vet vet = findById(id);
        vetRepository.delete(vet);
    }

    public List<Vet> findAll() {
        return vetRepository.findAll();
    }
}