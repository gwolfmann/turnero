package com.espou.turnero.storage;


import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class TaskRepository {
    private final ReactiveMongoTemplate mongoTemplate;

    public TaskRepository(ReactiveMongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public Mono<TaskDTO> save(TaskDTO task) {
        return mongoTemplate.save(task);
    }

    public Mono<TaskDTO> findById(String id) {
        return mongoTemplate.findById(id, TaskDTO.class);
    }

    public Flux<TaskDTO> findAll() {
        return mongoTemplate.findAll(TaskDTO.class);
    }

    public Mono<Void> deleteById(String id) {
        return mongoTemplate.remove(Query.query(Criteria.where("_id").is(id)), TaskDTO.class)
                .then();
    }

    // Add more custom repository methods as needed
}
