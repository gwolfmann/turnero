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

    public Mono<Void> deleteById(String id) {
        return mongoTemplate.remove(Query.query(Criteria.where("_id").is(id)), MeetDTO.class)
                .then();
    }
    public Flux<MeetDTO> findByResource_Id(String resourceId) {
        Query query = Query.query(Criteria.where("resource.id").is(resourceId));
        return mongoTemplate.find(query, MeetDTO.class);
    }

    public Flux<MeetDTO> findByProvider_Id(String providerId) {
        Query query = Query.query(Criteria.where("provider.id").is(providerId));
        return mongoTemplate.find(query, MeetDTO.class);
    }
    // Add more repository methods as needed
}
