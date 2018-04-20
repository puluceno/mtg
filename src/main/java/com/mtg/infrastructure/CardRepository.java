package com.mtg.infrastructure;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.mtg.model.Card;

@Repository
public interface CardRepository extends CrudRepository<Card, String> {

}
