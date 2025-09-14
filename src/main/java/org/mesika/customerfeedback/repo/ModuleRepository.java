package org.mesika.customerfeedback.repo;

import java.util.List;
import java.util.UUID;

import org.mesika.customerfeedback.models.clients.Module;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModuleRepository extends JpaRepository<Module, Long> {
    List<Module> findByClient_Id(UUID clientId);

}
