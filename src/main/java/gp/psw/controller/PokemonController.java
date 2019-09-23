package gp.psw.controller;

import gp.psw.dao.PokemonDAO;
import gp.psw.entity.Pokemon;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.MissingResourceException;
import java.util.Optional;

@RestController
public class PokemonController {

    @Autowired
    private PokemonDAO pokemonDAO;

    @RequestMapping(value = "/pokemon/{Id}", method = RequestMethod.GET)
    @ResponseBody
    Optional<Pokemon> getPokemonById(@PathVariable final Long Id) {
        return pokemonDAO.findById(Id);
    }

    @RequestMapping(value = "/pokemon", method = RequestMethod.GET)
    @ResponseBody
    List<Pokemon> getAll() {
        return pokemonDAO.findAll();
    }

    @RequestMapping(value = "/pokemon/{Id}", method = RequestMethod.DELETE)
    @ResponseBody
    void deletePokemonById(@PathVariable final Long Id) {
        pokemonDAO.deleteById(Id);
    }

   @PostMapping("/pokemon")
   @ResponseBody
    public ResponseEntity<Object> createStudent(@RequestBody Pokemon pokemon, @PathVariable final long Id) {
        Pokemon newPokemon = pokemonDAO.save(pokemon);

        newPokemon.setId(Id);
        
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{Id}")
                .buildAndExpand(newPokemon.getId()).toUri();

        return ResponseEntity.created(location).build();

    }

    @PutMapping("/pokemon/{Id}")
    @ResponseBody
    public ResponseEntity<Object> updateStudent(@Valid @RequestBody Pokemon pokemonDetails, @PathVariable final long Id) {
        Pokemon pokemon = pokemonDAO.findById(Id)
                .orElseThrow(() -> new ObjectNotFoundException("Pokemon not found for this id :: " + Id,Pokemon.class.getName()));

        pokemon.setName(pokemonDetails.getName());
        final Pokemon updatedPokemon = pokemonDAO.save(pokemon);
        return ResponseEntity.ok(updatedPokemon);
    }
}
