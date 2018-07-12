package by.local.test.repository;

import by.local.test.model.Pipeline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("pipelineRepository")
public interface PipelineRepository extends JpaRepository<Pipeline, Long> {
    Optional<Pipeline> findById(Long id);
}
