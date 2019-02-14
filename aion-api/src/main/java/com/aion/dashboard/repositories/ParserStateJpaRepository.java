package com.aion.dashboard.repositories;

import com.aion.dashboard.entities.ParserState;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParserStateJpaRepository extends PagingAndSortingRepository<ParserState, Integer> {
	@Override
	Optional<ParserState> findById(Integer id);
}
