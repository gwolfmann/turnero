package com.espou.turnero.storage;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class MeetRepository {
    private final ReactiveMongoTemplate mongoTemplate;

    public MeetRepository(ReactiveMongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public Mono<MeetDTO> save(MeetDTO meet) {
        return mongoTemplate.save(meet);
    }

    public Mono<MeetDTO> findById(String id) {
        return mongoTemplate.findById(id, MeetDTO.class);
    }
    public Flux<MeetDTO> findByResourceAndProvider(String resourceId, String providerId) {
        Query query = Query.query(Criteria.where("resource.id").is(resourceId).and("provider.id").is(providerId));
        return mongoTemplate.find(query, MeetDTO.class);
    }

    // Add more repository methods as needed
}
