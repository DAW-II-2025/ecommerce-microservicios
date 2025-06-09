package pe.edu.cibertec.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import pe.edu.cibertec.model.entity.Conversation;

@Repository
public interface IConverationRepository extends MongoRepository<Conversation, String>{

}
